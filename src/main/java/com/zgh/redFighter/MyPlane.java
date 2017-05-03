package com.zgh.redFighter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;


public class MyPlane extends Plane {
	static Image img;
	boolean firing = false;
	boolean l = false, r = false, u = false, d = false;//分别指明四个方向键是否处于被按下的状态
	int xDir = 0;//只指明了水平方向上的速度
	int fireLoop = 0;
	int frameCount = 4;
	int protectedLoop  = 0;//剩余的无敌时间
	int life = 3;//生命值，默认为3条
	final int LEFT=2, RIGHT=1, CENTER = 0; //用于xDir
	final int PROTECTED = 3; //用于state
	final int SPEED = 4;
	final int FIREFREQ = 3;
	final int PROTECTED_TIME = 110;
	final int REDUCTION = 5;//内切圆的半径的缩小量
	//private GameFrame gf;
	
	//用一个圆形表示myPlane所占空间，此圆形的范围根据reduction来缩小
	int OX, 	OY, 	radius;
	//用来表示正在检查的敌军子弹的圆形
	int ebOX, 	ebOY,	ebRadius ;
	
	static {
		img = GameFrame.imgMyPlane;
	}
	
	MyPlane(GameFrame gf) {
		super(gf);
		w = img.getWidth(null) / frameCount;
		h = img.getHeight(null);
		radius = w/2 - REDUCTION;
		ebRadius = EnBullet.getW()/2;
	}
	
	void KeyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			l = true;
			r = false;
			break;
		case KeyEvent.VK_RIGHT:
			r = true;
			l = false;
			break;
		case KeyEvent.VK_UP:
			u = true;
			break;
		case KeyEvent.VK_DOWN:
			d = true;
			break;
		case KeyEvent.VK_A:
			firing = true;
			break;
		case KeyEvent.VK_F2:
			life = 3;
			revive();
		}
	}
	void KeyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			l = false;
			break;
		case KeyEvent.VK_RIGHT:
			r = false;
			break;
		case KeyEvent.VK_UP:
			u = false;
			break;
		case KeyEvent.VK_DOWN:
			d = false;
			break;
		case KeyEvent.VK_A:
			firing = false;
			break;
		}
	}
	
	public void draw(Graphics g) {
		switch(state) {
		case ALIVE:	
			move();
			frame = xDir;
			Tools.drawClipImg(img, g, (int)x, (int)y, w*frame, 0, w, h);
/*g.setColor(Color.WHITE);
g.drawOval(OX-radius, OY-radius, radius*2, radius*2);*/
			break;
		case PROTECTED:	
			if(y > Client.UPBAR + Client.HEIGHT - h)
				y -= 2;
			else
				move();
			g.setColor(Color.green);
			g.drawRect((int)x, (int)y, w, h);
			Tools.drawClipImg(img, g, (int)x, (int)y, w*frame, 0, w, h);
			this.protectedLoop --;
			if(protectedLoop <= 0) 
				state = ALIVE;
			break;
		case DEAD:	break;
		}
		if(state == DEAD) return;
		

	}
	
	private void move() {
		locatexDir();
		switch(xDir) {
		case LEFT:
			x -= SPEED;
			break;
		case RIGHT:
			x += SPEED;
			break;
		}
		if(u) y -= SPEED;
		if(d) y += SPEED;
		
		//防止出界的代码
		if(x<Client.SIDEBAR) x= Client.SIDEBAR;
		else if(x>Client.SIDEBAR + Client.WIDTH - w) x = Client.SIDEBAR+Client.WIDTH-w;
		if(y<Client.UPBAR) y = Client.UPBAR;
		else if(y>Client.UPBAR+Client.HEIGHT - h) y = Client.UPBAR + Client.HEIGHT-h;
		
		if(firing) tryToFire();
		
		hitEnBullets();
	}
	
	private void hitEnBullets() {
		for(int i=0; i<gf.enBullets.length; i++) {
			if(this.state == ALIVE && this.intersect(gf.enBullets[i]) && gf.enBullets[i].getState() == ALIVE) {
				//被击中后：
				if(life == 0) 
					this.state = DEAD;
				else  {
					life --;
					revive();
				}
				gf.enBullets[i].setState(DEAD);
			}
		}
	}
	
	
	//@Override
	boolean intersect(EnBullet eb) { //重写碰撞检测方法：把自身和敌人子弹当成圆形来处理，增进可玩性
		//更新自己的圆心位置
		OX = (int) (x + w/2);
		OY = (int) (y + h/2);
		//获取正在检查的enemyBullet的位置
		ebOX = (int) (eb.getX() + EnBullet.getW()/2);
		ebOY = (int) (eb.getY() + EnBullet.getW()/2);
		//做判断
		if((OX-ebOX)*(OX-ebOX)+(OY-ebOY)*(OY-ebOY) > (radius+ebRadius)*(radius+ebRadius))
			return false;
		else 
			return true;
	}

	private void revive() {
		this.state = PROTECTED;
		this.protectedLoop = this.PROTECTED_TIME;
		x = Client.SIDEBAR+Client.WIDTH/2-this.getWidth()/2;
		y = Client.UPBAR+Client.HEIGHT + 2*h;
		frame = 0;
	}
	
	private void tryToFire() {
		fireLoop ++;
		if(fireLoop >= FIREFREQ) {
			fireLoop = 0;
			for(int i=0; i<gf.myBullets.length; i++) {
				if(gf.myBullets[i].getState() == DEAD) {
					gf.myBullets[i].setState(ALIVE);
					gf.myBullets[i].setLocation(x+w/2-gf.myBullets[i].getWidth()/2, y);
					break;
				}
			}
		}
	}
	
	private void locatexDir() {
		if(l) xDir = LEFT;
		else if(r) xDir = RIGHT;
		else xDir = 0;
	}

	public int getLife() {
		return life;
	}
	
	float getCenterX() {
		return this.x+w/2;
	}
	
	float getCenterY() {
		return this.y+h/2;
	}

	public void drawStingOfLive(Graphics g) {
		g.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
		g.setColor(Color.GREEN);
		g.drawString("life X "+String.valueOf(life), Client.SIDEBAR, Client.UPBAR+15);
	}

	public void powerPromote() {
		// TODO Auto-generated method stub
		
	}

	public void addBomb() {
		// TODO Auto-generated method stub
		
	}
	
	
}
