package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Entity {
	
	public static BufferedImage MEDKIT_EN = Game.spriteSheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_EN = Game.spriteSheet.getSprite(144, 0, 16, 16);
	public static BufferedImage AMMO_EN = Game.spriteSheet.getSprite(128, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spriteSheet.getSprite(32, 48, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	protected int maskx;
	protected int masky;
	protected int maskh;
	protected int maskw;
	
	private BufferedImage sprite;
	
	public Entity(double x, double y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.maskw = width;
		this.maskh = height;
	}
	
	public void setMask(int maskx, int masky, int maskw, int maskh) {
		this.maskx = maskx;
		this.masky = masky;
		this.maskw = maskw;
		this.maskh = maskh;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public static boolean isColliding(Entity en1, Entity en2) {
		Rectangle en1Mask = new Rectangle(en1.getX() + en1.maskx, en1.getY() + en1.masky, en1.maskw, en1.maskh);
		Rectangle en2Mask = new Rectangle(en2.getX() + en2.maskx, en2.getY() + en2.masky, en2.maskw, en2.maskh);
		
		return en2Mask.intersects(en1Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.yellow);
		//g.drawRect(this.getX() + this.maskx - Camera.x, this.getY() + this.masky - Camera.y, this.maskw, this.maskh);
	}
	
}
