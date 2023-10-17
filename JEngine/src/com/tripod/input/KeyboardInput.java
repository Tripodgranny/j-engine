package com.tripod.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.tripod.main.Game;

public class KeyboardInput implements KeyListener {
	
	private boolean[] keys = new boolean[127];
	public boolean escape;
	
	public void update() {
		escape = keys[KeyEvent.VK_ESCAPE];
	}

	@Override
	public void keyTyped(KeyEvent e) {	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		//System.out.println("KEY PRESSED");
	

		// keys[e.getKeyCode()] = true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			//Game.setFullScreen();
			Game.stop();
		}
		
	}

}
