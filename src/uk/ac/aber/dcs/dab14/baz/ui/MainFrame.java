package uk.ac.aber.dcs.dab14.baz.ui;

import uk.ac.aber.dcs.dab14.baz.Baz;
import uk.ac.aber.dcs.dab14.baz.game.input.InputControl;

import javax.swing.*;
import java.awt.*;

/**
 * Creates a new MainFrame and making sure only one instance is running at any one time.
 * Initialises a new panel and adds a key listener.
 *
 * @author Daniel Bursztynski
 */
public class MainFrame extends JFrame {

	private static MainFrame instance;
	private GamePanel panel;

	/**
	 * Creates a new MainFrame.
	 */
	private MainFrame() {
		super(Baz.APP_NAME);

		init();
	}

	/**
	 * Makes sure only one instance of the MainFrame is
	 * running at any one time
	 *
	 * @return an instance of a MainFrame
	 */
	public static MainFrame getInstance() {
		return instance != null ? instance : (instance = new MainFrame());
		// Could also be written as...
		// if (instance != null) return instance;
		// else return (instance = new MainFrame());
	}

	/**
	 * It return a panel Object.
	 *
	 * @return the BazPanel
	 */
	public GamePanel getPanel() {
		return panel;
	}

	/**
	 * Initialises a new panel and
	 * adds a key listener
	 */
	private void init() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		panel = new GamePanel();

		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);

		addKeyListener(new InputControl());

		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
	}
}