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
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import main.RunnerGamePanel.RunnerActionListener;
import main.RunnerGamePanel.TimerActionListener;
import shapes.ShapeContainer;
import shapes.Square;

public class GamePanel extends JPanel {
	
	// constants
	public static final int PANEL_WIDTH = 800;
	public static final int PANEL_HEIGHT = 300;
	public static final int BASEY = 250;
	public static final int MINGAP = 170;
	public static final int MAXGAP = 340;
	
	private ArrayList<Obstacle> obstacles;
	private Timer obstacleTimer, runnerTimer, jumpTimer; // swing Timer: constructed with delay (milliseconds) and an ActionListener that fires an action event every [delay] ms.
	private static int randomGap = (int)(Math.random() * (MAXGAP - MINGAP)) + MINGAP; // Creates 170 <= random gap < 340
	private ArrayList<Image> runnerGif; // Runner animation sprites
	private int runnerGifIndex;
	private int heightOfJump = 0;
	private boolean flag; // ?
	private JLabel scoreLabel;
	private int score;
	private Rectangle playerBounds;
	private boolean isGameOver;
	private int obstacleSpeed;
	private Timer obstacleTimer1;
	
	// constructor: one-time things
	public GamePanel() {
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setFocusable(true);
		
		scoreLabel = new JLabel(); // JLabel: display area for short string or image. Does not react to input events.
		add(scoreLabel);
		initialize(); // fields that must be initialized when the game resets
		
		this.addKeyListener(new JumpKeyListener()); // addKeyListener(KeyListener l)
	}
	
	// reinitializes fields every time you play again
	private void initialize() {
		score = 0;
		scoreLabel.setText("Score: " + score);
		
		obstacleSpeed = 6;
		runnerGifIndex = 0;
		heightOfJump = 0;
		flag = false;
		jumpTimer = new Timer(3, new JumpActionListener()); // Checks for a jump every 3 ms?
		playerBounds = new Rectangle(35, 60); // Local class Rectangle(width, height)
		playerBounds.setLocation(50, BASEY - 90 - heightOfJump); // (50, 160)
		
		isGameOver = false;
		
		obstacles = new ArrayList<Obstacle>();
		obstacles.add(new Obstacle(PANEL_WIDTH - 90, BASEY - 90)); // 90 x 90 Obstacle at 710, 160
		
		runnerGif = new ArrayList<Image>();
		for (int i = 0; i < 2; i ++) {
			runnerGif.add(new ImageIcon("C:\\Users\\s-zouci\\OneDrive - Bellevue School District\\2019-20\\7 - Special Topics in Computer Science\\Dino Sprites\\Dino-" + i + ".png").getImage());
			// Uses ImageIcon because its constructor takes files from path, Image doesn't
		}
		
		obstacleTimer = new Timer(obstacleSpeed, new TimerActionListener()); // obstacleSpeed is 6. Move obstacle 1 pixel left every 6 ms
		obstacleTimer1 = new Timer(obstacleSpeed, new TimerActionListener());
		runnerTimer = new Timer(32, new RunnerActionListener()); // Do ? every 32 ms
		obstacleTimer.start();
		obstacleTimer1.start();
		runnerTimer.start();
	}
	
	public void paintComponent(Graphics g) { // paintComponent is called whenever something is drawn or call repaint()
		super.paintComponent(g); // call JPanel's paintComponent method because you extend it
		setBackground(Color.WHITE);
		Graphics2D g2 = (Graphics2D) g; // Graphics2D has extra functionality for 2D
		
		// drawImage(Image img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
		// = drawImage(Image img, destination top left corner, destination bottom right corner relative to TL, source TL, source BR, observer)
		// source arguments "select" an area of the image to be displayed
		// source arguments are the whole image by default?
		g2.drawImage(runnerGif.get(runnerGifIndex), 30, BASEY - 90 - heightOfJump, 90, 90, this); // gif at 30, 160 with size 90, 90
		
		g2.fillRect(0, BASEY, getWidth(), 8); // fillRect(int x, int y, int width, int height)
		playerBounds.setLocation(50, BASEY - 90 - heightOfJump); // is this necessary
		
		Iterator i = obstacles.iterator();
		while(i.hasNext()) {
			g2.drawImage(((Obstacle) i.next()).getImage(), ((Obstacle) i.next()).getX(), BASEY - 90, 90, 90, this); // cactus at x, 160 with size 90, 90
		}
	}
	
	// Moves each obstacle 1 pixel left every 6 ms, removes obstacles that are off-screen, creates new obstacles after a certain distance
	// & increments score, checks if player collided with obstacle -> game over, stop all timers, & ask to play again
	class TimerActionListener implements ActionListener { // Delay = obstacleSpeed = 6 ms
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < obstacles.size(); i++) {
				Obstacle obstacle = obstacles.get(i);
				obstacle.setLocation(obstacle.getX() - 1, obstacle.getY()); // Move obstacle 1 pixel left
				
				if (obstacle.getX() == -10) { // If obstacle is off-screen
					obstacle.setSelected(true);
				}
			}
		    
			Obstacle obstacle = obstacles.get(obstacles.size() - 1); // Get last obstacle
			if (800 - obstacle.getX() == randomGap) { // When the last obstacle is randomGap far from the right
				obstacles.add(new Obstacle(780, BASEY - 20));
				randomGap = (int)(Math.random() * (MAXGAP - MINGAP)) + MINGAP;
				
				score++; // Score increases every time a new obstacle is created
				scoreLabel.setText( "Score: " + score);
			}
		    
			obstacles.remove(); // Removes obstacles that are off-screen
			
			for(int i = 0; !isGameOver && i < 90; i++) {
				// If runner and obstacle collides (if playerBounds contains any of the 90 points within obstacle), game over
				if(playerBounds.contains(obstacles.get(0).getX() + i, obstacles.get(0).getY()) != null) {
					isGameOver = true;
				}
			}
			
			if(isGameOver) {
				runnerTimer.stop();
				runnerTimer.removeActionListener(runnerTimer.getActionListeners()[0]);
				obstacleTimer.stop();
				obstacleTimer.removeActionListener(obstacleTimer.getActionListeners()[0]);
				jumpTimer.stop(); // Stop jumping
				if(jumpTimer.getActionListeners()[0] != null) {
					jumpTimer.removeActionListener(jumpTimer.getActionListeners()[0]);
				}
				
				// JOptionPane: simple dialog box
				// public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType)
				// showConfirmDialog: no parent component, message is Score\nPlay again?, title is "Game Over!", 0 is yes/no
				// returns option selected by user: 0 yes, 1 no
				int confirm = JOptionPane.showConfirmDialog(null, scoreLabel.getText() + "\n" + "Play again?", "Game Over!", 0);
				if(confirm == 0) { // If user selects yes, restart game
					initialize();
				} else {
					System.exit(0); // Terminates program
				}
			}
			
			repaint(); // Repaints the entire JPanel to show obstacles at new positions
		}
	}
	
	class RunnerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(runnerGifIndex == 1) { // If reached last frame of animation, go to first frame
				runnerGifIndex = 0;
			} else { // Increment frame index
				runnerGifIndex++;
			}
		}
	}
	
	// Moves the player up and down when they jump
	class JumpActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(heightOfJump == 65) {
				flag = true;
			}
			if(flag) {
				heightOfJump--; // If heightOfJump = 65, decrement jumpCount every 3 ms until 0
				if(heightOfJump == 0) {
					jumpTimer.stop(); // Once player reaches the ground, stop jumpTimer
					flag = false;
					score += 2; // +2 points per jump
					scoreLabel.setText("Score: " + score);
				}
			} else { // Else, increment heightOfJump every 3 ms until = 65
				heightOfJump++;
			}
		}
	}
	
	class JumpKeyListener extends KeyAdapter {
		// getExtendedKeyCode() returns unique id for key
		// VK_UP is static int that means up key pressed
		public void keyPressed(KeyEvent e){
			if(e.getExtendedKeyCode() == e.VK_UP) {
				jumpTimer.setDelay(3);
				jumpTimer.start(); // Starting timer each time causes initial delay to occur
			} else if(e.getExtendedKeyCode() == e.VK_DOWN) { // Player quickly descends if they're in the air
				jumpTimer.setDelay(2);
			} else if(e.getExtendedKeyCode() == e.VK_RIGHT) {
				if(obstacleSpeed > 1) {
					obstacleSpeed--;
					obstacleTimer.setDelay(obstacleSpeed); // Move obstacle 1 pixel left every 5 ms (move faster)
				}
			} else if(e.getExtendedKeyCode() == e.VK_LEFT) {
				if(obstacleSpeed < 5) { // If obstacles are already moving faster than 1 pixel every 5 ms, decrease obstacle speed.
					obstacleSpeed++;
					obstacleTimer.setDelay(obstacleSpeed);
				}
			}
		}
	}

}
