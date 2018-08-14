package net.termer.sockterminal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

public class SockTerminal {
	public static String VERSION = "0.1";
	
	public static Terminal window = null;
	public static Socket connection = null;
	
	public static void main(String[] args) {
		window = new Terminal();
	}
	
	public static void connect(String addr) {
		if(connection==null) {
			window.println("Connecting to "+addr.split(":")[0]+" at port "+addr.split(":")[1]+"...");
			ConnectionTime timer = new ConnectionTime();
			timer.start();
			try {
				connection = new Socket(InetAddress.getByName(addr.split(":")[0]),Integer.parseInt(addr.split(":")[1]));
				window.println("Connection established. Took "+Integer.toString(timer.halt())+"ms.");
				window.print("\n");
				ConnectionWindow.display(true);
				try {
					while(!connection.isClosed()) {
						window.append((char)connection.getInputStream().read());
					}
					connection=null;
					window.print("\n");
					window.println("Connection closed.");
				} catch(IOException e) {
					e.printStackTrace();
					window.println("Connection error: "+e.getMessage());
					window.print("Terminating connection...");
					try {
						connection.close();
						connection = null;
					} catch (IOException ex) {
						ex.printStackTrace();
						connection=null;
					}
					window.println("Terminated");
				}
			} catch (IOException e) {
				e.printStackTrace();
				window.println("Could not establish connection: "+e.getMessage());
				window.print("Terminating connection...");
				try {
					connection.close();
					connection = null;
				} catch (Exception ex) {
					ex.printStackTrace();
					connection=null;
				}
				window.println("Terminated");
			} catch (NumberFormatException nfe) {
				window.println("Invalid address or port!");
				window.print("Terminating connection...");
				try {
					connection.close();
					connection = null;
				} catch (IOException e) {
					e.printStackTrace();
					connection=null;
				}
				window.println("Terminated");
			}
		} else {
			JOptionPane.showMessageDialog(window, "To make a new connection, the current connection must be terminated", "Active Connection", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static class ConnectionTime extends Thread {
		private int ms = 0;
		private boolean r = true;
		
		public void run() {
			try {
				while(r) {
					sleep(1);
					ms++;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public int halt() {
			r=false;
			return ms;
		}
	}
}
