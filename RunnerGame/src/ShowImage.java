import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
public class ShowImage {

	public static void main(String[] args) {
		JFrame frame = new JFrame(); // JFrame is a window
		ImageIcon icon = new ImageIcon("C:\\Users\\s-zouci\\OneDrive - Bellevue School District\\Saved Pictures\\!!!!.PNG");
		// ImageIcon is an image
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

}
