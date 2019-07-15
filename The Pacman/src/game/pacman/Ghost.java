package game.pacman;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ghost extends Entity{
	
	
	public static final int BLUE = 5;
	public static final int GREEN = 6;
	public static final int ORANGE = 7;
	public static final int RED = 8;
	
	private BufferedImage ghost;
	private BufferedImage ghostSprite[];
	private BufferedImage ghostSpriteEdible[];
	private int i, j;
	private int originalI, originalJ;
	
	
	private int width, height;
	
	private boolean edible = false;

	public Ghost(int x, int y, int i, int j, int type) {
		super(x, y);
		
		this.originalI = this.i = i;
		this.originalJ = this.j = j;
		
		width = 30;
		height = 30;
		
		ghostSprite = new BufferedImage[3];
		ghostSpriteEdible = new BufferedImage[3];
		
		try {
			if(type == BLUE) {
				ghost = ImageIO.read(new File("images/ghost_blue.png"));
			} else if(type == GREEN) {
				ghost = ImageIO.read(new File("images/ghost_green.png"));
			} else if(type == ORANGE) {
				ghost = ImageIO.read(new File("images/ghost_orange.png"));
			} else if(type == RED) {
				ghost = ImageIO.read(new File("images/ghost_red.png"));
			}
			ghostSprite[0] = ghost.getSubimage(0, 0, 30, 30);
			ghostSprite[1] = ghost.getSubimage(30, 0, 30, 30);
			ghostSprite[2] = ghost.getSubimage(60, 0, 30, 30);
			
			ghost = ImageIO.read(new File("images/ghost_edible.png"));
			ghostSpriteEdible[0] = ghost.getSubimage(0, 0, 30, 30);
			ghostSpriteEdible[1] = ghost.getSubimage(30, 0, 30, 30);
			ghostSpriteEdible[2] = ghost.getSubimage(60, 0, 30, 30);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void respawn() {
		this.i = originalI;
		this.j = originalJ;
		this.x = originalJ * 2;
		this.y = originalI * 2;
		this.edible = false;
	}
	
	public int getOriginalI() {
		return originalI;
	}

	public int getOriginalJ() {
		return originalJ;
	}

	public boolean isEdible() {
		return edible;
	}

	public void setEdible(boolean edible) {
		this.edible = edible;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void render(Graphics2D g) {
		count += 0.15;
		
		if(edible) {
			g.drawImage(ghostSpriteEdible[(int) count % 3], x - 15, y - 15, null);
		} else {
			g.drawImage(ghostSprite[(int) count % 3], x - 15, y - 15, null);			
		}
	}
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}
	
	public void setJ(int j) {
		this.j = j;
	}
	
	public void setI(int i) {
		this.i = i;
	}
}
