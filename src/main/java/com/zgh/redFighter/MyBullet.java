package com.zgh.redFighter;

import java.awt.Graphics;
import java.awt.Image;

public class MyBullet extends Sprite{
	static Image img;
	static final int SPEED = 24;
	static int frameCount = 8;
	static int w, h;
	static GameFrame gf;//持有对方的引用

	static{
		img = GameFrame.imgMyBullet;
		w = img.getWidth(null) / frameCount;
		h = img.getHeight(null);
	}
	
	MyBullet(GameFrame gf) {
		MyBullet.gf = gf;
	}
	
	public void draw(Graphics g) {
		switch(state) {
		case DEAD: return;
		case ALIVE:
			move();
			Tools.drawClipImg(img, g, (int)x, (int)y, w*frame, 0, w, h);
			break;
		case EXPLODE:
			Tools.drawClipImg(img, g, (int)x, (int)y, w*frame, 0, w, h);
			frame ++;
			if(frame >= frameCount) {
				state = DEAD;
				frame = 0;
			}
			break;
		}
		
	}
	
	private void move() {
		if(state != ALIVE) return;
		y -= SPEED;
		if(y<0) this.state = DEAD;
		hitEnemies();
	}
	
	private void hitEnemies() {
		EnemyPlane en;
		for(int i=0; i<gf.enemies.size(); i++) {
			en = gf.enemies.get(i);
			if((en.state == ALIVE || en.getState()==EnemyPlane.COMING) && this.intersect(en)) {
				this.state = EXPLODE;
				en.getHurt();
			}
		}
	}
	
	int getWidth() { //因为我们重新定义了w和h,所以必须重写这个方法，否则将调用Sprite中的该方法，返回Sprite中的w,h，值为0
		return w;
	}
	int getHeight() {
		return h;
	}
}
