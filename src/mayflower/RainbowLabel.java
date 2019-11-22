package mayflower;

/**
 * A Label that randomly changes the color of the displayed text
 */
public class RainbowLabel extends Label
{
	private int r, b, g;
	private Timer timer;
	private int speed;
	
    public RainbowLabel(String text, int size) 
    {
    	this(text, size, Color.WHITE);
    }
    
    public RainbowLabel(String text, int size, Color color) 
    {
    	super(text, size, color);
    	r = 0;
    	g = 0;
    	b = 0;
    	speed = 10;
    	timer = new Timer(speed);
    }
    
    /**
     * Increase the number of milliseconds between color changes
     * 
     * @param amnt the number of milliseconds to increase the speed by
     */
    public void speedUp(int amnt)
    {
    	speed-=amnt;
    	if(speed < 1)
    		speed = 1;
    	timer = new Timer(speed);
    }
    
    /**
     * Decrease the number of milliseconds between color changes
     * 
     * @param amnt the number of milliseconds to decrease the speed by
     */
    public void slowDown(int amnt)
    {
    	speedUp(-amnt);
    }
    
    public void act()
    {
    	if(!timer.isDone())
    		return;
    	timer.reset();
    	r++;
    	if(r > 255)
    	{
    		r = 0;
    		g++;
    		if(g > 255)
    		{
    			g = 0;
    			b++;
    			
    			if(b > 255)
    			{
    				b = 0;
    			}
    		}
    	}
    	
    	setColor(new Color(r, g, b));
    }
    
}