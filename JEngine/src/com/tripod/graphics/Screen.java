package com.tripod.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Screen {
	private SpriteSheet sheet;
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private static final int MAP_WIDTH = 64;
	private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	
	private int[] tiles = new int[MAP_WIDTH * MAP_WIDTH];
	private int[] colors = new int[MAP_WIDTH * MAP_WIDTH];
	private int[] databits = new int[MAP_WIDTH * MAP_WIDTH];
	public int xScroll;
	public int yScroll;
	
	public static final int BIT_MIRROR_X = 0x01;
	public static final int BIT_MIRROR_Y = 0x02;
	
	public final int WIDTH, HEIGHT;
	public int[] pixels;
	
	public Screen (int width, int height, SpriteSheet sheet) {
		this.sheet = sheet;
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.pixels = new int[width * height];
		Random random = new Random();
	
		for (int i = 0; i < MAP_WIDTH * MAP_WIDTH; i++) {
			colors[i] = (colors[i] << 8) + random.nextInt(6*6*6);
			colors[i] = (colors[i] << 8) + random.nextInt(6*6*6);
			colors[i] = (colors[i] << 8) + random.nextInt(6*6*6);
			colors[i] = (colors[i] << 8) + random.nextInt(6*6*6);
			
			//databits[i] = 1;
			if (i % 2 == 0) databits[i] += 1;
			if (i / MAP_WIDTH % 2 == 0) databits[i] += 2;
		}
		
	}
	
	public void render() {
		for (int yt = yScroll >> 3; yt <= (yScroll + HEIGHT) >> 3; yt++) {
			int yp = yt * 8 - yScroll;
			for (int xt = xScroll >> 3; xt <= (xScroll + WIDTH) >> 3; xt++) {
				int xp = xt * 8 - xScroll;
				
				int ti = (xt & (MAP_WIDTH_MASK)) + (yt & (MAP_WIDTH_MASK)) * MAP_WIDTH;		
				render(xp, yp, 0, colors[ti], databits[ti]);
				
			}
		}
			
	}

	private void render(int xp, int yp, int tile, int colors, int bits) {
		boolean mirrorX = (bits & BIT_MIRROR_X) > 0;
		boolean mirrorY = (bits & BIT_MIRROR_Y) > 0;
		
		for (int y = 0; y < 8; y++) {
			int ys = y;
			if (mirrorY) ys = 7 - y;
			if (y + yp < 0 || y + yp >= HEIGHT) continue;
			for (int x = 0; x < 8; x++) {
				if (x + xp < 0 || x + xp >= WIDTH) continue;

				int xs = x;
				if (mirrorX) xs = 7 - x;
				int col = (colors >> (sheet.pixels[xs + ys * sheet.WIDTH] * 8)) & 255;
				if (col < 255) pixels[(x + xp) + (y + yp) * WIDTH] = col;
				
			}
		}
		
	}

}
