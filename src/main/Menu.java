package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	
	public String[] options = {"Novo Jogo", "Continuar", "Sair"};
	public int currOption = 0;
	public int maxOption = options.length - 2;
	
	public boolean up = false;
	public boolean down = false;
	public boolean enter = false;
	public boolean pause = false;
	
	public void tick() {
		if(up) {
			currOption --;
			up = false;
			if(currOption < 0) {
				currOption = maxOption;
			}
		}
		if(down) {
			currOption ++;
			down = false;
			if(currOption > maxOption) {
				currOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(options[currOption].equals("Novo Jogo") || options[currOption].equals("Continuar")) {
				Game.gameState = "Normal";
				pause = false;
			}
			if(options[currOption].equals("Sair")) {
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.yellow);
		g.setFont(new Font("Arial", Font.BOLD, 48));
		g.drawString("Cockroach Slayer", Game.WIDTH*Game.SCALE/2-190, Game.HEIGHT*Game.SCALE/2-95);
		g.setFont(new Font("Arial", Font.BOLD, 28));
		if(!pause)
			g.drawString(options[0], Game.WIDTH*Game.SCALE/2-140, Game.HEIGHT*Game.SCALE/2+20);
		if(pause)
			g.drawString(options[1], Game.WIDTH*Game.SCALE/2-140, Game.HEIGHT*Game.SCALE/2+20);
		g.drawString(options[2], Game.WIDTH*Game.SCALE/2-140, Game.HEIGHT*Game.SCALE/2+70);
		
		if(options[currOption].equals("Novo Jogo")) {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-160, Game.HEIGHT*Game.SCALE/2+20);
		}
		if(options[currOption].equals("Sair")) {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-160, Game.HEIGHT*Game.SCALE/2+70);
		}
		
	}
}
