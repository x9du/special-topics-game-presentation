package main;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Obstacle {
	private int x;
	private int y;
	private int width, height;
	private Image cactus;
	
	public Obstacle(int x, int y) {
        width = 60;
        height = 60;
        this.x = x;
        this.y = y;
        
        int i = (int)(Math.random() * 4);
        cactus = new ImageIcon("C:\\Users\\s-zouci\\git\\runner-game\\RunnerGame\\src\\cactusSprites\\Cactus-" + i + ".png").getImage();
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
