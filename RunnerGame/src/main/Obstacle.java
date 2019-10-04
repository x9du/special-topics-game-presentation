package main;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Obstacle {
	private int x;
	private int y;
	private int width, height;
	private Image cactus;
	private boolean selected;
	
	public Obstacle (int x, int y) {
        width = 60;
        height = 60;
        this.x = x;
        this.y = y;
        
        int i = (int)(Math.random() * 4);
        cactus = new ImageIcon("C:\\Users\\s-zouci\\OneDrive - Bellevue School District\\2019-20\\7 - Special Topics in Computer Science\\Cactus Sprites\\Cactus-" + i + ".png").getImage();
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
	
	public boolean getSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Obstacle contains(int x, int y) {
        if(x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height) {
        	return this;
        }
        return null;
    }

}
