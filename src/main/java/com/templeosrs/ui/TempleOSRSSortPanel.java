package com.templeosrs.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import javax.swing.JPanel;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleOSRSSortPanel extends JPanel
{
	private final TempleOSRSSortFilter name;

	private final TempleOSRSSortFilter total;

	private final TempleOSRSSortFilter rank;

	private final TempleOSRSSortFilter ehp;

	TempleOSRSSortPanel(TempleOSRSActivityPanel panel, HiscoreSkillType type)
	{
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
		setPreferredSize(new Dimension(PANEL_WIDTH, 20));

		JPanel display = new JPanel();

		name = new TempleOSRSSortFilter("Name");
		name.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				reset(total, rank, ehp);
				Comparator<TempleOSRSTableRow> comparator = name.increasing ? Comparator.comparing((TempleOSRSTableRow row) -> row.name) : Comparator.comparing((TempleOSRSTableRow row) -> row.name).reversed();
				panel.sort(comparator);
			}
		});

		total = new TempleOSRSSortFilter("Total");
		total.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				reset(name, rank, ehp);
				Comparator<TempleOSRSTableRow> comparator = total.increasing ? Comparator.comparingDouble((TempleOSRSTableRow row) -> row.total) : Comparator.comparingDouble((TempleOSRSTableRow row) -> row.total).reversed();
				panel.sort(comparator);
			}
		});

		rank = new TempleOSRSSortFilter("Rank");
		rank.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				reset(total, name, ehp);
				Comparator<TempleOSRSTableRow> comparator = rank.increasing ? Comparator.comparingDouble((TempleOSRSTableRow row) -> row.rank) : Comparator.comparingDouble((TempleOSRSTableRow row) -> row.rank).reversed();
				panel.sort(comparator);
			}
		});

		ehp = new TempleOSRSSortFilter(type.equals(HiscoreSkillType.SKILL) ? "EHP" : "EHB");
		ehp.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				reset(total, name, rank);
				Comparator<TempleOSRSTableRow> comparator = ehp.increasing ? Comparator.comparingDouble((TempleOSRSTableRow row) -> row.ehp) : Comparator.comparingDouble((TempleOSRSTableRow row) -> row.ehp).reversed();
				panel.sort(comparator);
			}
		});

		display.setLayout(new GridLayout(1, 4));
		display.add(name);
		display.add(total);
		display.add(rank);
		display.add(ehp);
		add(display);

	}

	private void reset(TempleOSRSSortFilter... types)
	{
		for (TempleOSRSSortFilter t : types)
		{
			t.reset();
			t.increasing = false;
		}
	}

	void reset()
	{
		reset(name, total, rank, ehp);
	}
}
