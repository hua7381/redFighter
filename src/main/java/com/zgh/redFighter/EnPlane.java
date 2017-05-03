package com.zgh.redFighter;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

abstract class EnPlane extends Plane{
	static Random random;//用于获取随机数
	//static GameFrame gf; //持有对方的引用
	static final int COMING = 3, RETREAT = 4; //两个新的状态
	Image img;
	int moveLoop = 0;
	int hp = 0;
	int fireLoop = 0;
	int fireFreq = 0;	//每个X个周期尝试发射一次
	int fireRate = 0; //能发射出的几率为X%
	int frameCount = 0; //图片的总帧数,由图片决定
	int ySpeed = 0;
	int xSpeed = 0;
	int bulletClip = 0;// 弹夹容量，用于控制敌机发射一定次数后撤退
	int rateOfDropWepon = 0;//死后掉落武器的概率
	float bulSpeed;//发射的子弹的速度
	
	static {
		random = new Random();//类的对象的初始化必须使用new语句
	}
	
	EnPlane(GameFrame gf){
		super(gf);
		state = COMING;
	}

	public void draw(Graphics g) {
		switch(state) {
		case DEAD: 
			gf.enemies.remove(this);
			break;
		case COMING:
			coming();
			Tools.drawClipImg(img, g, (int)x, (int)y, w*frame, 0, w, h);
			break;
		case ALIVE:
			move();
			Tools.drawClipImg(img, g, (int)x, (int)y, w*frame, 0, w, h);
			break;
		case RETREAT:
			retreat();
			Tools.drawClipImg(img, g, (int)x, (int)y, w*frame, 0, w, h);
			break;
		case EXPLODE:
			Tools.drawClipImg(img, g, (int)x, (int)y, w*frame, 0, w, h);
			frame ++;
			if(frame >= frameCount)
				state = DEAD;
			break;
		}
	}

	void retreat() { //默认的撤退方法。快速上移，移出边界后死亡
		y -= 5;
		if(y < 0) 
			state = DEAD;
	}

	abstract void coming();

	abstract void move();
	
	void tryToFire() { //使用fireFreq和fireRare计算是否该发射
		fireLoop  ++;
		if(fireLoop >= fireFreq) { 
			fireLoop = 0;
			if(random.nextInt(100) <= fireRate) {
				fire();
			}
		}
	}

	abstract void fire();

	public void getHurt() {
		hp  --;
		if(hp <= 0) {
			state = EXPLODE;
			gf.score.score += 100;
				dropWepon();
		}
	}

	private void dropWepon() {
System.out.println("good");
		if(random.nextInt(100) <= rateOfDropWepon) {
			gf.wepons.add(new Wepon(1, x+w/2, y+h/2, gf));
		}
	}

	float x2, y2;//代表mp的中心位置的坐标
	float line;
	float getSin(MyPlane mp) { //求正弦值
		comOfSinAndCos(mp);
		return (y2-gunY)/line;
	}
	
	float getCos(MyPlane mp) { //求余弦值
		comOfSinAndCos(mp);
		return (x2-gunX)/line;
	}
	
	void comOfSinAndCos(MyPlane mp) {
		upDateGunLocation();
		x2 = mp.getCenterX();
		y2 = mp.getCenterY();
		line = (float) Math.sqrt((x2-gunX)*(x2-gunX) + (y2-gunY)*(y2-gunY));//计算斜边长度
	}
	
	EnBullet enBul;//用于循环
	void findBulletToUse(float spdX, float spdY) {
		upDateGunLocation();
		for(int i=0; i<gf.enBullets.length; i++) { //寻找一个消亡的子弹,用来发射
			if(gf.enBullets[i].getState() == DEAD) {
				enBul = gf.enBullets[i];
				enBul.setState(ALIVE);
				enBul.setLocation(gunX-EnBullet.getW()/2, gunY-EnBullet.getH()/2);//使枪口位置与子弹中心对其
				enBul.setxSpeed(spdX);
				enBul.setySpeed(spdY);
				break;
			}
		}
	}
	
	void upDateGunLocation() {
		gunX = x + w/2;
		gunY = y + h;
	}
}
