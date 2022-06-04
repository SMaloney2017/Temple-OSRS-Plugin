package com.templeosrs.ui.competitions;

import com.google.common.base.Strings;
import com.templeosrs.TempleOSRSPlugin;
import static com.templeosrs.util.TempleService.fetchCompetitionAsync;
import com.templeosrs.util.comp.TempleCompetition;
import com.templeosrs.util.comp.TempleCompetitionInfo;
import com.templeosrs.util.comp.TempleCompetitionParticipant;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.LinkBrowser;
import okhttp3.HttpUrl;

public class TempleCompetitions extends PluginPanel
{
	private static final Pattern isNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

	public final IconTextField competitionLookup;

	private final Client client;

	private final TempleOSRSPlugin plugin;

	private final PluginErrorPanel errorPanel = new PluginErrorPanel();

	private final JPanel fetchLayout;

	private JButton searchButton;

	private JButton competitionButton;

	@Inject
	public TempleCompetitions(TempleOSRSPlugin plugin, Client client)
	{
		this.client = client;
		this.plugin = plugin;

		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		fetchLayout = new JPanel();
		fetchLayout.setLayout(new BoxLayout(fetchLayout, BoxLayout.Y_AXIS));
		fetchLayout.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		fetchLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* build and add search-text-field */
		competitionLookup = buildTextField();
		fetchLayout.add(competitionLookup);

		/* build and add search-buttons */
		JPanel buttons = buildFetchButtons();
		fetchLayout.add(buttons);

		add(fetchLayout);

		/* add default, error-panel when competition has not been fetched yet */
		errorPanel.setContent("Competitions", "You have not fetched competition information yet.");
		add(errorPanel);
	}

	private IconTextField buildTextField()
	{
		IconTextField lookup = new IconTextField();

		lookup.setIcon(IconTextField.Icon.SEARCH);
		lookup.setEditable(true);
		lookup.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		lookup.setBackground(ColorScheme.SCROLL_TRACK_COLOR);
		/* fetch competition on action */
		lookup.addActionListener(e -> fetchCompetition());
		/* reset on clear */
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
		buttonsLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		searchButton = createNewButton("Search", "Search for competition by ID (Found in TempleOSRS competition-page url)");
		searchButton.addActionListener(e -> fetchCompetition());

		competitionButton = createNewButton("Open Page", "Opens TempleOSRS competition page");
		competitionButton.addActionListener(e -> open());

		buttonsLayout.add(searchButton);
		buttonsLayout.add(competitionButton);

		return buttonsLayout;
	}

	/* create a single, JButton with similar style */
	public JButton createNewButton(String text, String tooltip)
	{
		JButton newButton = new JButton();

		newButton.setFont(FontManager.getRunescapeFont());
		newButton.setText(text);
		newButton.setToolTipText(tooltip);
		newButton.setForeground(ColorScheme.GRAND_EXCHANGE_LIMIT);

		/* add hover mouse-listener for buttons */
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

	/* fetch competition from search-text-field */
	public void fetchCompetition()
	{
		final String id = competitionLookup.getText();

		if (Strings.isNullOrEmpty(id))
		{
			return;
		}

		/* competition-id must be integer */
		if (!isNumeric.matcher(id).matches())
		{
			error();
			return;
		}

		loading();

		reset();

		/* create separate thread for completing competition-fetch/ panel rebuilds,
		 *  try to fetch competition,
		 *  when fetching completes, rebuild panel
		 *  if exception, set error status
		 */
		new Thread(() -> {
			try
			{
				fetchCompetitionAsync(id).whenCompleteAsync((result, err) -> response(id, result, err));
			}
			catch (Exception e)
			{
				error();
			}
		}).start();
	}

	private void response(String id, TempleCompetition result, Throwable e)
	{
		remove(errorPanel);

		if (!competitionLookup.getText().equals(id))
		{
			completed();
			return;
		}

		/* result is null, exception thrown, or error response */
		if (Objects.isNull(result) || Objects.nonNull(e) || result.error)
		{
			error();
			return;
		}
		rebuild(result);
	}

	private void rebuild(TempleCompetition result)
	{
		if (client == null)
		{
			return;
		}

		TempleCompetitionInfo info = result.compOverview.data.info;
		List<TempleCompetitionParticipant> participants = result.compOverview.data.participants;

		/* Event-Dispatch-Thread necessary for adding/ removing new components */
		SwingUtilities.invokeLater(() -> {
			/* create and add rankings/ overview */
			TempleCompetitionRankings rankings = new TempleCompetitionRankings(plugin, participants);

			TempleCompetitionOverview compOverview = new TempleCompetitionOverview(info, rankings.i);

			add(compOverview);
			add(rankings);
		});

		completed();

		revalidate();
		repaint();
	}

	private void open()
	{
		String id = competitionLookup.getText();
		if (Strings.isNullOrEmpty(id))
		{
			return;
		}

		if (!isNumeric.matcher(id).matches())
		{
			error();
			return;
		}

		/* if valid competition-id, open temple competition-page */
		loading();

		HttpUrl url = new HttpUrl.Builder()
			.scheme("https")
			.host("templeosrs.com")
			.addPathSegment("competitions")
			.addPathSegment("standings.php")
			.addQueryParameter("id", id).build();

		SwingUtilities.invokeLater(() -> LinkBrowser.browse(url.toString()));

		completed();
	}

	/* reset completion tab to default */
	private void reset()
	{
		removeAll();
		add(fetchLayout);
		add(errorPanel);

		repaint();
		revalidate();
	}

	/* set fields for error status */
	private void error()
	{
		searchButton.setEnabled(true);
		competitionButton.setEnabled(true);
		competitionLookup.setIcon(IconTextField.Icon.ERROR);
		competitionLookup.setEditable(true);
	}

	/* set fields for loading status */
	private void loading()
	{
		searchButton.setEnabled(false);
		competitionButton.setEnabled(false);
		competitionLookup.setIcon(IconTextField.Icon.LOADING);
		competitionLookup.setEditable(false);
	}

	/* set fields for completed status */
	private void completed()
	{
		searchButton.setEnabled(true);
		competitionButton.setEnabled(true);
		competitionLookup.setIcon(IconTextField.Icon.SEARCH);
		competitionLookup.setEditable(true);
	}
}
