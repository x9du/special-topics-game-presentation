package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
	
	// constants
	public static final int PANEL_WIDTH = 800;
	public static final int PANEL_HEIGHT = 300;
	public static final int BASEY = 250;
	public static final int MINGAP = 300;
	public static final int MAXGAP = 450;
	
	private ArrayList<Obstacle> obstacles;
	private Timer obstacleTimer, runnerTimer, jumpTimer; // swing Timer: constructed with delay (milliseconds) and an ActionListener that fires an action event every [delay] ms.
	private static int randomGap = (int)(Math.random() * (MAXGAP - MINGAP)) + MINGAP; // creates 300 <= random gap < 450
	private ArrayList<Image> runnerGif; // runner animation sprites
	private int runnerGifIndex;
	private int heightOfJump = 0;
	private boolean isMaxHeight;
	private JLabel scoreLabel;
	private int score;
	private PlayerBounds playerBounds; // hitbox
	private boolean isGameOver;
	private int obstacleSpeed;
	
	// constructor: one-time actions
	public GamePanel() {
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setFocusable(true); // a "focusable" component can "be the focus of"/receive user input
		
		scoreLabel = new JLabel(); // JLabel: display area for short string or image. Does not react to input events.
		add(scoreLabel);
		initialize(); // fields that must be initialized when the game resets
		
		this.addKeyListener(new JumpKeyListener()); // addKeyListener(KeyListener l)
	}
	
	// reinitializes fields every time you play again
	private void initialize() {
		score = 0;
		scoreLabel.setText("Score: " + score);
		
		obstacleSpeed = 2;
		runnerGifIndex = 0;
		heightOfJump = 0;
		isMaxHeight = false;
		isGameOver = false;
		
		playerBounds = new PlayerBounds(40, 40); // PlayerBounds(width, height)
		playerBounds.setLocation(40, BASEY - 60 - heightOfJump); // (40, 190)
		
		obstacles = new ArrayList<Obstacle>();
		obstacles.add(new Obstacle(PANEL_WIDTH - 60, BASEY - 60)); // 60 x 60 Obstacle at 740, 190
		
		runnerGif = new ArrayList<Image>();
		for(int i = 0; i < 2; i ++) {
			runnerGif.add(new ImageIcon("C:\\Users\\s-zouci\\git\\runner-game\\RunnerGame\\src\\dinoSprites\\Dino-" + i + ".png").getImage());
			// uses ImageIcon because its constructor takes files from path, Image doesn't
		}
		
		obstacleTimer = new Timer(obstacleSpeed, new TimerActionListener()); // delay = obstacleSpeed = 2. Move obstacle 1 pixel left every 2 ms
		runnerTimer = new Timer(100, new RunnerActionListener()); // next frame of animation every 100 ms
		jumpTimer = new Timer(2, new JumpActionListener()); // moves runner 1 pixel up/down every 2 ms
		obstacleTimer.start();
		runnerTimer.start();
	}
	
	public void paintComponent(Graphics g) { // paintComponent is called whenever something is drawn or call repaint()
		super.paintComponent(g); // call JPanel's paintComponent method because you extend it
		setBackground(Color.WHITE);
		
		Graphics2D g2 = (Graphics2D) g; // Graphics2D has extra functionality for 2D
		g2.fillRect(0, BASEY, PANEL_WIDTH, 3); // fills border. fillRect(int x, int y, int width, int height)
		
		// drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)
		// draws image inside the specified rectangle, scaled if necessary
		// ImageObserver: object to which method returns true/false
		g2.drawImage(runnerGif.get(runnerGifIndex), 30, BASEY - 90 - heightOfJump, 90, 90, this); // gif at 30, 160 with size 90, 90
		
		for(int i = 0; i < obstacles.size(); i++) {
			Obstacle obstacle = obstacles.get(i);
			g2.drawImage(obstacle.getImage(), obstacle.getX(), BASEY - 60, 60, 60, this); // cactus at x, 190 with size 60, 60
		}
		
		playerBounds.setLocation(40, BASEY - 60 - heightOfJump); // moves playerBounds to current height
	}
	
	// moves each obstacle 1 pixel left every 2 ms, removes obstacles that are off-screen, creates new obstacles after a certain distance
	// & increments score, checks if player collided with obstacle -> game over, stop all timers, & ask to play again
	class TimerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < obstacles.size(); i++) {
				Obstacle obstacle = obstacles.get(i);
				obstacle.setLocation(obstacle.getX() - 1, obstacle.getY()); // move obstacle 1 pixel left
				
				if (obstacle.getX() == -30) { // if obstacle is off-screen, remove it
					obstacles.remove(i);
					i--;
				}
			}
		    
			Obstacle obstacle = obstacles.get(obstacles.size() - 1); // get last obstacle
			if(PANEL_WIDTH - obstacle.getX() == randomGap) { // when the last obstacle is randomGap far from the right
				obstacles.add(new Obstacle(740, BASEY - 60));
				randomGap = (int)(Math.random() * (MAXGAP - MINGAP)) + MINGAP;
				
				score++; // score increases every time a new obstacle is created
				scoreLabel.setText("Score: " + score);
			}
			
			for(int i = 0; !isGameOver && i < 60; i++) {
				// if runner and obstacle collides (if playerBounds contains any of the 60 points within obstacle), game over
				if(playerBounds.contains(obstacles.get(0).getX() + i, obstacles.get(0).getY())) {
					isGameOver = true;
				}
			}
			
			if(isGameOver) {
				runnerTimer.stop();
				// getActionListeners() returns array of ActionListeners registered to this timer
				runnerTimer.removeActionListener(runnerTimer.getActionListeners()[0]);
				obstacleTimer.stop();
				obstacleTimer.removeActionListener(obstacleTimer.getActionListeners()[0]);
				jumpTimer.stop();
				jumpTimer.removeActionListener(jumpTimer.getActionListeners()[0]);
				
				// JOptionPane: simple dialog box
				// public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType)
				// showConfirmDialog: no parent component, message is Score\nPlay again?, title is "Game Over!", 0 is yes/no
				// returns option selected by user: 0 yes, 1 no
				int confirm = JOptionPane.showConfirmDialog(null, scoreLabel.getText() + "\n" + "Play again?", "Game Over!", 0);
				if(confirm == 0) { // if user selects yes, restart game
					initialize();
				} else {
					System.exit(0); // terminates program
				}
			}
			
			repaint(); // repaints the entire JPanel to show obstacles at new positions
		}
	}
	
	class RunnerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(runnerGifIndex == 1) { // if reached last frame of animation, go to first frame
				runnerGifIndex = 0;
			} else { // increment frame index
				runnerGifIndex++;
			}
		}
	}
	
	// moves the player up and down when they jump
	class JumpActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(heightOfJump == 130) {
				isMaxHeight = true;
			}
			if(isMaxHeight) {
				heightOfJump--; // if isMaxHeight, decrement heightOfJump every 2 ms until 0
				if(heightOfJump == 0) {
					jumpTimer.stop(); // once player reaches the ground, stop jumpTimer
					isMaxHeight = false;
					score += 2; // +2 points per jump
					scoreLabel.setText("Score: " + score);
				}
			} else { // else, increment heightOfJump every 2 ms until = 130
				heightOfJump++;
			}
		}
	}
	
	// KeyAdapter vs. KeyListener:
	// KeyListener is interface, must implement all methods, even ones you don't need
	// KeyAdapter is abstract class, you can just implement the methods you need
	class JumpKeyListener extends KeyAdapter {
		// getExtendedKeyCode() returns unique id for key
		// VK_UP is static int that means up key pressed
		public void keyPressed(KeyEvent e) {
			if(e.getExtendedKeyCode() == e.VK_UP) {
				jumpTimer.setDelay(2);
				jumpTimer.start(); // starting timer each time causes initial delay to occur
			} else if(e.getExtendedKeyCode() == e.VK_DOWN) { // player quickly descends if they're in the air
				jumpTimer.setDelay(1);
			} else if(e.getExtendedKeyCode() == e.VK_RIGHT) {
				if(obstacleSpeed > 0) {
					obstacleSpeed--;
					obstacleTimer.setDelay(obstacleSpeed); // move obstacle 1 pixel left every 1 ms (move faster)
				}
			} else if(e.getExtendedKeyCode() == e.VK_LEFT) {
				if(obstacleSpeed < 2) { // if obstacles are already moving faster than 1 pixel every 2 ms, decrease obstacle speed.
					obstacleSpeed++;
					obstacleTimer.setDelay(obstacleSpeed);
				}
			}
		}
	}

}
