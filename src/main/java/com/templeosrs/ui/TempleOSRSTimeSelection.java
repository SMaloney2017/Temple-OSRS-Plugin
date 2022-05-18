package com.templeosrs.ui;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class TempleOSRSTimeSelection extends JPanel
{
	private static final String[] OPTIONS = {
		"All Time", "Day", "Week", "Month", "Six Months", "Year"
	};

	public static JComboBox<String> jComboBox;

	TempleOSRSTimeSelection(TempleOSRSPanel panel)
	{
		setLayout(new BorderLayout());

		jComboBox = new JComboBox<>(OPTIONS);
		jComboBox.addActionListener(e -> panel.fetchUser());

		add(jComboBox);
	}
}
