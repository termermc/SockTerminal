package net.termer.sockterminal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ConnectionWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JButton close = new JButton("Close Connection");
	private static JLabel ip = new JLabel("");
	
	public static JCheckBox newline = new JCheckBox();
	
	private static ConnectionWindow cw = null;
	
	private ConnectionWindow() {
		super("Connection");
		setAutoRequestFocus(true);
		setAlwaysOnTop(true);
		setType(Type.UTILITY);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(200,100);
		setResizable(false);
		
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SockTerminal.connection.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		getContentPane().add(ip, "North");
		getContentPane().add(new JLabel("Enable auto-newline"), "Center");
		getContentPane().add(newline, "East");
		getContentPane().add(close, "South");
	}
	
	public static void display(boolean d) {
		if(d) {
			if(cw==null) {
				cw=new ConnectionWindow();
			}
			ip.setText("IP: "+SockTerminal.connection.getInetAddress().getHostAddress()+" PORT: "+Integer.toString(SockTerminal.connection.getPort()));
			cw.setVisible(true);
		} else {
			if(cw!=null) {
				cw.setVisible(false);
			}
		}
	}
}
