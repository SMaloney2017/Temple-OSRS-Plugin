package com.templeosrs.ui.clans;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.claninfo.TempleOSRSClanInfo;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

public class TempleOSRSClanOverview extends JPanel
{
	public final JPanel clanSocials;

	public JLabel clanName;

	public JLabel count;

	TempleOSRSClanOverview(TempleOSRSClanInfo info)
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.setBorder(new LineBorder(ColorScheme.SCROLL_TRACK_COLOR, 1));

		clanName = new JLabel(info.name);
		clanName.setBorder(new EmptyBorder(5, 5, 0, 0));
		clanName.setFont(FontManager.getRunescapeBoldFont());
		clanName.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		clanName.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel fieldLayout = new JPanel();
		fieldLayout.setLayout(new FlowLayout());

		JLabel field = new JLabel("Members:");
		field.setBorder(new EmptyBorder(5, 5, 0, 0));
		field.setFont(FontManager.getRunescapeSmallFont());
		field.setAlignmentX(Component.CENTER_ALIGNMENT);
		fieldLayout.add(field);

		count = new JLabel(String.valueOf(info.memberCount));
		count.setBorder(new EmptyBorder(5, 0, 0, 0));
		count.setFont(FontManager.getRunescapeSmallFont());
		fieldLayout.add(count);

		JPanel infoSelection = new JPanel();
		infoSelection.setLayout(new BoxLayout(infoSelection, BoxLayout.Y_AXIS));
		infoSelection.add(clanName);
		infoSelection.add(fieldLayout);

		clanSocials = new JPanel();
		clanSocials.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		clanSocials.setBorder(new EmptyBorder(0, 0, 0, 0));
		clanSocials.setLayout(new FlowLayout());
		clanSocials.setAlignmentX(Component.CENTER_ALIGNMENT);

		if (Objects.nonNull(info.discordLink))
		{
			ImageIcon icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/discord.png"));
			JLabel discord = new JLabel();
			discord.setPreferredSize(new Dimension(16, 16));
			discord.setIcon(icon);
			discord.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					String url = ("https://discord.com/invite/" + info.discordLink).trim();
					SwingUtilities.invokeLater(() -> LinkBrowser.browse(url));
				}
			});
			clanSocials.add(discord);
		}

		if (Objects.nonNull(info.twitterLink))
		{
			ImageIcon icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/twitter.png"));
			JLabel twitter = new JLabel();
			twitter.setPreferredSize(new Dimension(16, 16));
			twitter.setIcon(icon);
			twitter.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					String url = ("https://twitter.com/" + info.twitterLink).trim();
					SwingUtilities.invokeLater(() -> LinkBrowser.browse(url));
				}
			});
			clanSocials.add(twitter);
		}

		if (Objects.nonNull(info.youtubeLink))
		{
			ImageIcon icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/youtube.png"));
			JLabel youtube = new JLabel();
			youtube.setPreferredSize(new Dimension(16, 16));
			youtube.setIcon(icon);
			youtube.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					String url = ("https://www.youtube.com/channel/" + info.youtubeLink).trim();
					SwingUtilities.invokeLater(() -> LinkBrowser.browse(url));
				}
			});
			clanSocials.add(youtube);
		}

		if (Objects.nonNull(info.forumLink))
		{
			ImageIcon icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/forums.png"));
			JLabel forum = new JLabel();
			forum.setPreferredSize(new Dimension(16, 16));
			forum.setIcon(icon);
			forum.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					String url = ("https://secure.runescape.com/m=forum/sl=0/forums?" + info.forumLink).trim();
					SwingUtilities.invokeLater(() -> LinkBrowser.browse(url));
				}
			});
			clanSocials.add(forum);
		}

		if (Objects.nonNull(info.twitchLink))
		{
			ImageIcon icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/discord.png"));
			JLabel twitch = new JLabel();
			twitch.setPreferredSize(new Dimension(16, 16));
			twitch.setIcon(icon);
			twitch.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					String url = ("https://www.twitch.tv/" + info.twitchLink).trim();
					SwingUtilities.invokeLater(() -> LinkBrowser.browse(url));
				}
			});
			clanSocials.add(twitch);
		}

		infoSelection.add(clanSocials);

		layoutPanel.add(infoSelection);

		add(layoutPanel);
	}
}
