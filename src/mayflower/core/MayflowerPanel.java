package mayflower.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mayflower.Mayflower;


public class MayflowerPanel extends JPanel
{
	private Mayflower mayflower;
	private static final long serialVersionUID = 3644893585199489381L;

	public MayflowerPanel(Mayflower mayflower)
	{
		this.mayflower = mayflower;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		BufferedImage buffer = mayflower.getBuffer();
		if(null != buffer)
		{
			g.drawImage(buffer, 0, 0, null);
		}
	}
	
}
