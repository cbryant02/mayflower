package mayflower.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A helper class for the Server that constantly listens for messages from a client
 * <br><br>
 * You shouldn't extend or instantiate a ServerSideClient object
 */
public class ServerSideClient extends Thread
{
	private int id;
	private boolean running;
	private Socket client;
	private Server server;
	private PrintWriter out;
	private BufferedReader in;
	
	public ServerSideClient(int id, Socket client, Server server)
	{
		this.id = id;
		this.client = client;
		this.server = server;
		
		try
		{
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int getClientId()
	{
		return id;
	}
	
	public void send(String message)
	{
		out.println(message);
	}
	
	public void disconnect()
	{
		if(!running)
			return;
		
		running = false;
		
		try
		{
			in.close();
			out.close();
			client.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
					server.process(getClientId(), message);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				running = false;
				break;
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
		server.disconnect(id);
	}
	
	public boolean equals(Object other)
	{
		if(null == other || !(other instanceof ServerSideClient)) 
			return false;
		return ((ServerSideClient)other).getClientId() == this.id;
	}
	
	public int hashCode()
	{
		return this.id;
	}
}
