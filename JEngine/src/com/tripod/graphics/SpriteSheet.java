package com.tripod.graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	public final int WIDTH, HEIGHT;
	public int[] pixels;
	
	public SpriteSheet(BufferedImage image) {
		WIDTH = image.getWidth();
		HEIGHT = image.getHeight();
		pixels = image.getRGB(0, 0, WIDTH, HEIGHT, null, 0, WIDTH);
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = (pixels[i] & 0xff) / 64;
			//System.out.println(pixels[i]);
		}
	}

}
