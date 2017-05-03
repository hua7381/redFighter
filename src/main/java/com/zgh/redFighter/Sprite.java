package com.zgh.redFighter;

import java.util.Random;

public class Sprite {
	float x = Client.SIDEBAR, y = Client.UPBAR;//默认位置为左上角
	float xSpeed, ySpeed;
	int w, h;
	int frame = 0;//帧数，从零开始计数
	int state = DEAD;//状态机,默认为DEAD
	static final int ALIVE=0, EXPLODE=1, DEAD=2;
	static Random random = new Random();
	
	void setState(int state) {
		this.state = state;
		if(state<0 || state>2)
			System.out.println("state的值不合法");
	}
	
	int getState() {
		return state;
	}
	
	void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	boolean intersect(Sprite s) { //碰撞检测方法,unStrict的值使得要靠的更近才会发生碰撞
		if(this.x+w>s.x&& this.y+h>s.y && this.x<s.x+s.w && this.y<s.y+s.h)
			return true;
		return false;
	}
	
	void setWidth(int w) {
		this.w = w;
	}
	
	int getWidth() {
		return w;
	}
	
	int getHeight() {
		return h;
	}
	
	void setHeight(int h) {
		this.h = h;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setxSpeed(float xSpeed) {
		this.xSpeed = xSpeed;
	}

	public void setySpeed(float ySpeed) {
		this.ySpeed = ySpeed;
	}
	
}
