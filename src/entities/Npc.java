package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Npc extends Entity {
	
	public String[] frases = new String[2];
	private boolean showMessage = false;
	private int curIndex = 0;
	private int msgIndex = 0;
	private int count = 0;

	public Npc(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		frases[0] = "olá amigo";
		frases[1] = "Não sei pq estou aqui";
	}
	
	@Override
	public void tick() {
		//System.out.println(calcDistance(this.getX(), Game.player.getX(), this.getY(), Game.player.getY()));
		if(calcDistance(this.getX(), Game.player.getX(), this.getY(), Game.player.getY()) < 30) {
			showMessage = true;
		} else {
			showMessage = false;
		}
		
		if(showMessage) { 
			if(curIndex < frases[msgIndex].length()) {
				curIndex++;
			} else if(msgIndex < frases.length-1) {
				count++;
				if(count == 30) {
					msgIndex ++;
					curIndex = 0;
					count = 0;
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		if(showMessage) {
			System.out.println("Entrei");
			g.setFont(new Font("Arial", Font.BOLD, 9));
			g.setColor(Color.black);
			g.drawString(frases[msgIndex].substring(0, curIndex), this.getX() - Camera.x - 40, this.getY() - Camera.y - 10);
		}
	}

}
