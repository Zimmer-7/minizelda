package grafics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	private BufferedImage spritesheet;
	
	public static BufferedImage[] inimigo_front;
	
	public static BufferedImage[] player_front;
	
	public static BufferedImage tree;
	
	public SpriteSheet(String path) {
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}
	
}
