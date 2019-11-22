package mayflower;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MayflowerImage 
{
	private BufferedImage img;
	private double rotation;
	private int width, height;
	private int transparency;
	
	private BufferedImage original;
	
	private boolean[][] mask;
	
	private void init()
	{
		width = img.getWidth();
		height = img.getHeight();
		
		BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		copy.getGraphics().drawImage(img, 0, 0, null);
		this.original = copy;
	}
	
	private BufferedImage getOriginal()
	{
		BufferedImage copy = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		copy.getGraphics().drawImage(original, 0, 0, null);
		return copy;
	}
	
	public MayflowerImage(MayflowerImage img)
	{
		BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		copy.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
		this.img = copy;
		init();
	}
	
	public MayflowerImage(int width, int height)
	{
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		init();
	}
	
	public MayflowerImage(String filename)
	{
		try 
		{
			img = ImageIO.read(new File(filename));
			init();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public MayflowerImage(String string, int size, Color foreground)
	{
		this(string, size, foreground, null, null);
	}
	
	public MayflowerImage(String string, int size, Color foreground, Color notused, Color notused2)
	{
		/*
		try 
		{
			if(null == fonts.get(size))
			{
				fonts.put(size, new TrueTypeFont(new java.awt.Font("Comic Sans MS", java.awt.Font.PLAIN, size), true));
			}
			Font font = fonts.get(size);
			
			int w = font.getWidth(string);
			int h = font.getHeight(string);
			
			img = new Image(w, h);
			
			Graphics g = img.getGraphics();
			g.setFont(font);
			g.setColor(foreground.getSlickColor());
			g.drawString(string, 0, 0);
			g.flush();
			
			init();
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		*/
	}
	
	public boolean[][] getMask()
	{
		if(null == img)
		{
			return new boolean[0][0];
		}
		
		if(null == mask)
		{
			mask = new boolean[img.getHeight()][img.getWidth()];
			//TOOD: test this... especially with rotated/scaled images
			for(int r = 0; r < img.getHeight(); r++)
			{
				for(int c = 0; c < img.getWidth(); c++)
				{
					int pixel = img.getRGB(c, r);
					boolean transparent = (pixel >> 24) == 0x00;
					mask[r][c] = !transparent;
				}
			}
		}
		
		return mask;
	}
	
	public BufferedImage getBufferedImage()
	{
		return img;
	}
	
	public Color getColorAt(int x, int y)
	{
		if(null == img) return null;
		
		int clr=  img.getRGB(x,y); 
		int  red   = (clr & 0x00ff0000) >> 16;
		int  green = (clr & 0x0000ff00) >> 8;
		int  blue  =  clr & 0x000000ff;
		
		return new Color(red, green, blue);
	}
	
	public int getHeight()
	{
		if(null == img) return 0;
		return img.getHeight();
	}
	
	public int getTransparency()
	{
		return transparency;
	}
	
	public int getWidth()
	{
		if(null == img) return 0;
		return img.getWidth();
	}
	
	private BufferedImage createTransformed(BufferedImage image, AffineTransform at)
    {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
	
	public void mirrorHorizontally()
	{
		if(null == img) return;
		
		AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-img.getWidth(), 0));
        img = createTransformed(img, at);
        original = createTransformed(original, at);
        mask = null;
	}
	
	public void mirrorVertically()
	{
		if(null == img) return;
		
		AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -img.getHeight()));
        img = createTransformed(img, at);
        original = createTransformed(original, at);
        mask = null;
	}
	
	public void rotate(double degrees)
	{
		if(null == img) return;
		
		rotation += degrees;
		rotation = rotation % 360;
		
		double rads = Math.toRadians(rotation);
		
		AffineTransform at = AffineTransform.getRotateInstance(rads, img.getWidth()/2.0, img.getHeight()/2.0);
        img = createTransformed(getOriginal(), at);
        mask = null;
	}
	
	public void setRotation(double degrees)
	{
		rotate(degrees - rotation);
	}
	
	public double getRotation()
	{
		if(null == img) return 0;
		return rotation;
	}
	
	public void scale(int width, int height)
	{
		if(null == img) return;
		
		double offX = (double)width / getWidth();
		double offY = (double)height / getHeight();
		
		this.width = width;
		this.height = height;
		
		AffineTransform at = AffineTransform.getScaleInstance(offX, offY);
        img = createTransformed(img, at);
        original = createTransformed(original, at);
        mask = null;
	}
	
	public void scale(double scale)
	{
		if(null == img) return;
		scale((int)(width * scale), (int)(height * scale));
	}
	
	public void setColorAt(int x, int y, Color color)
	{
		if(null == img) return;
		if(null == color) return;
		//TODO:make sure this keeps transparency settings
		img.setRGB(x, y, color.getAwtColor().getRGB());
		mask = null;
	}
	
	public void setTransparency(int t)
	{
		if(null == img) return;
		
		transparency = t;
		
		//distinguish between pixels that are always 100% transparent
		//and pixels that should be opaque
		if(t == 100)
			t = 99;
		
		//convert 0..100 --> 0..255
		t = 100 - t;
		int alpha = (int)((t/100.0) * 255);
		alpha = alpha << 24;
		
		for(int w = 0; w < width; w++)
		{
			for(int h = 0; h < height; h++)
			{
				int clr =  img.getRGB(w, h);
				int rgb =  clr & 0x00ffffff;
				int a   = (clr & 0xff000000) >> 24;
			
				//don't change transparency of always-transparent pixels
				if(a != 0x00000000)
				{
					int rgba = rgb | alpha;
					img.setRGB(w, h, rgba);
				}
			}
		}
	}
	
	public String toString()
	{
		return img.toString();
	}
}
