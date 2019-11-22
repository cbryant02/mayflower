package mayflower;

public class Color
{
	public static final Color BLACK 		= new Color(java.awt.Color.black);
	public static final Color BLUE	 		= new Color(java.awt.Color.blue);
	public static final Color CYAN	 		= new Color(java.awt.Color.cyan);
	public static final Color DARK_GRAY		= new Color(java.awt.Color.darkGray);
	public static final Color GRAY	 		= new Color(java.awt.Color.gray);
	public static final Color GREEN 		= new Color(java.awt.Color.green);
	public static final Color LIGHT_GRAY 	= new Color(java.awt.Color.lightGray);
	public static final Color MEGENTA	 	= new Color(java.awt.Color.magenta);
	public static final Color ORANGE	 	= new Color(java.awt.Color.orange);
	public static final Color PINK		 	= new Color(java.awt.Color.pink);
	public static final Color RED		 	= new Color(java.awt.Color.red);
	public static final Color WHITE		 	= new Color(java.awt.Color.white);
	public static final Color YELLOW	 	= new Color(java.awt.Color.yellow);
	
	private java.awt.Color color;
	
	public Color(int r, int g, int b)
	{
		color = new java.awt.Color(r, g, b);
	}
	
	public Color(int r, int g, int b, int a)
	{
		color = new java.awt.Color(r, g, b, a);
	}
	
	public Color(java.awt.Color color)
	{
		this.color = color;
	}
	
	public Color brighter()
	{
		return new Color(color.brighter());
	}
	
	public Color darker()
	{
		return new Color(color.darker());
	}
	
	public boolean equals(Object obj)
	{
		if(null == obj)
			return false;
		if(obj instanceof Color)
			return color.equals(((Color)obj).getAwtColor());
		return false;
	}
	
	public int getAlpha()
	{
		return color.getAlpha();
	}
	
	public int getBlue()
	{
		return color.getBlue();
	}
	
	public int getGreen()
	{
	return color.getGreen();
	}
	
	public int getRed()
	{
		return color.getRed();
	}
	
	public int hashCode()
	{
		return color.hashCode() + 1;
	}
	
	public String toString()
	{
		return color.toString();
	}
	
	public java.awt.Color getAwtColor()
	{
		return color;
	}
}
