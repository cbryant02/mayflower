package mayflower.ui;

import mayflower.event.EventListener;
import java.util.LinkedList;
import java.util.List;

import mayflower.Actor;
import mayflower.Mayflower;
import mayflower.MayflowerImage;

public class Button extends Actor
{
	private String clickAction, onHoverAction, offHoverAction, releaseAction;
	private MayflowerImage img, clickImg, hoverImg;
	private List<EventListener> listeners;
	
	public Button(String img, String action)
	{
		setDefaultImage(img);
		this.clickAction = action;
		listeners = new LinkedList<EventListener>();
		
		setImage(this.img);
	}
	
	public Button(String defaultImg, String clickImg, String action)
	{
		this(defaultImg, action);
		setClickImage(clickImg);
	}
	
	public Button setClickAction(String action)
	{
		this.clickAction = action;
		return this;
	}
	
	public Button setOnHoverAction(String action)
	{
		this.onHoverAction = action;
		return this;
	}
	
	public Button setOffHoverAction(String action)
	{
		this.offHoverAction = action;
		return this;
	}
	
	public Button setReleaseAction(String action)
	{
		this.releaseAction = action;
		return this;
	}
	
	public Button setDefaultImage(String img)
	{
		this.img = new MayflowerImage(img);
		return this;
	}
	
	public Button setClickImage(String img)
	{
		this.clickImg = new MayflowerImage(img);
		return this;
	}
	
	public Button setHoverImage(String img)
	{
		this.hoverImg = new MayflowerImage(img);
		return this;
	}
	
	public Button addEventListener(EventListener listener)
	{
		listeners.add(listener);
		return this;
	}
	
	public Button removeEventListener(EventListener listener)
	{
		listeners.remove(listener);
		return this;
	}
	
	public Button clearEventListeners()
	{
		listeners.clear();
		return this;
	}
	
	private void triggerEvent(String event)
	{
		for(EventListener listener : listeners)
		{
			listener.onEvent(event);
		}
		
		//notify self of event
		this.onEvent(event);
	}

	@Override
	public void act() 
	{
		//check for mouse click/release
		if(Mayflower.mouseClicked(this))
		{
			if(null != clickAction)
				triggerEvent(clickAction);
			
			if(null != clickImg)
				setImage(clickImg);
		}
		else if(Mayflower.wasMouseClicked(this))
		{
			if(null != releaseAction)
				triggerEvent(releaseAction);
			
			if(null != img)
				setImage(img);
		}
		
		//check for house hover
		if(Mayflower.mouseHovered(this))
		{
			if(null != onHoverAction)
				triggerEvent(onHoverAction);
			
			if(null!= clickImg && Mayflower.mouseDown(this))
				setImage(clickImg);
			else if(null != hoverImg)
				setImage(hoverImg);
		}
		else if(Mayflower.wasMouseHovered(this))
		{
			if(null != offHoverAction)
				triggerEvent(offHoverAction);
			
			if(null != img)
				setImage(img);
		}
		
	}

	//Buttons can trigger on their own events by default
	public void onEvent(String action) 
	{
		//NOOP
	}
}
