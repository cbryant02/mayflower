package mayflower.test;

import mayflower.*;

public class StringActor extends Actor
{
	public StringActor(String message)
	{	
		setImage(new MayflowerImage(message, 36, Color.WHITE));
	}

	@Override
	public void act() 
	{
		
	}

}
