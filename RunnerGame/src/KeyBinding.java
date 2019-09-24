import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

@SuppressWarnings("serial")
public class KeyBinding extends JPanel { // Why do you want the class to extend JPanel?
	private static final String UP_KEY_PRESSED = "up key pressed";
	   private static final String UP_KEY_RELEASED = "up key released";
	   private static final int UP_TIMER_DELAY = 50;
	   private static final Color FLASH_COLOR = Color.red;

	   private Timer upTimer;
	   private JLabel label = new JLabel();

	   public KeyBinding() {
	      label.setFont(label.getFont().deriveFont(Font.BOLD, 32));
	      label.setOpaque(true);
	      add(label);

	      setPreferredSize(new Dimension(400, 300));

	      int condition = WHEN_IN_FOCUSED_WINDOW;
	      InputMap inputMap = getInputMap(condition);
	      ActionMap actionMap = getActionMap();
	      KeyStroke upKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false);
	      KeyStroke upKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true);

	      inputMap.put(upKeyPressed, UP_KEY_PRESSED);
	      inputMap.put(upKeyReleased, UP_KEY_RELEASED);

	      actionMap.put(UP_KEY_PRESSED, new UpAction(false));
	      actionMap.put(UP_KEY_RELEASED, new UpAction(true));

	   }
	   
	   private class UpAction extends AbstractAction {
		      private boolean onKeyRelease;

		      public UpAction(boolean onKeyRelease) {
		         this.onKeyRelease = onKeyRelease;
		      }

		      @Override
		      public void actionPerformed(ActionEvent evt) {
		         if (!onKeyRelease) {
		            if (upTimer != null && upTimer.isRunning()) {
		               return;
		            }
		            System.out.println("key pressed");
		            label.setText(UP_KEY_PRESSED);

		            upTimer = new Timer(UP_TIMER_DELAY, new ActionListener() {

		               @Override
		               public void actionPerformed(ActionEvent e) {
		                  Color c = label.getBackground();
		                  if (FLASH_COLOR.equals(c)) {
		                     label.setBackground(null);
		                     label.setForeground(Color.black);
		                  } else {
		                     label.setBackground(FLASH_COLOR);
		                     label.setForeground(Color.white);
		                  }
		               }
		            });
		            upTimer.start();
		         } else {
		            System.out.println("Key released");
		            if (upTimer != null && upTimer.isRunning()) {
		               upTimer.stop();
		               upTimer = null;
		            }
		            label.setText("");
		         }
		      }

		   }

		   private static void createAndShowGui() {
		      KeyBinding mainPanel = new KeyBinding();

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