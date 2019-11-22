package mayflower;

/**
 *	An Actor that displays text.
 */
public class Label extends Actor
{
	private int point;
	private String oldText;
	private Color color;
	
	public Label(String text)
	{
		this(text, 32);
	}
	
    public Label(String text, int size) 
    {
    	this(text, size, Color.WHITE);
    }
    
    public Label(String text, int size, Color color) 
    {
    	point = size;
    	setImage(new MayflowerImage(text, point, color));
    	oldText = text;
    	this.color = color;
    }
    
    public void act()
    {
    }
    
    public String getText()
    {
    	return oldText;
    }
    
    public void setText(String text)
    {
    	if(!text.equals(oldText))
    		setImage(new MayflowerImage(text, point, color));
    	oldText = text;
    }
    
    public void setColor(Color color)
    {
    	this.color = color;
    	String t = oldText;
    	setText("");
    	setText(t);
    }
    
    
}