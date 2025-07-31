package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import entities.Bullet;
import entities.Enemy;
import entities.Entity;
import entities.Player;
import grafics.Light;
import grafics.SpriteSheet;
import grafics.UI;
import world.Camera;
import world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 288; 
	public static final int HEIGHT = 208;
	public static final int SCALE = 3;
	public static Player player;
	public static World world;
	public static int curLevel = 1;
	private int maxLevel = 2;
	
	public int mx;
	public int my;
	
	private BufferedImage image;
	public Menu menu;
	
	public int[] pixels;
	//public Light light;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Bullet> bullets;
	public static List<Entity> items;
	public static SpriteSheet spriteSheet;
	
	public static Random rand;
	
	public UI ui;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("campus.ttf");
	public static Font newFontBig;
	public static Font newFontSmall;
	
	public static String gameState = "Menu";
	private boolean showGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	private boolean exit = false;
	
	private int countPrep = 5;
	private int framesPrep = 0;
	private int maxFramesPrep = 60;
	
	public Game () {
		AdvancedSound.background.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		//this.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		initFrame();
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		ui = new UI();
		
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		//light = new Light();
	
		entities = new ArrayList<>();
		enemies = new ArrayList<>();
		items = new ArrayList<>();
		bullets = new ArrayList<>();
		spriteSheet = new SpriteSheet("/recursos.png");	
		player = new Player(0, 0, 16, 16, spriteSheet.getSprite(32, 0, 16, 16));
		world = new World("/mapa1.png", 1);
		entities.add(player);
		
		try {
			newFontBig = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(80f);
			newFontSmall = newFontBig.deriveFont(35f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		menu = new Menu();
	}
	
	public void initFrame() {
		frame = new JFrame("Mini Zelda");		
		frame.add(this);
		//frame.setUndecorated(true);
		frame.pack();
		
		Image icone = null;
		try {
			icone = ImageIO.read(getClass().getResource("/icon.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image cursor = toolkit.getImage(getClass().getResource("/cursor.png"));
		Cursor c = toolkit.createCustomCursor(cursor, new Point(0, 0), "img");
		
		frame.setCursor(c);
		frame.setIconImage(icone);
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
				//String newWorld = "mapa"+curLevel+".png";
				World.startLevel(curLevel);
			}
		}
		if(gameState.equals("Prep")) {
			if(countPrep > 0) {
				framesPrep++;
				if(framesPrep == maxFramesPrep) {
					countPrep --;
					framesPrep = 0;
				}
			} else {
				gameState = "Normal";
				countPrep = 5;
			}
		}
		if(gameState.equals("Game Over") || gameState.equals("Victory") || gameState.equals("Prep")) {
			framesGameOver++;
			if(framesGameOver == 45) {
				framesGameOver = 0;
				if(showGameOver) {
					showGameOver = false;
				} else {
					showGameOver = true;
				}
			}
			if(restartGame) {
				curLevel = 1;
				World.startLevel(1);
				restartGame = false;
			}
			if(exit)
				System.exit(1);
		}
		if(gameState.equals("Menu")) {
			menu.tick();
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
		
		Collections.sort(entities, Entity.nodeSorter);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			Entity e = bullets.get(i);
			e.render(g);
		}
		
		//light.applyLight();
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		//g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null);
		
		ui.render(g);
		Graphics2D g2 = (Graphics2D) g;
		if(gameState.equals("Game Over")) {
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(newFontBig);
			g.setColor(Color.red);
			g.drawString("F", WIDTH*SCALE/2, HEIGHT*SCALE/2-15);
			g.setFont(newFontSmall);
			if(showGameOver) {
				g.drawString("Pressione Enter para reiniciar", WIDTH*SCALE/2-240, HEIGHT*SCALE/2+35);
				g.drawString("Pressione Esc para sair", WIDTH*SCALE/2-190, HEIGHT*SCALE/2+80);
			}
		}
		if(gameState.equals("Victory")) {
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(newFontBig);
			g.setColor(Color.green);
			g.drawString("Aí sim", WIDTH*SCALE/2-40, HEIGHT*SCALE/2-15);
			g.setFont(newFontSmall);
			if(showGameOver) {
				g.drawString("Pressione Enter para reiniciar", WIDTH*SCALE/2-240, HEIGHT*SCALE/2+35);
				g.drawString("Pressione Esc para sair", WIDTH*SCALE/2-190, HEIGHT*SCALE/2+80);
			}
		}
		if(gameState.equals("Menu")) {
			menu.render(g);
		}
		if(gameState.equals("Prep") && showGameOver) {
			g.setFont(newFontSmall);
			g.setColor(Color.white);
			g.drawString("WASD ou setas para se mover", WIDTH*SCALE/2-240, HEIGHT*SCALE/2-15);
			g.drawString("mouse ou espaco para atirar", WIDTH*SCALE/2-240, HEIGHT*SCALE/2+25);
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
			if(gameState.equals("Menu")) {
				menu.up = true;
			} else {
				player.up = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN ||
			e.getKeyCode() == KeyEvent.VK_S) {
			if(gameState.equals("Menu")) {
				menu.down = true;
			} else {
				player.down = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.shooting = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(gameState.equals("Menu")) {
				menu.enter = true;
			} else {
				restartGame = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if(gameState.equals("Game Over") || gameState.equals("Victory")) {
				exit = true;
			} else if(!gameState.equals("Menu")) {
				gameState = "Menu";
				menu.pause = true;
			}
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
			if(gameState.equals("Menu")) {
				menu.up = false;
			} 
			player.up = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN ||
			e.getKeyCode() == KeyEvent.VK_S) {
			if(gameState.equals("Menu")) {
				menu.down = false;
			}
			player.down = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShooting = true;
		player.mx = e.getX()/SCALE;
		player.my = e.getY()/SCALE;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
	}
	
}
