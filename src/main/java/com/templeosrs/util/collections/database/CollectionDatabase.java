package com.templeosrs.util.collections.database;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.templeosrs.util.collections.data.ObtainedCollectionItem;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@Slf4j
@Singleton
public class CollectionDatabase
{
	private static final String DB_URL = "jdbc:h2:file:" + RuneLite.RUNELITE_DIR + "/templeosrs/runelite-collections;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";

	private static final String COLLECTION_LOG_CACHE_TABLE_NAME = "collection_log_cache";
	private static final String PLAYER_METADATA_TABLE_NAME = "player_metadata";

	static
	{
		File pluginDir = new File(RuneLite.RUNELITE_DIR, "templeosrs");
		if (!pluginDir.exists())
		{
			if (!pluginDir.mkdirs())
			{
				log.warn("⚠️ Failed to create plugin directory at {}", pluginDir.getAbsolutePath());
			}
		}
	}

	public static Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(DB_URL);
	}

	public static void init()
	{
		try
		{
			// 🚨 Required for Plugin Hub: explicitly load the H2 JDBC driver
			Class.forName("org.h2.Driver");

			try (Connection conn = getConnection(); Statement stmt = conn.createStatement())
			{
				final String createCollectionLogCacheTableSql = String.format(
					"CREATE TABLE IF NOT EXISTS %s(" +
						"id IDENTITY PRIMARY KEY, " +
						"item_id INT, " +
						"item_name VARCHAR(255), " +
						"item_count INT, " +
						"player_name VARCHAR(255)" +
						")",
					COLLECTION_LOG_CACHE_TABLE_NAME
				);

				final String createPlayerMetadataTableSql = String.format(
					"CREATE TABLE IF NOT EXISTS %s(" +
						"id IDENTITY PRIMARY KEY, " +
						"player_name VARCHAR(255), " +
						"last_changed TIMESTAMP, " +
						"last_accessed TIMESTAMP" +
						")",
					PLAYER_METADATA_TABLE_NAME
				);

				stmt.executeUpdate(createCollectionLogCacheTableSql);
				stmt.executeUpdate(createPlayerMetadataTableSql);

				stmt.executeUpdate(String.format(
					"CREATE INDEX IF NOT EXISTS idx_%s_player_item ON %s(player_name, item_id)",
					COLLECTION_LOG_CACHE_TABLE_NAME,
					COLLECTION_LOG_CACHE_TABLE_NAME
				));

				stmt.executeUpdate(String.format(
					"CREATE INDEX IF NOT EXISTS idx_%s_player ON %s(player_name)",
					PLAYER_METADATA_TABLE_NAME,
					PLAYER_METADATA_TABLE_NAME
				));
			}
		}
		catch (ClassNotFoundException e)
		{
			log.warn("H2 Driver class not found: {}", e.getMessage());
		}
		catch (SQLException e)
		{
			log.warn("Database initialization failed: {}", e.getMessage());
		}
	}

	private static void addColumnIfNotExists(Connection conn, String table, String column, String type) throws SQLException
	{
		String checkQuery = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";
		try (PreparedStatement ps = conn.prepareStatement(checkQuery))
		{
			ps.setString(1, table.toUpperCase());
			ps.setString(2, column.toUpperCase());
			try (ResultSet rs = ps.executeQuery())
			{
				if (!rs.next())
				{
					try (Statement stmt = conn.createStatement())
					{
						stmt.executeUpdate("ALTER TABLE " + table + " ADD COLUMN " + column + " " + type);
					}
				}
			}
		}
	}

	public static boolean hasPlayerData(String playerName)
	{
		String sql = String.format("SELECT 1 FROM %s WHERE player_name = ? LIMIT 1", COLLECTION_LOG_CACHE_TABLE_NAME);

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setString(1, playerName.toLowerCase());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
		catch (SQLException e)
		{
			log.warn("Error checking player data: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * Saves the API response data to the API cache table
	 *
	 * @param playerName The player name associated with the response
	 * @param items      The items to persist to the database
	 */
	public static void upsertItemsBatch(
		@NotNull String playerName,
		@NotNull Set<ObtainedCollectionItem> items,
		Timestamp lastChanged
	)
	{
		try (Connection conn = getConnection())
		{
			conn.setAutoCommit(false);

			try (
				PreparedStatement ps1 = conn.prepareStatement(
					String.format("MERGE INTO %s USING DUAL ", COLLECTION_LOG_CACHE_TABLE_NAME) +
						"ON item_id = ? AND player_name = ? " +
						"WHEN MATCHED THEN UPDATE SET item_count = ? " +
						"WHEN NOT MATCHED THEN INSERT (player_name, item_id, item_count, item_name) VALUES (?, ?, ?, ?)"
				);
				PreparedStatement ps2 = conn.prepareStatement(
					String.format("MERGE INTO %s USING DUAL ", PLAYER_METADATA_TABLE_NAME) +
						"ON player_name = ? " +
						"WHEN MATCHED THEN UPDATE SET last_changed = ?, last_accessed = ? " +
						"WHEN NOT MATCHED THEN INSERT (player_name, last_changed, last_accessed) VALUES (?, ?, ?)"
				)
			)
			{
				final String lowerPlayerName = playerName.toLowerCase();
				final Timestamp lastAccessed = new Timestamp(System.currentTimeMillis());

				for (ObtainedCollectionItem item : items)
				{
					final int itemId = item.getId();
					final int itemCount = item.getCount();

					ps1.setInt(1, itemId);
					ps1.setString(2, lowerPlayerName);
					ps1.setInt(3, itemCount);
					ps1.setString(4, lowerPlayerName);
					ps1.setInt(5, itemId);
					ps1.setInt(6, itemCount);
					ps1.setString(7, item.getName());

					ps1.addBatch();
				}

				ps2.setString(1, lowerPlayerName);
				ps2.setTimestamp(2, lastChanged);
				ps2.setTimestamp(3, lastAccessed);
				ps2.setString(4, lowerPlayerName);
				ps2.setTimestamp(5, lastChanged);
				ps2.setTimestamp(6, lastAccessed);

				ps1.executeBatch();
				ps2.execute();
			}

			conn.commit();
		}
		catch (SQLException e)
		{
			log.warn("Error inserting items to the API cache: {}", e.getMessage());
		}
	}

	public static Multiset<Integer> getCollectionLogDiff(String playerName, Multiset<Integer> collectionLogItems)
	{
		try (Connection conn = getConnection())
		{
			try (PreparedStatement ps = conn.prepareStatement(
				String.format(
					"SELECT item_count, item_id FROM %s WHERE player_name = ? AND item_id = ? LIMIT 1",
					COLLECTION_LOG_CACHE_TABLE_NAME
				)
			))
			{
				final Multiset<Integer> foundItems = HashMultiset.create();

				for (Multiset.Entry<Integer> entry : collectionLogItems.entrySet())
				{
					final int itemId = entry.getElement();

					ps.setString(1, playerName.toLowerCase());
					ps.setInt(2, itemId);

					final ResultSet rs = ps.executeQuery();

					if (rs.next())
					{
						final int rsItemCount = rs.getInt("item_count");
						final int rsItemId = rs.getInt("item_id");

						foundItems.add(rsItemId, rsItemCount);
					}
				}

				return Multisets.difference(collectionLogItems, foundItems);
			}
		}
		catch (SQLException e)
		{
			log.warn("Error comparing collection log: {}", e.getMessage());

			return null;
		}
	}

	public static Timestamp getLatestTimestamp(String playerName)
	{
		String sql = String.format("SELECT last_changed FROM %s WHERE player_name = ?", PLAYER_METADATA_TABLE_NAME);

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setString(1, playerName.toLowerCase());
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				return rs.getTimestamp(1);
			}
		}
		catch (SQLException e)
		{
			log.warn("Error fetching latest timestamp: {}", e.getMessage());
		}

		return null;
	}

	public static void clearAll()
	{
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(String.format("DELETE FROM %s", COLLECTION_LOG_CACHE_TABLE_NAME));
		}
		catch (SQLException e)
		{
			log.warn("Error clearing all items: {}", e.getMessage());
		}
	}

	public static Set<ObtainedCollectionItem> getItemsByCategory(String playerName, Set<Integer> categoryItems)
	{
		Map<Integer, ObtainedCollectionItem> items = new HashMap<>();

		try (Connection conn = getConnection();
			 PreparedStatement ps1 = conn.prepareStatement(
				 String.format(
					 "SELECT item_id, item_name, item_count FROM %s WHERE player_name = ? AND item_id IN (%s)",
					 COLLECTION_LOG_CACHE_TABLE_NAME,
					 StringUtils.repeat("?", ",", categoryItems.size())
				 )
			 );
			 PreparedStatement ps2 = conn.prepareStatement(
				 String.format("UPDATE %s ", PLAYER_METADATA_TABLE_NAME) +
					 "SET last_accessed = ? " +
					 "WHERE player_name = ?"
			 )
		)
		{
			conn.setAutoCommit(false);

			final Timestamp lastAccessed = new Timestamp(System.currentTimeMillis());
			final String lowerPlayerName = playerName.toLowerCase();

			ps1.setString(1, playerName.toLowerCase());

			int paramIndex = 2;
			for (int id : categoryItems)
			{
				ps1.setInt(paramIndex, id);
				paramIndex++;
			}

			try (ResultSet rs = ps1.executeQuery())
			{
				while (rs.next())
				{
					int itemId = rs.getInt("item_id");
					String itemName = rs.getString("item_name");
					int count = rs.getInt("item_count");

					items.put(itemId, new ObtainedCollectionItem(itemId, itemName, count));
				}
			}

			ps2.setTimestamp(1, lastAccessed);
			ps2.setString(2, lowerPlayerName);

			ps2.execute();

			conn.commit();
		}
		catch (SQLException e)
		{
			log.warn("Error fetching items by category: {}", e.getMessage());
		}

		Set<ObtainedCollectionItem> sortedItems = new LinkedHashSet<>();

		// Sorts the database response to match the order found in the log tab
		for (int itemId : categoryItems)
		{
			if (items.containsKey(itemId))
			{
				sortedItems.add(items.get(itemId));
			}
		}

		return sortedItems;
	}

	public static void pruneOldPlayers(String yourUsername, int maxPlayers)
	{
		try (Connection conn = getConnection();
			 PreparedStatement ps1 = conn.prepareStatement(
				 "SELECT player_name, MIN(last_accessed) as oldest " +
					 String.format("FROM %s ", COLLECTION_LOG_CACHE_TABLE_NAME) +
					 "WHERE player_name != ? " +
					 "GROUP BY player_name " +
					 "ORDER BY oldest ASC"
			 );
			 PreparedStatement deleteStmt = conn.prepareStatement(
				 String.format(
					 "DELETE FROM %s AS a INNER JOIN %s AS b WHERE a.player_name = b.player_name AND a.player_name = ?",
					 COLLECTION_LOG_CACHE_TABLE_NAME,
					 PLAYER_METADATA_TABLE_NAME
				 )
			 )
		)
		{
			ps1.setString(1, yourUsername.toLowerCase());
			ResultSet rs = ps1.executeQuery();

			int count = 0;
			List<String> playersToRemove = new ArrayList<>();

			while (rs.next())
			{
				count++;
				if (count > maxPlayers)
				{
					playersToRemove.add(rs.getString("player_name"));
				}
			}

			for (String name : playersToRemove)
			{
				log.debug("\uD83E\uDDF9 Pruning cached player: {}", name);
				deleteStmt.setString(1, name);
				deleteStmt.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			log.warn("Error pruning old players: {}", e.getMessage());
		}
	}
}
