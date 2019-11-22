package mayflower.test;

import mayflower.*;

public class Test extends Mayflower
{
	public Test()
	{
		super("Test Mayflower", 800, 600);
	}
	
	public void init()
	{
		System.out.println("Init");
		Mayflower.showBounds(true);
		//Mayflower.showFPS(true);
		//Mayflower.setFullScreen(false);
		//Mayflower.showCursor(false);
		TestWorld w = new TestWorld();
		TestActor2 a = new TestActor2();
		TestActor2 b = new TestActor2();
		
		
		//w.addObject(testActor, 100, 100);
		w.addObject(a, 200, 200);
		w.addObject(b, 100, 100);
		
		
		System.out.println("Mayflower.setWorld(w)");
		Mayflower.setWorld(w);
		
		//Mayflower.setFullScreen(true);
		
		//Mayflower.playSound("rsrc/excellent_x.wav");
		Mayflower.setFullScreen(true);
	}
	
	public static void main(String[] args)
	{
		new Test();
	}

}
