package mayflower;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

//import org.newdawn.slick.Color;
//import org.newdawn.slick.Font;
//import org.newdawn.slick.Image;
//import org.newdawn.slick.SlickException;

public abstract class World
{
	private List<Actor> actors;
	private MayflowerImage background;
	private List<java.lang.Class<? extends Actor>> paintOrder;
	private List<java.lang.Class<? extends Actor>> actOrder;
	private int cellSize;
	private Map<String, TextInfo> texts;
	private Map<String, Font> fonts = new HashMap<String, Font>();
	private Font font;
	
	public World(int cellSize)
	{
		actors = new ArrayList<Actor>();
		setPaintOrder();
		setActOrder();
		this.cellSize = cellSize;
		texts = new HashMap<String, TextInfo>();
		fonts = new HashMap<String, Font>();
		font = loadFont("Comic Sans MS", 32);
	}
	
	public World()
	{
		this(1);
	}
	
	/**
	 * This is called once each frame. Overwrite this method.
	 */
	public abstract void act();
	
	/**
	 * This method can be overwritten to draw to the screen before it is displayed
	 * 
	 * @param g the graphics object
	 */
	public void render(Graphics2D g)
	{
		//do nothing.
		//This method is intended to be overridden
	}
	
	/**
	 * Add the specified object to the world at (x, y)
	 * 
	 * @param actor the Actor to add to the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void addObject(Actor object, int x, int y)
	{
		if(object == null)
			return;
		
		if(object.getWorld() != null)
			object.getWorld().removeObject(object);
			
		//object.setLocation(x*getCellSize(), y*getCellSize());
		object.setLocation(x, y);
		actors.add(object);
		object.addedToWorld(this);
	}
	
	/**
	 * Get the background image currently being displayed 
	 * 
	 * @return a MayflowerImage object
	 */
	public MayflowerImage getBackground()
	{
		return background;
	}
	
	public int getCellSize()
	{
		//return Mayflower.getCellSize();
		return cellSize;
	}
	
	/**
	 * Get the color of the word at (x, y)
	 * 
	 * @return Color returns a mayflower.Color object
	 */
	public Color getColorAt(int x, int y)
	{
		if(background == null)
			return Color.BLACK;
		return background.getColorAt(x, y);
	}
	
	public int getHeight()
	{
		return Mayflower.getHeight();
	}
	
	/**
	 * Get a list of all Actors in this world.
	 * 
	 * @return a list of all Actors in this world
	 */
	public List<Actor> getObjects()
	{
		return actors;
	}
	
	/**
	 * Get a list of all actors of the specified type in the world
	 * <br><br>
	 * Example (assuming a Foo extends Actor class exists)
	 * <pre>
	 * {@code
	 * List<Foo> allFoos = getObjects(Foo.class);
	 * }
	 * </pre>
	 * @param cls the type of objects to return
	 * @return a list of objects in the world of the specified type
	 */
	@SuppressWarnings("unchecked")
	public synchronized <A extends Actor> List<A> getObjects(java.lang.Class<A> cls)
	{
		List<A> ret = new LinkedList<A>();
		
		//Actor[] actorsCopy = actors.toArray(new Actor[] {});
		//Object[] actorsCopy = actors.toArray();
		//for(Object actor : actorsCopy)
		for(Object actor : actors)
			if(cls.isAssignableFrom(((Actor)actor).getClass()))
				ret.add((A)actor);
		return ret;
	}
	
	/**
	 * Get a list of all Actors that contain the point (x, y)
	 * @param x
	 * @param y
	 * @return a list of all Actors that contain the specfied point
	 */
	public synchronized List<Actor> getObjectsAt(int x, int y)
	{
		List<Actor> ret = new LinkedList<Actor>();
		
		//Actor[] actorsCopy = getObjects().toArray(new Actor[] {});
		//for(Actor actor : actorsCopy)
		for(Actor actor: getObjects())
			if(actor.getBounds().contains(x * cellSize, y * cellSize))
				ret.add(actor);
		return ret;
	}
	
	/**
	 * Get a list of all Actors of the specified type that contain the point (x, y)
	 * <br><br>
	 * <pre>
	 * {@code
	 * List<Foo> allFoos = getObjectsAt(42, 64, Foo.class);
	 * }
	 * </pre>
	 * @param x
	 * @param y
	 * @param cls the type of objects to return
	 * @return a list of Objects of the specified type that contain the specified point
	 */
	public synchronized <A extends Actor> List<A> getObjectsAt(int x, int y, java.lang.Class<A> cls)
	{
		//TODO: test if +1 to x-coor breaks cellSize 1 games
		List<A> ret = new LinkedList<A>();
		
		for(A actor : getObjects(cls))
			if(actor.getBounds().contains(x * cellSize, y * cellSize))
				ret.add(actor);
		return ret;
	}
	
	public int getWidth()
	{
		return Mayflower.getWidth();
	}
	
	/**
	 * How many Actors are in the world
	 * @return how many Actors are in the world
	 */
	public int numberOfObjects()
	{
		return actors.size();
	}
	
	/**
	 * Remove the specified actor from the world
	 * @param object the actor to remove from the world
	 */
	public void removeObject(Actor object)
	{
		if(actors.remove(object))
			object.addedToWorld(null);
	}
	
	/**
	 * Remove a List of actors from the world
	 * 
	 * @param objects a List of actors to remove from the world
	 */
	public void removeObjects(Collection<? extends Actor> objects)
	{
		for(Actor a : objects)
			removeObject(a);
	}
	
	/**
	 * Display the specified text at the specified (x, y) coordinate.
	 * <br><br>
	 * The upper left corner of the text will be located at (x, y)
	 * @param text
	 * @param x
	 * @param y
	 */
	public void showText(String text, int x, int y)
	{
		showText(text, 32, x, y);
	}
	
	/**
	 * Display the specified text at the specified location and specified font size.
	 * @param text
	 * @param size
	 * @param x
	 * @param y
	 */
	public void showText(String text, int size, int x, int y)
	{
		showText(text, size, x, y, mayflower.Color.WHITE);
	}
	
	/**
	 * Display the specified text at the sepecified location and specified font size & color.
	 * @param text
	 * @param size
	 * @param x
	 * @param y
	 * @param color
	 */
	public void showText(String text, int size, int x, int y, mayflower.Color color)
	{
		String key = x + "," + y;
		Font font = loadFont(this.font.getName(), size);
		texts.put(key, new TextInfo(text, x, y, font, color));
	}
	
	/**
	 * Display the specified text at the specified location and specified font size & color.
	 * @param text
	 * @param x
	 * @param y
	 * @param color
	 */
	public void showText(String text, int x, int y, mayflower.Color color)
	{
		String key = x + "," + y;
		texts.put(key, new TextInfo(text, x, y, font, color));
	}
	
	private Font loadFont(String name, int size)
	{
		
		String key = name+"_"+size;
		Font font = fonts.get(key);
		if(null == font)
		{
			System.out.println("Loading font: "+ key);
			font = new mayflower.Font(name, size);
			fonts.put(key, font);
		}
		
		return font;
	}
	
	/**
	 * Set the font that is used by showText method. 
	 * You can call the Font.getAvailableFonts() method to find out which fonts are available
	 * <pre>
	 * {@code
	 * String[] fonts = Font.getAvailableFonts();
	 * }
	 * </pre> 
	 * @param name name of font
	 * @param size size of font
	 */
	public void setFont(String name, int size)
	{
		font = loadFont(name, size);
	}
	
	/**
	 * Noop
	 */
	public void repaint()
	{
		//noop
	}
	
	/**
	 * Set the order that the act method will be called on Actors
	 * <br><br>
	 * The following example will setup the world to call the act method on all Foo actors first, then all Bar actors, then all Fizz actors, and finally all Buzz actors.
	 * <pre>
	 * {@code
	 * setActOrder(Foo.class, Bar.class, Fizz.class, Buzz.class);
	 * }
	 * </pre>
	 * @param classes
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setActOrder(java.lang.Class... classes)
	{
		actOrder = new LinkedList<>();
		try
		{
			for(java.lang.Class cls : classes)
				actOrder.add((java.lang.Class<? extends Actor>)cls);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		actOrder.add(Actor.class);
	}
	
	/**
	 * Set the background to the specified image 
	 * @param img the image to display
	 */
	public void setBackground(MayflowerImage img)
	{
		background = img;
	}
	/**
	 * Set the background to the specified image
	 * @param filename the path to an image file
	 */
	public void setBackground(String filename)
	{
//		try 
//		{
			setBackground(new MayflowerImage(filename));
//		} 
//		catch (SlickException e) 
//		{
//			e.printStackTrace();
//		}
	}
	
	/**
	 * Set the order that Actors will be drawn to the screen.
	 * <br><br>
	 * Actors that are drawn earlier will be covered up by actors that are drawn later.
	 * <br><br>
	 * The following example will setup the world to draw all Foo actors first, then all Bar actors, then all Fizz actors, and finally all Buzz actors.
	 * <pre>
	 * {@code
	 * setPaintOrder(Foo.class, Bar.class, Fizz.class, Buzz.class);
	 * }
	 * </pre>
	 * @param classes
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setPaintOrder(java.lang.Class...classes)
	{
		paintOrder = new LinkedList<>();
		try
		{
			for(java.lang.Class cls : classes)
				paintOrder.add((java.lang.Class<? extends Actor>)cls);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		paintOrder.add(Actor.class);
	}
	
	/**
	 * Noop
	 */
	public void started()
	{
		//noop
	}
	
	/**
	 * Noop
	 */
	public void stopped()
	{
		//noop
	}

	public Map<String, TextInfo> getTexts()
	{
		return texts;
	}
	
	protected List<java.lang.Class<? extends Actor>> getPaintOrder()
	{
		return paintOrder;
	}
	
	protected List<java.lang.Class<? extends Actor>> getActOrder()
	{
		return actOrder;
	}
	
	class TextInfo
	{
		private String text;
		private int size, x, y;
		private Color color;
		private Font font;
		public TextInfo(String text, int x, int y, Font font, Color color)
		{
			this.size = size;
			this.x = x;
			this.y = y;
			this.color = color;
			this.text = text;
			this.font = font;
		}
		
		public Font getFont()
		{
			return font;
		}
		
		public String getText()
		{
			return text;
		}
		
		public int getX()
		{
			return x;
		}
		
		public int getY()
		{
			return y;
		}
		
		public int getSize()
		{
			return size;
		}
		
		public Color getColor()
		{
			return color;
		}
	}
}
