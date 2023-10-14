package com.tripod.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	
	private Random random = new Random();
	
	//Game Attributes
	private final String NAME;
	
	//Panel Attributes
	private final int WIDTH;
	private final int HEIGHT;
	private final int SCALE;
	private final Dimension SIZE;
	
	//Graphics
	private JFrame frame;
	private BufferedImage image;
	private int[] pixels;
	
	//Thread
	private Thread thread;
	private volatile boolean running = false;
	private double UPS = 1f;
	private double FPS = 60f;
	private boolean RENDER_TIME = true;
	
	//Game Constructors
	public Game(String name, int width, int height, int scale) {
		this.NAME = name;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.SCALE = scale;
		
		this.SIZE = new Dimension(this.WIDTH, this.HEIGHT);
		image = new BufferedImage(this.WIDTH, this.HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		frame = new JFrame();
		
	}
	
	public Game(String name, int width, int scale) {
		this.NAME = name;
		this.WIDTH = width;
		this.HEIGHT = width / 16 * 9;
		this.SCALE = scale;
		
		this.SIZE = new Dimension(this.WIDTH, this.HEIGHT);
		image = new BufferedImage(this.WIDTH, this.HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		frame = new JFrame();
		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////
//											Game class Methods                                        //
////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	//Thread Methods
	public void start() {
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		frame.setSize(SIZE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		this.thread = new Thread(this);
		thread.start();
		running = true;
		
	}
	
	public void stop() {
		
		running = false;
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		long initialTime = System.nanoTime();
		final double timeU = 1000000000 / UPS;
		final double timeF = 1000000000 / FPS;
		double deltaU = 0, deltaF = 0;
		int frames = 0, ticks = 0;
		long timer = System.currentTimeMillis();
		
		//Game Loop
		while (running) {

	        long currentTime = System.nanoTime();
	        deltaU += (currentTime - initialTime) / timeU;
	        deltaF += (currentTime - initialTime) / timeF;
	        initialTime = currentTime;

	        if (deltaU >= 1) {
	            //getInput();
	            update();
	            ticks++;
	            deltaU--;
	        }

	        if (deltaF >= 1) {
	            render();
	            frames++;
	            deltaF--;
	        }

	        if (System.currentTimeMillis() - timer > 1000) {
	            if (RENDER_TIME) {
	                System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
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
		System.out.println("UPDATE");
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////
//													RENDER                                               //
///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void render() {
		System.out.println("RENDER");
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = bs.getDrawGraphics();
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				pixels[x + y * WIDTH] = random.nextInt();
			}
		}
		
		graphics.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		graphics.dispose();
		bs.show();
	
	}

}
