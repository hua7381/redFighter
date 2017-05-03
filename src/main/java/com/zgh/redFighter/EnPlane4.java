package com.zgh.redFighter;

public class EnPlane4 extends EnPlane {
	// 不同种类的EnPlane之间的区别只有如下属性和move方法。
	
	static final int RELOADTIME = 7;
	
	EnPlane4(GameFrame gf) {
		super(gf);
		this.img = GameFrame.imgEnPlane4;
		this.frameCount = 6;
		this.w = img.getWidth(null) / frameCount; // 获取宽、高
		this.h = img.getHeight(null);
		this.ySpeed = 1;
		this.fireFreq = 5;
		this.fireRate = 100;
		this.hp = 26;
		this.bulletClip = 5;
		this.bulSpeed = 4;
		this.rateOfDropWepon =100;
	}

	int minY = -100;
	int maxY = Client.UPBAR + 80;
	@Override
	void move() {
		x += xSpeed;
		y += ySpeed;
		//控制它在竖直方向上来回移动的代码
		if(y > maxY) {
			ySpeed = - ySpeed;
			minY = Client.UPBAR;
		} else if(y < minY) {
			ySpeed = - ySpeed;
		}

		moveLoop++;
		if (moveLoop == 20) { // 每20个周期为水平速度随机赋值
			moveLoop = 0;
			xSpeed = random.nextInt(3) - 1;
		}

		if (y > 0) {
			tryToFire();
		}

		// 处理出界
		if (x < 0) { // 碰到左边界停止并转向
			x = 0;
			xSpeed = -xSpeed;
		}
		if (x > Client.SIDEBAR + Client.WIDTH - w) { // 碰到左边界停止并转向
			x = Client.SIDEBAR + Client.WIDTH - w;
			xSpeed = -xSpeed;
		}
	}
	
	int k = -4; //用于决定子弹x方向上的速度
	int reloadLoop = RELOADTIME;
	@Override
	void fire() {
		if (reloadLoop < RELOADTIME) { //处于装载阶段，不发射
			reloadLoop ++;
			return;
		}
		k ++;
		if(k == 5) { //方向已达到最右，一轮发射完毕
			bulletClip --;
			if(bulletClip == 0) { //若发射完所有子弹，则进入撤退状态，并终止本次发射
				state = RETREAT;
				return;
			}
			
			k = -4;
			reloadLoop = 0;
			return;
		}
		this.findBulletToUse(k, bulSpeed);
	}
	@Override
	void coming() {
		state = ALIVE;
	}
}
