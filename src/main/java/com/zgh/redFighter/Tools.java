package com.zgh.redFighter;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Tools extends Frame{
	private static final long serialVersionUID = 1L;

	static void luanchFrame(Frame f) {
		f.setBounds(600, 120, Client.WIDTH + Client.SIDEBAR*2, Client.HEIGHT + Client.UPBAR + Client.SIDEBAR);
		f.setBackground(Color.GRAY);
		f.setResizable(false);
		f.setVisible(true);
	}
	
	static Toolkit toolKit = Toolkit.getDefaultToolkit();
	static Image img = null;
	static Image getImage(String name) {
		img = toolKit.getImage(Tools.class.getClassLoader().getResource(name));
		return img;
	}
	
	static void drawClipImg(Image img, Graphics g, int x, int y, int mx, int my, int mw, int mh) {
		g.setClip(x, y, mw, mh);//设置切割区域，设置后画笔只会画这一部分
		g.drawImage(img, x-mx, y-my, null);//画整个大图
		g.setClip(Client.SIDEBAR, Client.UPBAR, Client.WIDTH, Client.HEIGHT);//设置回全屏
	}
}
