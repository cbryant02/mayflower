package mayflower;

public abstract class MayflowerHeadless extends Mayflower
{

	public MayflowerHeadless(String title, int width, int height) 
	{
		super(title, width, height);
	}
	
	public void initGUI(String title)
	{
		//NOOP
		System.out.println("No GUI Loaded");
		init();
	}
	
	public void render()
	{
		//NOOP
	}
}
