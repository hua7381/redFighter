package com.zgh.redFighter;

public class Plane extends Sprite{
	static GameFrame gf; //持有对方的引用
	float gunX, gunY;//枪口的位置
	Plane(GameFrame gf) {
		Plane.gf = gf;
	}
}
