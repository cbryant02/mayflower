package mayflower.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import mayflower.Mayflower;

public class MayflowerKeyboardListener implements KeyListener
{
	private Mayflower mayflower;
	
	public MayflowerKeyboardListener(Mayflower mayflower)
	{
		this.mayflower = mayflower;
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		mayflower.keyPressed(e.getKeyCode(), e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		mayflower.keyReleased(e.getKeyCode(), e.getKeyChar());
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
	}
}
