package minizelda;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	public static BufferedImage spritesheet;
	
	public static BufferedImage[] inimigo_front;
	
	public static BufferedImage[] player_front;
	
	public static BufferedImage tree;
	
	public SpriteSheet() {
		try {
			spritesheet = ImageIO.read(getClass().getResource("/spritesheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player_front = new BufferedImage[2];
		player_front[0] = SpriteSheet.getSprite(0, 11, 16, 16);
		player_front[1] = SpriteSheet.getSprite(16, 11, 16, 16);
		inimigo_front = new BufferedImage[2];
		inimigo_front[0] = SpriteSheet.getSprite(168, 11, 16, 16);
		inimigo_front[1] = SpriteSheet.getSprite(185, 11, 16, 16);
		tree = SpriteSheet.getSprite(279, 206, 15, 18);
	}
	
	public static BufferedImage getSprite(int x, int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}
	
}
