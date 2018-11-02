package com.zgh.redFighter;

/**
 * 应用启动类
 * @author zhangguihua(hua7381@163.com)
 * @date: 2018年11月1日
 */
public class Application {
	static final int WIDTH = 240, HEIGHT = 400; 
	static final int UPBAR = 25, SIDEBAR = 3; //边框所占的宽度
	static Thread thread = null;
	
	public static void main(String[] args) {
		Thread thread = new Thread(new MenuFrame(MenuFrame.LOGO));
		thread.start();
	}

}
