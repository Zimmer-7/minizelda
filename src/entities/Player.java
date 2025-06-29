package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import grafics.SpriteSheet;
import main.Game;
import world.Camera;
import world.World;

public class Player extends Entity {
	
	public double maxLife = 100;
	public double life = maxLife;
	
	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;
	public double speed = 1.5;
	
	public int dirY = -1;
	public int dirX = 1;
	
	private int frames = 0;
	private int damageFrames = 0;
	private int maxFrames = 6;
	private int index = 0;
	private int maxIndex = 1;
	
	private boolean moved = false;
	public boolean damaged = false;
	
	public static int ammo = 0;
	
	private BufferedImage[] playerRightDown;
	private BufferedImage[] playerLeftDown;
	private BufferedImage[] playerRightUp;
	private BufferedImage[] playerLeftUp;
	private BufferedImage playerStopRightDown;
	private BufferedImage playerStopLeftDown;
	private BufferedImage playerStopRightUp;
	private BufferedImage playerStopLeftUp;
	private BufferedImage playerDamage;
	
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
		playerDamage = Game.spriteSheet.getSprite(112, 48, 16, 16);
	}
	
	@Override
	public void tick() {
		moved = false;
		double lowSpeed = speed * 0.75;
		double auxSpeed = speed;
		
		if(right&&up || right&&down || left&&up || left&&down) {
			speed = lowSpeed;
		}
		
		if(right && World.isFree((int)(x+speed), (int)y)) {
			moved = true;
			dirX = 1;
			x+=speed;
		}
		if(left && World.isFree((int)(x-speed), (int)y)) {
			moved = true;
			dirX = -1;
			x-=speed;
		}
		if(up && World.isFree((int)x, (int)(y-speed))) {
			moved = true;
			dirY = 1;
			y-=speed;
		}
		if(down && World.isFree((int)x, (int)(y+speed))) {
			moved = true;
			dirY = -1;
			y+=speed;
		}
		
		speed = auxSpeed;
		
		if(moved) {
			
			frames ++;
			if(frames == maxFrames) {
				index++;
				frames = 0;
				
				if(index > maxIndex) 
					index = 0;
				
			}
		}
		
		if(damaged) {
			damageFrames ++;
			if(damageFrames == maxFrames) {
				damageFrames = 0;
				damaged = false;
			}
		}
		
		if(life <= 0) {
			Game.entities.clear();
			Game.enemies.clear();
			Game.items.clear();
			Game.entities = new ArrayList<>();
			Game.enemies = new ArrayList<>();
			Game.items = new ArrayList<>();
			Game.spriteSheet = new SpriteSheet("/recursos.png");	
			Game.player = new Player(0, 0, 16, 16, Game.spriteSheet.getSprite(32, 0, 16, 16));
			Game.world = new World("/mapa1.png");
			Game.entities.add(Game.player);
			return;
		}
		
		checkItems();
		
		Camera.x = Camera.clamp(this.getX() - Game.WIDTH/2, 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - Game.HEIGHT/2, 0, World.HEIGHT*16 - Game.HEIGHT);
		
	}
	
	public void checkItems() {
		for(int i = 0; i < Game.items.size(); i++) {
			Entity atual = Game.items.get(i);
			if(Entity.isColliding(this, atual)) {
				if(atual instanceof MedKit && life < 100) {
					life += 25;
						
					if(life > 100)
						life = 100;
						
					Game.items.remove(atual);
					Game.entities.remove(atual);
				}
				if(atual instanceof Ammo) {
					ammo += 30;
						
					Game.items.remove(atual);
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g){
		if(dirX == 1 && dirY == -1 && moved) 
			g.drawImage(playerRightDown[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		if(dirX == -1 && dirY == -1 && moved) 
			g.drawImage(playerLeftDown[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		if(dirX == 1 && dirY == 1 && moved) 
			g.drawImage(playerRightUp[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		 
		if(dirX == -1 && dirY == 1 && moved) 
			g.drawImage(playerLeftUp[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		if(dirX == 1 && dirY == -1 && !moved) 
			g.drawImage(playerStopRightDown, this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		if(dirX == -1 && dirY == -1 && !moved) 
			g.drawImage(playerStopLeftDown, this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		if(dirX == 1 && dirY == 1 && !moved) 
			g.drawImage(playerStopRightUp, this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		if(dirX == -1 && dirY == 1 && !moved) 
			g.drawImage(playerStopLeftUp, this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		if(damaged) {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
	}
	
}
