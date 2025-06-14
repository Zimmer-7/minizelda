package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entities.*;

import main.Game;

public class World {
	
	private Tile[] tiles;
	public static int WIDTH;
	public static int HEIGHT;
	
	public World(String path) {
		
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
					tiles[pos] = new Floor(xx*16, yy*16, Tile.TILE_FLOOR);
					if(pixels[pos] == 0xFFFFFFFF) {
						//parede
						tiles[pos] = new Wall(xx*16, yy*16, Tile.TILE_WALL);
					}
					if(pixels[pos] == 0xFF4838FF) {
						//personagem
						Game.player.setX(xx*16.0);
						Game.player.setY(yy*16.0);
					}
					if(pixels[pos] == 0xFFFF0000) {
						//inimigo
						Game.entities.add(new Enemy(xx*16.0, yy*16.0, 16, 16, Entity.ENEMY_EN));
					}
					if(pixels[pos] == 0xFF4CFF00) {
						//arma
						Game.entities.add(new Gun(xx*16.0, yy*16.0, 16, 16, Entity.GUN_EN));
					}
					if(pixels[pos] == 0xFFFF00DC) {
						//cura
						Game.entities.add(new MedKit(xx*16.0, yy*16.0, 16, 16, Entity.MEDKIT_EN));
					}
					if(pixels[pos] == 0xFFFFE97F) {
						//munição
						Game.entities.add(new Ammo(xx*16.0, yy*16.0, 16, 16, Entity.AMMO_EN));
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void render(Graphics g) {
		Tile tile;
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
