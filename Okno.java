import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
//import java.io.File;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JTextField;

public class Okno{

	public static final int ŠIRINA = 400; //konstanta za širino okna
	public static final int VIŠINA = ŠIRINA * 16/9; //konstanta za višino okna
	
	JFrame platno= new JFrame();
	
	JLabel oznaka1 = new JLabel();
	JLabel oznaka2 = new JLabel();
	
	JButton gumb1 = new JButton("Player vs Ai");  
	JButton gumb2 = new JButton("Player vs Player");   
	
	public Okno() {
		platno.getContentPane().setBackground(Color.LIGHT_GRAY);
		platno.setSize(ŠIRINA,VIŠINA);    
		platno.setLayout(null);    
		platno.setVisible(true);
		platno.setLocationRelativeTo(null);
		platno.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		//Lokacija gumbov in njihove lastnosti
		gumb1.setBounds((int) (ŠIRINA / 4.5), 170, 200, 80); 
		gumb1.setBackground(Color.PINK);
		gumb1.setFont(new Font("Arial", Font.PLAIN, 20));
		gumb2.setBounds((int) (ŠIRINA / 4.5), 300, 200, 80); 
		gumb2.setBackground(Color.PINK);
		gumb2.setFont(new Font("Arial", Font.PLAIN, 20));
		
		//Če izberemo prvi gumb Player vs Ai
		gumb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
					new PlayerVsAi();
			}       
	      });
		//Če izberemo drugi gumb Player vs Player
		gumb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					new PlayerVsPlayer();
			}          
		  });

		//Na okvir dodamo oznake in gumbe
		platno.add(oznaka1); 
		platno.add(oznaka2);
		platno.add(gumb1);     
		platno.add(gumb2);
		platno.repaint();
	}
	
	public static void main(String[] args) {
		new Okno();
	}
}