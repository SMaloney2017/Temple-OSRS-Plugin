package com.templeosrs.ui.clans;

import com.google.common.base.Strings;
import com.templeosrs.TempleOSRSConfig;
import com.templeosrs.TempleOSRSPlugin;
import static com.templeosrs.util.TempleService.addClanMembersAsync;
import static com.templeosrs.util.TempleService.fetchClanAsync;
import static com.templeosrs.util.TempleService.syncClanMembersAsync;
import com.templeosrs.util.clan.TempleClan;
import com.templeosrs.util.clan.TempleClanAchievement;
import com.templeosrs.util.clan.TempleClanOverviewInfo;
import com.templeosrs.util.sync.TempleSync;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.runelite.api.Client;
import net.runelite.api.clan.ClanMember;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.clan.ClanTitle;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.LinkBrowser;
import net.runelite.client.util.Text;
import okhttp3.HttpUrl;

public class TempleClans extends PluginPanel
{
	private static final Pattern isNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

	public final IconTextField clanLookup;

	private final Client client;

	private final ClientThread thread;

	private final TempleOSRSPlugin plugin;

	private final TempleOSRSConfig config;

	private final JButton verifyButton;

	private final PluginErrorPanel errorPanel = new PluginErrorPanel();

	private final JPanel fetchLayout;

	public TempleClanAchievements clanAchievements;

	public TempleClanMembers clanMembers;

	public TempleClanCurrentTop clanCurrentTop;

	private JButton searchButton;

	private JButton clanButton;

	@Inject
	public TempleClans(TempleOSRSConfig config, TempleOSRSPlugin plugin, Client client, ClientThread thread)
	{
		this.plugin = plugin;
		this.client = client;
		this.thread = thread;
		this.config = config;

		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		fetchLayout = new JPanel();
		fetchLayout.setLayout(new BoxLayout(fetchLayout, BoxLayout.Y_AXIS));
		fetchLayout.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		fetchLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* build and add search-text-field */
		clanLookup = buildTextField();
		fetchLayout.add(clanLookup);

		/* build and add search-buttons */
		JPanel buttons = buildFetchButtons();
		fetchLayout.add(buttons);

		/* build and add members-verification */
		JPanel verifyLayout = new JPanel();
		verifyLayout.setLayout(new BorderLayout());
		verifyLayout.setBorder(new EmptyBorder(0, 0, 5, 0));
		verifyLayout.setOpaque(false);

		verifyButton = createNewButton("Sync Members", "Sync all members of the clan you are currently in (Requires key)");
		verifyButton.addActionListener(e -> verify());
		verifyLayout.add(verifyButton);

		fetchLayout.add(verifyLayout);
		add(fetchLayout);

		/* add default, error-panel when clan has not been fetched yet */
		errorPanel.setContent("Clans", "You have not fetched clan information yet.");
		add(errorPanel);

		/* load default clan on start-up */
		if (config.fetchDefaults())
		{
			if (config.defaultClan() != 0)
			{
				clanLookup.setText(Integer.toString(config.defaultClan()));
				fetchClan();
			}
		}
	}

	private IconTextField buildTextField()
	{
		IconTextField lookup = new IconTextField();

		lookup.setIcon(IconTextField.Icon.SEARCH);
		lookup.setEditable(true);
		lookup.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		lookup.setBackground(ColorScheme.SCROLL_TRACK_COLOR);
		/* fetch clan on action */
		lookup.addActionListener(e -> fetchClan());
		lookup.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() != 2)
				{
					return;
				}

				if (config.defaultClan() != 0)
				{
					clanLookup.setText(Integer.toString(config.defaultClan()));
					fetchClan();
				}
			}
		});
		/* reset on clear */
		lookup.addClearListener(() -> {
			completed();
			reset();
		});

		return lookup;
	}

	private JPanel buildFetchButtons()
	{
		JPanel buttonsLayout = new JPanel();
		buttonsLayout.setLayout(new FlowLayout());
		buttonsLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		searchButton = createNewButton("Search", "Search for clan by ID (Found in TempleOSRS clan-page url)");
		searchButton.addActionListener(e -> fetchClan());

		clanButton = createNewButton("Open Page", "Opens TempleOSRS clan page");
		clanButton.addActionListener(e -> open());

		buttonsLayout.add(searchButton);
		buttonsLayout.add(clanButton);

		return buttonsLayout;
	}

	/* create a single, JButton with similar style */
	private JButton createNewButton(String text, String tooltip)
	{
		JButton newButton = new JButton();

		newButton.setFont(FontManager.getRunescapeFont());
		newButton.setText(text);
		newButton.setToolTipText(tooltip);
		newButton.setForeground(ColorScheme.GRAND_EXCHANGE_LIMIT);

		/* add hover mouse-listener for buttons */
		newButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				newButton.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
			}

			public void mouseExited(MouseEvent evt)
			{
				newButton.setForeground(ColorScheme.GRAND_EXCHANGE_LIMIT);
			}
		});

		return newButton;
	}

	/* fetch clan from search-text-field */
	public void fetchClan()
	{
		final String id = clanLookup.getText();

		if (Strings.isNullOrEmpty(id))
		{
			return;
		}

		/* clan-id must be integer */
		if (!isNumeric.matcher(id).matches())
		{
			error();
			return;
		}

		loading();

		reset();

		/* create separate thread for completing clan-fetch/ panel rebuilds,
		 *  try to fetch clan,
		 *  when fetching completes, rebuild panel
		 *  if exception, set error status
		 */
		new Thread(() -> {
			try
			{
				fetchClanAsync(id, config.currentTopRange().getRange()).whenCompleteAsync((result, err) -> response(id, result, err));
			}
			catch (Exception e)
			{
				error();
			}
		}).start();
	}

	/* reload fetched clan after syncing member-list */
	private void reload(String id)
	{
		loading();

		reset();

		try
		{
			fetchClanAsync(id, config.currentTopRange().getRange()).whenCompleteAsync((result, err) -> response(id, result, err));
		}
		catch (Exception e)
		{
			error();
		}
	}

	private void rebuild(TempleClan result)
	{
		if (client == null)
		{
			return;
		}

		String[] leaders = result.clanOverview.data.leaders;
		String[] members = result.clanOverview.data.members;
		TempleClanOverviewInfo info = result.clanOverview.data.info;
		List<TempleClanAchievement> clanActivity = result.clanAchievements.data;
		/* Event-Dispatch-Thread necessary for adding/ removing new components */
		SwingUtilities.invokeLater(() -> {

			add(new TempleClanOverview(info));

			/* create achievements-component, only add if config option */
			clanAchievements = new TempleClanAchievements(plugin, clanActivity);
			if (config.clanAchievements())
			{
				add(clanAchievements);
			}

			/* create current-top-component, only add if config option enabled */
			TempleClanCurrentTopList clanCurrentTopEhp = new TempleClanCurrentTopList(plugin, result.clanCurrentTopEhp.list, config.currentTopRange());
			TempleClanCurrentTopList clanCurrentTopEhb = new TempleClanCurrentTopList(plugin, result.clanCurrentTopEhb.list, config.currentTopRange());

			clanCurrentTop = new TempleClanCurrentTop(clanCurrentTopEhp, clanCurrentTopEhb);
			if (config.clanCurrentTop())
			{
				add(clanCurrentTop);
			}

			/* create members-component, only add if config option */
			clanMembers = new TempleClanMembers(new TempleClanMembersList(plugin, "Leaders", leaders), new TempleClanMembersList(plugin, "Members", members));
			if (config.clanMembers())
			{
				add(clanMembers);
			}
		});

		completed();

		revalidate();
		repaint();
	}

	private void open()
	{
		String id = clanLookup.getText();
		if (Strings.isNullOrEmpty(id))
		{
			return;
		}

		if (!isNumeric.matcher(id).matches())
		{
			error();
			return;
		}

		/* if valid clan-id, open temple clan-page */
		loading();

		HttpUrl url = new HttpUrl.Builder()
			.scheme("https")
			.host("templeosrs.com")
			.addPathSegment("groups")
			.addPathSegment("overview.php")
			.addQueryParameter("id", id).build();

		SwingUtilities.invokeLater(() -> LinkBrowser.browse(url.toString()));

		completed();
	}

	private void verify()
	{
		if (client == null)
		{
			return;
		}

		if (Strings.isNullOrEmpty(config.clanKey()))
		{
			return;
		}

		ClanSettings localClan = client.getClanSettings();

		if (localClan == null)
		{
			return;
		}

		/* add confirmation to sync clan-members with fetched clan (requires key) */
		final int confirmation = JOptionPane.showOptionDialog(verifyButton, "This will sync the fetched clan's TempleOSRS members-list to all members in the current account's clan (Requires Key).",
			"Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
			null, new String[]{
				"Yes",
				"No"
			}, "No");

		if (confirmation != JOptionPane.YES_OPTION)
		{
			return;
		}

		/* exclude certain clan ranks from members-sync */
		filter(localClan);
	}

	private void verify(List<String> filteredList)
	{
		loading();

		String id = clanLookup.getText();
		/* create separate thread for completing clan-post/ panel reload,
		 *  try to post clan members given clan-id, verification, and list,
		 *  when post completes, check response -> reload panel
		 *  if exception, set error status
		 */
		new Thread(() -> {
			try
			{
				if (config.onlyAddMembers())
				{
					addClanMembersAsync(id, config.clanKey(), filteredList).whenCompleteAsync((result, err) -> response(id, result, err));
				}
				else
				{
					syncClanMembersAsync(id, config.clanKey(), filteredList).whenCompleteAsync((result, err) -> response(id, result, err));
				}
			}
			catch (Exception e)
			{
				error();
			}
		}).start();
	}

	/* filter unwanted ranks for members-sync */
	private void filter(ClanSettings localClan)
	{
		List<String> clanList = new ArrayList<>();
		List<String> ignoredRanks = Text.fromCSV(config.getIgnoredRanks());

		/* ClientThread necessary for method titleForRank */
		thread.invoke(() -> {
			for (ClanMember member : localClan.getMembers())
			{
				ClanRank rank = member.getRank();
				ClanTitle clanTitle = localClan.titleForRank(rank);
				if (clanTitle != null)
				{
					String title = clanTitle.getName();
					if (!ignoredRanks.contains(title))
					{
						/* create a list of clan members retrieved from RuneLite, ignoring excluded ranks */
						clanList.add(format(member.getName()));
					}
				}
			}
			verify(clanList);
		});
	}

	/* Sync members response */
	private void response(String id, TempleSync response, Throwable e)
	{
		/* response is null, exception thrown, or error response */
		if (Objects.isNull(response) || Objects.nonNull(e) || response.error)
		{
			error();
			return;
		}
		reload(id);
	}

	/* Group information response */
	private void response(String id, TempleClan result, Throwable e)
	{
		remove(errorPanel);

		if (!clanLookup.getText().equals(id))
		{
			completed();
			return;
		}

		/* result is null, exception thrown, or error response */
		if (Objects.isNull(result) || Objects.nonNull(e) || result.error)
		{
			error();
			return;
		}
		rebuild(result);
	}

	/* reset clan tab to default */
	private void reset()
	{
		clanAchievements = null;
		clanMembers = null;

		removeAll();
		add(fetchLayout);
		add(errorPanel);

		repaint();
		revalidate();
	}

	/* set fields for error status */
	private void error()
	{
		searchButton.setEnabled(true);
		clanButton.setEnabled(true);
		verifyButton.setEnabled(true);
		clanLookup.setIcon(IconTextField.Icon.ERROR);
		clanLookup.setEditable(true);
	}

	/* set fields for loading status */
	private void loading()
	{
		searchButton.setEnabled(false);
		clanButton.setEnabled(false);
		verifyButton.setEnabled(false);
		clanLookup.setIcon(IconTextField.Icon.LOADING);
		clanLookup.setEditable(false);
	}

	/* set fields for completed status */
	private void completed()
	{
		searchButton.setEnabled(true);
		clanButton.setEnabled(true);
		verifyButton.setEnabled(true);
		clanLookup.setIcon(IconTextField.Icon.SEARCH);
		clanLookup.setEditable(true);
	}

	/* format username to be accepted by Temple API */
	private String format(String text)
	{
		return text.replace('\u00A0', ' ');
	}
}
