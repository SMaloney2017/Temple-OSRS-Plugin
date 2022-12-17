package com.templeosrs.ui.competitions;

import com.google.common.base.Strings;
import com.templeosrs.TempleOSRSConfig;
import com.templeosrs.TempleOSRSPlugin;
import static com.templeosrs.util.TempleService.fetchCompetitionAsync;
import com.templeosrs.util.comp.TempleCompetition;
import com.templeosrs.util.comp.TempleCompetitionInfo;
import com.templeosrs.util.comp.TempleCompetitionParticipant;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.LinkBrowser;
import okhttp3.HttpUrl;

public class TempleCompetitions extends PluginPanel
{
	private static final Pattern isNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

	public final IconTextField lookup;

	private final Client client;

	private final TempleOSRSPlugin plugin;

	private final TempleOSRSConfig config;

	private final PluginErrorPanel errorPanel = new PluginErrorPanel();

	private final JPanel fetchLayout;

	@Inject
	public TempleCompetitions(TempleOSRSConfig config, TempleOSRSPlugin plugin, Client client)
	{
		this.plugin = plugin;
		this.client = client;
		this.config = config;

		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		fetchLayout = new JPanel();
		fetchLayout.setLayout(new BoxLayout(fetchLayout, BoxLayout.Y_AXIS));
		fetchLayout.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		fetchLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* build and add search-text-field */
		lookup = buildTextField();
		fetchLayout.add(lookup);

		add(fetchLayout);

		/* add default, error-panel when competition has not been fetched yet */
		errorPanel.setContent("Competitions", "You have not fetched competition information yet.");
		add(errorPanel);

		/* load default competition on start-up */
		if (config.fetchDefaults())
		{
			if (config.defaultComp() != 0)
			{
				lookup.setText(Integer.toString(config.defaultComp()));
				fetchCompetition();
			}
		}
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
		lookup.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (config.defaultComp() != 0 && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
				{
					lookup.setText(Integer.toString(config.defaultComp()));
					fetchCompetition();
				}

				if (SwingUtilities.isRightMouseButton(e))
				{
					JPopupMenu menu = new JPopupMenu();
					JMenuItem fetchPlayerMenuItem = new JMenuItem();
					fetchPlayerMenuItem.setText("Search");
					fetchPlayerMenuItem.addActionListener(ev -> fetchCompetition());
					menu.add(fetchPlayerMenuItem);

					JMenuItem openPlayerPageMenuItem = new JMenuItem();
					openPlayerPageMenuItem.setText("Open TempleOSRS");
					openPlayerPageMenuItem.addActionListener(ev -> open());
					menu.add(openPlayerPageMenuItem);
					lookup.add(menu);

					menu.show(lookup, e.getX(), e.getY());
				}
			}
		});
		/* reset on clear */
		lookup.addClearListener(() -> {
			completed();
			reset();
		});

		return lookup;
	}

	/* fetch competition from search-text-field */
	public void fetchCompetition()
	{
		final String id = lookup.getText();

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

		if (!lookup.getText().equals(id))
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
		String id = lookup.getText();
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
		lookup.setIcon(IconTextField.Icon.ERROR);
		lookup.setEditable(true);
	}

	/* set fields for loading status */
	private void loading()
	{
		lookup.setIcon(IconTextField.Icon.LOADING);
		lookup.setEditable(false);
	}

	/* set fields for completed status */
	private void completed()
	{
		lookup.setIcon(IconTextField.Icon.SEARCH);
		lookup.setEditable(true);
	}
}
