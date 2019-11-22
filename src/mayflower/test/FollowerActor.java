package mayflower.test;

import mayflower.Actor;
import mayflower.MayflowerImage;
import mayflower.Timer;

public class FollowerActor extends Actor 
{
	private Actor target;
	private Timer t;
	public FollowerActor(Actor target)
	{
		this.target = target;
		setImage(new MayflowerImage("rsrc/moblin.png"));
		t = new Timer(500);
	}
	
	@Override
	public void act() 
	{	
		if(t.isDone())
		{
			if(null != target)
				turnTowards(target);
			move(32);
			t.reset();
		}
	}
	
	

}
