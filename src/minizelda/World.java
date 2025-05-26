package minizelda;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
	
	public static List<Arvore> arvores = new ArrayList<>();
	
	public World() {
		for(int xx = 0; xx < 19; xx++) {
			if(new Random().nextInt(100) < 50)
				arvores.add(new Arvore(xx*32, 0));
		}
		for(int xx = 0; xx < 19; xx++) {
			if(new Random().nextInt(100) < 50)
				arvores.add(new Arvore(xx*32, 480-32));
		}
		for(int yy = 1; yy < 14; yy++) {
			if(new Random().nextInt(100) < 50)
				arvores.add(new Arvore(0, yy*32));
		}
		for(int yy = 0; yy < 15; yy++) {
			if(new Random().nextInt(100) < 50)
				arvores.add(new Arvore(640-32, yy*32));
		}
		
		for(int i = 0; i < 8; i++) {
			arvores.add(new Arvore(new Random().nextInt(32, 576), new Random().nextInt(32, 416)));
		}
		
	}
	
	public static boolean isFree(int x, int y) {
		for(int i = 0; i < arvores.size(); i++) {
			Arvore arvoreAtual = arvores.get(i);
			if(arvoreAtual.intersects(new Rectangle(x, y, 32, 32))) {
				return false;
			}
		}
		
		return true;
	}

	public void render(Graphics g) {
		for(int i = 0; i < arvores.size(); i++) {
			arvores.get(i).render(g);
		}
	}
	
}
