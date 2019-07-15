package game.pacman;

import java.awt.Color;
import java.awt.Graphics2D;

public class PowerPill extends Pill{

	public PowerPill(int i, int j, int width, int height) {
		super(i, j, width, height);
	}

	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.PINK.darker());
		g.fillOval(x, y, width, height);
	}
}
