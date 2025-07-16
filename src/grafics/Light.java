package grafics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Light {
	
	public BufferedImage light;
	public int[] lightPixels;
	
	public Light() {
		try {
			light = ImageIO.read(getClass().getResource("/light.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		lightPixels = new int[light.getWidth() * light.getHeight()];
		light.getRGB(0, 0, light.getWidth(), light.getHeight(), lightPixels, 0, light.getWidth());
	}
	
	/*public void applyLight() {
		// Coming Soon
	}*/
	
}
