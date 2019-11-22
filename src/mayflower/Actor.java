package mayflower;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

import mayflower.util.FastTrig;

/**
 * An Actor is an object that exists in a Mayflower World
 */
public abstract class Actor
{
	private MayflowerImage img;
	private Shape bounds;
	private double x, y;
	private double rotation;
	private World world;
	private AffineTransform at;
	
	/**
	 * Construct an Actor
	 */
	public Actor()
	{
		bounds = new Rectangle(0, 0, 1, 1);
		at = new AffineTransform();
	}
	
	/**
	 * The act method is called by the Mayflower framework to give actors a chance to perform some action.
	 * At each action step in the environment, each object's act method is invoked, in unspecified order.
	 * <p>
	 * The default implementation does nothing. This method should be overridden in subclasses to implement an actor's action.
	 */
	public abstract void act();

	/**
	 * Returns the image used to represent this actor. This image can be modified to change the actor's appearance.
	 * @return	The object's image
	 */
	public MayflowerImage getImage()
	{
		return img;
	}
	
	/**
	 * Return the current rotation of this actor. Rotation is expressed as a degree value, range (0..359). Zero degrees is towards the east (right-hand side of the world), and the angle increases clockwise.
	 * 
	 * @return	The rotation in degrees.
	 */
	public int getRotation()
	{
		if(null == img)
			return 0;
		return (int)rotation;
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public int getX()
	{
		return (int)x;
	}
	
	public int getY()
	{
		return (int)y;
	}
	
	public int getCenterX()
	{
		int cellSize = null == getWorld() ? 1 : getWorld().getCellSize();
		
		int x = getX() * cellSize;
		if(img != null)
		{
			x += img.getWidth()/2;
		}
		return x;
	}
	
	public int getCenterY()
	{
		int cellSize = null == getWorld() ? 1 : getWorld().getCellSize();
		
		
		int y = getY() * cellSize;
		if(img != null)
		{
			y += img.getHeight()/2;
		}
		return y;
	}
	
	public Shape getBounds()
	{
		return at.createTransformedShape(bounds);
	}
	
	public boolean isAtEdge()
	{ 
		Shape walls = Mayflower.getBounds();
		return !Mayflower.fullyContains(walls, getBounds());
	}
	
	/**
	 * Move the specified distance in the current direction
	 * 
	 * @param distance how far to move (in pixels)
	 */
	public void move(int distance)
	{
		move((double)distance);
	}
	
	/**
	 * Move the specified distance in the current direction
	 * <br><br>
	 * The actor will not move frantional pixels, but it remembers them for later.
	 * <br><br>
	 * 1.5 will move 1 pixel
	 * <br>
	 * another 1.5 will move an addition 2 pixels (for a total of 3 pixels)
	 * 
	 * @param distance how far to move (in pixels)
	 */
	public void move(double distance)
	{
		double rotation = Math.toRadians(getRotation());
		
		double dx = distance * FastTrig.cos(rotation);
		double dy = distance * FastTrig.sin(rotation);
		
		x += dx;
		y += dy;
		
		//at.translate(dx, dy);
		at.translate(distance, 0);
	}
	
	public void setImage(MayflowerImage img)
	{
		if(img == null)
		{
			System.out.println("Cannot setImage(null)");
			return;
		}
		
		//ignore if not actually changing image
		if(null != this.img && this.img.equals(img))
			return;
		
		this.img = img;
		
		int w = img.getWidth();
		int h = img.getHeight();
		
		bounds = new Rectangle(0, 0, w, h);
	}
	
	public void setImage(String filename)
	{
		setImage(new MayflowerImage(filename));
	}
	
	public void setLocation(double x, double y)
	{
		this.x = x;
		this.y = y;
		
		at = new AffineTransform();

		at.translate(this.x, this.y);
		
		double hw = img.getWidth()/2;
		double hh = img.getHeight()/2; 
		at.rotate(Math.toRadians(rotation), hw, hh);
	}
	
	public void setRotation(int rotation)
	{
		if(null != img)
		{
			turn(rotation - getRotation());
		}
	}
	
	public void turn(int amount)
	{
		if(null != img)
		{
			double hw = img.getWidth()/2;
			double hh = img.getHeight()/2; 
			
			//at = new AffineTransform();
			//at.translate(this.x, this.y);
			at.rotate(Math.toRadians(amount), hw, hh);
			
			rotation += amount;
			rotation = rotation % 360;
		}
	}
	
	public void turnTowards(int x, int y)
	{
		int dx = x - getX();
		int dy = y - getY();
		
		double rad = Math.atan2(dy, dx);
		double deg = Math.toDegrees(rad);

		setRotation((int)deg);
	}
	
	public void turnTowards(Actor actor)
	{
		if(actor == null)
			return;
		MayflowerImage img = actor.getImage();
		if(img == null)
			turnTowards(actor.getX(), actor.getY());
		else 
			turnTowards(actor.getX() + img.getWidth()/2, actor.getY() + img.getHeight()/2);
	}
	
	protected void addedToWorld(World world)
	{
		this.world = world;
	}
	
	protected <A extends Actor> List<A> getIntersectingObjects(java.lang.Class<A> cls)
	{
		List<A> ret = new LinkedList<A>();
		
		World w = getWorld();
		if(null == w)
			return ret;
		
		for(A actor : w.getObjects(cls))
			if(this.intersects(actor))
				ret.add(actor);
		return ret;
	}
	
	protected <A extends Actor> List<A> getNeighbors(int distance, boolean diagonal, java.lang.Class<A> cls)
	{
		//store A objects that are located at neighboring points
		List<A> ret = new LinkedList<A>();
		
		World w = getWorld();
		if(null == w)
			return ret;
		
		//find all points within range
		List<Point> nearbyPoints = new LinkedList<Point>();
		
		//store all the different directions you can look
		List<Point> offsets = new LinkedList<Point>();
		
		offsets.add(new Point(1, 0));
		offsets.add(new Point(-1, 0));
		offsets.add(new Point(0, 1));
		offsets.add(new Point(0, -1));
		if(diagonal)
		{
			offsets.add(new Point(1, 1));
			offsets.add(new Point(-1, -1));
			offsets.add(new Point(-1, 1));
			offsets.add(new Point(1, -1));
		}
		
		//keep track of all the points added from the last unit of distance
		List<Point> lastRing = new LinkedList<Point>();
		
		//start looking from your current point
		lastRing.add(new Point(getCenterX(), getCenterY()));
		
		//run this algorithm once per unit of distance
		for(int i=0; i < distance; i++)
		{
			//remember points that were added this iteration
			List<Point> newPoints = new LinkedList<Point>();

			//for each point that is looking for neighbors
			for(Point point : lastRing)
			{
				//check each offset for a new neighbor
				for(Point offset : offsets)
				{
					//combine the point being checked with the offfset (compensating for the cell size)
					int x = (int) (point.getX() + offset.getX() * w.getCellSize());
					int y = (int) (point.getY() + offset.getY() * w.getCellSize());
					
					Point neighborPoint = new Point(x, y);

					//check if this point has already been traversed
					if(!nearbyPoints.contains(neighborPoint))
					{
						nearbyPoints.add(neighborPoint);
						newPoints.add(neighborPoint);
						
						//check for an actor (of the correct class) at this point
						List<A> neighbors = w.getObjectsAt((int)(neighborPoint.getX()/w.getCellSize()), (int)(neighborPoint.getY()/w.getCellSize()), cls);
						ret.addAll(neighbors);
					}
				}
			}
			
			//reset lastRing to reference the new (ring) of points
			lastRing = newPoints;
		}
		
		return ret;
	}
	
	protected <A extends Actor> List<A> getObjectsAtOffset(int dx, int dy, java.lang.Class<A> cls)
	{
		List<A> ret = new LinkedList<A>();
		
		World w = getWorld();
		if(null == w)
			return ret;
		
		int cellSize = w.getCellSize();
		
		int x = dx * cellSize + getCenterX();
		int y = dy * cellSize + getCenterY();
		
		for(A actor : w.getObjects(cls))
			if(actor != this && actor.getBounds().contains(x, y))
				ret.add(actor);
		return ret;
	}
	
	protected <A extends Actor> List<A> getObjectsInRange(int radius, java.lang.Class<A> cls)
	{
		int x = getCenterX();
		int y = getCenterY();
		
		List<A> ret = new LinkedList<A>();
		
		World w = getWorld();
		if(null == w)
			return ret;
		
		int diameter = radius * 2;
		Shape range = new Ellipse2D.Double(x-radius, y-radius, diameter, diameter);
		
		for(A actor : w.getObjects(cls))
		{
			Shape bounds = actor.getBounds();
			if(actor != this && Mayflower.partiallyContains(range, bounds))
				ret.add(actor);
		}
		return ret;
	}
	
	protected <A extends Actor> A getOneIntersectingObject(java.lang.Class<A> cls)
	{
		List<A> ret = getIntersectingObjects(cls);
		if(ret.size() == 0)
			return null;
		return ret.get(0);
	}
	
	protected <A extends Actor> A getOneObjectAtOffset(int dx, int dy, java.lang.Class<A> cls)
	{	
		List<A> ret = getObjectsAtOffset(dx, dy, cls);
		if(ret.size() == 0)
			return null;
		return ret.get(0);
	}
	
	protected boolean intersects(Actor other)
	{
		if(other == null || other == this)
			return false;
		
		Shape oBounds = other.getBounds();
		return Mayflower.partiallyContains(getBounds(), oBounds);
	}
	
	protected boolean isTouching(java.lang.Class<? extends Actor> cls)
	{
		return getIntersectingObjects(cls).size() > 0;
	}
	
	protected <A extends Actor> void removeTouching(java.lang.Class<A> cls)
	{
		world.removeObjects(getIntersectingObjects(cls));
	}
	
	/**
	 * Scale the image and bounds of this Actor
	 * 
	 * @param width the new width
	 * @param height the new height
	 */
	public void scale(int width, int height)
	{
		if(null == img) return;
		
		float dw = (float)width/img.getWidth();
		float dh = (float)height/img.getHeight();
	
		//scale and transform the bounding box
		at.scale(dw,  dh);
		
		float dx = getCenterX() - dw/2;
		float dy = getCenterY() - dh/2;
		at.translate(dx,  dy);
	}
	
	/**
	 * Scale the image and the bounds of this Actor
	 * <br><br>
	 * 1  = no change<br>
	 * 2  = double the size<br>
	 * .5 = half the size
	 * 
	 * @param scale the amount to scale the Actor by.
	 */
	public void scale(double scale)
	{
		if(null == img) return;
		scale((int)(img.getWidth() * scale), (int)(img.getHeight() * scale));
	}
	
	public void draw(Graphics2D g)
	{
		if(null == img || null == img.getBufferedImage())
			return;
		g.drawImage(img.getBufferedImage(), at, null);
	}
	
	public boolean contains(int x, int y)
	{
		return this.getBounds().contains(x, y);
	}
	
}
