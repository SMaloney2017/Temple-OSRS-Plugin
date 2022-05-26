package com.templeosrs.ui.clans;

import com.google.common.base.Strings;
import com.templeosrs.TempleOSRSConfig;
import com.templeosrs.TempleOSRSPlugin;
import static com.templeosrs.util.TempleService.CLAN_PAGE;
import static com.templeosrs.util.TempleService.HOST;
import static com.templeosrs.util.TempleService.fetchClanAsync;
import static com.templeosrs.util.TempleService.postClanMembersAsync;
import com.templeosrs.util.clan.TempleClan;
import com.templeosrs.util.clan.TempleClanAchievement;
import com.templeosrs.util.clan.TempleClanInfo;
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
import net.runelite.api.clan.ClanSettings;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.LinkBrowser;

public class TempleClans extends PluginPanel
{
	private static final Pattern isNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

	public final IconTextField clanLookup;

	private final Client client;

	private final TempleOSRSPlugin plugin;

	private final TempleOSRSConfig config;

	private final JButton verifyButton;

	private final PluginErrorPanel errorPanel = new PluginErrorPanel();

	private final JPanel fetchLayout;

	public TempleClanAchievements clanAchievements;

	public TempleClanMembers clanMembers;

	private JButton searchButton;

	private JButton clanButton;

	@Inject
	public TempleClans(TempleOSRSConfig config, TempleOSRSPlugin plugin, Client client)
	{
		this.plugin = plugin;
		this.client = client;
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
	}

	private IconTextField buildTextField()
	{
		IconTextField lookup = new IconTextField();

		lookup.setIcon(IconTextField.Icon.SEARCH);
		lookup.setEditable(true);
		lookup.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		lookup.setBackground(ColorScheme.SCROLL_TRACK_COLOR);
		lookup.addActionListener(e -> fetchClan());
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

	/* fetch player from search-text-field */
	public void fetchClan()
	{
		final String clanID = clanLookup.getText();

		if (Strings.isNullOrEmpty(clanID))
		{
			return;
		}

		/* clanID must be integer */
		if (!isNumeric.matcher(clanID).matches())
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
				fetchClanAsync(clanID).whenCompleteAsync((result, err) -> rebuild(clanID, result, err));
			}
			catch (Exception ignored)
			{
				error();
			}
		}).start();
	}

	/* reload fetched clan after syncing member-list */
	private void reload(String clanID)
	{
		loading();

		reset();

		try
		{
			fetchClanAsync(clanID).whenCompleteAsync((result, err) -> rebuild(clanID, result, err));
		}
		catch (Exception ignored)
		{
			error();
		}
	}

	private void rebuild(String clanID, TempleClan result, Throwable err)
	{
		remove(errorPanel);

		/* search-text-field has changed since start of fetching player data */
		if (!clanLookup.getText().equals(clanID))
		{
			completed();
			return;
		}

		/* result is null, error is not null, or error response */
		if (Objects.isNull(result) || Objects.nonNull(err) || result.error)
		{
			error();
			return;
		}
		rebuild(result);
	}

	private void rebuild(TempleClan result)
	{
		if (client == null)
		{
			return;
		}

		String[] leaders = result.clanOverview.data.leaders;
		String[] members = result.clanOverview.data.members;
		TempleClanInfo info = result.clanOverview.data.info;
		List<TempleClanAchievement> clanActivity = result.clanAchievements.data;

		/* Event-Dispatch-Thread necessary for adding/ removing new components */
		SwingUtilities.invokeLater(() -> {

			add(new TempleClanOverview(info));

			/* create achievements-component, only add if config option */
			clanAchievements = new TempleClanAchievements(clanActivity);
			if (config.clanAchievements())
			{
				add(clanAchievements);
			}

			/* create members-component, only add if config option */
			clanMembers = new TempleClanMembers(new TempleClanMembersList(plugin, "Leaders", leaders), new TempleClanMembersList(plugin, "Members", members));
			if (config.clanAchievements())
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
		String clanID = clanLookup.getText();
		if (Strings.isNullOrEmpty(clanID))
		{
			return;
		}

		if (!isNumeric.matcher(clanID).matches())
		{
			error();
			return;
		}

		/* if valid clanID, open temple clan-page */
		loading();

		String clanPageURL = HOST + CLAN_PAGE + clanID;
		SwingUtilities.invokeLater(() -> LinkBrowser.browse(clanPageURL));

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
		final int confirmation = JOptionPane.showOptionDialog(verifyButton, "This will sync the fetched clan's TempleOSRS members-list to all members in the current account's clan.",
			"Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
			null, new String[]{
				"Yes",
				"No"
			}, "No");

		if (confirmation != JOptionPane.YES_OPTION)
		{
			return;
		}

		/* create a list of clan members retrieved from RuneLite */
		List<String> clanList = new ArrayList<>();

		for (ClanMember member : localClan.getMembers())
		{
			clanList.add(format(member.getName()));
		}

		loading();


		String clanID = clanLookup.getText();
		/* create separate thread for completing clan-post/ panel reload,
		 *  try to post clan members given clanID, verification, and list,
		 *  when post completes, check response -> reload panel
		 *  if exception, set error status
		 */
		new Thread(() -> {
			try
			{
				postClanMembersAsync(clanID, config.clanKey(), clanList).whenCompleteAsync((result, err) -> response(clanID, result, err));
			}
			catch (Exception ignored)
			{
				error();
			}
		}).start();

	}

	private void response(String clanID, TempleSync response, Throwable err)
	{
		/* response is null, error is not null, or error response */
		if (Objects.isNull(response) || Objects.nonNull(err) || response.error)
		{
			syncingError();
			return;
		}
		reload(clanID);
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
		reset();
	}

	/* sync error -> don't reset panel */
	private void syncingError()
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
