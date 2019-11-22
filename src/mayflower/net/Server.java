package mayflower.net;

import java.util.Map;

import mayflower.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The parent class for Server programs.
 * <br><br>
 * Listens for connections from and manages Client programs.
 */
public abstract class Server extends Thread
{
	private int port;
	private boolean running;
	private Map<Integer, ServerSideClient> clients;
	private int nextClientId;
	private Socket clientSocket;
	private List<Logger> loggers;
	
	/**
	 * Create a Server listening to the specified port
	 * <br><br>
	 * This server will start immediately.
	 * @param port the port to listen to
	 */
	public Server(int port)
	{
		this(port, false);
	}
	
	/**
	 * Create a server listening to the specified port.
	 * <br><br>
	 * This server will not start until the start() method is called.
	 *  
	 * @param port the port to listen to
	 * @param delayStart should this server wait for the start() method to be called before it starts?
	 */
	public Server(int port, boolean delayStart)
	{
		this.port = port;
		this.clients = new HashMap<Integer, ServerSideClient>();
		this.loggers = new ArrayList<>();
		nextClientId = 1;
		if(!delayStart)
			start();
	}
	
	/**
	 * The main loop of the Server. You should not call or override this method.
	 */
	public void run()
	{
		running = true;
		ServerSocket serverSocket = null;
		
		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch(Exception e)
		{
			log(e.getMessage());
			running = false;
		}
		
		ServerSideClient client;
		while(running)
		{
			try
			{
				//ServerSocket serverSocket = new ServerSocket(port);
				clientSocket = serverSocket.accept();
				client = new ServerSideClient(getNextClientId(), clientSocket, this);
				clients.put(client.getClientId(), client);
				client.start();
				onJoin(client.getClientId());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			if(null != clientSocket)
				clientSocket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Stop the server from running and disconnect all clients.
	 */
	public void shutdown()
	{
		running = false;
		
		try
		{
			if(null != clientSocket)
				clientSocket.close();
			disconnectAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int getPort()
	{
		return port;
	}
	
	public String getIP()
	{
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "???.???.???.???";
	}
	
	/**
	 * Send a message to the specified client.
	 * 
	 * @param id the client id
	 * @param message the message to send
	 */
	public void send(int id, String message)
	{
		ServerSideClient client = clients.get(id);
		if(null != client)
		{
			client.send(message);
		}
	}
	
	/**
	 * Send a message to all clients
	 * 
	 * @param message the message to send
	 */
	public void send(String message)
	{
		ServerSideClient[] clients = this.clients.values().toArray(new ServerSideClient[] {});
		for(ServerSideClient client : clients)
		{
			send(client.getClientId(), message);
		}
	}
	
	/**
	 * Disconnect the specified client.
	 * @param id the id of the client to disconnect
	 */
	public void disconnect(int id)
	{
		ServerSideClient client = clients.remove(id);
		if(null != client)
		{
			client.send("disconnect");
			client.disconnect();
			onExit(id);
		}
	}
	
	/**
	 * Disconnect all clients
	 */
	public void disconnectAll()
	{
		ServerSideClient[] clients = this.clients.values().toArray(new ServerSideClient[] {});
		for(ServerSideClient client : clients)
		{
			disconnect(client.getClientId());
		}
	}
	
	/**
	 * How many clients are currently connected
	 * 
	 * @return the number of connected clients
	 */
	public int numConnects()
	{
		return clients.size();
	}
	
	/**
	 * Get the ServerSideClient with the specified id
	 * 
	 * @param id the id of the client to get
	 * @return the connected client with the specified id or null
	 */
	public ServerSideClient getClient(int id)
	{
		return clients.get(id);
	}
	
	/**
	 * Send a message to all registered Loggers
	 * 
	 * @param message the message to log
	 */
	public void log(String message)
	{
		System.out.println(message);
		for(Logger logger : loggers)
			logger.log(message);
	}
	
	/**
	 * Register a Logger to listen for cals to the log() method
	 * 
	 * @param logger the Logger to register
	 */
	public void addLogger(Logger logger)
	{
		loggers.add(logger);
	}
	
	/**
	 * Handle a message from a client
	 * 
	 * @param id the id of the client that send the message
	 * @param message the message send from the client
	 */
	public abstract void process(int id, String message);
	
	/**
	 * Handle a new connection to the server
	 * 
	 * @param id the id of the client that just connected to the server
	 */
	public abstract void onJoin(int id);
	
	/**
	 * Handle a client that disconnected from the server
	 * 
	 * @param id the id of the client that disconnected
	 */
	public abstract void onExit(int id);
	
	private int getNextClientId()
	{
		return nextClientId++;
	}
}
