import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Client extends JFrame {
	
	Socket socket;
	
	BufferedReader br;
	PrintWriter out;
	
	private JLabel heading=new JLabel("Client Area");
	private JTextArea messageArea=new JTextArea();
	private JTextField messageInput=new JTextField();
	private Font font=new Font("Roboto", Font.PLAIN, 20);
	
	

	public Client() {
		try {
			System.out.println("Sending request to server");
			socket=new Socket("192.168.119.131",7777);
			System.out.println("Connection done");
			
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream());
			
			createGUI();
			handleEvents();
			startReading();
//			startWriting();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void handleEvents() {
		
		messageInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode()==10) {
					String contenttoSend=messageInput.getText();
					messageArea.append("Me : "+contenttoSend+"\n");
					out.println(contenttoSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
				
			}
		});
		
	}


	private void createGUI() {
		 	
		this.setTitle("Client Message[END]");
		this.setSize(600,600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//coding for component
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		
		
		heading.setIcon(new ImageIcon("F:\\smallic.jpg"));
		//heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalAlignment(SwingConstants.BOTTOM);

		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		messageArea.setEditable(false);
		//frame ka layout set krenge
		
		this.setLayout(new BorderLayout());
		
		//ading components to the frame
		
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane=new JScrollPane(messageArea);
		this.add(jScrollPane,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		messageArea.setCaretPosition(messageArea.getDocument().getLength());
   
		
		
	}
	
	
	private void startReading() {
		Runnable r1=()->{
			System.out.println("reader started....");

			try {
			while(true) {
				String msg=br.readLine();
				if(msg.equals("exit")) {
					System.out.println("Server has terminated the chat....");
					JOptionPane.showMessageDialog(this, "Server Terminated the chat");
					socket.close();
					messageInput.setEnabled(false);
					break;
				}
				//System.out.println("Server : "+msg);
				messageArea.append("Server : "+msg+"\n");
				}
				
			}
			catch(Exception e) {
				//e.printStackTrace();
				System.out.println("Connection is closed");
			}
		};
		new Thread(r1).start();
		
	}




	private void startWriting() {
		Runnable r2=()->{
			System.out.println("Writer started....");

			try {
			while(!socket.isClosed()) {
					BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
					String content=br1.readLine();
					out.println(content);
					out.flush();
					if(content.equals("exit")) {
						socket.close();
						break;
					}
				}
			System.out.println("Connection is closed");
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		};
		new Thread(r2).start();
		
	}

	public static void main(String[] args) {
		System.out.println("This is client");
		new Client();

	}

}
