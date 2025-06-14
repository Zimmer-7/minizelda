package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Entity {
	
	public static BufferedImage MEDKIT_EN = Game.spriteSheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_EN = Game.spriteSheet.getSprite(144, 0, 16, 16);
	public static BufferedImage AMMO_EN = Game.spriteSheet.getSprite(128, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spriteSheet.getSprite(32, 48, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	public Entity(double x, double y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
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
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX(), this.getY(), null);
	}
	
}
