package com.zgh.redFighter;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameFrame extends Frame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	MyPlane myPlane;
	MyBullet[] myBullets = new MyBullet[100];//装myPlane的子弹的数组，可重复利用
	EnBullet[] enBullets = new EnBullet[500];//装敌军子弹的数组
	List<EnPlane> enemies = new ArrayList<EnPlane>();//装敌机的
	List<Wepon> wepons = new ArrayList<Wepon>(); //装武器道具的
	Map1 map1 = new Map1();
	Random random = new Random();
	Score score;
	
	static Image imgMap1, imgMap2, imgMyPlane, imgMyBullet, imgEnBullet, imgWepon;
	static Image imgEnPlane1,imgEnPlane2,imgEnPlane3,imgEnPlane4,imgBoos1;
	
	int mapIndex = 0, mapLoop = 0;
	int scrollIndex = 99;

	GameFrame() { //构造方法	
		score = new Score();
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
			}
		});
		
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				myPlane.KeyReleased(e);
			}
			@Override
			public void keyPressed(KeyEvent e) {
				myPlane.KeyPressed(e);
			}
		});
		
		//加载图片
		imgMap1 = Tools.getImage("images/map1.png");
		imgMap2 = Tools.getImage("images/map2.png");
		imgMyPlane = Tools.getImage("images/myplane.png");
		imgMyBullet = Tools.getImage("images/mybullet.png");
		imgEnBullet = Tools.getImage("images/enbullet.png");
		imgEnPlane1 = Tools.getImage("images/enplane1.png");
		imgEnPlane2 = Tools.getImage("images/enplane2.png");
		imgEnPlane3 = Tools.getImage("images/enplane3.png");
		imgEnPlane4 = Tools.getImage("images/enplane4.png");
		imgBoos1 = Tools.getImage("images/boss1.png");
		imgWepon = Tools.getImage("images/wepon.png");
		
		//装载图片（为了正确地获取图片的宽、高）
		MediaTracker mTracker = new MediaTracker(this);
		mTracker.addImage(imgMap1,1);
		mTracker.addImage(imgMap2,1);
		mTracker.addImage(imgMyPlane, 1);
		mTracker.addImage(imgMyBullet, 1);
		mTracker.addImage(imgEnBullet, 1);
		mTracker.addImage(imgWepon, 1);
		mTracker.addImage(imgEnPlane1, 1);
		mTracker.addImage(imgEnPlane2, 1);
		mTracker.addImage(imgEnPlane3, 1);
		mTracker.addImage(imgEnPlane4, 1);
		mTracker.addImage(imgBoos1, 1);
		try {
			mTracker.waitForID(1);
		} catch (InterruptedException e1) {
		}
		
		Tools.luanchFrame(this);
		
		for(int i=0; i<myBullets.length; i++) {//初始化子弹数组
			myBullets[i] = new MyBullet(this);
		}
		for(int i=0; i<enBullets.length; i++) {
			enBullets[i] = new EnBullet();
		}
		myPlane = new MyPlane(this);
		myPlane.setState(Sprite.ALIVE);
		myPlane.setLocation(Client.SIDEBAR+Client.WIDTH/2-myPlane.getWidth()/2, Client.UPBAR+Client.HEIGHT-myPlane.getHeight());
	}

	@Override
	public void paint(Graphics g) {
		refreshEn();
		
		drawMap(g,imgMap1); //画地图
		EnPlane en;
		for(int i=0; i<enemies.size(); i++) {  //画敌机
			en = enemies.get(i);
			en.draw(g);
		}
		myPlane.draw(g);//画玩家的飞机
		for(int i=0; i<myBullets.length; i++)  //画我的子弹
			myBullets[i].draw(g);
		
		for(int i=0; i<enBullets.length; i++) {  //画敌军子弹
			enBullets[i].draw(g);
		}
		
		Wepon wepon;
		for(int i=0; i<wepons.size(); i++) {
			wepon = wepons.get(i);
			wepon.draw(g);
		}
		/*
		//画界面数据
		g.setColor(Color.WHITE);
		g.drawString("life = "+ myPlane.getLife(), Client.SIDEBAR, Client.UPBAR+10);
		g.drawString("num of enemies:" + enemies.size() , Client.SIDEBAR, Client.UPBAR+25);
		*/
		g.setColor(Color.WHITE);
		g.drawString("wepon count = "+ wepons.size(), Client.SIDEBAR, Client.UPBAR+10);
		
		score.draw(g);
		myPlane.drawStingOfLive(g);
	}
	
	void refreshEn() {
		if(mapIndex == map1.points.length) { //若到达地图尾部，不再刷新
			return;
		}
		
		EnPlane en;
		mapLoop++;
		if(mapLoop==15) {  //每x个周期读取一次map
			mapLoop = 0;
			for(int i=0; i<map1.points[mapIndex].length; i++) {
				int n = map1.points[mapIndex][i];
				if(n>=1 && n<=5) {
					en = revivePlane(map1.points[mapIndex][i]);
					en.setLocation(Client.SIDEBAR + Client.WIDTH*i/map1.points[mapIndex].length,
							-en.getHeight()); //根据其在Map的数组中的水平方向上的位置来设置其初始位置
				}
			}
			mapIndex++;
			if(mapIndex == map1.points.length) {
				System.out.println("出现boss");
			}
		}
	}
	
	private EnPlane revivePlane(int type) {
		EnPlane en = null;
		switch(type) {
		case 1:	en = new EnPlane1(this);	break;
		case 2:	en = new EnPlane2(this);	break;
		case 3:	en = new EnPlane3(this);	break;
		case 4:	en = new EnPlane4(this);	break;
		case 5: en = new Boss1(this);		break;
		}
		enemies.add(en);
		return en;
	}
	
	private void drawMap(Graphics g, Image img) { //画不断滚动的背景地图
		this.scrollIndex += 4;
		if(scrollIndex > 0) scrollIndex = -img.getHeight(null);
		for(int i=scrollIndex; i<Client.HEIGHT; i+=img.getHeight(null)) {
			g.drawImage(img, Client.SIDEBAR, i + Client.UPBAR, null);
		}
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	//加入双缓冲效果
	private Image offScreenImage = null;
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) 
			offScreenImage = this.createImage(Client.WIDTH + Client.SIDEBAR*2, Client.HEIGHT + Client.UPBAR);//创建缓冲图
		Graphics gOffScreen = offScreenImage.getGraphics();//获取缓冲图的画笔
		
		paint(gOffScreen); //将内容画在缓冲图上
		g.drawImage(offScreenImage, 0, 0, null);//将缓冲图画到屏幕上
	}
}
