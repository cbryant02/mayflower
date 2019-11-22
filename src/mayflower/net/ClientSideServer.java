package mayflower.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * A helper class for the Client that constantly listens for messages from the server
 * <br><br>
 * You shouldn't extend or instantiate a ClientSideServer object
 */
public class ClientSideServer extends Thread 
{
	private Client client;
	private boolean running;
	private BufferedReader in;
	
	public ClientSideServer(Socket socket, Client client)
	{
		this.client = client;
		
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		if(!running)
			return;
		
		running = false;
		
		try
		{
			in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		client.disconnect();
	}
	
	public void run()
	{
		running = true;
		while(running)
		{	
			try
			{
				String message = in.readLine();
				if(null == message)
					break;
				else
					client.process(message);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				running = false;
			}
			
			try
			{
				Thread.sleep(1);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		disconnect();
	}
}
