package mayflower.test;

import mayflower.Actor;
import mayflower.Color;
import mayflower.Mayflower;
import mayflower.MayflowerImage;
import mayflower.ui.Button;

public class TestActor2 extends Actor
{
	private int count = 0;
	public TestActor2()
	{
		setImage(new MayflowerImage("rsrc/moblin.png"));
		//super("rsrc/button1.png", "clicked-button");
		//this.setHoverImage("rsrc/button2.png");
		//this.setClickImage("rsrc/button3.png");
	}
	
	public void onEvent(String action)
	{
		System.out.println("Banana");
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	@Override
	public void act() 
	{	
		if(Mayflower.mouseClicked(this))
		{
			getWorld().showText("CLICKED " + count++, 24, 20, 90, Color.BLACK );
		}
	}
	
	public String toString()
	{
		return "Moblin @ ("+getX()+", "+getY()+")";
	}
	*/
}
