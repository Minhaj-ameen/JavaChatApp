package com.javachatapp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



public class ClientSocket extends JFrame{
	
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
	//Declaring components
	private JLabel heading = new JLabel("Client Messenger");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto", Font.PLAIN,20);
	
	public ClientSocket() {
		try {
		System.out.println("request sending to server");
		socket = new Socket("127.0.0.1",7777);
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out= new PrintWriter(socket.getOutputStream());
		createGUI();
		handleEvent();
		startReader();
//		startWriter();
		
		}
		catch(Exception e) {
			System.out.println("All connections are closed");
		}
		
	}
	
	private void handleEvent() {
		
		messageInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==10) {
//					System.out.println("You have pressed enter");
					String sendMsgToServer = messageInput.getText();
					messageArea.append("Me: " + sendMsgToServer + "\n");
					out.println(sendMsgToServer);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
					
				}
			}
			
		});
	}
	
	public void createGUI() {
		this.setTitle("Client ChatBox");
		this.setSize(500,600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,15,20));
		messageArea.setEditable(false);
		//setting border
		this.setLayout(new BorderLayout());
		
		//adding componets to border
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jscrollpane = new JScrollPane(messageArea);
		this.add(jscrollpane,BorderLayout.CENTER);
		this.add(messageInput, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public void startReader() {
		Runnable r1 =()->{
			
				try {
					System.out.println("Client reader Started....");
					while(!socket.isClosed()) {
						String msg=br.readLine();
						if(msg.equals("exit")) {
//							System.out.println("SERVER ENDED THE CHAT!");
							JOptionPane.showMessageDialog(this, "SERVER ENDED THE CHAT!");
							messageInput.setEnabled(false);
							socket.close();
							break;
						}
//						System.out.println("Server: " + msg);
						messageArea.append("Server: " + msg + "\n");
					}
				}
				catch(Exception e) {
					
				}
			
		};
		new Thread(r1).start();
	}
	
	public void startWriter() {
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Client Writing started....");
		Runnable r2 = ()->{				
					try {
						while(!socket.isClosed()) {
							
							String sendMsg = br1.readLine();
							out.println(sendMsg);
							out.flush();
							if(sendMsg.equals("exit")) {
								System.out.println("YOU ENDED THE CHAT!");
								socket.close();
								break;
							}
						}
					}catch(Exception e) {
						
					}
		};
		
		new Thread(r2).start();
	}
	

	public static void main(String[] args) throws Exception {
		System.out.println("Client Socket stated....");
		new ClientSocket();
		
	}

}

