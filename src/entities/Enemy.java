package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import main.AdvancedSound;
import main.Game;
import main.Sound;
import world.AStar;
import world.Camera;
import world.Node;
import world.Vector2i;
import world.World;

public class Enemy extends Entity {
	
	private double speed = 2;
	private boolean right = false;
	private boolean left = false;
	private int life = 6;
	private boolean damaged = false;
	
	private int frames = 0;
	private int damageFrames = 0;
	private int maxFrames = 6;
	private int index = 0;
	private int maxIndex = 1;
	
	private BufferedImage[] enemyRight;
	private BufferedImage[] enemyLeft;
	private BufferedImage enemyDamageLeft;
	private BufferedImage enemyDamageRight;

	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		enemyRight = new BufferedImage[2];
		enemyLeft = new BufferedImage[2];
		
		enemyLeft[0] = Game.spriteSheet.getSprite(32, 48, 16, 16);
		enemyLeft[1] = Game.spriteSheet.getSprite(32, 64, 16, 16);
		enemyRight[0] = Game.spriteSheet.getSprite(48, 48, 16, 16);
		enemyRight[1] = Game.spriteSheet.getSprite(48, 64, 16, 16);
		enemyDamageLeft = Game.spriteSheet.getSprite(32, 80, 16, 16);
		enemyDamageRight = Game.spriteSheet.getSprite(48, 80, 16, 16);
		
		depth = 0;
		
		this.setMask(3, 5, 8, 6);
	}
	
	public void tick() {
		
		if(this.calcDistance(this.getX(), Game.player.getX(), this.getY(), Game.player.getY()) < 1000){
			
			if(path == null || path.isEmpty()) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)((Game.player.x+8)/16), (int)((Game.player.y+8)/16));
				path = AStar.findPath(start, end);
			}
			
			followPath(path, speed);
			
			if(Game.rand.nextInt(100) < 10) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)((Game.player.x+8)/16), (int)((Game.player.y+8)/16));
				path = AStar.findPath(start, end);
			}
			
			if(touching() && frames == 0) {
				AdvancedSound.hurt.play();
				Game.player.life --;
				if(Game.rand.nextInt(100) < 25)
					Game.player.life --;
				Game.player.damaged = true;
			}
			
			frames ++;
			if(frames == maxFrames) {
				index++;
				frames = 0;
					
				if(index > maxIndex) 
					index = 0;
					
			}
			
			checkDamage();
			
			if(life <= 0) {
				Game.entities.remove(this);
				Game.enemies.remove(this);
				return;
			}
			
			if(damaged) {
				damageFrames ++;
				if(damageFrames == maxFrames) {
					damageFrames = 0;
					damaged = false;
				}
			}
				
		} 
		
	}
		
	
	public boolean touching() {
		Rectangle current = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return current.intersects(player);
	}
	
	public boolean isCollidingEn(int xnext, int ynext) {
		Rectangle current = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			
			Rectangle target = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			
			if(current.intersects(target))
				return true;
		}
		
		return false;
	}
	
	public boolean checkDamage() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Bullet bullet = Game.bullets.get(i);
			
			if(Entity.isColliding(this, bullet)) {
				damaged = true;
				life--;
				Game.bullets.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	public void followPath(List<Node> path, double speed) {
		if(path != null && !path.isEmpty()) {
			Vector2i target = path.get(path.size() - 1).tile;
			
			if(Game.rand.nextInt(100) < 50) {	
				if(x < target.x*16 &&
					!isCollidingEn((int)(x+speed), (int)y)) {
					x+=speed;
					right = true;
					left = false;
				}
				if(x > target.x*16 &&
					!isCollidingEn((int)(x-speed), (int)y)) {
					x-=speed;
					right = false;
					left = true;
				}
				if(y < target.y*16 &&
					!isCollidingEn((int)x, (int)(y+speed))) 
					y+=speed;
		
				if(y > target.y*16 &&
					!isCollidingEn((int)x, (int)(y-speed))) 
					y-=speed;
					
				if(x == target.x*16 && y == target.y*16)
					path.remove(path.size() - 1);
			}
		}
			
	}
	
	@Override
	public void render(Graphics g) {
		if(right) {
			g.drawImage(enemyRight[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			if(damaged) {
				g.drawImage(enemyDamageRight, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
		if(left) {
			g.drawImage(enemyLeft[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			if(damaged) {
				g.drawImage(enemyDamageLeft, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
		
		//g.setColor(Color.blue);
		//g.drawRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}

}
