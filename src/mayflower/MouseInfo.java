package mayflower;

/**
 * Information about the current status of the Mouse.
 * <br><br>
 * You should not create an instance of MouseInfo. Instead, use Mayflower.getMouseInfo() to get an reference to the current MouseInfo object.
 * Example:
 * <pre>
 * {@code
 * MouseInfo minfo = Mayflower.getMouseInfo();
 * }
 * </pre>
 */
public class MouseInfo 
{
	private Actor actor;
	private int button;
	private int clickCount;
	private int x, y;
	
	public MouseInfo(Actor actor, int button, int clickCount, int x, int y)
	{
		this.actor = actor;
		this.button = button;
		this.clickCount = clickCount;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Return one of the actors that the mouse is hovering over
	 * 
	 * @return one of the actors that hte mouse is hovering over
	 */
	public Actor getActor()
	{
		return actor;
	}
	
	public int getButton()
	{
		return button;
	}
	
	public int getClickCount()
	{
		return clickCount;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public String toString()
	{
		return "MouseInfo[actor:"+actor+", button:"+button+",clicks:"+clickCount+", x:"+x+", y:"+y+"]";
	}
}
