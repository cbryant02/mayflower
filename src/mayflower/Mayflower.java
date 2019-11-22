package mayflower;

import mayflower.core.*;

import jaco.mp3.player.MP3Player;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;

//TODO: http://stackoverflow.com/questions/1611357/how-to-make-a-jar-file-that-includes-dll-files

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import kuusisto.tinysound.*;

public abstract class Mayflower
{
	private static final String version = "3.0";
	
	private World world;
	private Set<Integer> pressedKeys;
	private Set<Integer> wasPressedKeys;
	private int lastKeyPressed;
	private int width, height;
	private boolean showBounds;
	private Shape bounds;
	private Map<String, Sound> sounds;
	private Map<String, MP3Player> musics;
	private boolean fullscreen;

	private int mouseX, mouseY;
	private int mouseButton = -1;
	private boolean mouseMoved, mousePressed, lastMousePressed, nowMousePressed;
	private Map<String, MouseClick> clicks;
	private Map<String, MouseClick> pastClicks;

	private boolean paused;
	
	private JFrame frame;
	private MayflowerPanel panel;
	private MayflowerKeyboardListener keyboardListener;
	private MayflowerMouseListener mouseListener;
	private MayflowerThread thread;
	
	private BufferedImage buffer;
	private BufferedImage textLayer;
	
	private Set<Object> oldHoveredObjects;
	private Set<Object> hoveredObjects;
	
	private int mouseOffsetX, mouseOffsetY;
	
	private float fps;
	
	private long lastTick = 0;
	private long tickWait = (long)(1.0/61 * 1000000000);//60 frames per second(ish)
	
	private static Mayflower instance;
	
	public Mayflower(String title, int width, int height)
	{
		this(title, width, height, true);
	}
	
	/**
	 * 
	 * @param title
	 * @param width
	 * @param height
	 * @param syncfps should the framerate be locked to 60 fps
	 */
	public Mayflower(String title, int width, int height, boolean syncfps)
	{		
		//super(title);
				
		this.width = width;
		this.height = height;
		this.paused = false;
		pressedKeys = new HashSet<Integer>();
		wasPressedKeys = new HashSet<Integer>();
		sounds = new HashMap<String, Sound>();
		musics = new HashMap<String, MP3Player>();
		clicks = new HashMap<String, MouseClick>();
		pastClicks = new HashMap<String, MouseClick>();
		
		hoveredObjects = new HashSet<Object>();
		oldHoveredObjects = new HashSet<Object>();

		bounds = new Rectangle(-1, -1, width+3, height+3);
		
		//initialize static instance
		if(null == instance)
			instance = this;

		initGUI(title);
		
		//Start "the loop"
		thread = new MayflowerThread(this);
		thread.start();
	}
	/**
	 * This method initializes the GUI. It is intended to be overwritten in order to change the type of GUI that is opened.
	 */
	public void initGUI(String title)
	{
		TinySound.init();
		
	    frame = new JFrame(title);
	    
	    panel = new MayflowerPanel(this);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		keyboardListener = new MayflowerKeyboardListener(this);
		frame.addKeyListener(keyboardListener);
		
		mouseListener = new MayflowerMouseListener(this);
		frame.addMouseListener(mouseListener);
		frame.addMouseMotionListener(mouseListener);
		/*
		setStage(stage);
		setUpdateSpeed(1000/60);
		 */		
		init();
		
		/*
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice myDevice = ge.getDefaultScreenDevice();
	    
		try {
		    //myDevice.setFullScreenWindow(myWindow);
		    if(myDevice.isFullScreenSupported())
		    {
		    	System.out.println("Yes");
		    }
		    else
		    	System.out.println("No");
		} finally {
		    myDevice.setFullScreenWindow(null);
		}
		*/
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	/**
	 * This method is called right before the Game Window is opened.
	 */
	public abstract void init();

	/**
	 * This method calls the update and render methods as fast as the tickWait allows.
	 */
	public void tick()
	{
		long now = getTime();
		long diff = now - lastTick;
		if(diff > 0)
		{
			//fps = 1000 / diff;
			fps = 1000000000 / diff;
		}
		if(0==lastTick || lastTick + tickWait < now)
		{
			lastTick = now;
			update();
			render();
		}
	}
	
	//public void render(GameContainer arg0, Graphics arg1) throws SlickException
	public void render()
	{
		if(null == world)
			return;
		//if(null == textLayer)
			textLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	
		BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buffer.createGraphics();
		
		//draw world background
		MayflowerImage background = world.getBackground();
		if(null != background)
			g.drawImage(background.getBufferedImage(), 0, 0, null);
		
		Set<Actor> painted = new HashSet<Actor>();
		for(java.lang.Class<? extends Actor> drawClass : world.getPaintOrder())
		{
			for(Actor actor : world.getObjects(drawClass))
			{
				//don't paint any actors more than once
				if(painted.contains(actor))
					continue;
				painted.add(actor);

				actor.draw(g);
				if(showBounds)
				{
					//draw bounds to screen
					Shape s = actor.getBounds();
					g.setColor(java.awt.Color.BLACK);
					g.draw(s);
				}
			}
		}
		
		//allow the world to render custom graphics
		world.render(g);
		
		//redraw all text to the screen
		Graphics2D g2d = (Graphics2D) textLayer.getGraphics();
		//g2d.clearRect(0, 0, width, height); //TODO: clear g2d to an empty transparent image
		Map<String, World.TextInfo> texts = world.getTexts();
		for(String key : texts.keySet())
		{
			World.TextInfo info = texts.get(key);
			g2d.setColor(info.getColor().getAwtColor());
			g2d.setFont(info.getFont().getAwtFont());
			g2d.drawString(info.getText(), info.getX(), info.getY());
		}
		
		//show fps
		//g2d.setColor(java.awt.Color.black);
		//g2d.drawString("fps:"+fps, 0, 20);
		
		g.drawImage(textLayer, 0, 0, null);
		
		//store the buffer & repaint panel
		this.buffer = buffer;
		panel.repaint();
	}
	
	public BufferedImage getBuffer()
	{
		return buffer;
	}
	
	//public void update(GameContainer arg0, int arg1) throws SlickException 
	public void update()
	{
		if(null == world)
			return;
		
		if(fullscreen && this._isKeyDown(Keyboard.KEY_ESCAPE))
			this._setFullScreen(false);
		
		world.act();
		if(!paused)
		{
			Set<Actor> acted = new HashSet<Actor>();
			for(java.lang.Class<? extends Actor> actClass : world.getActOrder())
			{
				for(Actor actor: world.getObjects(actClass))
				{
					//check if actor was removed from the world
					if(actor.getWorld() != null)
					{
						//don't paint any actors more than once
						if(acted.contains(actor))
							continue;
						acted.add(actor);
						
						actor.act();
					}
				}
			}
		}
		
		//remember which keys were pressed during this frame
		wasPressedKeys.clear();
		wasPressedKeys.addAll(pressedKeys);
		
		pastClicks.clear();
		for(String key : clicks.keySet())
		{
			pastClicks.put(key, clicks.get(key));
		}
		
		//remember which objects where hovered over by the mouse
		oldHoveredObjects.clear();
		oldHoveredObjects.addAll(hoveredObjects);
		
		//detect which objects ARE hovered over by the mouse
		hoveredObjects.clear();
		MouseInfo mi = getMouseInfo();
		List<Actor> hovered = world.getObjectsAt(mi.getX(), mi.getY());
		hoveredObjects.addAll(hovered);

		
		clicks.clear();
		mouseMoved = false;
		lastMousePressed = mousePressed;
		mousePressed = false;
	}
	
	public void keyPressed(int key, char c)
	{
		pressedKeys.add(key);
		lastKeyPressed = key;
	}
	
	public void keyReleased(int key, char c)
	{
		pressedKeys.remove(key);
	}
	
	public void mousePressed(int button, int x, int y)
	{
		mousePressed = true;
		mouseButton = button;
		nowMousePressed = true;
		mouseX = x;
		mouseY = y;
	}
	
	public void mouseReleased(int button, int x, int y)
	{
		mousePressed = false;
		nowMousePressed = false;
		mouseButton = -1;
	}
	
	public void mouseDragged(int oldX, int oldy, int newx, int newy)
	{
		mouseX = newx;
		mouseY = newy;
	}
	
	public void mouseClicked(int button, int x, int y, int clickCount)
	{
		String key = x + " " + y + " " + button;
		
		MouseClick info = clicks.get(key);
		if(null == info)
			clicks.put(key, new MouseClick(x, y, button, clickCount));
		else
			info.addClicks(clickCount);
			
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	{
		mouseX = newx;
		mouseY = newy;
		
		//System.out.println(newx+", " + newy);
		
		mouseMoved = true;
	}
	
	public Shape _getBounds()
	{
		return bounds;
	}
	
	public void _showBounds(boolean showBounds)
	{
		this.showBounds = showBounds;
	}
	
	public void _showFPS(boolean showFPS)
	{
		//appgc.setShowFPS(showFPS);
	}
	
	public int _getWidth()
	{
		return width;
	}
	
	public int _getHeight()
	{
		return height;
	}
	
	/**
	 * Stop execution of the engine for the specified time.
	 * @param time how long (in milliseconds) to stop execution.
	 */
	public void _delay(int time)
	{
		Timer t = new Timer(time);
		while(!t.isDone())
			Thread.yield();
	}
	
	/**
	 * Get the last key that was pressed
	 * @return the last key that was pressed
	 */
	public int _getKey()
	{
		return lastKeyPressed;
	}
	
	public MouseInfo _getMouseInfo()
	{
		Actor act = null;
		if(null != world)
		{
			List<Actor> acts = world.getObjectsAt(mouseX + mouseOffsetX, mouseY + mouseOffsetY);
			if(acts.size() > 0)
				act = acts.get(0);
		}
		return new MouseInfo(act, mouseButton, mouseButton>=0 ? 1 : 0, mouseX + mouseOffsetX, mouseY + mouseOffsetY);
	}
	
	/**
	 * Check if the specified key is currently pressed
	 * 
	 * @param keyName the key to check
	 * @return whether the specified key is pressed
	 */
	public boolean _isKeyDown(int keyName)
	{
		return pressedKeys.contains(keyName);
	}
	
	/**
	 * Check if the specified key was pressed during the last frame
	 * 
	 * @param keyName the key to check
	 * @return whether the specified key was pressed last frame
	 */
	public boolean _wasKeyDown(int keyName)
	{
		return wasPressedKeys.contains(keyName);
	}
	
	/**
	 * Check if the specified key is pressed right now, but was not pressed last frame
	 * <br><br>
	 * This method will not return true again until the key is released and pressed again.
	 * 
	 * @param keyName the key to check
	 * @return whether the specified key was pressed (for the first time)
	 */
	public boolean _isKeyPressed(int keyName)
	{
		return this._isKeyDown(keyName) && !this._wasKeyDown(keyName);
	}
	
	public List<Actor> _mouseClicked()
	{
		MouseInfo mi = getMouseInfo();
		return world.getObjectsAt(mi.getX(), mi.getY());
	}
	
	/**
	 * Check if the specified object was clicked.
	 * 
	 * @param obj the object to check for a click
	 * @return whether the specified object was clicked
	 */
	public boolean _mouseClicked(Object obj)
	{
		Collection<MouseClick> clicks = this.clicks.values();
		
		//chcek for any click
		if(null == obj)
			return clicks.size() > 0;
		
		//check if world was clicked
		if(obj instanceof World)
		{	
			World world = (World)obj;
			for(MouseClick click : clicks)
				if(world.getObjectsAt(click.getX(), click.getY()).size() > 0)
					return false;
			return true;
		}
		else if(obj instanceof Actor)
		{
			Actor actor = (Actor)obj;
			
			//check if obj was clicked
			for(MouseClick click : clicks)
			{
				if(actor.getBounds().contains(click.getX(), click.getY()))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean _wasMouseClicked(Object obj)
	{
		Collection<MouseClick> clicks = this.pastClicks.values();
		
		//chcek for any click
		if(null == obj)
			return clicks.size() > 0;
		
		//check if world was clicked
		if(obj instanceof World)
		{	
			World world = (World)obj;
			for(MouseClick click : clicks)
				if(world.getObjectsAt(click.getX(), click.getY()).size() > 0)
					return false;
			return true;
		}
		else if(obj instanceof Actor)
		{
			Actor actor = (Actor)obj;
			
			//check if obj was clicked
			for(MouseClick click : clicks)
			{
				if(actor.getBounds().contains(click.getX(), click.getY()))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean _mouseHovered(Object obj)
	{
		if(null == obj)
			return false;

		return hoveredObjects.contains(obj);
	}
	
	private boolean _wasMouseHovered(Object obj)
	{
		if(null == obj)
			return false;
		
		return oldHoveredObjects.contains(obj);
	}
	
	/**
	 * Not implemented
	 * 
	 * @param obj
	 * @return
	 */
	public boolean _mouseDragEnded(Object obj)
	{
		//TODO:
		System.out.println("mouseDragEnded is not implemented yet");
		return false;
	}
	
	/**
	 * Not implemented
	 * 
	 * @param obj
	 * @return
	 */
	public boolean _mouseDragged(Object obj)
	{
		//TODO:
		System.out.println("mouseDragged is not implemented yet");
		return false;
	}
	
	/**
	 * Check if the mouse button is being pressed over the specified object.
	 * <br><br>
	 * This method will return true as long as the mouse button is down and over the object
	 * 
	 * @param obj the object to check for a click
	 * @return whether the specified object was clicked.
	 */
	public boolean _mouseDown(Object obj)
	{
		if(nowMousePressed)
		{
			if(null == obj)
				return true;
			else if(obj instanceof World)
				return ((World)obj).getObjectsAt(mouseX + mouseOffsetX, mouseY + mouseOffsetY).size() == 0;
			else if(obj instanceof Actor)
				return ((Actor)obj).getBounds().contains(mouseX + mouseOffsetX, mouseY + mouseOffsetY);
		}
		return false;
	}
	
	/**
	 * Check if the specified object was clicked this frame, but not last frame.
	 * <br><br>
	 * This method will not return true again until the mouse is released and clicked again.
	 * 
	 * @param obj the object to check for a click
	 * @return whether the specified object was clicked for the first time.
	 */
	public boolean _mousePressed(Object obj)
	{
		if(mousePressed && !lastMousePressed)
		{
			if(null == obj)
				return true;
			else if(obj instanceof World)
				return ((World)obj).getObjectsAt(mouseX + mouseOffsetX, mouseY + mouseOffsetY).size() == 0;
			else if(obj instanceof Actor)
				return ((Actor)obj).getBounds().contains(mouseX + mouseOffsetX, mouseY + mouseOffsetY);
		}
		return false;
	}
	
	public boolean _mouseMoved(Object obj)
	{
		if(!mouseMoved)
			return false;
		if(null == obj)
			return true;
		else if(obj instanceof World)
			return ((World)obj).getObjectsAt(mouseX + mouseOffsetX, mouseY + mouseOffsetY).size() == 0;
		else if(obj instanceof Actor)
			return ((Actor)obj).getBounds().contains(mouseX + mouseOffsetX, mouseY + mouseOffsetY);
		return false;
	}
	
	/**
	 * Preload a Sound file (wav)
	 * 
	 * @param soundFile the path to the sound file to play
	 */
	public void _loadSound(String soundFile)
	{
		File file = new File(soundFile);
		if(file.exists())
		{
			Sound sound = TinySound.loadSound(file);
			sounds.put(soundFile, sound);
		}
	}
	
	public void _loadMusic(String musicFile)
	{	
		File file = new File(musicFile);
		if(file.exists())
		{
			MP3Player player = new MP3Player(file);
			musics.put(musicFile, player);
		}
	}
	
	/**
	 * Play the specified sound file (wav or ogg)
	 * 
	 * @param soundFile the path to the sound file to play
	 */
	public void _playSound(String soundFile)
	{
		Sound sound = sounds.get(soundFile);
		if(null == sound)
		{
			this._loadSound(soundFile);
			sound = sounds.get(soundFile);
		}
		
		if(null != sound)
			sound.play();
	}
	
	public void _stopMusic(String musicFile)
	{
		MP3Player music = musics.get(musicFile);
		if(null != music)
		{
			music.stop();
		}
	}
	
	public void _playMusic(String musicFile)
	{
		MP3Player music = musics.get(musicFile);
		if(null == music)
		{
			this._loadMusic(musicFile);
			music = musics.get(musicFile);
		}
		
		if(null != music)
		{
			try
			{
				music.play();
			}
			catch(Exception e)
			{
			}
		}
	}
	
	/**
	 * Not implemented.
	 * 
	 * @param speed
	 */
	public void _setSpeed(int speed)
	{
		System.out.println("Setting speed doesn't do anything...");
	}
	
	/**
	 * Set the active world
	 * 
	 * @param world the world to set as active
	 */
	public void _setWorld(World world)
	{
		if(this.world != null)
			this.world.stopped();
		
		this.world = world;

		if(world != null)
			this.world.started();
	}
	
	/**
	 * Unpause the game
	 */
	public void _start()
	{
		if(!paused)
			return;
		paused = false;
		if(null != world)
			world.started();
	}
	
	/**
	 * Check if the game is paused
	 * 
	 * @return whether the game is paused
	 */
	public boolean _isStopped()
	{
		return paused;
	}
	
	/**
	 * Pause the game.
	 * <br><br>
	 * This will stop calling the act method on all actors. It will continue to call the act method on the world (so that the game can be unpaused)
	 */
	public void _stop()
	{
		if(paused)
			return;
		paused = true;
		if(null != world)
			world.stopped();
	}

	/**
	 * Set the fullscreen status of the game. 
	 * 
	 * @param fullscreen should the game be in fullscreen or windowed mode
	 */
	public void _setFullScreen(boolean fullscreen)
	{
		/*
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice myDevice = ge.getDefaultScreenDevice();
	    //myDevice.setFullScreenWindow(myWindow);
	    if(fullscreen)
	    {
		    if(myDevice.isFullScreenSupported())
		    {
		    	//System.out.println("Yes");
		    	myDevice.setFullScreenWindow(frame);
		    }
	    }
	    else
	    {
	    	myDevice.setFullScreenWindow(null);
	    }
	    */
	    
	    
		/*
		try 
		{
			appgc.setFullscreen(fullscreen);
			Mayflower.fullscreen = fullscreen;
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		*/
	}
	
	/**
	 * Coming soon.
	 * 
	 * @param showCursor should the cursor be shown.
	 */
	public void _showCursor(boolean showCursor)
	{
		//TODO: use a transparent image for mouse cursor instead of mouse grabbed (which is un-undoable)
		//appgc.setMouseGrabbed(!showCursor);
	}
	
	public void _setMouseOffset(int x, int y)
	{
		mouseOffsetX = x;
		mouseOffsetY = y;
	}
	
	////////////////
	//STATIC METHODS
	////////////////
	public static void setMouseOffset(int x, int y)
	{
		instance._setMouseOffset(x,  y);
	}
	
	public static Shape getBounds()
	{
		return instance._getBounds();
	}
	
	public static void showBounds(boolean showBounds)
	{
		instance._showBounds(showBounds);
	}
	
	public static void showFPS(boolean showFPS)
	{
		instance._showFPS(showFPS);
	}
	
	public static int getWidth()
	{
		return instance._getWidth();
	}
	
	public static int getHeight()
	{
		return instance._getHeight();
	}
	
	/**
	 * Open a popup that prompts the user to enter some text.
	 * 
	 * @param prompt What message should be displayed to the user?
	 * @return the String that the user entered
	 */
	public static String ask(String prompt)
	{
		return JOptionPane.showInputDialog(prompt);
	}
	
	/**
	 * Stop execution of the engine for the specified time.
	 * @param time how long (in milliseconds) to stop execution.
	 */
	public static void delay(int time)
	{
		instance._delay(time);
	}
	
	/**
	 * Get the last key that was pressed
	 * @return the last key that was pressed
	 */
	public static int getKey()
	{
		return instance._getKey();
	}
	
	public static MouseInfo getMouseInfo()
	{
		return instance._getMouseInfo();
	}
	
	/**
	 * Check if the specified key is currently pressed
	 * 
	 * @param keyName the key to check
	 * @return whether the specified key is pressed
	 */
	public static boolean isKeyDown(int keyName)
	{
		return instance._isKeyDown(keyName);
	}
	
	/**
	 * Check if the specified key was pressed during the last frame
	 * 
	 * @param keyName the key to check
	 * @return whether the specified key was pressed last frame
	 */
	public static boolean wasKeyDown(int keyName)
	{
		return instance._wasKeyDown(keyName);
	}
	
	/**
	 * Check if the specified key is pressed right now, but was not pressed last frame
	 * <br><br>
	 * This method will not return true again until the key is released and pressed again.
	 * 
	 * @param keyName the key to check
	 * @return whether the specified key was pressed (for the first time)
	 */
	public static boolean isKeyPressed(int keyName)
	{
		return instance._isKeyPressed(keyName);
	}
	
	/**
	 * Check if the specified object was clicked.
	 * 
	 * @param obj the object to check for a click
	 * @return whether the specified object was clicked
	 */
	public static boolean mouseClicked(Object obj)
	{
		return instance._mouseClicked(obj);
	}
	
	public static List<Actor> mouseClicked()
	{
		return instance._mouseClicked();
	}
	
	public static boolean wasMouseClicked(Object obj)
	{
		return instance._wasMouseClicked(obj);
	}
	
	public static boolean mouseHovered(Object obj)
	{
		return instance._mouseHovered(obj);
	}
	
	public static boolean wasMouseHovered(Object obj)
	{
		return instance._wasMouseHovered(obj);
	}
	
	
	
	/**
	 * Not implemented
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean mouseDragEnded(Object obj)
	{
		return instance._mouseDragEnded(obj);
	}
	
	/**
	 * Not implemented
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean mouseDragged(Object obj)
	{
		return instance._mouseDragged(obj);
	}
	
	/**
	 * Check if the mouse button is being pressed over the specified object.
	 * <br><br>
	 * This method will return true as long as the mouse button is down and over the object
	 * 
	 * @param obj the object to check for a click
	 * @return whether the specified object was clicked.
	 */
	public static boolean mouseDown(Object obj)
	{
		return instance._mouseDown(obj);
	}
	
	/**
	 * Check if the specified object was clicked this frame, but not last frame.
	 * <br><br>
	 * This method will not return true again until the mouse is released and clicked again.
	 * 
	 * @param obj the object to check for a click
	 * @return whether the specified object was clicked for the first time.
	 */
	public static boolean mousePressed(Object obj)
	{
		return instance._mousePressed(obj);
	}
	
	public static boolean mouseMoved(Object obj)
	{
		return instance._mouseMoved(obj);
	}
	
	/**
	 * Preload a Sound file (wav)
	 * 
	 * @param soundFile the path to the sound file to play
	 */
	public static void loadSound(String soundFile)
	{
		instance._loadSound(soundFile);
	}
	
	public static void loadMusic(String musicFile)
	{	
		instance._loadMusic(musicFile);
	}
	
	/**
	 * Play the specified sound file (wav or ogg)
	 * 
	 * @param soundFile the path to the sound file to play
	 */
	public static void playSound(String soundFile)
	{
		instance._playSound(soundFile);
	}
	
	public static void stopMusic(String musicFile)
	{
		instance._stopMusic(musicFile);
	}
	
	public static void playMusic(String musicFile)
	{
		instance._playMusic(musicFile);
	}
	
	/**
	 * Not implemented.
	 * 
	 * @param speed
	 */
	public static void setSpeed(int speed)
	{
		instance._setSpeed(speed);
	}
	
	/**
	 * Set the active world
	 * 
	 * @param world the world to set as active
	 */
	public static void setWorld(World world)
	{
		instance._setWorld(world);
	}
	
	/**
	 * Unpause the game
	 */
	public static void start()
	{
		instance._start();
	}
	
	/**
	 * Check if the game is paused
	 * 
	 * @return whether the game is paused
	 */
	public static boolean isStopped()
	{
		return instance._isStopped();
	}
	
	/**
	 * Pause the game.
	 * <br><br>
	 * This will stop calling the act method on all actors. It will continue to call the act method on the world (so that the game can be unpaused)
	 */
	public static void stop()
	{
		instance._stop();
	}

	/**
	 * Set the fullscreen status of the game. 
	 * 
	 * @param fullscreen should the game be in fullscreen or windowed mode
	 */
	public static void setFullScreen(boolean fullscreen)
	{
		instance._setFullScreen(fullscreen);
	}
	
	/**
	 * Coming soon.
	 * 
	 * @param showCursor should the cursor be shown.
	 */
	public static void showCursor(boolean showCursor)
	{
		instance._showCursor(showCursor);
	}
	
	/**
	 * End the game. Close the window.
	 */
	public static void exit()
	{
		//appgc.exit();
		//thread.stop();
		System.exit(0);
	}
	
	public static long getTime()
	{
		//return System.currentTimeMillis();
		return System.nanoTime();
	}
	
	/**
	 * Get a random number between [0, limit)
	 * @param limit the upper bound (exclusive) of the random number
	 * @return a random number between [0, limit)
	 */
	public static int getRandomNumber(int limit)
	{
		return (int)(Math.random() * limit);
	}
	
	/**
	 * Return the squared distance between (x1, y1) and (x2, y2)
	 * <br><br>
	 * This is faster than getDistance()
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the squared distance between (x1, y1) and (x2, y2)
	 */
	public static int getDistance2(int x1, int y1, int x2, int y2)
	{
		int dx = x2 - x1;
		int dy = y2 - y1;
		return dx * dx - dy * dy;
	}
	
	/**
	 * Return the distance between (x1, y1) and (x2, y2)
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance between (x1, y1) and (x2, y2)
	 */
	public static int getDistance(int x1, int y1, int x2, int y2)
	{
		return (int)Math.sqrt(getDistance2(x1, y1, x2, y2));
	}
	
	public static boolean fullyContains(Shape out, Shape in)
	{
		if(out == null || in == null)
			return false;
		if(in == out)
			return false;
		
		Area areaOut = new Area(out);
		Area areaIn = new Area(in);
		areaIn.subtract(areaOut);
		return areaIn.isEmpty();
	}
	
	public static boolean partiallyContains(Shape a, Shape b)
	{
		if(a == null || b == null)
			return false;
		if(a == b)
			return false;
		//https://stackoverflow.com/questions/15690846/java-collision-detection-between-two-shape-objects
		if(a.getBounds2D().intersects(b.getBounds2D()))
		{
			Area areaA = new Area(a);
			Area areaB = new Area(b);
			areaA.intersect(areaB);
			return !areaA.isEmpty();
		}
		
		return false;
	}
	
	public static boolean pixelPerfectTouching(Actor a, Actor b)
	{
		System.out.println("pixelPerfectTouching is not implemented yet");
		
		if(a == null || b == null)
			return false;
	
		if(Mayflower.partiallyContains(a.getBounds(), b.getBounds()))
		{
			//check if any pixel of A is touching any pixel of b (ignoring fully transparent pixels)
			boolean[][] maskA = a.getImage().getMask();
			boolean [][] maskB = b.getImage().getMask();
			
			int aOffX = a.getX();
			int aOffY = a.getY();
			
			int bOffX = b.getX();
			int bOffY = b.getY();
			
			Set<Point> points = new HashSet<Point>();
			
			for(int r = 0; r < maskA.length; r++)
			{
				for(int c = 0; c < maskA[r].length; c++)
				{
					if(maskA[r][c])
					{
						int x = aOffX + c;
						int y = aOffY + r;
						
						points.add(new Point(x, y));
					}
				}
			}
			
			for(int r = 0; r < maskB.length; r++)
			{
				for(int c = 0; c < maskB[r].length; c++)
				{
					int x = bOffX + c;
					int y = bOffY + r;
					
					//if you can't add this point to the set, then there is an overlap!
					if(maskB[r][c] && !points.add(new Point(x, y)))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	class MouseClick
	{
		private int x, y, clicks, button;
		
		public MouseClick(int x, int y, int button, int clicks)
		{
			this.x = x;
			this.y = y;
			this.button = button;
			this.clicks = clicks;
		}
		
		public int getX()
		{
			return x;
		}
		
		public int getY()
		{
			return y;
		}
		
		public int getNumClicks()
		{
			return clicks;
		}
		
		public int getButton()
		{
			return button;
		}
		
		public void addClicks(int num)
		{
			clicks += num;
		}
	}
}
