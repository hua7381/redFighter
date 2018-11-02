package com.zgh.redFighter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



public class MenuFrame extends Frame implements Runnable{
	private static final long serialVersionUID = 1L;
	private int chosenIndex = 0;
	private final int START=0, SETTING=1, HELP=2, ABOUT=3, EXIT=4;
	private String[] options = {
			"开始游戏",
			"游戏设置",
			"帮助",
			"关于",
			"退出游戏"
	};

	final int fontSize = 25;
	
	static final int LOGO = 5, MENU = 6;
	int state;
	
	Font fontNomal = new Font(Font.MONOSPACED,Font.BOLD,fontSize-5);
	Font fontChosen = new Font(Font.MONOSPACED,Font.ITALIC,25);
	int flashIndex = 0; //控制字体闪烁的index
	
	static Toolkit toolKit = Toolkit.getDefaultToolkit();
	private static Image imgLogo, imgAnyKey, imgMenu;
	
	MenuFrame(int state) { //构造方法
		imgLogo = Tools.getImage("images/logo.png");
		imgAnyKey = Tools.getImage("images/anyKey.png");
		imgMenu = Tools.getImage("images/menu.png");
		
		Tools.luanchFrame(this);
		this.state = state;
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				switch(getState()) {
				case LOGO:
					setState(MENU);	
					break;
				case MENU:
					switch(e.getKeyCode()) {
					case KeyEvent.VK_UP:
						chosenIndex --;
						if(chosenIndex < 0) //循环跳转
							chosenIndex = options.length - 1;
						break;
					case KeyEvent.VK_DOWN:
						chosenIndex ++;
						if(chosenIndex >= options.length) //循环跳转
							chosenIndex = 0;
						break;
					case KeyEvent.VK_ENTER: //按回车
						switch(chosenIndex) {
						case START:
							setState(START); //使run方法中的while循环结束
							setVisible(false);
							Application.thread = new Thread(new GameFrame());
							Application.thread.start();
							break;
						case SETTING:
							setState(SETTING);
							break;
						case HELP:
							setState(HELP);
							break;
						case ABOUT:
							setState(ABOUT);
							break;
						case EXIT:
							setState(EXIT);//使run方法中的while循环结束
							setVisible(false);
							break;
						}
						break;
					}//switch(e.getKeyCode())
					break;
				//以下三种状态下按Esc返回Menu
				case SETTING:
				case ABOUT:
				case HELP:
					if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
						setState(MENU);
					break;
				}//switch(getState())
			}
		});
	}

	public void run() {
		while(state!=START && state!=EXIT) {
			try {
				Thread.sleep(18);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		switch(state) {
		case LOGO:
			g.drawImage(imgLogo, Application.SIDEBAR, Application.UPBAR, null);
			flash(g);
			break;
		case MENU:
			g.drawImage(imgMenu, Application.SIDEBAR, Application.UPBAR, null);
			drawOptions(g);
			break;
		case HELP:
			drawHelp(g);
			break;
		case SETTING:
			drawSettings(g);
			break;
		case ABOUT:
			drawAbout(g);
			break;
		}
	}
	
	private void drawOptions(Graphics g) {
		//显示未被选中的项
		g.setFont(fontNomal);
		g.setColor(Color.WHITE);
		for(int i=0; i<options.length; i++) {
			if(i == chosenIndex) continue;
			g.drawString(options[i], 120, 250+fontSize*i);
		}
		//显示被选中的项
		g.setColor(Color.YELLOW);
		g.setFont(fontChosen);
		g.drawString(options[chosenIndex], 120, 250+fontSize*chosenIndex);
	}
	
	private void drawHelp(Graphics g) {
		refresh(g);
		g.setColor(Color.WHITE);
		g.setFont(fontNomal);
		g.drawString("帮助文档：", Application.SIDEBAR, Application.UPBAR+30);
		g.drawString("按Esc返回", 0, Application.UPBAR+Application.HEIGHT-3);
	}
	
	private void drawSettings(Graphics g) {
		refresh(g);
		g.setColor(Color.WHITE);
		g.setFont(fontNomal);
		g.drawString("游戏设置：", Application.SIDEBAR, Application.UPBAR+30);
		g.drawString("按Esc返回", 0, Application.UPBAR+Application.HEIGHT-3);
	}
	
	private void drawAbout(Graphics g) {
		refresh(g);
		g.setColor(Color.WHITE);
		g.setFont(fontNomal);
		g.drawString("关于本游戏：", Application.SIDEBAR, Application.UPBAR+30);
		g.drawString("按Esc返回", 0, Application.UPBAR+Application.HEIGHT-3);
	}
	
	private void refresh(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(Application.SIDEBAR, Application.UPBAR, Application.WIDTH, Application.HEIGHT);//刷屏
	}
	
	
	private void flash(Graphics g) { //字符闪烁
		flashIndex ++;
		if(flashIndex > 500) flashIndex = 0;
		if(flashIndex % 50 > 18) g.drawImage(imgAnyKey, Application.SIDEBAR + 65, Application.UPBAR + 240, null);
	}
	
	private Image offScreenImage = null;
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) 
			offScreenImage = this.createImage(Application.WIDTH + Application.SIDEBAR*2, Application.HEIGHT + Application.UPBAR);//创建缓冲图
		Graphics gOffScreen = offScreenImage.getGraphics();//获取缓冲图的画笔
		
		paint(gOffScreen); //将内容画在缓冲图上
		g.drawImage(offScreenImage, 0, 0, null);//将缓冲图画到屏幕上
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}

}
