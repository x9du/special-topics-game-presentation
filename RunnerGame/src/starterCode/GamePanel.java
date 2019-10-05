package starterCode;

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
		//**Implement constructor
	}
	
	// reinitializes fields every time you play again
	private void initialize() {
		//**Reset score
		
		obstacleSpeed = 2;
		runnerGifIndex = 0;
		heightOfJump = 0;
		isMaxHeight = false;
		isGameOver = false;
		
		//**Initialize playerBounds
		
		//**Create ArrayList of obstacles
		
		//**Create ArrayList of dinosaur sprites for runnerGif
		
		//**Initialize obstacleTimer, runnerTimer, and jumpTimer
	}
	
	public void paintComponent(Graphics g) { // paintComponent is called whenever something is drawn or call repaint()
		//**Set background color
		//**Draw border
		//**Show runnerGif
		//**Show obstacles
		//**Move playerBounds to current position
	}
	
	class TimerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//**Obstacle movement
			//**Remove obstacles that are off-screen
			//**Generate a random new obstacle
			//**If player collides with obstacle, game over and ask to play again
		}
	}
	
	class RunnerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//**Increment next frame of animation
		}
	}
	
	class JumpActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//**Implement jumping
		}
	}
	
	class JumpKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			//**Up: jump
			//**Down: quickly descend
			//**Right: increase speed
			//**Left: decrease speed
		}
	}

}
