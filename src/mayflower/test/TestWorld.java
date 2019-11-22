package mayflower.test;

import java.awt.List;

import mayflower.*;
import mayflower.event.EventListener;

public class TestWorld extends World implements EventListener 
{
	private int x= 0;
	private boolean mouseDown;
	
	public TestWorld()
	{
		setBackground("img/grass.png");
		Mayflower.showBounds(true);
	}
	
	public void act()
	{
		MouseInfo mi = Mayflower.getMouseInfo();
		
		showText("Test: " + mi.getX() + ", " + mi.getY(), 24, 20, 20, Color.BLACK);
		
		/*
		java.util.List<Actor> clicked = Mayflower.mouseClicked();
		if(clicked.size() != 0)
		{
			if(!mouseDown)
			{
				System.out.println("Mouse Down!");
			}
			mouseDown = true;
		}
		else if(mouseDown)
		{
			System.out.println("Released!");
		}
		*/
		
		/*
		if(Mayflower.mouseClicked(TestActor2.class))
		{
			System.out.println("xx");
			showText("Click:" + mi.getX() + ", " + mi.getY(), 24, 20, 50, Color.BLACK);
		}
		*/
	}

	@Override
	public void onEvent(String action) {
		// TODO Auto-generated method stub
		System.out.println(action);
		
	}
}