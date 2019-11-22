package mayflower.net;

import java.io.PrintWriter;
import java.net.Socket;

/**
 * The parent class of Client programs
 */
public abstract class Client 
{
	private ClientSideServer server;
	private boolean connected;
	private Socket socket;
	private PrintWriter out;
	
	/**
	 * Connect to a server on the localhost listening to the specified port
	 * @param port
	 * @return
	 */
	public boolean connect(int port)
	{
		return connect("localhost", port);
	}
	
	/**
	 * Connect to a server at the specified IP listening to the specified port
	 * @param ip
	 * @param port
	 * @return
	 */
	public boolean connect(String ip, int port)
	{
		if(connected)
			disconnect();
		
		connected = false;
		try
		{
			socket = new Socket(ip, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			connected = true;
			
			server = new ClientSideServer(socket, this);
			server.start();
			onConnect();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			disconnect();
		}
		
		return connected;
	}
	
	/**
	 * Send a message to the server
	 * 
	 * @param message the message to send
	 */
	public void send(String message)
	{
		if(connected)
		{
			out.println(message);
		}
	}
	
	/**
	 * Disconnect from the server
	 */
	public void disconnect()
	{
		connected = false;
		server.disconnect();
		try
		{
			out.close();
			socket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		onDisconnect("Disconnected from Server");
	}
	
	/**
	 * Handle a message received from the server
	 * 
	 * @param message the message received from the server
	 */
	public abstract void process(String message);
	
	/**
	 * Handle being disconnected by the server
	 * 
	 * @param message the message send by the server explaining why you were disconnected
	 */
	public abstract void onDisconnect(String message);
	
	/**
	 * Handle being connected to the server.
	 */
	public abstract void onConnect();
}
