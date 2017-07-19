package KelvinsTankWarGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {
	public static final int XSPEED = 15;
	public static final int YSPEED = 15;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x, y;
	Tank.Direction dir;
	private TankClient tc;
	
	private boolean bLive = true;
	private boolean enemy;
	
	public boolean isLive() {
		return bLive;
	}

	public Bullet(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Bullet(int x, int y, Tank.Direction dir, boolean enemy, TankClient tc) {
		this(x, y, dir);
		this.enemy = enemy;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!bLive) {
			tc.bullets.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}

	private void move() {
		switch(dir) {
		case L :
			x -= XSPEED;
			break;
		case LU :
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U :
			y -= YSPEED;
			break;
		case RU :
			x += XSPEED;
			y -= YSPEED;
			break;
		case R :
			x += XSPEED;
			break;
		case RD :
			x += XSPEED;
			y += YSPEED;
			break;
		case D :
			y += YSPEED;
			break;
		case LD :
			x -= XSPEED;
			y += YSPEED;
			break;
		}
		
		if(x<0 || y<0 || x>TankClient.SCRWIDTH || y>TankClient.SCRHEIGHT) {
			bLive = false;
			tc.bullets.remove(this);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitTank(Tank enemyTank) {
		if(this.bLive && this.getRect().intersects(enemyTank.getRect()) && enemyTank.isLive() && this.enemy != enemyTank.isEnemy()) {
			enemyTank.setLife(enemyTank.getLife()-20);
			if(enemyTank.isEnemy() || enemyTank.getLife()<=0) {
				enemyTank.setLive(false);
			}
			this.bLive = false;
			Explode e = new Explode(x+WIDTH/2, y+HEIGHT/2, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall w) {
		if(this.bLive && this.getRect().intersects(w.getRect()) && w.isLive()) {
			if(!w.isSteel()) w.setLive(false);
			this.bLive = false;
			Explode e = new Explode(x+WIDTH/2, y+HEIGHT/2, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
}
