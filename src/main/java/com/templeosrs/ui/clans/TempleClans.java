package com.templeosrs.ui.clans;

import com.google.common.base.Strings;
import com.templeosrs.TempleOSRSConfig;
import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.TempleService;
import com.templeosrs.util.clan.TempleClan;
import com.templeosrs.util.clan.TempleClanAchievement;
import com.templeosrs.util.clan.TempleClanOverviewInfo;
import com.templeosrs.util.sync.TempleSync;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;
import net.runelite.client.util.Text;
import okhttp3.HttpUrl;

public class TempleClans extends PluginPanel
{
	private static final Pattern isNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

	public final IconTextField lookup;

	private final Client client;

	private final ClientThread thread;

	private final TempleOSRSPlugin plugin;

	private final TempleOSRSConfig config;

	private final PluginErrorPanel errorPanel = new PluginErrorPanel();

	private final JPanel fetchLayout;

	public TempleClanAchievements clanAchievements;

	public TempleClanMembers clanMembers;

	public TempleClanCurrentTop clanCurrentTop;

	public TempleService service;

	@Inject
	public TempleClans(TempleOSRSConfig config, TempleOSRSPlugin plugin, Client client, ClientThread thread, TempleService templeService)
	{
		this.plugin = plugin;
		this.client = client;
		this.thread = thread;
		this.config = config;
		this.service = templeService;

		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		fetchLayout = new JPanel();
		fetchLayout.setLayout(new BoxLayout(fetchLayout, BoxLayout.Y_AXIS));
		fetchLayout.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		fetchLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* build and add search-text-field */
		JPanel searchLayout = new JPanel();
		searchLayout.setLayout(new BoxLayout(searchLayout, BoxLayout.X_AXIS));
		searchLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		lookup = buildTextField();
		searchLayout.add(lookup);

		JLabel actions = new JLabel();
		actions.setBorder(new EmptyBorder(0, 5, 0, 0));
		actions.setIcon(new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "gears.png")));
		actions.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (client == null)
				{
					return;
				}

				JPopupMenu menu = new JPopupMenu();
				JMenuItem fetchPlayerMenuItem = new JMenuItem();
				fetchPlayerMenuItem.setText("Search");
				fetchPlayerMenuItem.addActionListener(ev -> fetchClan());
				menu.add(fetchPlayerMenuItem);

				JMenuItem openPlayerPageMenuItem = new JMenuItem();
				openPlayerPageMenuItem.setText("Open TempleOSRS");
				openPlayerPageMenuItem.addActionListener(ev -> open());
				menu.add(openPlayerPageMenuItem);

				JMenuItem syncClanMembersMenuItem = new JMenuItem();
				syncClanMembersMenuItem.setText("Sync Clan Members");
				syncClanMembersMenuItem.addActionListener(ev -> verify());
				menu.add(syncClanMembersMenuItem);

				actions.add(menu);
				menu.show(actions, e.getX(), e.getY());
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				actions.setIcon(new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "gears_light.png")));
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				actions.setIcon(new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "gears.png")));
			}

		});

		searchLayout.add(actions);
		fetchLayout.add(searchLayout);

		add(fetchLayout);

		/* add default, error-panel when clan has not been fetched yet */
		errorPanel.setContent("Clans", "You have not fetched clan information yet.");
		add(errorPanel);

		/* load default clan on start-up */
		if (config.fetchDefaults())
		{
			if (config.getDefaultClan() != 0)
			{
				lookup.setText(Integer.toString(config.getDefaultClan()));
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
				if (config.getDefaultClan() != 0 && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
				{
					lookup.setText(Integer.toString(config.getDefaultClan()));
					fetchClan();
				}

				if (SwingUtilities.isRightMouseButton(e))
				{
					JPopupMenu menu = new JPopupMenu();
					JMenuItem fetchPlayerMenuItem = new JMenuItem();
					fetchPlayerMenuItem.setText("Search");
					fetchPlayerMenuItem.addActionListener(ev -> fetchClan());
					menu.add(fetchPlayerMenuItem);

					JMenuItem openPlayerPageMenuItem = new JMenuItem();
					openPlayerPageMenuItem.setText("Open TempleOSRS");
					openPlayerPageMenuItem.addActionListener(ev -> open());
					menu.add(openPlayerPageMenuItem);
					lookup.add(menu);

					JMenuItem syncClanMembersMenuItem = new JMenuItem();
					syncClanMembersMenuItem.setText("Sync Clan Members");
					syncClanMembersMenuItem.addActionListener(ev -> verify());
					menu.add(syncClanMembersMenuItem);
					lookup.add(menu);
					menu.show(lookup, e.getX(), e.getY());
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

	/* fetch clan from search-text-field */
	public void fetchClan()
	{
		final String id = lookup.getText();

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
				service.fetchClanAsync(id, config.getCurrentTopRange().getRange()).whenCompleteAsync((result, err) -> response(id, result, err));
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
			service.fetchClanAsync(id, config.getCurrentTopRange().getRange()).whenCompleteAsync((result, err) -> response(id, result, err));
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
			if (config.displayClanAchievements())
			{
				add(clanAchievements);
			}

			/* create current-top-component, only add if config option enabled */
			TempleClanCurrentTopList clanCurrentTopEhp = new TempleClanCurrentTopList(plugin, result.clanCurrentTopEhp.list, config.getCurrentTopRange());
			TempleClanCurrentTopList clanCurrentTopEhb = new TempleClanCurrentTopList(plugin, result.clanCurrentTopEhb.list, config.getCurrentTopRange());

			clanCurrentTop = new TempleClanCurrentTop(clanCurrentTopEhp, clanCurrentTopEhb);
			if (config.displayClanCurrentTop())
			{
				add(clanCurrentTop);
			}

			/* create members-component, only add if config option */
			clanMembers = new TempleClanMembers(new TempleClanMembersList(plugin, "Leaders", leaders), new TempleClanMembersList(plugin, "Members", members));
			if (config.displayClanMembers())
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
		String id = lookup.getText();
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
		final int confirmation = JOptionPane.showOptionDialog(lookup, "This will sync the fetched clan's TempleOSRS members-list to all members in the current account's clan (Requires Key).",
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

		String id = lookup.getText();
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
					service.addClanMembersAsync(id, config.clanKey(), filteredList).whenCompleteAsync((result, err) -> response(id, result, err));
				}
				else
				{
					service.syncClanMembersAsync(id, config.clanKey(), filteredList).whenCompleteAsync((result, err) -> response(id, result, err));
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

		if (!lookup.getText().equals(id))
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
		lookup.setIcon(IconTextField.Icon.ERROR);
		lookup.setEditable(true);
	}

	/* set fields for loading status */
	private void loading()
	{
		lookup.setIcon(IconTextField.Icon.LOADING);
		lookup.setEditable(false);
	}

	/* set fields for completed status */
	private void completed()
	{
		lookup.setIcon(IconTextField.Icon.SEARCH);
		lookup.setEditable(true);
	}

	/* format username to be accepted by Temple API */
	private String format(String text)
	{
		return text.replace('\u00A0', ' ');
	}
}
