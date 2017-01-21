package uk.ac.aber.dcs.dab14.baz.ui;

import uk.ac.aber.dcs.dab14.baz.being.Bonk;
import uk.ac.aber.dcs.dab14.baz.being.Zap;
import uk.ac.aber.dcs.dab14.baz.game.GameEngine;
import uk.ac.aber.dcs.dab14.baz.being.Position;

import javax.swing.*;
import java.awt.*;

/**
 * This is the class that is responsible for drawing all of the rooms in the window
 *
 * @author Daniel Bursztynski
 */
public class GamePanel extends JPanel {

	public static final Color PANEL_BACKGROUND = Color.BLACK; // Used to set the background colour of the window
	public static final double ROOM_MARGIN = 0; // Used to set a gap/margin around the rooms
	public static final boolean SQUARES_ROOMS = true; // USed to determine if the rooms must be square or to fill the whole window

	@Override
	public void paintComponent(Graphics g) {
		int x, y;
		Position position;

		int panelWidth = getWidth();
		int panelHeight = getHeight();

		double roomWidth = panelWidth / GameEngine.GRID_SIZE_X - ROOM_MARGIN - 2;
		double roomHeight = panelHeight / GameEngine.GRID_SIZE_Y - ROOM_MARGIN - 2;

		if (SQUARES_ROOMS) {
			if (roomWidth < roomHeight) {
				roomHeight = roomWidth;
			} else if (roomHeight < roomWidth) {
				roomWidth = roomHeight;
			}
		}

		double gridWidth = (roomWidth + ROOM_MARGIN) * GameEngine.GRID_SIZE_X - ROOM_MARGIN;
		double gridHeight = (roomHeight + ROOM_MARGIN) * GameEngine.GRID_SIZE_Y - ROOM_MARGIN;

		double gridOffsetX = (panelWidth - gridWidth) / 2;
		double gridOffsetY = (panelHeight - gridHeight) / 2;

		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.clearRect(0, 0, panelWidth, panelHeight);
		g.setColor(PANEL_BACKGROUND);
		g.fillRect(0, 0, panelWidth, panelHeight);

		for (x = 0; x < GameEngine.GRID_SIZE_X; x++) {
			for (y = 0; y < GameEngine.GRID_SIZE_Y; y++) {
				position = GameEngine.getPositionAt(x, y);

				if (position == null) {
					continue;
				}

				double roomX = gridOffsetX + x * roomWidth + (ROOM_MARGIN * x);
				double roomY = gridOffsetY + y * roomHeight + (ROOM_MARGIN * y);

				position.draw(g, (int) roomX, (int) roomY, (int) roomWidth, (int) roomHeight);
			}
		}

		FontMetrics metrics = g.getFontMetrics();
		String[] myMessage = {"Round: " + GameEngine.TURN,
				"Bonks left: " + GameEngine.monsters.stream().filter(m -> (m instanceof Bonk)).count(),
				"Zaps left: " + GameEngine.monsters.stream().filter(m -> (m instanceof Zap)).count()};

		int posIdx = 0;
		for (String s : myMessage) {
			g.setColor(Color.WHITE);
			g.drawString(s, ((int) gridOffsetX / 2), 100 + (20 * posIdx));
			posIdx++;
		}
	}
}
