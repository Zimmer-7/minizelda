package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import entities.Bullet;
import entities.Enemy;
import entities.Entity;
import entities.Player;
import grafics.SpriteSheet;
import grafics.UI;
import world.Camera;
import world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 288; 
	public static final int HEIGHT = 208;
	private int SCALE = 3;
	public static Player player;
	public static World world;
	public static int curLevel = 1;
	private int maxLevel = 2;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Bullet> bullets;
	public static List<Entity> items;
	public static SpriteSheet spriteSheet;
	
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "Normal";
	private boolean showGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	private boolean exit = false;
	
	public Game () {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		ui = new UI();
	
		entities = new ArrayList<>();
		enemies = new ArrayList<>();
		items = new ArrayList<>();
		bullets = new ArrayList<>();
		spriteSheet = new SpriteSheet("/recursos.png");	
		player = new Player(0, 0, 16, 16, spriteSheet.getSprite(32, 0, 16, 16));
		world = new World("/mapa1.png");
		entities.add(player);
		
	}
	
	public void initFrame() {
		frame = new JFrame("Mini Zelda");
		
		frame.add(this);
		
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		if(gameState.equals("Normal")) {
			restartGame = false;
			exit = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			for(int i = 0; i < bullets.size(); i++) {
				Entity e = bullets.get(i);
				e.tick();
			}
			
			if(enemies.isEmpty()) {
				curLevel ++;
				if(curLevel > maxLevel) {
					gameState = "Victory";
					return;
				}
				String newWorld = "mapa"+curLevel+".png";
				World.startLevel(newWorld);
			}
		}
		if(gameState.equals("Game Over") || gameState.equals("Victory")) {
			framesGameOver++;
			if(framesGameOver == 50) {
				framesGameOver = 0;
				if(showGameOver) {
					showGameOver = false;
				} else {
					showGameOver = true;
				}
			}
			if(restartGame) {
				curLevel = 1;
				World.startLevel("Mapa1.png");
				restartGame = false;
			}
			if(exit)
				System.exit(1);
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(g);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			Entity e = bullets.get(i);
			e.render(g);
		}
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		
		ui.render(g);
		if(gameState.equals("Game Over")) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("Arial", Font.BOLD, 48));
			g.setColor(Color.red);
			g.drawString("F", WIDTH*SCALE/2, HEIGHT*SCALE/2-15);
			g.setFont(new Font("Arial", Font.BOLD, 28));
			if(showGameOver) {
				g.drawString("Pressione Enter para reiniciar", WIDTH*SCALE/2-182, HEIGHT*SCALE/2+35);
				g.drawString("Pressione Esc para sair", WIDTH*SCALE/2-149, HEIGHT*SCALE/2+60);
			}
		}
		if(gameState.equals("Victory")) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("Arial", Font.BOLD, 48));
			g.setColor(Color.green);
			g.drawString("Aí sim", WIDTH*SCALE/2, HEIGHT*SCALE/2-15);
			g.setFont(new Font("Arial", Font.BOLD, 28));
			if(showGameOver) {
				g.drawString("Pressione Enter para reiniciar", WIDTH*SCALE/2-182, HEIGHT*SCALE/2+35);
				g.drawString("Pressione Esc para sair", WIDTH*SCALE/2-149, HEIGHT*SCALE/2+60);
			}
		}
		
		bs.show();
		
	}
	
	public static void main (String[] args) {
		Game game = new Game();
		game.start();
	}
	
	@Override
	public void run() {
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		long now;
		requestFocus();
		
		while(isRunning) {
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		stop();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
			e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT ||
			e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP ||
			e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN ||
			e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.shooting = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			exit = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
			e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT ||
			e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP ||
			e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN ||
			e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShooting = true;
		player.mx = e.getX()/3;
		player.my = e.getY()/3;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
