package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Player extends Entity {
	
	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;
	public double speed = 1.5;
	
	public int dirY = -1;
	public int dirX = 1;
	
	private int frames = 0;
	private int maxFrames = 6;
	private int index = 0;
	private int maxIndex = 1;
	
	private boolean moved = false;
	
	private BufferedImage[] playerRightDown;
	private BufferedImage[] playerLeftDown;
	private BufferedImage[] playerRightUp;
	private BufferedImage[] playerLeftUp;
	private BufferedImage playerStopRightDown;
	private BufferedImage playerStopLeftDown;
	private BufferedImage playerStopRightUp;
	private BufferedImage playerStopLeftUp;
	
	public Player(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		playerRightDown = new BufferedImage[2];
		playerLeftDown = new BufferedImage[2];
		playerRightUp = new BufferedImage[2];
		playerLeftUp = new BufferedImage[2];
		
		for(int i = 0; i < 2; i++) {
			playerRightDown[i] = Game.spriteSheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 2; i++) {
			playerLeftDown[i] = Game.spriteSheet.getSprite(80 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 2; i++) {
			playerRightUp[i] = Game.spriteSheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		for(int i = 0; i < 2; i++) {
			playerLeftUp[i] = Game.spriteSheet.getSprite(80 + (i*16), 16, 16, 16);
		}
		
		playerStopRightDown = Game.spriteSheet.getSprite(64, 0, 16, 16);
		playerStopLeftDown = Game.spriteSheet.getSprite(112, 0, 16, 16);
		playerStopRightUp = Game.spriteSheet.getSprite(64, 16, 16, 16);
		playerStopLeftUp = Game.spriteSheet.getSprite(112, 16, 16, 16);
	}
	
	@Override
	public void tick() {
		moved = false;
		if(right) {
			moved = true;
			dirX = 1;
			x+=speed;
		}
		if(left) {
			moved = true;
			dirX = -1;
			x-=speed;
		}
		if(up) {
			moved = true;
			dirY = 1;
			y-=speed;
		}
		if(down) {
			moved = true;
			dirY = -1;
			y+=speed;
		}
		
		if(moved) {
			frames ++;
			if(frames == maxFrames) {
				index++;
				frames = 0;
				
				if(index > maxIndex) 
					index = 0;
				
			}
		}
	}
	
	@Override
	public void render(Graphics g){
		if(dirX == 1 && dirY == -1 && moved) 
			g.drawImage(playerRightDown[index], this.getX(), this.getY(), null);
		
		if(dirX == -1 && dirY == -1 && moved) 
			g.drawImage(playerLeftDown[index], this.getX(), this.getY(), null);
		
		if(dirX == 1 && dirY == 1 && moved) 
			g.drawImage(playerRightUp[index], this.getX(), this.getY(), null);
		
		if(dirX == -1 && dirY == 1 && moved) 
			g.drawImage(playerLeftUp[index], this.getX(), this.getY(), null);
		
		if(dirX == 1 && dirY == -1 && !moved) 
			g.drawImage(playerStopRightDown, this.getX(), this.getY(), null);
		
		if(dirX == -1 && dirY == -1 && !moved) 
			g.drawImage(playerStopLeftDown, this.getX(), this.getY(), null);
		
		if(dirX == 1 && dirY == 1 && !moved) 
			g.drawImage(playerStopRightUp, this.getX(), this.getY(), null);
		
		if(dirX == -1 && dirY == 1 && !moved) 
			g.drawImage(playerStopLeftUp, this.getX(), this.getY(), null);
		
		
	}
	
}
