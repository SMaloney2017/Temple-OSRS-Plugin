package com.templeosrs.ui.competitions;

import com.google.common.base.Strings;
import com.templeosrs.util.TempleOSRSCompetition;
import static com.templeosrs.util.TempleOSRSService.fetchCompetitionAsync;
import com.templeosrs.util.compinfo.TempleOSRSCompInfo;
import com.templeosrs.util.compinfo.TempleOSRSCompParticipant;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;

public class TempleOSRSCompetitions extends PluginPanel
{
	private static final Pattern isNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

	public final IconTextField competitionLookup;

	private final Client client;

	private final PluginErrorPanel errorPanel = new PluginErrorPanel();

	private JButton searchButton;

	private JButton competitionButton;

	@Inject
	public TempleOSRSCompetitions(Client client)
	{
		this.client = client;

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

		errorPanel.setContent("Clans", "You have not fetched clan information yet.");
		add(errorPanel);
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

	public void fetchCompetition()
	{
		final String compID = competitionLookup.getText();

		if (Strings.isNullOrEmpty(compID))
		{
			return;
		}

		if (!isNumeric.matcher(compID).matches())
		{
			error();
			return;
		}

		loading();

		reset();

		new Thread(() -> {
			try
			{
				fetchCompetitionAsync(compID).whenCompleteAsync((result, err) -> rebuild(compID, result, err));
			}
			catch (Exception ignored)
			{
				error();
			}
		}).start();
	}

	private void rebuild(String clanID, TempleOSRSCompetition result, Throwable err)
	{
		remove(errorPanel);

		if (!competitionLookup.getText().equals(clanID))
		{
			completed();
			return;
		}

		if (Objects.isNull(result) || Objects.nonNull(err) || result.error)
		{
			error();
			return;
		}
		rebuild(result);
	}

	private void rebuild(TempleOSRSCompetition result)
	{
		if (client == null)
		{
			return;
		}

		TempleOSRSCompInfo info = result.compOverview.data.info;
		List<TempleOSRSCompParticipant> participants = result.compOverview.data.participants;
		SwingUtilities.invokeLater(() -> {
			TempleOSRSRankings rankings = new TempleOSRSRankings(participants);

			TempleOSRSCompOverview compOverview  = new TempleOSRSCompOverview(info, rankings.i);

			add(compOverview);
			add(rankings);
		});

		completed();

		revalidate();
		repaint();
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
