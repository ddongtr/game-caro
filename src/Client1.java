import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Client1 extends JFrame implements MouseListener,Runnable{

	public static void main(String[] args) throws IOException {
		JFrame startscreen = new JFrame("Co caro 1");
		JButton start = new JButton("Start game!");
		
		startscreen.getContentPane().add(start);
		
		startscreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startscreen.setVisible(true);
		startscreen.setSize(550,550);
		startscreen.setResizable(false);
		start.addActionListener(listener -> {
			startscreen.setVisible(false);
			new Client1();
		});
	}
	
	String noti;
	int n=15,s=30,m=50;		//n la so hang/cot
	Vector<Point> dadanh = new Vector<>();
	Socket soc;
	
	public void showresult(String s, Vector<Point> d) {
		
		JFrame gameover = new JFrame("Ca caro - Player 1 - Tro choi ket thuc");
		gameover.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameover.setVisible(true);
		gameover.toFront();
		gameover.setSize(550,550);
		gameover.setFocusable(true);
		gameover.setResizable(false);
		gameover.setLayout(new FlowLayout());
		
		JPanel pn = new JPanel(new GridLayout(2,1));
		JPanel pn1 = new JPanel(new FlowLayout());
		JLabel result = new JLabel("", SwingConstants.CENTER);
		result.setFont(new Font("arial", Font.BOLD, 30));
		result.setText(s);
		pn1.add(result);
		pn.add(pn1);
		
		JPanel pn2 = new JPanel(new FlowLayout());
		JButton restart = new JButton("Restart");
		restart.addActionListener(listener -> {
			gameover.setVisible(false);
			d.removeAllElements();
			this.setVisible(true);
			this.repaint();
		});
		pn2.add(restart);
		pn.add(pn2);
		
		gameover.add(pn);
		this.repaint();
	}
	
	public Client1() {
		this.setTitle("Co Caro - Player 1");
		this.setSize(m*2+n*s,m*2+n*s);
		this.setDefaultCloseOperation(3);
		this.setResizable(false);
		this.addMouseListener(this);
		
		try {
			soc = new Socket("localhost",5000);
		} catch (Exception e) {
		}
		
		new Thread(this).start();
		this.setVisible(true);
		}
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);
		for (int i=0;i<=n;i++) {
			g.drawLine(m, m + i*s, m+n*s, m + i*s);
			g.drawLine(m + i*s, m, m + i*s,  m+n*s);
		}
		
		g.setFont(new Font("arial", Font.BOLD, s));
		for (int i=0;i<dadanh.size();i++) {
			String st ="x";
			if (i%2!=0) st = "o";
			
			int x = m+ dadanh.get(i).x*s + s/5;
			int y = m+ (dadanh.get(i).y+1)*s - s/5;
			
			g.drawString(st, x, y);
		}
		
	}

	@Override
	public void run() {
		while(true) {
			try {
				ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
				data data = (data)ois.readObject();
				dadanh.add(new Point(data.ix,data.iy));
				this.repaint();
				
				if (data.result != null) {
					this.setVisible(false);
					showresult(data.result, dadanh);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
//		System.out.println(x+","+y);
		if (x<m || x>=m+s*n) return;
		if (y<m || y>=m+s*n) return;
		
		int ix = (x-m)/s;
		int iy = (y-m)/s;
		
		//Gui toa do o
		try {
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			dos.writeUTF(ix+"");
			dos.writeUTF(iy+"");
		}catch (Exception e1) {
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}