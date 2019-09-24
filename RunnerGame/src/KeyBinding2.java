import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.*;

@SuppressWarnings("serial")
public class KeyBinding2 extends JPanel {
	enum Dir {
		LEFT("Left", KeyEvent.VK_LEFT, -1, 0),
		RIGHT("Right", KeyEvent.VK_RIGHT, 1, 0),
		UP("Up", KeyEvent.VK_UP, 0, -1),
		DOWN("Down", KeyEvent.VK_DOWN, 0, 1);
		
		private String name;
		private int keyCode;
		private int deltaX;
		private int deltaY;
		
		private Dir(String name, int keyCode, int deltaX, int deltaY) {
			this.name = name;
			this.keyCode = keyCode;
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}
		
		public String getName() {
			return name;
		}
		
		public int getKeyCode() {
			return keyCode;
		}
		
		public int getDeltaX() {
			return deltaX;
		}
		
		public int getDeltaY() {
			return deltaY;
		}
	}
	
	public static final int TIMER_DELAY = 10;
	public static final int DELTA_X = 2;
	public static final int DELTA_Y = DELTA_X;
	public static final int SPRITE_WIDTH = 10;
	public static final int SPRITE_HEIGHT = SPRITE_WIDTH;
	private static final String PRESSED = "pressed";
	private static final String RELEASED = "released";
	private static final int PREF_W = 800;
	private static final int PREF_H = 650;
	private Map<Dir, Boolean> dirMap = new EnumMap<>(Dir.class);
	private int spriteX = 0;
	private int spriteY = 0;
	private BufferedImage sprite;
	private Timer animationTimer = new Timer(TIMER_DELAY, new AnimationListener());
	
	public KeyBinding2() {
		for (Dir dir : Dir.values()) {
			dirMap.put(dir, Boolean.FALSE);
		}
		sprite = createSprite();
		setKeyBindings();
		animationTimer.start();
	}
	
	private BufferedImage createSprite() {
		BufferedImage sprt = new BufferedImage(SPRITE_WIDTH, SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics g = sprt.getGraphics();
		g.setColor(Color.RED);
		g.fillRect(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
		g.dispose();
		return sprt;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (sprite != null) {
			g.drawImage(sprite, spriteX, spriteY, this);
		}
	}
	
	private void setKeyBindings() {
		int condition = WHEN_IN_FOCUSED_WINDOW;
		InputMap inputMap = getInputMap(condition);
		ActionMap actionMap = getActionMap();
		
		for (Dir dir : Dir.values()) {
			KeyStroke keyPressed = KeyStroke.getKeyStroke(dir.getKeyCode(), 0, false);
			KeyStroke keyReleased = KeyStroke.getKeyStroke(dir.getKeyCode(), 0, true);
			
			inputMap.put(keyPressed, dir.toString() + PRESSED);
			inputMap.put(keyReleased, dir.toString() + RELEASED);
			
			actionMap.put(dir.toString() + PRESSED, new DirAction(dir, PRESSED));
			actionMap.put(dir.toString() + RELEASED, new DirAction(dir, RELEASED));
		}
	}

	   private class AnimationListener implements ActionListener {
	      @Override
	      public void actionPerformed(ActionEvent e) {
	         int newX = spriteX;
	         int newY = spriteY;
	         for (Dir dir : Dir.values()) {
	            if (dirMap.get(dir)) {
	               newX += dir.getDeltaX() * DELTA_X;
	               newY += dir.getDeltaY() * DELTA_Y;
	            }
	         }
	         if (newX < 0 || newY < 0) {
	            return;
	         }
	         if (newX + SPRITE_WIDTH > getWidth() || newY + SPRITE_HEIGHT > getHeight()) {
	            return;
	         }
	         spriteX = newX;
	         spriteY = newY;
	         repaint();         
	      }
	   }

	   private class DirAction extends AbstractAction {

	      private String pressedOrReleased;
	      private Dir dir;

	      public DirAction(Dir dir, String pressedOrReleased) {
	         this.dir = dir;
	         this.pressedOrReleased = pressedOrReleased;
	      }

	      @Override
	      public void actionPerformed(ActionEvent evt) {
	         if (pressedOrReleased.equals(PRESSED)) {
	            dirMap.put(dir, Boolean.TRUE);
	         } else if (pressedOrReleased.equals(RELEASED)) {
	            dirMap.put(dir, Boolean.FALSE);
	         }
	      }

	   }

	   private static void createAndShowGui() {
	      KeyBinding2 mainPanel = new KeyBinding2();

	      JFrame frame = new JFrame("KeyBindingEg");
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.getContentPane().add(mainPanel);
	      frame.pack();
	      frame.setLocationByPlatform(true);
	      frame.setVisible(true);
	   }

	   public static void main(String[] args) {
	      SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	            createAndShowGui();
	         }
	      });
	   }

}