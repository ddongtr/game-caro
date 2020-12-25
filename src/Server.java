import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

public class Server {

	public static void main(String[] args) throws IOException{
		new Server();
	}
	
	Socket s1, s2;
	int n=15;
	Vector<Point> dadanh = new Vector<>();
	String arr[][]= new String[n][n];
	int winnerID;
	
 	public Server(){
		try {
			ServerSocket server = new ServerSocket(5000);
			
			s1 = server.accept();
			new Xuly(this,s1).start();
			
			s2 = server.accept();
			new Xuly(this,s2).start();
			
		}	catch (Exception e) {
		}
	}
}

class Xuly extends Thread{
	Server server;
	Socket s;
	
	public Xuly(Server server, Socket s) {
		this.server = server;
		this.s = s;
	}
	
	public void Checkwin(Socket s, int ix, int iy) {
		int count = 0;
		
		//check row
		for (int i= 0; i < server.n; i++) {
			if (s == server.s1) {
				if (server.arr[i][iy] == "X") {
					count++;
					if (count == 5) {
						sendRessult(s, ix, iy);
						server.winnerID = 1;
					}
				}
				else count = 0;
			} else {
				if (server.arr[i][iy] == "O") {
					count++;
					if (count == 5) {
						sendRessult(s, ix, iy);
						server.winnerID = 2;
					}
				}
				else count = 0;
			}
		}
		
		//check column
		for (int i= 0; i < server.n; i++) {
			if (s == server.s1) {
				if (server.arr[ix][i] == "X") {
					count++;
					if (count == 5) {
						sendRessult(s, ix, iy);
						server.winnerID = 1;
					}
				}
				else count = 0;
			} else {
				if (server.arr[ix][i] == "O") {
					count++;
					if (count == 5) {
						sendRessult(s, ix, iy);
						server.winnerID = 2;
					}
				}
				else count = 0;
			}
		}
		
		//check inline 1
		int k1 = ix; int h1 = iy; int cl1 = 0;
		if (s == server.s1) {
			while (server.arr[k1][h1] == "X") {
				cl1++;
				k1++;
				h1++;
			}
			k1 = ix - 1; h1 = iy-1;
			while (server.arr[k1][h1] == "X") {
				cl1++;
				k1--;
				h1--;
			}
			if (cl1 == 5) {
				sendRessult(s, ix, iy);
				server.winnerID = 1;
			}
		} else {
			while (server.arr[k1][h1] == "O") {
				cl1++;
				k1++;
				h1++;
			}
			k1 = ix - 1; h1 = iy-1;
			while (server.arr[k1][h1] == "O") {
				cl1++;
				k1--;
				h1--;
			}
			if (cl1 == 5) {
				sendRessult(s, ix, iy);
				server.winnerID = 2;
			}
		}
		
		//check inline 2
		int cl2 = 0; int k2 = ix; int h2 = iy;
		if (s == server.s1) {
			while (server.arr[k2][h2] == "X") {
				cl2++;
				k2++;
				h2--;
			}
			k2 = ix - 1; h2 = iy + 1;
			while (server.arr[k2][h2] == "X") {
				cl2++;
				k2--;
				h2++;
			}
			if (cl2 == 5) {
				sendRessult(s, ix, iy);
				server.winnerID = 1;
			}
		} else {
			while (server.arr[k2][h2] == "O") {
				cl2++;
				k2++;
				h2--;
			}
			k2 = ix - 1; h2 = iy + 1;
			while (server.arr[k2][h2] == "O") {
				cl2++;
				k2--;
				h2++;
			}
			if (cl2 == 5) {
				sendRessult(s, ix, iy);
				server.winnerID = 2;
			}
		}
	}
	
	public void remake(Vector<Point> d, String[][] a) {
		d.removeAllElements();
		for (int k = 0; k < server.n; k ++) {
			for (int l = 0; l < server.n; l ++) {
				a[k][l] = null;
			}
		}
	}
	
	public void sendRessult(Socket s, int ix, int iy) {
		String result1 = "<html><center>YOU WIN!<center><br/></html>";
		String result2 = "<html><center>YOU LOSE!<center><br/></html>";
		
		data data1 = new data();
		data1.ix = ix;
		data1.iy = iy;
		data1.result = result1;
		
		data data2 = new data();
		data2.ix = ix; 
		data2.iy = iy;
		data2.result = result2;
		
		try {
			ObjectOutputStream oos1 = new ObjectOutputStream(server.s1.getOutputStream());
			ObjectOutputStream oos2 = new ObjectOutputStream(server.s2.getOutputStream());
 
			if (s == server.s1) {
				oos1.writeObject((Object)data1);
				oos1.flush(); 
				oos2.writeObject((Object)data2);
				oos2.flush();
			} else {
				oos1.writeObject((Object)data2);
				oos1.flush(); 
				oos2.writeObject((Object)data1);
				oos2.flush();
			}
			} catch (Exception e) {
			
		}
		remake(server.dadanh, server.arr);
	}	
	

	public void run() {
		loop: while(true) {
			try {
				DataInputStream dis = new DataInputStream(s.getInputStream());	
				int ix = Integer.parseInt(dis.readUTF());
				int iy = Integer.parseInt(dis.readUTF());
				System.out.println(ix+","+iy);
				
				if (server.s1==null || server.s2==null) continue;
				
				if (!((s == server.s1 && server.dadanh.size()%2==0) || 
						(s == server.s2 && server.dadanh.size()%2==1)))
							continue;
				
				//Kiem tra tinh hop le
				
				//0 trung
				for (Point p : server.dadanh) {
					if (ix == p.x && iy == p.y) continue loop;
				}
				
				
				server.dadanh.add(new Point(ix,iy));
				
				if (s == server.s1 ) server.arr[ix][iy] = "X";
				else server.arr[ix][iy] = "O";
				
				//checkwin
				Checkwin(s,ix,iy); 
								
				data data = new data();
					data.ix = ix;
					data.iy = iy;
					data.result = null;
					
				ObjectOutputStream oos1 = new ObjectOutputStream(server.s1.getOutputStream());
				oos1.writeObject((Object)data);
				oos1.flush();  
					
				ObjectOutputStream oos2 = new ObjectOutputStream(server.s2.getOutputStream());
				oos2.writeObject((Object)data);
				oos2.flush();  
				
			} catch (Exception e) {
			}
		}
	}
}