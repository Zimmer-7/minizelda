package entities;

import java.awt.image.BufferedImage;

public class Ammo extends Entity {

	public Ammo(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.setMask(2, 5, 9, 7);
	}

}
