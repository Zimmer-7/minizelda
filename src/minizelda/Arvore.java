package minizelda;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Arvore extends Rectangle {
	
	public Arvore(int x, int y) {
		super(x, y, 32, 32);
	}
	
	public void render(Graphics g) {
		g.drawImage(SpriteSheet.tree, x, y, 32, 32, null);
	}
	
}
