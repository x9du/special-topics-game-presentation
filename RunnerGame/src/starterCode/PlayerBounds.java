package starterCode;

public class PlayerBounds {
	private int width;
	private int height;
	private int x;
	private int y;
	
	public PlayerBounds(int width, int height) {
		this.width = width;
		this.height= height;
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
	
	public boolean contains(int x, int y) {
		//**Check if PlayerBounds contains point
	}

}
