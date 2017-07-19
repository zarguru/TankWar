package KelvinsTankWarGame;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

//a class for our tank and enemy tanks
public class Tank {
	private int x, y, oldX, oldY;
	private static final int SPEED = 8;

	public static final int WIDTH = 70;
	public static final int HEIGHT = 70;
	
	TankClient tc;
	
	private boolean enemy;
	private boolean live = true;
	
	private int life = 100;

	private static Random r = new Random();
	private int step = r.nextInt(20) + 12;
	
	private boolean bL=false, bU=false, bR=false, bD=false;
	enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};
	
	private Direction dir = Direction.STOP;
	private Direction barrel_dir = Direction.U;
	
	private BloodBar bb = new BloodBar();
	
	public void setLife(int life) {
		this.life = life;
	}

	public int getLife() {
		return life;
	}
	
	public boolean isLive() {
		return live;
	}
	
	public Tank(int x, int y, boolean enemy) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.enemy = enemy;
	}

	public Tank(int x, int y, boolean enemy, Direction dir, TankClient tc) {
		this(x, y, enemy);
		this.dir = dir;
		this.tc = tc;
	}
	
	//draw the tank
	public void draw(Graphics g) {
		if(!live) {
			if(this.enemy) {				
				tc.enemyTanks.remove(this);
			} 
			return;
		}
		
		Color c = g.getColor();
		if(this.enemy) g.setColor(Color.BLUE);
		else g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);	
		
		
		switch(barrel_dir) {
		case L :
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y+Tank.HEIGHT/2);
			break;
		case LU :
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y);
			break;
		case U :
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y);
			break;
		case RU :
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y);
			break;
		case R :
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y+Tank.HEIGHT/2);
			break;
		case RD :
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y+Tank.HEIGHT);
			break;
		case D :
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y+Tank.HEIGHT);
			break;
		case LD :
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y+Tank.HEIGHT);
			break;
		}
		g.setColor(c);	
		
		if(!this.enemy) 
			bb.draw(g);
		
		move();		
	}
	
	private void move() {
		this.oldX = x;
		this.oldY = y;
		
		switch(dir) {
		case L :
			x -= SPEED;
			break;
		case LU :
			x -= SPEED;
			y -= SPEED;
			break;
		case U :
			y -= SPEED;
			break;
		case RU :
			x += SPEED;
			y -= SPEED;
			break;
		case R :
			x += SPEED;
			break;
		case RD :
			x += SPEED;
			y += SPEED;
			break;
		case D :
			y += SPEED;
			break;
		case LD :
			x -= SPEED;
			y += SPEED;
			break;
		case STOP :
			break;
		}
		
		if(this.dir != Direction.STOP) {
			this.barrel_dir = this.dir;
		}
		
		if(x<0) x=0;
		if(y<60) y=60;
		if(x + Tank.WIDTH > TankClient.SCRWIDTH) x = TankClient.SCRWIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.SCRHEIGHT) y = TankClient.SCRHEIGHT - Tank.HEIGHT;
		
		if(enemy) {
			Direction[] dirs = Direction.values();
			if(step == 0) {
				step = r.nextInt(20) + 8;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
							
			step --;
			if(r.nextInt(40)>38) fire();
		}		
	}
	
	public void keyPressed(KeyEvent e) {
		//System.out.println("OK");
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F2 :
			if(!this.live) {
				this.live = true;
				this.life = 100;
				this.x = 50;
				this.y = 65;
			}
			break; 
		case KeyEvent.VK_LEFT :
			bL = true;
			break;  //不加break会产生s穿透的问题，坦克移动怪异
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		case KeyEvent.VK_A :
			superFire();
			break;			
		}
		locateDirection();
		//move();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_CONTROL :
			fire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;  //不加break会产生s穿透的问题，坦克移动怪异
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		}
		locateDirection();
	}	
	
	void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
		//move();
		//bL=false;
		//bU=false;
		//bR=false;
		//bD=false;
	}

	//make the tank back off 1 step
	private void backoff() {
		x = oldX;
		y = oldY;
	}
	
	//On pressing and releasing Ctrl, make tank fire a bullet
	public Bullet fire() {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Bullet.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Bullet.HEIGHT/2;
		Bullet b = new Bullet(x, y, barrel_dir, this.enemy, this.tc);
		tc.bullets.add(b);
		return b;
	}
	
	//make tank fire a bullet to a certain direction
	public Bullet fire(Direction dir) {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Bullet.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Bullet.HEIGHT/2;
		Bullet b = new Bullet(x, y, dir, this.enemy, this.tc);
		tc.bullets.add(b);
		return b;
	}
	
	//get the tank's current geometry and location parameters
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	//check if the tank is alive
	public void setLive(boolean b) {
		this.live = b;		
	}

	//check if the tank is our tank or an enemy tank
	public boolean isEnemy() {
		return this.enemy;
	}

	//Check if tank collide with wall; make walls can't be transpassed
	public boolean collidesWithWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()) {
		    this.backoff();
			return true;
		}
		return false;
	}	

	//Check if tank collide with other tanks; make tanks can't transpass each other
	public boolean collidesWithTank(Tank t) {
		if(this!=t && this.live && this.getRect().intersects(t.getRect()) && t.isLive()) {
		    this.backoff();
		    t.backoff();
			return true;
		}
		return false;
	}
	
	//On pressing "A", tank fires bullets in 8 directions
	private void superFire() {
		Direction[] dire = Direction.values();
		for(int i=0; i<8; i++) {
			fire(dire[i]);
		}
	}

	//A class for the blood bar showing the remaining life value of our tank
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.pink);
			g.drawRect(x, y-15, WIDTH, 10);
			int wi = (WIDTH-2)*life/100;
			g.setColor(Color.RED);
			g.fillRect(x+1, y-14, wi, 8);
			g.setColor(c);
		}
	}

	//after tank eats blood, set the life value of our tank back to 100
	public boolean eat(Blood b) {
		if(this.live && !enemy && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.setLife(100);
			b.setLive(false); 
			return true;
		}
		else return false;		
	}
}


