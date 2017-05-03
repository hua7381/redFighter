package com.zgh.redFighter;

public class EnPlane3 extends EnPlane {
	// 不同种类的EnPlane之间的区别只有如下属性和move方法。

	EnPlane3(GameFrame gf) {
		super(gf);
		this.img = GameFrame.imgEnPlane3;
		this.frameCount = 8;
		this.w = img.getWidth(null) / frameCount; // 获取宽、高
		this.h = img.getHeight(null);
		this.ySpeed = 7;
		this.fireFreq = 20;
		this.fireRate = 100;
		this.hp = 2;
		this.bulSpeed = 4.5f;
		this.rateOfDropWepon = 5;
	}

	@Override
	void move() {
		x += xSpeed;
		y += ySpeed;

		if (y > 0) {
			tryToFire();
		}

		// 处理出界
		if (x < 0 || x > Client.SIDEBAR+Client.WIDTH || y> Client.UPBAR+Client.HEIGHT) { 
			this.state = DEAD;// 超出左、右、下边界就死亡
		}
		//控制移动轨迹的代码
		if(y > Client.UPBAR + 5) xSpeed = 1;
		if(y > Client.UPBAR + 50) {
			this.xSpeed = 2;
			this.ySpeed = 5;
		}
		if(y > Client.UPBAR + 70) {
			this.xSpeed = 3;
			this.ySpeed = 4;
			
		}
		if(y > Client.UPBAR + 90) {
			this.xSpeed = 5;
			this.ySpeed = 0;
			this.frame = 1;
		}
		if(x > Client.SIDEBAR + 150) {
			this.xSpeed = 2;
			this.ySpeed = -4;
			this.frame = 2;
		}
	}

	@Override
	void fire() {
		this.findBulletToUse(bulSpeed*getCos(gf.myPlane), bulSpeed*getSin(gf.myPlane));
	}

	@Override
	void coming() {
		state = ALIVE;
	}
}
