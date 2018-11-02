package com.zgh.redFighter;

public class EnPlane1 extends EnemyPlane {
	// 不同种类的EnPlane之间的区别只有如下属性和move方法。

	EnPlane1(GameFrame gf) {
		super(gf);
		this.img = GameFrame.imgEnPlane1;
		this.frameCount = 10;
		this.w = img.getWidth(null) / frameCount; // 获取宽、高
		this.h = img.getHeight(null);
		this.ySpeed = 1;
		this.fireFreq = 30;
		this.fireRate = 100;
		this.hp = 2;
		this.bulSpeed = 6;
		this.fireLoop = fireFreq;
		this.rateOfDropWepon = 5;
	}

	@Override
	void move() {
		x += xSpeed;
		y += ySpeed;

		moveLoop++;
		if (moveLoop == 20) { // 每20个周期为水平速度随机赋值
			moveLoop = 0;
			xSpeed = random.nextInt(5) - 2;
		}

		if (y > 0) {
			tryToFire();
		}
		
		if(y > Application.UPBAR + 200) //超过屏幕的一半后进入撤退状态
			state = RETREAT;

		// 处理出界
		if (x < 0) { // 碰到左边界停止并转向
			x = 0;
			xSpeed = -xSpeed;
		}
		if (x > Application.SIDEBAR + Application.WIDTH - w) { // 碰到左边界停止并转向
			x = Application.SIDEBAR + Application.WIDTH - w;
			xSpeed = -xSpeed;
		}
		if (y > Application.UPBAR + Application.HEIGHT) // 越过下边界后死亡
			this.state = DEAD;
		
	}

	@Override
	void fire() {
		this.findBulletToUse(bulSpeed*getCos(gf.myPlane), bulSpeed*getSin(gf.myPlane));
	}

	@Override
	void coming() {
		y += ySpeed*6;
		if(y > Application.UPBAR + 40)
			state = ALIVE;
	}

	@Override
	void retreat() { //重写了该方法，让其有着不同的退场方式
		y += ySpeed*2;
		x += xSpeed + (xSpeed>0? 3:-3);
		if(x<0 || x>Application.SIDEBAR+Application.WIDTH)
			state = DEAD;
	}
	
	
}
