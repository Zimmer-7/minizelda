package grafics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import entities.Player;
import main.Game;
import world.World;

public class UI {
	
	public BufferedImage miniMapa;
	public int[] miniMapaPixels;

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(18,18, 204, 24);
		g.setColor(Color.red);
		g.fillRect(20, 20, (int)((Game.player.life/Game.player.maxLife)*200), 20);
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife, 12, 14);
		g.drawString("AMMO: " + Game.player.ammo, 12, 60);
		miniMapa = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		miniMapaPixels = ((DataBufferInt)miniMapa.getRaster().getDataBuffer()).getData();
		World.renderMiniMap(miniMapaPixels);
		g.drawImage(miniMapa, 668, 0, World.WIDTH*4, World.HEIGHT*4, null);
	}
	
}
