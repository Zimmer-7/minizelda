package minizelda;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Player extends Rectangle {
	
	public int speed = 4;
	public boolean up;
	public boolean down;
	public boolean left;
	public boolean right;
	
	public int curAnimation = 0;
	public int curFrames = 0;
	public int targetFrames = 10;
	
	public static List<Bullet> bullets = new ArrayList<>();
	
	public boolean shoot = false;
	public int dirX = 0;
	public int dirY = 0;
	
	public Player(int x, int y) {
		super(x, y, 32, 32);
	}
	
	public void tick() {
		boolean moved = false;
		
		if (right && World.isFree(x+speed, y)) {
			x+=speed;
			moved = true;
			dirX = 1;
			dirY = 0;
		} 
		if (left && World.isFree(x-speed, y)) {
			x-=speed;
			moved = true;
			dirX = -1;
			dirY = 0;
		}
		
		if (up && World.isFree(x, y-speed)) {
			y-=speed;
			moved = true;
			dirX = 0;
			dirY = -1;
		} 
		if (down && World.isFree(x, y+speed)) {
			y+=speed;
			moved = true;
			dirX = 0;
			dirY = 1;
		}
		
		if(moved) {
			curFrames ++;
			if(curFrames == targetFrames) {
				curFrames = 0;
				curAnimation ++;
			
				if(curAnimation == SpriteSheet.player_front.length) {
					curAnimation = 0;
				}
			}
		}
		
		if(shoot) {
			bullets.add(new Bullet(x, y, dirX, dirY));
			shoot = false;
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
		
	}
	
	public void render(Graphics g) {
		g.drawImage(SpriteSheet.player_front[curAnimation], x, y, 32, 32, null);
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
	}
	
}
