package mayflower;

import java.awt.GraphicsEnvironment;


public class Font 
{
	private java.awt.Font font;
	private String name;
	private boolean bold, italic;
	private int size;
	
	public static String[] getAvailableFonts()
	{
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		String[] fontNames = new String[fonts.length];
		for (int i = 0; i < fonts.length; i++)
		    fontNames[i] = fonts[i].toString();
		return fontNames;
	}
	
	public Font(boolean bold, boolean italic, int size)
	{
		this("Comic Sans MS", bold, italic, size);
	}
	
	public Font(int size)
	{
		this("Comic Sans MS", false, false, size);
	}
	
	public Font(String name, boolean bold, boolean italic, int size)
	{
		int modifiers = java.awt.Font.PLAIN;
		if(bold)
			modifiers = modifiers | java.awt.Font.BOLD;
		if(italic)
			modifiers = modifiers | java.awt.Font.ITALIC;
		
		font = new java.awt.Font(name, modifiers, size);
		
		this.name = name;
		this.bold = bold;
		this.italic = italic;
		this.size = size;
	}
	
	public Font(String name, int size)
	{
		this(name, false, false, size);
	}
	
	public Font deriveFont(float size)
	{
		return new Font(name, bold, italic, (int)size);
	}
	
	public boolean equals(Object obj)
	{
		if(null == obj)
			return false;
		if(obj instanceof Font)
			return font.equals(((Font)obj).getAwtFont());
		return false;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int hashCode()
	{
		return font.hashCode() + 1;
	}
	
	public boolean isBold()
	{
		return bold;
	}
	
	public boolean isItalic()
	{
		return italic;
	}
	
	public boolean isPlain()
	{
		return !bold && !italic;
	}
	
	public String toString()
	{
		return font.toString();
	}
	
	public java.awt.Font getAwtFont()
	{
		return font;
	}
}
