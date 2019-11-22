package mayflower.core;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import mayflower.Mayflower;

public class MayflowerMouseListener implements MouseListener, MouseMotionListener
{
	private int x, y;
	private Mayflower mayflower;
	
	//Compensate for the window chrome
	private int offX = 3;
	private int offY = 24;
	
	public MayflowerMouseListener(Mayflower mayflower)
	{
		this.mayflower = mayflower;
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		mayflower.mouseDragged(x, y, e.getX()-offX, e.getY()-offY);
		x = e.getX() - offX;
		y = e.getY() - offY;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mayflower.mouseMoved(x, y, e.getX()-offX, e.getY()-offY);
		x = e.getX() - offX;
		y = e.getY() - offY;
		//System.out.println(x +","+y);
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		mayflower.mouseClicked(e.getButton(), e.getX()-offX, e.getY()-offY, e.getClickCount());
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{	
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		mayflower.mousePressed(e.getButton(), e.getX()-offX, e.getY()-offY);
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		mayflower.mouseReleased(e.getButton(), e.getX()-offX, e.getY()-offY);
	}
	

}
