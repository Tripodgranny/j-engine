package com.tripod.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.tripod.graphics.Screen;
import com.tripod.graphics.SpriteSheet;
import com.tripod.input.KeyboardInput;

public class Game extends Canvas implements Runnable {

	private int[] colors = new int[256];

	private Random random = new Random();

	// Game Attributes
	private final String NAME;

	// Panel Attributes
	private final int WIDTH;
	private final int HEIGHT;
	private final int SCALE;
	private final Dimension SIZE;

	// Graphics
	private static JFrame frame;
	private BufferedImage image;
	private int[] pixels;
	private static boolean fullScreen = false;
	private static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	private Screen screen;

	// Input
	private KeyboardInput keyboard = new KeyboardInput();

	// Thread
	private static Thread thread;
	private volatile static boolean running = false;
	private double UPS = 60f;
	private double FPS = 60f;
	private boolean RENDER_TIME = true;

	// Game Constructors
	public Game(String name, int width, int height, int scale) {
		this.NAME = name;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.SCALE = scale;

		this.SIZE = new Dimension(this.WIDTH * this.SCALE, this.HEIGHT * this.SCALE);
		image = new BufferedImage(this.WIDTH, this.HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		frame = new JFrame();

	}

	public Game(String name, int height, int scale) {
		this.NAME = name;
		this.HEIGHT = height;
		this.WIDTH = this.HEIGHT * 16 / 9;
		this.SCALE = scale;

		this.SIZE = new Dimension(this.WIDTH * this.SCALE, this.HEIGHT * this.SCALE);
		image = new BufferedImage(this.WIDTH, this.HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		frame = new JFrame();

	}

////////////////////////////////////////////////////////////////////////////////////////////////////////
//											Game class Methods                                        //
////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void init() {
		
		try {
			screen = new Screen(WIDTH, HEIGHT,
					new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int pp = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					colors[pp++] = (r * 255 / 5) << 16 | (g * 255 / 5) << 8 | (b * 255 / 5);
				}
			}
		}
	}
	
	// Thread Methods
	public void start() {
		frame.add(this);
		frame.setTitle(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		frame.setMinimumSize(SIZE);
		frame.setPreferredSize(SIZE);
		frame.setMaximumSize(SIZE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(fullScreen);
		frame.pack();

		if (fullScreen) {
			device.setFullScreenWindow(frame);
		}

		frame.setVisible(true);

		this.addKeyListener(keyboard);
		this.requestFocusInWindow();

		thread = new Thread(this);
		thread.start();
		running = true;

	}

	public static void stop() {

		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(running);
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void run() {

		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000 / 60.0;
		int frames = 0, ticks = 0;
		long timer = System.currentTimeMillis();
		
		init();

		// Game Loop
		while (running) {

			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;

			while (unprocessed >= 1) {
				ticks++;
				update();
				unprocessed -= 1;
			}

			frames++;
			render();

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (System.currentTimeMillis() - timer > 1000) {
				if (RENDER_TIME) {

					String d = String.format(" | UPS: %s, FPS: %s", ticks, frames);
					frame.setTitle(NAME + d);
				}
				frames = 0;
				ticks = 0;
				timer += 1000;
			}
		}

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//													UPDATE                                               //
///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void update() {

		keyboard.update();
		screen.xScroll++;
		screen.yScroll++;
		// Log.logFromClass(getClass(), String.valueOf(keyboard.escape));

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//													RENDER                                               //
///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void render() {
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = bs.getDrawGraphics();
		screen.render();
		for (int y = 0; y < screen.HEIGHT; y++) {
			for (int x = 0; x < screen.WIDTH; x++) {
				pixels[x + y * WIDTH] = colors[screen.pixels[x + y * screen.WIDTH]];
			}
		}
		graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		graphics.dispose();
		bs.show();
	
	}

	public static void setFullScreen() {
		if (!fullScreen) {
			fullScreen = true;
			device.setFullScreenWindow(frame);
		} else {
			fullScreen = false;
			device.setFullScreenWindow(null);
		}

	}

}
