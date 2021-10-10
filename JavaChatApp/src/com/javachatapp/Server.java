package com.javachatapp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Server extends JFrame{
	
	ServerSocket server;
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
	private JLabel heading = new JLabel("Server Messenger");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto", Font.PLAIN,20);
	
	//constructor
	public Server() {
		try {
		server = new ServerSocket(7777);
		System.out.println("Server is waiting to accecpt connection....");
		System.out.println("Waiting.....");
		socket = server.accept();
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
		
		createGUI();
		handleEvent();
		startReading();
//		startWriting();
		}
		catch(Exception e) {
			System.out.println("All serverside connection closed...");
		}
		
	}
	
	public void handleEvent() {
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
				if(e.getKeyCode()==10) {
//					System.out.println("You have pressed enter");
					String sendMsgToClient = messageInput.getText();
					messageArea.append("Me: " + sendMsgToClient + "\n");
					out.println(sendMsgToClient);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				
				}
			
			}
		});
	}
	
	public void createGUI() {
		this.setTitle("Server ChatBox");
		this.setSize(500,600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,15,20));
//		messageInput.setBorder(BorderFactory.createEmptyBorder(10,20,15,20));
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
	
	
	public void startReading() {
		//reading the data 
		Runnable r1 = ()->{
			System.out.println("Reading stated...");
			try {
				String msg;
				while(!socket.isClosed()) {
					msg = br.readLine();
					if(msg.equals("exit")) {
						System.out.println("CLIENT ENDED THE CHAT!");
						JOptionPane.showMessageDialog(this, "CLIENT ENDED THE CHAT!");
						messageInput.setEnabled(false);
						socket.close();
						break;
					}
//					System.out.println("Client: " + msg);
					messageArea.append("CLIENT: " + msg + "\n");
				}
			} catch (IOException e) {
			
			}
		};
			new Thread(r1).start();
	}
	
	public void startWriting() {
		//sending data to client
		Runnable r2 = ()->{
			
				System.out.println("Server writing started...");
				try {
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					while(!socket.isClosed()) {
							
							String content = br1.readLine();
							out.println(content);
							out.flush();
						if(content.equals("exit")) {
							System.out.println("YOU ENDED THE CHAT!");
							socket.close();
							break;
						}
					}
				}	
				catch (Exception e) {
					
				}
			
		};
		new Thread(r2).start();
		
	}
	

	public static void main(String[] args) throws Exception {
		System.out.println("Server is started......");
		new Server();
		
	}

}

