package grafics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import entities.Player;
import main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(18,18, 204, 24);
		g.setColor(Color.red);
		g.fillRect(20, 20, (int)((Game.player.life/Game.player.maxLife)*200), 20);
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife, 12, 14);
		g.drawString("AMMO: " + Player.ammo, 12, 60);
	}
	
}
