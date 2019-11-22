package mayflower.test;

import java.util.*;

import mayflower.Actor;
import mayflower.Color;
import mayflower.Direction;
import mayflower.Keyboard;
import mayflower.Mayflower;
import mayflower.MayflowerImage;
import mayflower.MouseInfo;

public class TestActor extends Actor 
{
	int moveDistance = 1;
	
	public TestActor()
	{
		setImage(new MayflowerImage("img/karel.png"));

		
	}
	
	@Override
	public void act()
	{
		if(Mayflower.isKeyPressed(Keyboard.KEY_W))
		{
			System.out.println("PRESS W");
		}
		if(!Mayflower.isKeyDown(Keyboard.KEY_W) && Mayflower.wasKeyDown(Keyboard.KEY_W))
		{
			System.out.println("RELEASE W");
		}
		
		if(Mayflower.isKeyPressed(Keyboard.KEY_D))
		{
			System.out.println("PRESS D");
		}
		if(!Mayflower.isKeyDown(Keyboard.KEY_D) && Mayflower.wasKeyDown(Keyboard.KEY_D))
		{
			System.out.println("RELEASE D");
		}
	}
	public void actX() 
	{	
		//List<Actor> inRange = getObjectsInRange(100, Actor.class);
		//System.out.println(inRange.size());
		
		MouseInfo mi = Mayflower.getMouseInfo();
		//setLocation(mi.getX(), mi.getY());
		
		if(Mayflower.isKeyDown(Keyboard.KEY_W))
		{
			this.move(1);
		}
		if(Mayflower.isKeyDown(Keyboard.KEY_A))
		{
			this.turn(-1);
		}
		if(Mayflower.isKeyDown(Keyboard.KEY_D))
		{
			this.turn(1);
		}
		if(Mayflower.isKeyPressed(Keyboard.KEY_SPACE))
		{
			this.setLocation(100, 100);
			//this.setRotation(90);
		}
		
		getWorld().showText("R:"+getRotation(), 300, 60);
		getWorld().showText("X:"+getX(), 300, 110);
		getWorld().showText("Y:"+getY(), 300, 160);
		
		/*
		if(isTouching(TestActor2.class))
		{
			System.out.println("touching");
		}
		
		if(Mayflower.isKeyPressed(Keyboard.KEY_SPACE))
		{
			System.exit(0);
		}

		if(Mayflower.isKeyPressed(Keyboard.KEY_SPACE))
		{
			System.out.println("a");
		}
		
		if(Mayflower.mouseClicked(this))
		{
			System.out.println(label);
		}
		turnAndMove();
		/*
		if(Mayflower.isKeyPressed(Keyboard.KEY_W))
		{
			scale(getImage().getWidth()+1, getImage().getHeight()+1);
		}
		if(Mayflower.isKeyPressed(Keyboard.KEY_S))
		{
			scale(getImage().getWidth()-1, getImage().getHeight()-1);
		}
		if(Mayflower.isKeyPressed(Keyboard.KEY_A))
		{
			turn(-10);
		}
		if(Mayflower.isKeyPressed(Keyboard.KEY_D))
		{
			turn(10);
		}
		
		if(Mayflower.isKeyPressed(Keyboard.KEY_SPACE))
		{
			System.out.println(getNeighbors(2, false, TestActor2.class));
		}
		
		/*
		List<Actor> x = getIntersectingObjects(Actor.class);
		if(x.size() > 0)
			System.out.println(x);
		*/
	}
	
	private void compassTurnMove()
	{
		
		if(Mayflower.isKeyDown(Keyboard.KEY_LEFT))
		{
			//System.out.println("aa");
			setRotation(Direction.WEST);
			move(moveDistance);
		}
		else if(Mayflower.isKeyDown(Keyboard.KEY_RIGHT))
		{
			setRotation(Direction.EAST);
			move(moveDistance);
		}
		else if(Mayflower.isKeyDown(Keyboard.KEY_UP))
		{
			setRotation(Direction.NORTH);
			move(moveDistance);
		}
		else if(Mayflower.isKeyDown(Keyboard.KEY_DOWN))
		{
			setRotation(Direction.SOUTH);
			move(moveDistance);
		}
		
		if(isAtEdge())
			move(-moveDistance);
		
		
	}
	
	private void compassMove()
	{
		if(Mayflower.isKeyDown(Keyboard.KEY_LEFT))
		{
			setLocation(getX()-1, getY());
		}
		if(Mayflower.isKeyDown(Keyboard.KEY_RIGHT))
		{
			setLocation(getX()+1, getY());
		}
		if(Mayflower.isKeyDown(Keyboard.KEY_UP))
		{
			setLocation(getX(), getY()-1);
		}
		if(Mayflower.isKeyDown(Keyboard.KEY_DOWN))
		{
			setLocation(getX(), getY()+1);
		}
		
		if(Mayflower.isKeyDown(Keyboard.KEY_LSHIFT) && !Mayflower.wasKeyDown(Keyboard.KEY_LSHIFT))
		{
			turn(-45);
			if(isAtEdge())
				turn(45);
		}
		
		if(Mayflower.isKeyDown(Keyboard.KEY_RSHIFT) && !Mayflower.wasKeyDown(Keyboard.KEY_RSHIFT))
		{
			turn(45);
			if(isAtEdge())
				turn(-45);
		}
	}
	
	private void turnAndMove()
	{
		int d = 10;
		if(Mayflower.isKeyDown(Keyboard.KEY_LEFT))
		{
			turn(-d);
			if(isAtEdge())
				turn(d);
		}
		if(Mayflower.isKeyDown(Keyboard.KEY_RIGHT))
		{
			turn(d);
			if(isAtEdge())
				turn(-d);
			
		}
		if(Mayflower.isKeyDown(Keyboard.KEY_UP))
		{
			move(moveDistance);
			if(isAtEdge())
				move(-moveDistance);
		}
		if(Mayflower.isKeyDown(Keyboard.KEY_DOWN))
		{
			move(-moveDistance);
			if(isAtEdge())
				move(moveDistance);
		}
	}

}
