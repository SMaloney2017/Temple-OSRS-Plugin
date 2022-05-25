package com.templeosrs.ui.clans;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.clan.TempleClanInfo;
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
	public final JPanel clanSocials;

	public JLabel clanName;

	public JLabel count;

	TempleClanOverview(TempleClanInfo info)
	{
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		clanName = new JLabel(info.name);
		clanName.setBorder(new EmptyBorder(5, 5, 0, 0));
		clanName.setFont(FontManager.getRunescapeBoldFont());
		clanName.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		clanName.setAlignmentX(Component.CENTER_ALIGNMENT);
		layoutPanel.add(clanName);

		JPanel fieldLayout = new JPanel();
		fieldLayout.setLayout(new FlowLayout());
		fieldLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JLabel field = new JLabel("Members:");
		field.setBorder(new EmptyBorder(5, 5, 0, 0));
		field.setFont(FontManager.getRunescapeSmallFont());
		field.setAlignmentX(Component.CENTER_ALIGNMENT);
		fieldLayout.add(field);

		count = new JLabel(String.valueOf(info.memberCount));
		count.setBorder(new EmptyBorder(5, 0, 0, 0));
		count.setFont(FontManager.getRunescapeSmallFont());
		fieldLayout.add(count);

		layoutPanel.add(fieldLayout);

		clanSocials = new JPanel();
		clanSocials.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		clanSocials.setLayout(new FlowLayout());
		clanSocials.setAlignmentX(Component.CENTER_ALIGNMENT);
		clanSocials.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		if (Objects.nonNull(info.discordLink))
		{
			createSocialsButton("https://discord.com/invite/" + info.discordLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/discord.png")));
		}

		if (Objects.nonNull(info.twitterLink))
		{
			createSocialsButton("https://twitter.com/" + info.twitterLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/twitter.png")));
		}

		if (Objects.nonNull(info.youtubeLink))
		{
			createSocialsButton("https://www.youtube.com/channel/" + info.youtubeLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/youtube.png")));
		}

		if (Objects.nonNull(info.forumLink))
		{
			createSocialsButton("https://secure.runescape.com/m=forum/sl=0/forums?" + info.forumLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/forums.png")));
		}

		if (Objects.nonNull(info.twitchLink))
		{
			createSocialsButton("https://www.twitch.tv/" + info.twitchLink, new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "clans/discord.png")));
		}

		if (clanSocials.getComponentCount() > 0)
		{
			layoutPanel.add(clanSocials);
		}

		add(layoutPanel);
	}

	private void createSocialsButton(String link, ImageIcon icon)
	{
		JLabel social = new JLabel();
		social.setPreferredSize(new Dimension(16, 16));
		social.setIcon(icon);
		social.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String url = link.trim();
				SwingUtilities.invokeLater(() -> LinkBrowser.browse(url));
			}
		});
		clanSocials.add(social);
	}
}
