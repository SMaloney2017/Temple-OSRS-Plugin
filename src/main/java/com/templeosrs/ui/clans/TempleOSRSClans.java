package com.templeosrs.ui.clans;

import com.google.common.base.Strings;
import com.templeosrs.util.TempleOSRSClan;
import static com.templeosrs.util.TempleOSRSService.CLAN_PAGE;
import static com.templeosrs.util.TempleOSRSService.HOST;
import static com.templeosrs.util.TempleOSRSService.fetchClanAsync;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.api.clan.ClanChannel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.LinkBrowser;
import static org.pushingpixels.substance.internal.utils.LazyResettableHashMap.reset;

public class TempleOSRSClans extends PluginPanel
{
	public final IconTextField clanLookup;

	private final Client client;

	private  JButton searchButton;

	private JButton clanButton;
	@Inject
	public TempleOSRSClans(Client client)
	{
		this.client = client;

		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel fetchPlayer = new JPanel();
		fetchPlayer.setLayout(new BoxLayout(fetchPlayer, BoxLayout.Y_AXIS));
		fetchPlayer.setBorder(new EmptyBorder(5, 5, 0, 5));
		fetchPlayer.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		clanLookup = buildTextField();
		fetchPlayer.add(clanLookup);

		JPanel buttons = buildFetchButtons();
		fetchPlayer.add(buttons);

		layoutPanel.add(fetchPlayer);

		add(layoutPanel);
	}

	private IconTextField buildTextField()
	{
		IconTextField lookup = new IconTextField();

		lookup.setIcon(IconTextField.Icon.SEARCH);
		lookup.setEditable(true);
		lookup.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		lookup.setBackground(ColorScheme.SCROLL_TRACK_COLOR);
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
				if (client == null)
				{
					return;
				}

				ClanChannel localClan = client.getClanChannel();

				if (localClan != null)
				{
					fetchClan(localClan.getName());
				}
			}
		});
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
		buttonsLayout.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		searchButton = createNewButton("Search", "Search for player profile");
		searchButton.addActionListener(e -> fetchClan());

		clanButton = createNewButton("Open Page", "Opens TempleOSRS player page");
		clanButton.addActionListener(e -> open());

		buttonsLayout.add(searchButton);
		buttonsLayout.add(clanButton);

		return buttonsLayout;
	}

	private JButton createNewButton(String text, String tooltip)
	{
		JButton newButton = new JButton();

		newButton.setFont(FontManager.getRunescapeFont());
		newButton.setText(text);
		newButton.setToolTipText(tooltip);
		newButton.setForeground(ColorScheme.GRAND_EXCHANGE_LIMIT);

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

	public void fetchClan(String clanname)
	{
		clanLookup.setText(clanname);
		fetchClan();
	}

	public void fetchClan()
	{
		final String clanID = clanLookup.getText();

		if (Strings.isNullOrEmpty(clanID))
		{
			return;
		}

		if (clanID.length() > 12)
		{
			error();
			return;
		}

		loading();

		reset();


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

	private void rebuild(String ClanID, TempleOSRSClan result, Throwable err)
	{

	}

	private void open()
	{
		String clanID = clanLookup.getText();
		if (Strings.isNullOrEmpty(clanID))
		{
			return;
		}
		else if (clanID.length() > 12)
		{
			error();
			return;
		}

		loading();

		String clanPageURL = HOST + CLAN_PAGE + clanID;
		SwingUtilities.invokeLater(() -> LinkBrowser.browse(clanPageURL));

		completed();
	}

	private void completed()
	{
		searchButton.setEnabled(true);
		clanButton.setEnabled(true);
		clanLookup.setIcon(IconTextField.Icon.SEARCH);
		clanLookup.setEditable(true);
	}

	private void loading()
	{
		searchButton.setEnabled(false);
		clanButton.setEnabled(false);
		clanLookup.setIcon(IconTextField.Icon.LOADING);
		clanLookup.setEditable(false);
	}

	private void error()
	{
		searchButton.setEnabled(false);
		clanButton.setEnabled(false);
		clanLookup.setIcon(IconTextField.Icon.ERROR);
		clanLookup.setEditable(false);
	}
}
