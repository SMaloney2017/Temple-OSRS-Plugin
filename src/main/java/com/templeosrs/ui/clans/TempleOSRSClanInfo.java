package com.templeosrs.ui.clans;

import com.templeosrs.ui.activities.TempleOSRSOverviewSection;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleOSRSClanInfo extends JPanel
{
	public final JPanel clanOverview;

	public final JPanel clanSocials;

	public final TempleOSRSInfoSection clanName;

	public final TempleOSRSInfoSection clanMembers;

	TempleOSRSClanInfo()
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(0, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		JPanel layoutPanel = new JPanel(new BorderLayout());
		layoutPanel.setBorder(new LineBorder(ColorScheme.SCROLL_TRACK_COLOR, 1));

		JLabel clanInfoLabel = new JLabel("Overview");
		clanInfoLabel.setBorder(new EmptyBorder(5, 5, 0, 0));
		clanInfoLabel.setFont(FontManager.getRunescapeBoldFont());
		clanInfoLabel.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);

		JPanel infoSelection = new JPanel();
		infoSelection.setLayout(new BoxLayout(infoSelection, BoxLayout.Y_AXIS));
		infoSelection.add(clanInfoLabel);

		layoutPanel.add(infoSelection);

		clanOverview = new JPanel();
		clanOverview.setBorder(new EmptyBorder(5, 5, 5, 5));
		clanOverview.setLayout(new GridLayout(0, 1));

		clanName = new TempleOSRSInfoSection("Clan Name", ColorScheme.DARKER_GRAY_COLOR);
		clanMembers = new TempleOSRSInfoSection("Members", ColorScheme.DARK_GRAY_HOVER_COLOR);

		clanOverview.add(clanName);
		clanOverview.add(clanMembers);

		clanSocials = new JPanel();
		clanSocials.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		clanSocials.setBorder(new EmptyBorder(0, 10, 0, 0));
		clanSocials.setLayout(new GridLayout(1, 5));
		clanSocials.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		clanOverview.add(clanSocials);

		layoutPanel.add(clanOverview, BorderLayout.SOUTH);
		add(layoutPanel);
	}

	public void reset()
	{
		clanName.reset();
		clanMembers.reset();
		clanSocials.removeAll();
	}
}
