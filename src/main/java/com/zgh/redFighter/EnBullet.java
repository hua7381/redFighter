package com.zgh.redFighter;

import java.awt.Graphics;
import java.awt.Image;

public class EnBullet extends Sprite{
	static Image img;
	static int w,h;
	static int frameCount = 7;
	static final float SPEED = 6.0f;/////////
	static {
		img = GameFrame.imgEnBullet;
		h = img.getHeight(null);
		w = img.getWidth(null)/frameCount; 
	}  
	
	public void draw(Graphics g) {
		switch(state) {
		case DEAD: return;
		case ALIVE:
			move();
			Tools.drawClipImg(img, g, (int)x, (int)y, w*0, 0, w, h);
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
		y += ySpeed;
		x += xSpeed;
		if(y > Client.UPBAR+Client.HEIGHT || y<0 || x<0 || x>Client.SIDEBAR+Client.WIDTH) //越过边界后死亡
			this.state = DEAD;
	}
	
	public static int getW() {
		return w;
	}

	public static int getH() {
		return h;
	}

	
}
