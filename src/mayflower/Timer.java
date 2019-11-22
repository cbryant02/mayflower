package mayflower;

/**
 * A Timer object that can be used to trigger events based on time rather than frame rate.
 * <br><br>
 * A Timer object should be kept as an instance variable and the isDone method should be checked in the act method.
 * <br><br>
 * Example:
 * <pre> 
 * {@code
 * public class TestTimer extends Actor
 * {
 *   private Timer t;
 *   
 *   public TestTimer()
 *   {
 *     t = new Timer(1000); //initialize a timer to trigger every second
 *   }
 *   
 *   public void act()
 *   {
 *     if( t.isDone() ) //check if the timer is done
 *     {
 *       t.reset(); //restart the timer
 *       
 *       //do something that should happen every 1000 milliseconds
 *     }
 *   } 
 * }
 * }
 * </pre>
 * 
 */
public class Timer 
{
	private int goal;
	private long start;
	
	/**
	 * Initialize and start a Timer with a goal of 0 milliseconds.
	 * 
	 * This timer will always return true when isDone is called.
	 */
	public Timer()
	{
		reset();
		goal = 0;
	}
	
	/**
	 * Initialize and start a Timer with a specified goal time. (in milliseconds)
	 * 
	 * The isDone method will return false until goal milliseconds have passed.
	 * 
	 * @param goal how many milliseconds must pass before isDone will return true
	 */
	public Timer(int goal)
	{
		reset();
		this.goal = goal;
	}
	
	/**
	 * Change the goal time for this timer (in milliseconds)
	 * 
	 * This method will reset the timer.
	 * 
	 * @param goal the new goal time
	 */
	public void set(int goal)
	{
		reset();
		this.goal = goal;
	}
	
	/**
	 * Adjust the goal time of this timer by the specified amount (in milliseconds)
	 * 
	 * This method will NOT reset the timer.
	 * 
	 * @param diff the number of milliseconds to adjust the timer by.
	 */
	public void adjust(int diff)
	{
		goal += diff;
	}
	
	/**
	 * Check if the timer's goal has been met.
	 * 
	 * @return true if goal milliseconds have passed since the timer was started.
	 */
	public boolean isDone()
	{
		return getTimeLeft() <= 0;
	}
	
	/**
	 * Get the number of milliseconds that remain until the timer is done.
	 * 
	 * @return the number of milliseconds that remain until the timer is done.
	 */
	public long getTimeLeft()
	{
		return ((goal + start) - Mayflower.getTime());
	}
	
	/**
	 * Check if there is at least the specified number of milliseconds left until the timer is done.
	 * 
	 * @param time how many milliseconds need to be remaining for this method to return true
	 * @return whether the specified number of milliseconds (or more) remain before the timer is done 
	 */
	public boolean isTimeLeft(int time)
	{
		return getTimeLeft() >= 0;
	}
	
	/**
	 * Reset the timer. It will start counting from 0 again.
	 */
	public void reset()
	{
		start = Mayflower.getTime();
	}
}
