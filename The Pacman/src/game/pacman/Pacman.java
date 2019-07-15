package game.pacman;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pacman extends Entity{
	
	private BufferedImage image;
	private BufferedImage[] pacmanLeft;
	private BufferedImage[] pacmanRight;
	private BufferedImage[] pacmanUp;
	private BufferedImage[] pacmanDown;
	
	private int width, height;
	
	protected double count = 0;
	
	private int originalX, originalY;
	private int originalI, originalJ;
	
	
	public Pacman(int x, int y, int originalI, int originalJ) {
		super(x, y);
		
		this.originalX = x;
		this.originalY = y;
		
		this.originalI = originalI;
		this.originalJ = originalJ;
		
		width = height = 30;
		
		try {
			image = ImageIO.read(new File("images/pac.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pacmanLeft = new BufferedImage[3];
		pacmanLeft[0] = image.getSubimage(0, 0, 30, 30);
		pacmanLeft[1] = image.getSubimage(30, 0, 30, 30);
		pacmanLeft[2] = image.getSubimage(60, 0, 30, 30);
		
		pacmanRight = new BufferedImage[3];
		pacmanRight[0] = image.getSubimage(0, 30, 30, 30);
		pacmanRight[1] = image.getSubimage(30, 30, 30, 30);
		pacmanRight[2] = image.getSubimage(60, 30, 30, 30);
		
		pacmanDown = new BufferedImage[3];
		pacmanDown[0] = image.getSubimage(0, 60, 30, 30);
		pacmanDown[1] = image.getSubimage(30, 60, 30, 30);
		pacmanDown[2] = image.getSubimage(60, 60, 30, 30);
		
		pacmanUp = new BufferedImage[3];
		pacmanUp[0] = image.getSubimage(0, 90, 30, 30);
		pacmanUp[1] = image.getSubimage(30, 90, 30, 30);
		pacmanUp[2] = image.getSubimage(60, 90, 30, 30);
		
		this.x = x;
		this.y = y;
	}
	
	public int getOriginalI() {
		return originalI;
	}

	public int getOriginalJ() {
		return originalJ;
	}

	public void respawn() {
		this.x = originalX;
		this.y = originalY;
	}
	
	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}


	@Override
	public void render(Graphics2D g) {
		count = count + 0.3;
		if(left) {
			g.drawImage(pacmanLeft[(int)count % 3], x - 15, y - 15, null);
		} else if(right) {
			g.drawImage(pacmanRight[(int) count % 3], x - 15, y - 15, null);
		} else if(up) {
			g.drawImage(pacmanUp[(int)count % 3], x - 15, y - 15, null);
		} else if(down) {
			g.drawImage(pacmanDown[(int)count % 3], x - 15, y - 15, null);
		} 
	}

}
