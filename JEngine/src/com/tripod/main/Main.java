package com.tripod.main;


public class Main {

	public static void main(String[] args) {
		
		Log.code(Log.VERSION);
		
		Game game = new Game("Dungeons", 320, 3);
		game.start();
		
	}

}
