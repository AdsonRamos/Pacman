package game.pacman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener, KeyListener {
	public static final int WIDTH = 600, HEIGHT = 700;

	private Timer timer;

	private File mapFile;

	private Scanner scanner;

	private int[][] map;

	private ArrayList<String> lines = new ArrayList<>();

	private Pacman pacman;

	private Ghost[] ghosts;
	private int nGhosts = 0;

	private int playerX, playerY;

	private int playerI, playerJ;

	private boolean pause = false;

	private int reqDirPlayer = -1, curDirPlayer = -1; // 0 - up, 1 - down, 2 - left, 3 - right;
	private int[] reqDirEnemy = new int[4], curDirEnemy = new int[4];

	private ArrayList<Pill> pills;
	private ArrayList<PowerPill> power;

	private int nPills, nPower;
	
	private int score = 0;

	private BufferedImage background;

	private Random r;
	
	private boolean stopGhosts = false;
	
	private int lives = 3;
	
	private boolean eatPower = false;
	private int countEdible = 0;

	private boolean gameOver;

	/*
	 * Configurar power pill
	 * 
	 * 
	 */

	public GamePanel() {

		addKeyListener(this);
		setFocusable(true);

		pills = new ArrayList<>();
		power = new ArrayList<>();

		ghosts = new Ghost[4];
		loadMap();

		try {
			background = ImageIO.read(new File("images/map.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		r = new Random();

		timer = new Timer(10, this);
		timer.start();
		
	}

	private void loadMap() {
		mapFile = new File("maze.txt");

		try {
			scanner = new Scanner(mapFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNextLine()) {
			lines.add(scanner.nextLine());
		}

		map = new int[lines.size()][lines.get(0).length()];

		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(i).length(); j++) {
				map[i][j] = Integer.parseInt("" + lines.get(i).charAt(j));

				if (lines.get(i).charAt(j) == '4') {

					playerI = i;
					playerJ = j;

					playerX = j * 2;
					playerY = i * 2;
					
					pacman = new Pacman(playerX, playerY, i, j);

				} else if (lines.get(i).charAt(j) == '2') {
					pills.add(new Pill(i, j, 10, 10));
				} else if (lines.get(i).charAt(j) == '3') {
					power.add(new PowerPill(i, j, 16, 16));
				} else if (lines.get(i).charAt(j) == '5') {
					ghosts[nGhosts] = new Ghost(j * 2, i * 2, i, j, Ghost.BLUE);
					nGhosts++;
				} else if (lines.get(i).charAt(j) == '6') {
					ghosts[nGhosts] = new Ghost(j * 2, i * 2, i, j, Ghost.GREEN);
					nGhosts++;
				} else if (lines.get(i).charAt(j) == '7') {
					ghosts[nGhosts] = new Ghost(j * 2, i * 2, i, j, Ghost.ORANGE);
					nGhosts++;
				} else if (lines.get(i).charAt(j) == '8') {
					ghosts[nGhosts] = new Ghost(j * 2, i * 2, i, j, Ghost.RED);
					nGhosts++;
				}
			}
		}

		nPills = pills.size();
		nPower = power.size();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			reqDirPlayer = 2;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			reqDirPlayer = 3;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			reqDirPlayer = 0;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			reqDirPlayer = 1;
		} else if (e.getKeyCode() == KeyEvent.VK_F) {
			reqDirEnemy[0] = r.nextInt(4);
			reqDirEnemy[1] = r.nextInt(4);
			reqDirEnemy[2] = r.nextInt(4);
			reqDirEnemy[3] = r.nextInt(4);
		} else if(e.getKeyCode() == KeyEvent.VK_Y) {
			System.out.println(ghosts[0].getOriginalI() + " - "+ghosts[0].getI());			
		} else if(e.getKeyCode() ==  KeyEvent.VK_S) {
			// stop ghosts
			stopGhosts = !stopGhosts;
		}
		
		
		else if (e.getKeyCode() == KeyEvent.VK_P) {
			pause = !pause;
			if (pause) {
				timer.stop();
			} else {
				timer.start();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
	}

	private void update() {

		movePlayer();

		if(!stopGhosts) {
			moveGhost();			
		}
		

		if (map[playerI][playerJ] == Integer.parseInt('2' + "")) {
			for (int i = 0; i < pills.size(); i++) {
				if (pills.get(i).getI() == playerI && pills.get(i).getJ() == playerJ) {
					pills.remove(i);
				}
			}
		} else if (map[playerI][playerJ] == Integer.parseInt('3' + "")) {
			eatPower = true;
			countEdible = 0;
			
			// set ghosts edible
			for(int i = 0; i < ghosts.length; i++) {
				ghosts[i].setEdible(true);
			}
			
			// remove power pills from the map
			for (int i = 0; i < power.size(); i++) {
				if (power.get(i).getI() == playerI && power.get(i).getJ() == playerJ) {
					power.remove(i);
				}
			}
			
		}
		
		if(eatPower) {
			countEdible++;
			if(countEdible >= 300) {
				countEdible = 0;
				eatPower = !eatPower;
				for(int i = 0; i < ghosts.length; i++) {
					ghosts[i].setEdible(false);
				}
			}
		}
		
		// check if player wins
		if (pills.size() == 0 && power.size() == 0) {
			System.out.println("Fim do jogo");
		}
		
		// check if player collide with enemy
		for(int i = 0; i < nGhosts; i++) {
			if(new Rectangle(ghosts[i].getX(), ghosts[i].getY(), ghosts[i].getWidth(), ghosts[i].getHeight())
					.intersects(new Rectangle(pacman.getX(), pacman.getY(), pacman.getWidth(), pacman.getHeight()))) {
				//System.out.println("Player is colliding with ghost "+ (i + 1));
				
				if(eatPower) {
					// enemy dies
					score += 200;
					ghosts[i].respawn();
					repaint();
					
				} else {
					// pacman loses 1 life
					lives--;
					playerI = pacman.getOriginalI();
					playerJ = pacman.getOriginalJ();
					playerX = playerJ * 2;
					playerY = playerI * 2;
					pacman.respawn();
					repaint();
				}
			}
		}
		
		if(lives < 0) {
			gameOver = true;
		}

		
	}

	private void moveGhost() {
		for (int i = 0; i < nGhosts; i++) {
			if (reqDirEnemy[i] == 2 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0) {
				// left
				curDirEnemy[i] = 2;
			}

			if (reqDirEnemy[i] == 3 && map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
				// right
				curDirEnemy[i] = 3;
			}

			if (reqDirEnemy[i] == 0 && map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0) {
				// up
				curDirEnemy[i] = 0;
			}

			if (reqDirEnemy[i] == 1 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0) {
				// down
				curDirEnemy[i] = 1;
			}

			if (curDirEnemy[i] == 2) {
				// moving left
				if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0) {
					reqDirEnemy[i] = curDirEnemy[i] = r.nextInt(3);
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() - 1] == 0) {
					reqDirEnemy[i] = curDirEnemy[i] = r.nextInt(2);
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] == 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() - 1] == 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 0; // up
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 3; // right
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() - 1] == 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 1; // down
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 3; // right
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 1; // down
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 2; // left
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] == 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 0; // up
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 2; // left
					}
				}
			} else if (curDirEnemy[i] == 3) {
				// moving right
				if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					reqDirEnemy[i] = curDirEnemy[i] = r.nextInt(3);
					if (reqDirEnemy[i] == 2) {
						reqDirEnemy[i] = curDirEnemy[i] = 3;
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] == 0) {
					reqDirEnemy[i] = curDirEnemy[i] = r.nextInt(2);
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] == 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] == 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 0; // up
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 2; // left
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] == 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 1; // down
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 2; // left
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 1; // down
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 3; // right
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] == 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 0; // up
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 3; // right
					}
				}
			} else if (curDirEnemy[i] == 0) {
				// moving up
				if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					reqDirEnemy[i] = curDirEnemy[i] = 1 + r.nextInt(3);
					if (reqDirEnemy[i] == 1) {
						reqDirEnemy[i] = curDirEnemy[i] = 0;
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] == 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 0; // up
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 2; // left
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					reqDirEnemy[i] = curDirEnemy[i] = 2 + r.nextInt(2); // left or right
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] == 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 0; // up
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 3; // right
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] == 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 1; // down
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 2; // left
					}
				} else if (map[ghosts[i].getI() - 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] == 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 0; // down
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 3; // right
					}
				}
			} else if (curDirEnemy[i] == 1) {
				// moving down
				if (map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					reqDirEnemy[i] = curDirEnemy[i] = 1 + r.nextInt(3);
				} else if (map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] == 0) {
					reqDirEnemy[i] = curDirEnemy[i] = 1 + r.nextInt(2); // left or down
				} else if (map[ghosts[i].getI() + 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					reqDirEnemy[i] = curDirEnemy[i] = 2 + r.nextInt(2); // left or right
				} else if (map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] == 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 1; // down
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 3; // right
					}
				} else if (map[ghosts[i].getI() + 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] == 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 0; // up
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 2; // left
					}
				} else if (map[ghosts[i].getI() + 1][ghosts[i].getJ()] == 0 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] == 0
						&& map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
					if (r.nextBoolean()) {
						reqDirEnemy[i] = curDirEnemy[i] = 0; // up
					} else {
						reqDirEnemy[i] = curDirEnemy[i] = 3; // right
					}
				}
			}

			if (curDirEnemy[i] == 2 && map[ghosts[i].getI()][ghosts[i].getJ() - 1] != 0) {
				// left
				ghosts[i].setJ(ghosts[i].getJ() - 1);
				ghosts[i].setX(ghosts[i].x - ghosts[i].getMove());
				ghosts[i].setLeft(true);
				ghosts[i].setRight(false);
				ghosts[i].setDown(false);
				ghosts[i].setUp(false);
				ghosts[i].update(ghosts[i].getX(), ghosts[i].getY());
				if (ghosts[i].getJ() == 0) {
					ghosts[i].setJ(299);
					ghosts[i].setX(ghosts[i].getJ() * 2);
				}
			} else if (curDirEnemy[i] == 3 && map[ghosts[i].getI()][ghosts[i].getJ() + 1] != 0) {
				// right
				ghosts[i].setJ(ghosts[i].getJ() + 1);
				ghosts[i].setX(ghosts[i].x + ghosts[i].getMove());
				ghosts[i].setLeft(false);
				ghosts[i].setRight(true);
				ghosts[i].setDown(false);
				ghosts[i].setUp(false);
				ghosts[i].update(ghosts[i].getX(), ghosts[i].getY());
				if (ghosts[i].getJ() > 298) {
					ghosts[i].setJ(0);
					ghosts[i].setX(0);
				}
			} else if (curDirEnemy[i] == 0 && map[ghosts[i].getI() - 1][ghosts[i].getJ()] != 0) {
				// up
				ghosts[i].setI(ghosts[i].getI() - 1);
				ghosts[i].setY(ghosts[i].getY() - ghosts[i].getMove());
				ghosts[i].setLeft(false);
				ghosts[i].setRight(false);
				ghosts[i].setDown(false);
				ghosts[i].setUp(true);
				ghosts[i].update(ghosts[i].getX(), ghosts[i].getY());
			} else if (curDirEnemy[i] == 1 && map[ghosts[i].getI() + 1][ghosts[i].getJ()] != 0) {
				// down
				ghosts[i].setI(ghosts[i].getI() + 1);
				ghosts[i].setY(ghosts[i].getY() + ghosts[i].getMove());
				ghosts[i].setLeft(false);
				ghosts[i].setRight(false);
				ghosts[i].setDown(true);
				ghosts[i].setUp(false);
				ghosts[i].update(ghosts[i].getX(), ghosts[i].getY());
			}
		}
	}

	private void movePlayer() {

		if (reqDirPlayer == 2 && map[playerI][playerJ - 1] != 0) {
			// left
			curDirPlayer = 2;
		}

		if (reqDirPlayer == 3 && map[playerI][playerJ + 1] != 0) {
			// right
			curDirPlayer = 3;
		}

		if (reqDirPlayer == 0 && map[playerI - 1][playerJ] != 0) {
			// up
			curDirPlayer = 0;
		}

		if (reqDirPlayer == 1 && map[playerI + 1][playerJ] != 0) {
			// down
			curDirPlayer = 1;
		}

		if (curDirPlayer == 2 && map[playerI][playerJ - 1] != 0) {
			// left
			playerJ--;
			playerX = playerX - pacman.getMove();
			pacman.setLeft(true);
			pacman.setRight(false);
			pacman.setDown(false);
			pacman.setUp(false);
			pacman.update(playerX, playerY);
			if (playerJ == 0) {
				playerJ = 299;
				playerX = playerJ * 2;
			}
		} else if (curDirPlayer == 3 && map[playerI][playerJ + 1] != 0) {
			// right
			playerJ++;
			playerX = playerX + pacman.getMove();
			pacman.setLeft(false);
			pacman.setRight(true);
			pacman.setDown(false);
			pacman.setUp(false);
			pacman.update(playerX, playerY);
			if (playerJ > 298) {
				playerJ = 0;
				playerX = 0;
			}
		} else if (curDirPlayer == 0 && map[playerI - 1][playerJ] != 0) {
			// up
			playerI--;
			playerY = playerY - pacman.getMove();
			pacman.setLeft(false);
			pacman.setRight(false);
			pacman.setDown(false);
			pacman.setUp(true);
			pacman.update(playerX, playerY);
		} else if (curDirPlayer == 1 && map[playerI + 1][playerJ] != 0) {
			// down
			playerI++;
			playerY = playerY + pacman.getMove();
			pacman.setLeft(false);
			pacman.setRight(false);
			pacman.setDown(true);
			pacman.setUp(false);
			pacman.update(playerX, playerY);
		}

	}

	private void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// draw map
		g.drawImage(background, 0, 0, null);

		// draw pills
		for (int i = 0; i < pills.size(); i++) {
			pills.get(i).render(g);
		}

		// draw powerpills
		for (int i = 0; i < power.size(); i++) {
			power.get(i).render(g);
		}

		// draw player
		pacman.render(g);

		// draw ghosts
		for (int i = 0; i < nGhosts; i++) {
			ghosts[i].render(g);
		}

		// draw stats
		g.setColor(Color.BLACK);
		g.fillRect(0, 580, 600, 2);

		g.setFont(new Font("Trebuchet MS", Font.BOLD, 22));
		g.drawString("Score: " + (score + ((nPills - pills.size()) * 10 + (nPower - power.size()) * 40)), 30, 610);
		g.drawString("Lives: " + lives, 30, 640);

		g.dispose();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}
}
