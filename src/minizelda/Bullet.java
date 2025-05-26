package minizelda;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends Rectangle {
	
	public int dirX;
	public int dirY;
	public int speed = 8;
	public int frames = 0;
	
	public Bullet(int x, int y, int dirX, int dirY) {
		super(x+16, y+16, 12, 12);
		this.dirX = dirX;
		this.dirY = dirY;
		
	}
	
	public void tick() {
		x+=speed*dirX;
		y+=speed*dirY;
		
		frames++;
		if(frames == 75) {
			Player.bullets.remove(this);
		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(x, y, width, height);
	}
	
}
