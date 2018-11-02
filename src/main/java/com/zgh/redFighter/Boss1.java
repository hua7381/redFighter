package com.zgh.redFighter;

public class Boss1 extends EnemyPlane{

	final int RELOADTIME = 37;
	int reloadLoop = RELOADTIME;
	float bulSpd = 4;
	float bulSpdY, bulSpdX;
	float gunX1, gunX2, gunX3, gunX4;//4个附加的枪口位置
	Boss1(GameFrame gf) {
		super(gf);
		this.img = GameFrame.imgBoos1;
		this.frameCount = 11;
		this.w = img.getWidth(null)/frameCount;
		this.h = img.getHeight(null);
		this.hp = 180;
		this.ySpeed = 3;
		this.fireFreq = 1;
		this.fireRate = 100;
	}

	float tempY;
	int yMax = 80;
	float tempX;
	@Override
	void coming() {
		tempY = y;
		y += 3;
		if(y > Application.UPBAR + yMax) {
			y = tempY; //退到上一步
			state = ALIVE;
		}
	}
	
	@Override
	void move() {
		tempX = x;
		tempY = y;
		x += xSpeed;
		y += ySpeed;
		//控制竖直方向来回反弹
		if (y > Application.UPBAR + yMax || y < Application.UPBAR) {
			ySpeed = -ySpeed;
		}
		if(y < Application.UPBAR) {
			y = tempY;
			ySpeed = 2;
		} else if(y > Application.UPBAR + yMax) {
			y = tempY;
			ySpeed = -1;
		}
		
		//控制水平方向来回反弹
		if(x < Application.SIDEBAR || x > Application.SIDEBAR+Application.WIDTH-w) {
			x = tempX; //退到上一步
			xSpeed = -xSpeed;
		}
		//每20个周期为水平速度重新随机赋值
		moveLoop++;
		if (moveLoop == 20) {
			moveLoop = 0;
			xSpeed = random.nextInt(3) - 1;
		}
		
		tryToFire();
	}

	@Override
	void fire() {
		reloadFires();
		fire1();
		fire2();
		fire3();
	}

	static final float BULSPD1 = 3;
	int clip1 = 2; //剩余子弹数量
	final int clipAmount1 = 2; //弹夹容量
	int angDeg1 = 360; //角度
	private void fire1() {//旋转一周扫射
		if(clip1 > 0) {
			if(angDeg1 > 0) {
				for(int i=0; i<3; i++) {
					bulSpdY =- (float) (bulSpd * Math.sin(Math.toRadians(angDeg1))); 
					bulSpdX = (float) (bulSpd * Math.cos(Math.toRadians(angDeg1)));
					this.findBulletToUse(bulSpdX, bulSpdY);
					angDeg1 -= 13;
				}
				
			} else {
				clip1 --;
				angDeg1 = 360;
			}
		}
	}
	
	static final float BULSPD2 = 9;
	int fire2Loop;//控制每发之间的间隔
	int clip2 = 3; //剩余子弹数量
	final int clipAmount2 = 3; //弹夹容量
	float bulSpdX2, bulSpdY2;
	private void fire2() { //中间位置2个炮筒瞄准myPlane发射
		if(clip2 == clipAmount2) { //子弹刚刚加满，应该获取速度
			bulSpdX2 = BULSPD2*getCos(gf.myPlane);
			bulSpdY2 = BULSPD2*getSin(gf.myPlane);
		}
		if(clip2 > 0) {
			if(fire2Loop == 5) {
				fire2Loop = 0;
				clip2 --;
				this.findBulletToUse(bulSpdX2, bulSpdY2, gunX2);
				this.findBulletToUse(bulSpdX2, bulSpdY2, gunX);
				this.findBulletToUse(bulSpdX2, bulSpdY2, gunX3);
			}else {
				fire2Loop ++;
			}
		}
	}

	static final float BULSPD3 = 5;
	int clip3 = 0; //剩余子弹数量
	final int clipAmount3 = 12; //弹夹容量
	int fire3Loop;
	int angDeg2;
	private void fire3() { 
		if(clip3 > 0) {
			if(fire3Loop == 5) {
				fire3Loop = 0;
				clip3 --;
				angDeg2 = random.nextInt(60)+60;
				bulSpdX = (float) (BULSPD3 * Math.cos(Math.toRadians(angDeg2)));
				bulSpdY = (float) (BULSPD3 * Math.sin(Math.toRadians(angDeg2)));
				this.findBulletToUse(bulSpdX, bulSpdY , gunX1);
				angDeg2 = random.nextInt(90)+45;
				bulSpdX = (float) (BULSPD3 * Math.cos(Math.toRadians(angDeg2)));
				bulSpdY = (float) (BULSPD3 * Math.sin(Math.toRadians(angDeg2)));
				this.findBulletToUse(bulSpdX, bulSpdY , gunX4);
			} else {
				fire3Loop ++;
			}
		}
	}
	
	int fireFreq1 = 45, fireRate1 = 50;
	int fireFreq2 = 115,	fireRate2 = 50;
	int fireFreq3 = 25,	fireRate3 = 80;
	int fireLoop1, fireLoop2, fireLoop3;
	private void reloadFires() {
		if(clip1 <= 0 && clip3 <= 0) {
			fireLoop1  ++;
			if(fireLoop1 >= fireFreq1) { 
				fireLoop1 = 0;
				if(random.nextInt(100) <= fireRate1) {
					clip1 = this.clipAmount1;
				}
			}
		}
		if(clip2 <= 0) {
			fireLoop2  ++;
			if(fireLoop2 >= fireFreq2) { 
				fireLoop2 = 0;
				if(random.nextInt(100) <= fireRate2) {
					clip2 = this.clipAmount2;
				}
			}
		}
		if(clip3 <= 0 && clip1 <= 0) {
			fireLoop3  ++;
			if(fireLoop3 >= fireFreq3) { 
				fireLoop3 = 0;
				if(random.nextInt(100) <= fireRate3) {
					clip3 = this.clipAmount3;
				}
			}
		}
	}

	void findBulletToUse(float spdX, float spdY, float myGunX) {
		upDateGunLocation();//更新枪口位置
		for(int i=0; i<gf.enBullets.length; i++) { //寻找一个消亡的子弹,用来发射
			if(gf.enBullets[i].getState() == DEAD) {
				enBul = gf.enBullets[i];
				enBul.setState(ALIVE);
				enBul.setLocation(myGunX-EnBullet.getW()/2, gunY-EnBullet.getH()/2);//使枪口位置与子弹中心对其
				enBul.setxSpeed(spdX);
				enBul.setySpeed(spdY);
				break;
			}
		}
	}

	@Override
	void upDateGunLocation() {
		super.upDateGunLocation();
		gunX1 = x + 2;
		gunX2 = x+ w/2 - 9;
		gunX3 = x + w/2 + 9;
		gunX4 = x + w -2;
	}
}
