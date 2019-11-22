package mayflower.net;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import mayflower.util.Logger;

/**
 * A Simple GUI for a Server program.
 * <br><br>
 * This opens a window with a text area that displays messages from the Server's log() method
 */
public class ServerGUI implements Logger
{
	private JTextArea textArea;
	
	/**
	 * Create a ServerGUI with the specified Server
	 * 
	 * @param server the Server the GUI should display logs for
	 */
	public ServerGUI(Server server)
	{	
		textArea = new JTextArea();
		
		server.addLogger(this);
		
		JFrame frame = new JFrame("Server");
		
		frame.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	server.log("Shutting down...");
		        server.shutdown();
		    }
		});
		
		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("IP Address: " + server.getIP() + " Port: " + server.getPort()));
		
		frame.add(topPanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		textArea.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		panel.add(scrollPane);
		
		frame.getContentPane().add(panel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setResizable(true);
		frame.setVisible(true);
	}

	/**
	 * Append the specified message to the text area
	 * 
	 * @param message the message to be appended to the text area
	 */
	public void log(Object message)
	{
		textArea.append(message + "\n");
	}	
}
