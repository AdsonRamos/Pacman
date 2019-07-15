package game.pacman;

import java.awt.Graphics2D;

public class Entity {
	protected int x, y, move = 2;
	protected boolean right = false, left = false, up = true, down = false;
	
	protected double count = 0;
	
	public Entity (int x, int y) {
		
		this.x = x;
		this.y = y;
	}
	
	
	public boolean isUp() {
		return up;
	}


	public void setX(int x) {
		this.x = x;
	}


	public void setY(int y) {
		this.y = y;
	}


	public void setUp(boolean up) {
		this.up = up;
	}


	public void update(int playerX, int playerY) {
		this.x = playerX;
		this.y = playerY;
	}
	
	public void render(Graphics2D g) {
		
	}

	public boolean isRight() {
		return right;
	}


	public void setRight(boolean right) {
		this.right = right;
	}


	public boolean isLeft() {
		return left;
	}


	public void setLeft(boolean left) {
		this.left = left;
	}


	public boolean isDown() {
		return down;
	}


	public void setDown(boolean down) {
		this.down = down;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}


	public int getMove() {
		return move;
	}
}
