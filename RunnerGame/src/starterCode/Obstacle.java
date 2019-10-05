package starterCode;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Obstacle {
	private int x;
	private int y;
	private int width, height;
	private Image cactus;
	
	public Obstacle(int x, int y) {
		//**Initialize fields
	}
	
	public Image getImage() {
		return cactus;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
