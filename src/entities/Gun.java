package entities;

import java.awt.image.BufferedImage;

public class Gun extends Entity {

	public Gun(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.setMask(4, 4, 10, 8);
	}

}
