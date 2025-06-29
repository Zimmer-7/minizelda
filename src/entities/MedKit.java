package entities;

import java.awt.image.BufferedImage;

public class MedKit extends Entity {

	public MedKit(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.setMask(2, 2, 11, 10);
	}
	
	

}
