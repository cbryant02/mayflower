package mayflower.core;

import mayflower.Mayflower;

public class MayflowerThread implements Runnable 
{
	private Mayflower mayflower;
	private boolean running = false;
	
	public MayflowerThread(Mayflower mayflower)
	{
		this.mayflower = mayflower;
	}
	
	public void start()
	{
		(new Thread(this)).start();
	}
	
	public void run()
	{
		if(running)
			return;
		running = true;
		while(running)
		{
			mayflower.tick();
			Thread.yield();
		}
	}
	
	public void stop()
	{
		running = false;
	}
}
