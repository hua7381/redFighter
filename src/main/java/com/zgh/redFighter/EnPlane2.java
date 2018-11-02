package com.zgh.redFighter;

public class EnPlane2 extends EnemyPlane {
	
	EnPlane2(GameFrame gf) {
		super(gf);
		this.img = GameFrame.imgEnPlane2;
		this.frameCount = 6;
		this.w = img.getWidth(null) / frameCount; // 获取宽、高
		this.h = img.getHeight(null);
		this.ySpeed = 1;
		this.fireFreq = 55;
		this.fireLoop = fireFreq/2;
		this.fireRate = 100;
		this.hp = 5;
		this.bulletClip = 4;
		this.bulSpeed = 5;
		this.rateOfDropWepon = 50;
	}

	int minY = -100;
	int maxY = Application.UPBAR + 70;
	@Override
	void move() {
		x += xSpeed;
		y += ySpeed;
		//控制竖直方向来回反弹
		if(y > maxY) {
			ySpeed = - ySpeed;
			minY = Application.UPBAR;
		} else if(y < minY) {
			ySpeed = - ySpeed;
		}
		// 每20个周期为水平速度重新随机赋值
		moveLoop++;
		if (moveLoop == 20) { 
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
		if (x > Application.SIDEBAR + Application.WIDTH - w) { // 碰到右边界停止并转向
			x = Application.SIDEBAR + Application.WIDTH - w;
			xSpeed = -xSpeed;
		}
		if (y > Application.UPBAR + Application.HEIGHT) // 越过下边界后死亡
			this.state = DEAD;
	}

	@Override
	void fire() {
		bulletClip --; //弹夹中剩余量减一
		if(bulletClip == -1) 
			state = RETREAT; //发射完成后进入撤退状态
		
		for(int k=-1; k<=1; k++) { // 以散弹形式发射三个子弹
			this.findBulletToUse(k, bulSpeed);
		}
	}

	@Override
	void coming() {
		state = ALIVE;
	}
}
