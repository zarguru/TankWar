package KelvinsTankWarGame;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/*
 * This class is the main frame of the game
 * @ author Kelvin 
 */

public class TankClient extends Frame {
	
	public static final int SCRWIDTH = 1800, SCRHEIGHT = 1600;
	
	Image offScreenImage = null;
	
	Tank myTank = new Tank(50, 65, false, Tank.Direction.STOP, this);
	List<Tank> enemyTanks = new ArrayList<Tank>(); //(60, 60, true);
	List<Bullet> bullets = new ArrayList<Bullet>();
	
	List<Explode> explodes = new ArrayList<Explode>();
	
	Wall w1 = new Wall(true, 300, 200, 20, 150, this);
	Wall w2 = new Wall(true, 500, 600, 300, 20, this);
	Wall w3 = new Wall(false, 500, 1000, 300, 20, this);
	
	Blood blood = new Blood();

	//The method for painting game window, tanks, bullets, walls, and everything
	public void paint(Graphics g) {
		/*
		 * Show instruction and the current tank life value
		 */
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		//g.drawString("Bullet Count:" + bullets.size(), 50, 90);
		//g.drawString("Explode Count:" + explodes.size(), 50, 140);
		g.drawString("Press Ctrl to Shoot", 50, 90);
		g.drawString("Press A to Super Shoot", 50, 140);
		g.drawString("Tank's Life:" + myTank.getLife(), 50, 190);
		g.setColor(c);
		
		//if all enemy tanks eliminated, produce 10 new enemy tanks
		if(enemyTanks.size() == 0) {
			for(int i=0; i<10; i++) {
				enemyTanks.add(new Tank(50 + 80*(i+1), 50, true, Tank.Direction.D, this));
			}
		}

		for(int i=0; i<bullets.size(); i++) {
			Bullet b = bullets.get(i);
			for(int j=0; j<enemyTanks.size(); j++) {
				Tank eT = enemyTanks.get(j);
				b.hitTank(eT);
			}
			b.hitTank(myTank);
			b.hitWall(w1);
			b.hitWall(w2);
			b.hitWall(w3);
			b.draw(g);
			//if(!b.isLive()) bullets.remove(b);
			//else b.draw(g);
		}
		//for(int i=0; i<enemyTanks.size(); i++) {
		//	Tank eT = enemyTanks.get(i);
		//	eT.draw(g);
			//if(!b.isLive()) bullets.remove(b);
			//else b.draw(g);
		//}
		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
			//if(!b.isLive()) bullets.remove(b);
			//else b.draw(g);
		}
		
		Tank t, t1;
		for(int i=0; i<=enemyTanks.size(); i++) {
			if(i<enemyTanks.size()) 
				t = enemyTanks.get(i);
			else
				t = myTank;
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			for(int j=0; j<=enemyTanks.size(); j++) {
				if (j == i)
					continue;
				else if(j<enemyTanks.size()) 
					t1 = enemyTanks.get(j);
				else
					t1 = myTank;
				t.collidesWithTank(t1);				
			}
			t.draw(g);
		}
		//myTank.draw(g);
		w1.draw(g);
		w2.draw(g);
		w3.draw(g);
		
		blood.draw(g);
		myTank.eat(blood);
	}
	
	//paint on a backend picture first, then put on screen
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(SCRWIDTH, SCRHEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		//Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GRAY); //ALT+/
		gOffScreen.fillRect(0, 0, SCRWIDTH, SCRHEIGHT);
		//gOffScreen.setColor(c);
		paint(gOffScreen);   
		g.drawImage(offScreenImage, 0, 0, null);
	} 

	//this method creates the game window and initiates the game
	public void launchFrame() {
		this.setLocation(400,300);
		this.setSize(SCRWIDTH, SCRHEIGHT);
		this.setTitle("Tank War");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { //用一个匿名类
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GRAY);		

		for(int i=0; i<10; i++) {
			enemyTanks.add(new Tank(50 + 80*(i+1), 50, true, Tank.Direction.D, this));
		}
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		
		//start a painting thread
		new Thread(new PaintThread()).start();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	
	private class PaintThread implements Runnable {
		
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
		
		
	}

}
