package net.termer.sockterminal;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Terminal extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextArea t = new JTextArea(12, 40);
	private JButton conn = new JButton("Make Connection");
	private JTextArea input = new JTextArea();
	private String lastConnection = "";
	
	public Terminal() {
		// Setup Window
		super("SockTerminal v"+SockTerminal.VERSION);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		input.setMargin(new Insets(4,4,4,4));
		setSize(500,500);
		
		conn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Connection().start();
			}
		});
		input.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					if(e.isShiftDown()) {
						String txt = input.getText();
						int pos = input.getCaretPosition();
						input.setText(txt.substring(0, pos)+"\n"+txt.substring(pos, txt.length()));
						input.setCaretPosition(pos+1);
					} else {
						try {
							String txt = input.getText();
							OutputStream out = SockTerminal.connection.getOutputStream();
							for(int i = 0; i < txt.length(); i++) {
								out.write((int)txt.charAt(i));
							}
							if(ConnectionWindow.newline.isSelected()) {
								out.write((int)'\n');
							}
							new Thread() {
								public void run() {
									try {
										Thread.sleep(50);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									input.setText("");
								}
							}.start();
						} catch (IOException ex) {
							JOptionPane.showMessageDialog(null, "Failed to send data\n"+ex.getClass().getName()+": "+ex.getMessage());
							ex.printStackTrace();
						}
					}
				}
			}
			
			public void keyReleased(KeyEvent e) {
				
			}

			public void keyTyped(KeyEvent e) {
				
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
		StringBuilder sb = new StringBuilder(t.getText());
		if(c == 65535) {
			try {
				SockTerminal.connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			t.setText(sb.append(c).toString());
		}
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