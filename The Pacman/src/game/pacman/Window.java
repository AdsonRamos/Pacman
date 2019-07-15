package game.pacman;

import javax.swing.JFrame;

public class Window {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Pac-Man");
		GamePanel game = new GamePanel();
		int width = game.getWidth();
		int height = game.getHeight();
		
		frame.add(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
