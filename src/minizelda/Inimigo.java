package minizelda;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Inimigo extends Rectangle {
	
	public int speed = 3;
	public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right = true;
	
	public int curAnimation = 0;
	public int curFrames = 0;
	public int targetFrames = 10;
	
	public static List<Bullet> bullets = new ArrayList<>();
	
	public boolean shoot = false;
	public int dirX = 0;
	public int dirY = 0;
	
	public Inimigo(int x, int y) {
		super(x, y, 32, 32);
	}
	
	public void perseguirPlayer() {
		Player p = Game.player;
		
		if(x < p.x && World.isFree(x+speed, y) && new Random().nextInt(100) < 80)
				x+=speed;
		
		if(x > p.x && World.isFree(x-speed, y) && new Random().nextInt(100) < 80)
				x-=speed;
		
		if(y < p.y && World.isFree(x, y+speed) && new Random().nextInt(100) < 80)
				y+=speed;
		
		if(y > p.y && World.isFree(x, y-speed) && new Random().nextInt(100) < 80)
				y-=speed;
		
	}
	
	public void tick() {
		boolean moved = true;
		
		perseguirPlayer();
		
		if(moved) {
			curFrames ++;
			if(curFrames == targetFrames) {
				curFrames = 0;
				curAnimation ++;
			
				if(curAnimation == SpriteSheet.inimigo_front.length) {
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
		g.drawImage(SpriteSheet.inimigo_front[curAnimation], x, y, 32, 32, null);
		
	}
	
}
