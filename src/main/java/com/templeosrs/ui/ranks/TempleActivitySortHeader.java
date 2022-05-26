package com.templeosrs.ui.ranks;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleActivitySortHeader extends JPanel
{
	private final TempleActivitySortFilter name;

	private final TempleActivitySortFilter total;

	private final TempleActivitySortFilter rank;

	private final TempleActivitySortFilter ehp;

	TempleActivitySortHeader(TempleActivity panel, HiscoreSkillType type)
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
		setPreferredSize(new Dimension(PANEL_WIDTH, 20));

		/* create sorting-filters with unique comparators (Name, Total, Rank, Ehp),
		 * reset all other filters on selection */
		name = new TempleActivitySortFilter("Name");
		name.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				reset(total, rank, ehp);
				Comparator<TempleActivityTableRow> comparator = name.increasing ? Comparator.comparing((TempleActivityTableRow row) -> row.name) : Comparator.comparing((TempleActivityTableRow row) -> row.name).reversed();
				/* resort skills-panel on click */
				panel.sort(comparator);
			}
		});

		total = new TempleActivitySortFilter("Total");
		total.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				reset(name, rank, ehp);
				Comparator<TempleActivityTableRow> comparator = total.increasing ? Comparator.comparingDouble((TempleActivityTableRow row) -> row.total) : Comparator.comparingDouble((TempleActivityTableRow row) -> row.total).reversed();
				panel.sort(comparator);
			}
		});

		rank = new TempleActivitySortFilter("Rank");
		rank.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				reset(total, name, ehp);
				Comparator<TempleActivityTableRow> comparator = rank.increasing ? Comparator.comparingDouble((TempleActivityTableRow row) -> row.rank) : Comparator.comparingDouble((TempleActivityTableRow row) -> row.rank).reversed();
				panel.sort(comparator);
			}
		});

		ehp = new TempleActivitySortFilter(type.equals(HiscoreSkillType.SKILL) ? "EHP" : "EHB");
		ehp.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				reset(total, name, rank);
				Comparator<TempleActivityTableRow> comparator = ehp.increasing ? Comparator.comparingDouble((TempleActivityTableRow row) -> row.ehp) : Comparator.comparingDouble((TempleActivityTableRow row) -> row.ehp).reversed();
				panel.sort(comparator);
			}
		});

		JPanel display = new JPanel();
		display.setLayout(new GridLayout(1, 4));

		/* add filters to sort-panel */
		display.add(name);
		display.add(total);
		display.add(rank);
		display.add(ehp);

		add(display);
	}

	/* reset a list of filters to default */
	private void reset(TempleActivitySortFilter... types)
	{
		for (TempleActivitySortFilter t : types)
		{
			t.reset();
			t.increasing = false;
		}
	}

	/* reset all filters to default */
	void reset()
	{
		reset(name, total, rank, ehp);
	}
}
