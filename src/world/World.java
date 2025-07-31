package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.*;
import grafics.SpriteSheet;
import main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH;
	public static int HEIGHT;
	
	public World(String path, int level) {
		
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[WIDTH * HEIGHT];
			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			int pos;
			for(int xx = 0; xx < WIDTH; xx++) {
				for(int yy = 0; yy < HEIGHT; yy++) {
					pos = xx + (yy*WIDTH);
					if(level == 1) {
						if(Game.rand.nextInt(100) < 50) {
							tiles[pos] = new Floor(xx*16, yy*16, Tile.TILE_FLOOR_GRASS_1);
						} else {
							tiles[pos] = new Floor(xx*16, yy*16, Tile.TILE_FLOOR_GRASS_2);
						}
					}
					if(level == 2) {
						if(Game.rand.nextInt(100) < 50) {
							tiles[pos] = new Floor(xx*16, yy*16, Tile.TILE_FLOOR_EARTH_1);
						} else {
							tiles[pos] = new Floor(xx*16, yy*16, Tile.TILE_FLOOR_EARTH_2);
						}
					}
					if(pixels[pos] == 0xFFFFFFFF) {
						//parede
						if(level == 1)
							tiles[pos] = new Wall(xx*16, yy*16, Tile.TILE_WALL_DARK);
						if(level == 2)
							tiles[pos] = new Wall(xx*16, yy*16, Tile.TILE_WALL_LIGHT);
					}
					if(pixels[pos] == 0xFF4838FF) {
						//personagem
						Game.player.setX(xx*16.0);
						Game.player.setY(yy*16.0);
					}
					if(pixels[pos] == 0xFF808080) {
						//NPC
						Game.npc.setX(xx*16.0);
						Game.npc.setY(yy*16.0);
					}
					if(pixels[pos] == 0xFFFF0000) {
						//inimigo
						Enemy en = new Enemy(xx*16.0, yy*16.0, 16, 16, null);
						Game.entities.add(en);
						Game.enemies.add(en);
					}
					if(pixels[pos] == 0xFF4CFF00) {
						//arma
						Gun gun = new Gun(xx*16.0, yy*16.0, 16, 16, Entity.GUN_EN);
						Game.entities.add(gun);
						Game.items.add(gun);
					}
					if(pixels[pos] == 0xFFFF00DC) {
						//cura
						MedKit medkit = new MedKit(xx*16.0, yy*16.0, 16, 16, Entity.MEDKIT_EN);
						Game.entities.add(medkit);
						Game.items.add(medkit);
					}
					if(pixels[pos] == 0xFFFFE97F) {
						//munição
						Ammo ammo = new Ammo(xx*16.0, yy*16.0, 16, 16, Entity.AMMO_EN);
						Game.entities.add(ammo);
						Game.items.add(ammo);
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isFree(int x, int y) {
		int x1 = x/16;
		int y1 = y/16;
		int x2 = (x+16-1) / 16;
		int y2 = (y+16-1) / 16;
		
		return !(tiles[x1 + (y1*WIDTH)] instanceof Wall ||
				tiles[x1 + (y2*WIDTH)] instanceof Wall ||
				tiles[x2 + (y1*WIDTH)] instanceof Wall ||
				tiles[x2 + (y2*WIDTH)] instanceof Wall);
	}
	
	public static void startLevel(int level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.items.clear();
		Game.bullets.clear();
		Game.entities = new ArrayList<>();
		Game.enemies = new ArrayList<>();
		Game.items = new ArrayList<>();
		Game.bullets = new ArrayList<>();
		Game.spriteSheet = new SpriteSheet("/recursos.png");	
		Game.player = new Player(0, 0, 16, 16, Game.spriteSheet.getSprite(32, 0, 16, 16));
		Game.npc = new Npc(0, 0, 16, 16, Game.spriteSheet.getSprite(80, 48, 16, 16));
		Game.world = new World("/mapa"+level+".png", level);
		Game.entities.add(Game.player);
		Game.entities.add(Game.npc);
		Game.gameState = "Normal";
	}
	
	public static void renderMiniMap(int[] miniMap) {
		/*for(int i = 0; i < miniMap.length; i++) {
			miniMap[i] = 0;
		}*/
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx + yy*WIDTH] instanceof Wall)
					miniMap[xx + yy*WIDTH] = 0xffffff;
			}
		}
		
		int x;
		int y;
		Entity en;
		
		for(int i = 0; i < Game.entities.size(); i++) {
			en = Game.entities.get(i);
			x = en.getX()/16;
			y = Game.entities.get(i).getY()/16;
			
			if(en instanceof Player) 
				miniMap[x + y*WIDTH] = 0x0000ff;
			if(en instanceof Enemy)
				miniMap[x + y*WIDTH] = 0xff0000;
			if(en instanceof MedKit)
				miniMap[x + y*WIDTH] = 0xff00ff;
			if(en instanceof Ammo)
				miniMap[x + y*WIDTH] = 0xffff00;
			if(en instanceof Gun)
				miniMap[x + y*WIDTH] = 0x00ff00;
			if(en instanceof Npc) 
				miniMap[x + y*WIDTH] = 0x808080;
		}
	}
	
	public static void generateParticles(int amount, int x, int y) {
		for(int i = 0; i < amount; i++) 
			Game.entities.add(new Particle(x, y, 1, 1, null));
	}

	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		Tile tile;
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				
				tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
