package game.pacman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MenuPanel extends JPanel implements ActionListener, KeyListener{
	
	private Timer timer;
	
	private BufferedImage title, selector, start, quit;
	
	private int menuChoice = 0;
	
	public static final int WIDTH = 600, HEIGHT = 700;
	
	public MenuPanel() {
		
		timer = new Timer(20, this);
		timer.start();
		
		addKeyListener(this);
		setFocusable(true);
		
		try {
			title = ImageIO.read(new File("images/title.png"));
			selector = ImageIO.read(new File("images/selector.png"));
			start = ImageIO.read(new File("images/start.png"));
			quit = ImageIO.read(new File("images/quit.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void update() {
		
	}
	
	public void render(Graphics2D g) {
		g.setColor(new Color(81, 81, 81));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.drawImage(title, 207, 138, null);
		g.drawImage(start, 246, 290, null);
		g.drawImage(quit, 259, 330, null);
		
		
		if(menuChoice == 0) {
			g.drawImage(selector, 207, 290, null);
		} else if(menuChoice == 1) {
			g.drawImage(selector, 225, 331, null);
		}
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			menuChoice--;
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			menuChoice++;
		} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(menuChoice == 0) {
				
			} else if(menuChoice == 1) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
		}
		
		if(menuChoice < 0) {
			menuChoice = 1;
		} else if(menuChoice > 1) {
			menuChoice = 0;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
	}
	
}
