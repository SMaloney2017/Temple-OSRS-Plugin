package com.templeosrs.ui.competitions;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;

public class TempleOSRSCompetitions extends PluginPanel
{
	public final IconTextField competitionLookup;

	private JButton searchButton;

	private JButton competitionButton;

	@Inject
	public TempleOSRSCompetitions()
	{
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

//		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Competition");
//		title.setTitleJustification(TitledBorder.RIGHT);
//		setBorder(title);

		JPanel fetchLayout = new JPanel();
		fetchLayout.setLayout(new BoxLayout(fetchLayout, BoxLayout.Y_AXIS));
		fetchLayout.setBorder(new EmptyBorder(5, 5, 0, 5));
		fetchLayout.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		competitionLookup = buildTextField();
		fetchLayout.add(competitionLookup);

		JPanel buttons = buildFetchButtons();
		fetchLayout.add(buttons);

		add(fetchLayout);
	}

	private IconTextField buildTextField()
	{
		IconTextField lookup = new IconTextField();

		lookup.setIcon(IconTextField.Icon.SEARCH);
		lookup.setEditable(true);
		lookup.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		lookup.setBackground(ColorScheme.SCROLL_TRACK_COLOR);
		lookup.addActionListener(e -> fetchCompetition());
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

		searchButton = createNewButton("Search", "Search for competition by ID");
		searchButton.addActionListener(e -> fetchCompetition());

		competitionButton = createNewButton("Open Page", "Opens TempleOSRS competition page");
		competitionButton.addActionListener(e -> open());

		buttonsLayout.add(searchButton);
		buttonsLayout.add(competitionButton);

		return buttonsLayout;
	}

	public JButton createNewButton(String text, String tooltip)
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

	private void fetchCompetition()
	{
	}

	private void open()
	{
	}

	private void reset()
	{
	}

	private void error()
	{
		searchButton.setEnabled(true);
		competitionButton.setEnabled(true);
		competitionLookup.setIcon(IconTextField.Icon.ERROR);
		competitionLookup.setEditable(true);
	}

	private void loading()
	{
		searchButton.setEnabled(false);
		competitionButton.setEnabled(false);
		competitionLookup.setIcon(IconTextField.Icon.LOADING);
		competitionLookup.setEditable(false);
	}

	private void completed()
	{
		searchButton.setEnabled(true);
		competitionButton.setEnabled(true);
		competitionLookup.setIcon(IconTextField.Icon.SEARCH);
		competitionLookup.setEditable(true);
	}
}
