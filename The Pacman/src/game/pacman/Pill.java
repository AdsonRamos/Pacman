package game.pacman;

import java.awt.Color;
import java.awt.Graphics2D;

public class Pill {
	
	private int i, j;
	
	protected int x, y;
	
	protected int width, height;
	
	public Pill(int i, int j, int width, int height) {
	
		this.i = i;
		this.j = j;
		
		this.width = width;
		this.height = height;
		
		x = 2*j - width / 2;
		y = 2*i - height / 2;
		
	}
	
	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public void render(Graphics2D g) {
		g.setColor(Color.GREEN.darker().darker());
		g.fillOval(x, y, width, height);
	}
	
	public void update() {
		
	}
	
}
