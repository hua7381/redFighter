package com.zgh.redFighter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Score {
	int score = 0;
	int fontSize = 15;
	Font font;
	Score() {
		font = new Font(Font.MONOSPACED,Font.BOLD,fontSize);
	}
	public void draw(Graphics g) {
		g.setColor(Color.green);
		g.setFont(font);
		g.drawString("得分："+String.valueOf(score), Client.SIDEBAR + Client.WIDTH/2, Client.UPBAR + fontSize);
	}
}
