package com.templeosrs.ui.clans;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.clan.TempleClanOverviewInfo;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

public class TempleClanOverview extends JPanel
{

	TempleClanOverview(TempleClanOverviewInfo info)
	{
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* add name to layout */
		JLabel clanName = new JLabel(info.name);
		clanName.setBorder(new EmptyBorder(5, 5, 0, 0));
		clanName.setFont(FontManager.getRunescapeBoldFont());
		clanName.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		clanName.setAlignmentX(Component.CENTER_ALIGNMENT);
		clanName.setToolTipText(
			"<html> <table>" +
				"<tr><th style='text-align: left'>Total Xp: </th><td style='color:#6ee16e'>" + info.totalXp + "</td></tr>" +
				"<tr><th style='text-align: left'>Avg. Ehp: </th><td style='color:#6ee16e'>" + String.format("%.2f", info.averageEhp) + "</td></tr>" +
				"<tr><th style='text-align: left'>Avg. Ehb: </th><td style='color:#6ee16e'>" + String.format("%.2f", info.averageEhb) + "</td></tr>" +
				"</table> </html>");
		layoutPanel.add(clanName);

		JPanel fieldLayout = new JPanel();
		fieldLayout.setLayout(new FlowLayout());
		fieldLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* create and add members-count to layout */
		JLabel field = new JLabel("Members:");
		field.setBorder(new EmptyBorder(5, 5, 0, 0));
		field.setFont(FontManager.getRunescapeSmallFont());
		field.setAlignmentX(Component.CENTER_ALIGNMENT);
		fieldLayout.add(field);

		JLabel count = new JLabel(String.valueOf(info.memberCount));
		count.setBorder(new EmptyBorder(5, 0, 0, 0));
		count.setFont(FontManager.getRunescapeSmallFont());
		fieldLayout.add(count);

		layoutPanel.add(fieldLayout);

		/* create and add socials to layout */
		JPanel clanSocials = new JPanel();
		clanSocials.setPreferredSize(new Dimension(PANEL_WIDTH, 30));
		clanSocials.setLayout(new FlowLayout());
		clanSocials.setAlignmentX(Component.CENTER_ALIGNMENT);
		clanSocials.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* if social link exists, create and add button to socials-panel */
		if (Objects.nonNull(info.discordLink))
		{
			JButton social = createSocialsButton("https://discord.com/invite/" + info.discordLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/discord.png")));
			clanSocials.add(social);
		}

		if (Objects.nonNull(info.twitterLink))
		{
			JButton social = createSocialsButton("https://twitter.com/" + info.twitterLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/twitter.png")));
			clanSocials.add(social);
		}

		if (Objects.nonNull(info.youtubeLink))
		{
			JButton social = createSocialsButton("https://www.youtube.com/channel/" + info.youtubeLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/youtube.png")));
			clanSocials.add(social);
		}

		if (Objects.nonNull(info.forumLink))
		{
			JButton social = createSocialsButton("https://secure.runescape.com/m=forum/sl=0/forums?" + info.forumLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/forums.png")));
			clanSocials.add(social);
		}

		if (Objects.nonNull(info.twitchLink))
		{
			JButton social = createSocialsButton("https://www.twitch.tv/" + info.twitchLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/discord.png")));
			clanSocials.add(social);
		}

		/* if there is at least one social linked, add to layout */
		if (clanSocials.getComponentCount() > 0)
		{
			layoutPanel.add(clanSocials);
		}

		add(layoutPanel);
	}

	private JButton createSocialsButton(String link, ImageIcon icon)
	{
		JButton socialButton = new JButton();
		socialButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		socialButton.setBorder(new EmptyBorder(4, 4, 4, 4));

		JLabel socialLabel = new JLabel();
		socialLabel.setPreferredSize(new Dimension(16, 16));
		socialLabel.setIcon(icon);

		/* on social-icon click mouse-event, open link to social */
		socialButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String url = link.trim();
				SwingUtilities.invokeLater(() -> LinkBrowser.browse(url));
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				socialButton.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				socialButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
			}
		});

		socialButton.add(socialLabel);

		return socialButton;
	}
}
