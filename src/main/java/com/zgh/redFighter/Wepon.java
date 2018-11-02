package com.zgh.redFighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Wepon extends Sprite {
	static Image img = GameFrame.imgWepon;
	float xSpeed;
	float ySpeed;
	int kind;
	static final int POWER = 1, BOMB = 2;
	static final int frameCount = 3;
	static GameFrame gf;
	
	Wepon(int kind, float x, float y, GameFrame gf) {
		this.w = img.getWidth(null)/frameCount;
		this.h = img.getHeight(null);
		switch(kind) {
		case POWER: frame = 0;
		case BOMB:	frame = 1;
		}
		this.x = x;
		this.y = y;
		
		switch(random.nextInt(2)) {
		case 0: xSpeed = 3; break;
		case 1: xSpeed = -3; break;
		}
		switch(random.nextInt(2)) {
		case 0: ySpeed = 3; break;
		case 1: ySpeed = -3; break;
		}
		
		Wepon.gf = gf;
	}

	public void draw(Graphics g) {
		move();
		Tools.drawClipImg(img, g, (int)x, (int)y, w*frame, 0, w, h);
		
	}
	
	int reboundTimes = 0;
	public void move() {
		x += xSpeed;
		y += ySpeed;
		hitMyPlane();
		if(reboundTimes < 5) {//若还剩有次数，反弹
			if(x > Application.SIDEBAR + Application.WIDTH - w || x < Application.SIDEBAR) {
				xSpeed = -xSpeed;
				reboundTimes ++;
			}
			if(y > Application.UPBAR + Application.HEIGHT - h || y < Application.UPBAR) {
				ySpeed = -ySpeed;
				reboundTimes ++;
			}
		} else {//若超过了碰撞次数，不再反弹，如果出界则消失
			if(x<0 || x>Application.SIDEBAR+Application.WIDTH || y<0 || y>Application.UPBAR+Application.HEIGHT) {
				gf.wepons.remove(this);
			}
		}
		
	}

	private void hitMyPlane() {
		if(this.intersect(gf.myPlane)) {
			gf.wepons.remove(this);
			switch(kind) {
			case POWER: 
				gf.myPlane.powerPromote();
				break;
			case BOMB:
				gf.myPlane.addBomb();
				break;
			}
		}
	}

}
