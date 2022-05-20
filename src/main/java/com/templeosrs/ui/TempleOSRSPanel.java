package com.templeosrs.ui;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.ui.activities.TempleOSRSRanks;
import com.templeosrs.ui.clans.TempleOSRSGroups;
import com.templeosrs.ui.competitions.TempleOSRSCompetitions;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Instant;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import static net.runelite.client.RuneLite.SCREENSHOT_DIR;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

public class TempleOSRSPanel extends PluginPanel
{
	public static final String DEFAULT = "--";

	private static final String PATH = SCREENSHOT_DIR + File.separator + "TempleOSRS" + File.separator;

	public final TempleOSRSRanks ranks;

	public final TempleOSRSGroups groups;

	public final TempleOSRSCompetitions competitions;

	public final MaterialTabGroup tabGroup;

	public final MaterialTab skillsTab;

	public final MaterialTab groupsTab;

	public final MaterialTab competitionsTab;

	@Inject
	public TempleOSRSPanel(TempleOSRSRanks ranks, TempleOSRSGroups clans, TempleOSRSCompetitions comps)
	{
		this.ranks = ranks;
		this.groups = clans;
		this.competitions = comps;

		getScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));

		JPanel display = new JPanel();
		tabGroup = new MaterialTabGroup(display);

		skillsTab = new MaterialTab("Ranks", tabGroup, ranks);
		groupsTab = new MaterialTab("Clans", tabGroup, groups);
		competitionsTab = new MaterialTab("Competitions", tabGroup, competitions);

		tabGroup.addTab(skillsTab);
		tabGroup.addTab(groupsTab);
		tabGroup.addTab(competitionsTab);

		tabGroup.select(skillsTab);

		layoutPanel.add(tabGroup);
		layoutPanel.add(display);
		layoutPanel.add(buildScreenshots());

		add(layoutPanel);
	}

	private JPanel buildScreenshots()
	{
		JPanel saveLayout = new JPanel(new BorderLayout());
		saveLayout.setBorder(new EmptyBorder(5, 5, 5, 5));
		saveLayout.setOpaque(false);
		JPopupMenu menu = new JPopupMenu();

		JMenuItem takeScreenshot = new JMenuItem();
		takeScreenshot.setText("Take Screenshot of current view...");
		takeScreenshot.addActionListener(e -> screenshot(this));
		menu.add(takeScreenshot);

		JMenuItem openFolder = new JMenuItem();
		openFolder.setText("Open screenshot folder...");
		openFolder.addActionListener(e -> {
			if (SCREENSHOT_DIR.exists() || SCREENSHOT_DIR.mkdirs())
			{
				LinkBrowser.open(SCREENSHOT_DIR.getAbsolutePath());
			}
		});
		menu.add(openFolder);

		JButton screenshotButton = new JButton();
		screenshotButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		screenshotButton.setIcon(new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "save.png")));
		screenshotButton.setBackground(ColorScheme.SCROLL_TRACK_COLOR);
		screenshotButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				menu.show(screenshotButton, e.getX(), e.getY());
			}
		});
		saveLayout.add(screenshotButton, BorderLayout.WEST);

		return saveLayout;
	}

	private void screenshot(JPanel panel)
	{
		String timestamp = String.valueOf(Instant.now().getEpochSecond());

		File directory = new File(PATH);
		if (directory.exists() || directory.mkdirs())
		{
			BufferedImage img = new BufferedImage(panel.getSize().width, panel.getSize().height, BufferedImage.TYPE_INT_RGB);
			panel.paint(img.createGraphics());
			File imageFile = new File(PATH + timestamp + ".png");
			try
			{
				if (imageFile.createNewFile())
				{
					ImageIO.write(img, "png", imageFile);
				}
			}
			catch (Exception ignored)
			{
			}
		}
	}
}
