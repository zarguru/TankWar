package KelvinsTankWarGame;
import java.awt.*;

//a blood class for adding life value of our tank
public class Blood {
	int x, y, w, h;
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	int step = 0;
	
	TankClient tc;
	
	//define the moving trajectory of the blood
	private int[][] pos = {
					  {350, 300}, {360, 300}, {375, 305}, {400, 310}, {360, 290}, {364, 290}, {350, 295}
					  };
	
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = h = 16;
	}
	
	//draw blood when its alive
	public void draw(Graphics g) {
		if(!live) return;
		
		Color c = g.getColor();
		g.setColor(Color.magenta);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}
	
	//update the position
	private void move() {
		step++;
		if(step == pos.length) {
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
		
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
	

}
