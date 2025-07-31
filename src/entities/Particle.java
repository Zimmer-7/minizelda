package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Particle extends Entity {
	private int lifeTime = 8;
	private int curTime = 0;
	
	private int speed = 3;
	private double dx;
	private double dy;

	public Particle(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		dx = Game.rand.nextGaussian();
		dy = Game.rand.nextGaussian();
	}
	
	@Override
	public void tick() {
		x+=dx*speed;
		y+=dy*speed;
		curTime++;
		if(curTime >= lifeTime) {
			Game.entities.remove(this);
		}
			
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}

}
