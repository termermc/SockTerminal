package net.termer.sockterminal;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Terminal extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextArea t = new JTextArea(12, 40);
	private JButton conn = new JButton("Make Connection");
	private JTextField input = new JTextField(40);
	private String lastConnection = "";
	
	public Terminal() {
		// Setup Window
		super("SockTerminal v"+SockTerminal.VERSION);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		setSize(500,400);
		
		conn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Connection().start();
			}
		});
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String processed = input.getText().replace("\\\\", "\\").replace("\\n", "\n");
					for(char c : processed.toCharArray()) {
						SockTerminal.connection.getOutputStream().write((int) c);
					}
					if(ConnectionWindow.newline.isSelected()) {
						SockTerminal.connection.getOutputStream().write((int) '\n');
					}
					input.setText("");
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		input.setEnabled(false);
		t.setMargin(new Insets(4, 4, 4, 4));
		t.setEditable(false);
		
		getContentPane().add(conn, "North");
		getContentPane().add(new JScrollPane(t), "Center");
		getContentPane().add(input, "South");
		
		setVisible(true);
	}
	
	public void append(char c) {
		t.setText(t.getText()+c);
	}
	public void print(String s) {
		t.setText(t.getText()+s);
	}
	public void print(char[] s) {
		t.setText(t.getText()+s.toString());
	}
	public void println(String s) {
		t.setText(t.getText()+s+"\n");
	}
	
	private class Connection extends Thread {
		public void run() {
			input.setEnabled(true);
			conn.setEnabled(false);
			String addr = JOptionPane.showInputDialog("Please enter IP address and port.\ne.g. 0.0.0.0:8080", lastConnection);
			lastConnection=addr;
			SockTerminal.connect(addr);
			ConnectionWindow.display(false);
			input.setEnabled(false);
			conn.setEnabled(true);
		}
	}
}
