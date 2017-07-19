package KelvinsTankWarGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

//a class for undestroyable and destroyable walls 
public class Wall {

	int x, y;
	int WIDTH, HEIGHT;
	
	TankClient tc;

	private boolean steel;
	
	//check if the current wall is destroyable
	public boolean isSteel() {
		return steel;
	}

	private boolean live = true;

	public Wall(boolean sTEEL, int x, int y, int wIDTH, int hEIGHT, TankClient tc) {
		this.steel = sTEEL;
		this.x = x;
		this.y = y;
		WIDTH = wIDTH;
		HEIGHT = hEIGHT;
		this.tc = tc;
	}
	
	//draw the wall using different colors according to if it's destroyable
	public void draw(Graphics g) {
		if(!this.live) {			
			return;
		}
		Color c = g.getColor();
		if(this.isSteel())
			g.setColor(Color.ORANGE);
		else
			g.setColor(Color.MAGENTA);
		g.fillRect(x, y, WIDTH, HEIGHT);
		g.setColor(c);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean b) {
		this.live = b;		
	}
	
	
}
