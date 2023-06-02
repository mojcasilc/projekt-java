import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Razlicica Krizci & Krozci za enega igralca in umetno inteligenco

@SuppressWarnings("serial")
public class PlayerVsAi extends JFrame {
   // Poimenovane konstante za igralno ploščo
   public static final int VRSTICE = 3; 
   public static final int STOLPCI = 3;
 
   //Dimenzije za risanje grafike
   public static final int VELIKOST_CELICE = 150; //Širina in višina celice v obliki kvadrata
   public static final int SIRINA_PLATNA = VELIKOST_CELICE * STOLPCI;  //Platno
   public static final int VISINA_PLATNA = VELIKOST_CELICE * VRSTICE;  //Platno
   public static final int SIRINA_MREZE = 8; //Širina vmesnih črt
   public static final int POLOVICNA_SIRINA_MREZE = SIRINA_MREZE / 2; //Polovična širina mrežnih črt
   //Križec in krožec sta prikazana znotraj celice
   public static final int OBLOGA_CELICE = VELIKOST_CELICE / 6;
   public static final int VELIKOST_SIMBOLA = VELIKOST_CELICE - OBLOGA_CELICE * 2;  //Velikost znaka
   public static final int SIRINA_OBROBE_SIMBOLA = 8; //Širina znaka
 
   //Možna stanja igre
   public enum StanjeIgre {
      IGRANJE, NEODLOCENO, ZMAGA_KRIZEC, ZMAGA_KROZEC
   }
   private StanjeIgre trenutnoStanje;  //Trenutno stanje igre
 
   //Stanje celice
   public enum Celica {
      PRAZNO, KRIZEC, KROZEC
   }
   private Celica trenutniIgralec;  //Trenutni igralec
 
   private Celica[][] plosca   ; //Igralna plošča s VRSTICE x STOLPCI celicami
   private NarisiPlatno platno; //Platno za igralno ploščo
   private JLabel vrsticaStanja;  //Vrstica stanja
 
   //Konstruktor za nastavitev igre in komponent
   public PlayerVsAi() {
      platno = new NarisiPlatno(); //Nariše igralno platno
      platno.setPreferredSize(new Dimension(SIRINA_PLATNA, VISINA_PLATNA));//Nastavitev velikosti platna
      
      //Dogodek ob pritisku na miško
      platno.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {  // rokovanje s klikom miške
			int naslednjiX = 0;
			int naslednjiY = 0;
            int miskaX = e.getX();
            int miskaY = e.getY();
            //Pridobi vrstico in stoplec klika
            int VRSTICEIzbrane = miskaY / VELIKOST_CELICE;
            int STOLPCIIzbrani = miskaX / VELIKOST_CELICE;
 
            if (trenutnoStanje == StanjeIgre.IGRANJE) {
               if (VRSTICEIzbrane >= 0 && VRSTICEIzbrane < VRSTICE && STOLPCIIzbrani >= 0
                     && STOLPCIIzbrani < STOLPCI && plosca[VRSTICEIzbrane][STOLPCIIzbrani] == Celica.PRAZNO) {
                  plosca[VRSTICEIzbrane][STOLPCIIzbrani] = trenutniIgralec; //Naredi potezo
                  posodobiIgro(trenutniIgralec, VRSTICEIzbrane, STOLPCIIzbrani); //Posodobitev stanja
                  //Menjava igralcev
                  trenutniIgralec = (trenutniIgralec == Celica.KRIZEC) ? Celica.KROZEC : Celica.KRIZEC;
               }
			   int korak = 0;
            //Nastavitev AI poteze
			   while(trenutniIgralec == Celica.KROZEC){
				   naslednjiX = (int)(Math.random() * 300 + 1);
				   naslednjiY = (int)(Math.random() * 300 + 1);
				   
				   VRSTICEIzbrane = naslednjiX / VELIKOST_CELICE;
				   STOLPCIIzbrani = naslednjiY / VELIKOST_CELICE;
				   
				   if (VRSTICEIzbrane >= 0 && VRSTICEIzbrane < VRSTICE && STOLPCIIzbrani >= 0
                     && STOLPCIIzbrani < STOLPCI && plosca[VRSTICEIzbrane][STOLPCIIzbrani] == Celica.PRAZNO) {
                  plosca[VRSTICEIzbrane][STOLPCIIzbrani] = trenutniIgralec; //Naredi potezo
                  posodobiIgro(trenutniIgralec, VRSTICEIzbrane, STOLPCIIzbrani); //Posodobitev stanja
                  //Menjava igralcev
                  trenutniIgralec = Celica.KRIZEC;
					 }
				   korak = korak + 1;	 
				   if(korak > 1000){
						break;
				   }
			   }   
            } else { //Ko je igre konec
               pricniIgro(); //Resetiranje igre
            }
            //Osvežitev igralnega platna
            repaint();  //Ponoven klic paintComponent()
         }
      });
 
      //Nastavi vrstico stanja za prikaz stanja
      vrsticaStanja = new JLabel("  ");
      vrsticaStanja.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
      vrsticaStanja.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
 
      Container cp = getContentPane();
      cp.setLayout(new BorderLayout());
      cp.add(platno, BorderLayout.CENTER);
      cp.add(vrsticaStanja, BorderLayout.PAGE_END); //Na spodnjem delu okna
 
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack(); //Zapakira vse komponente v ta JFrame
      setTitle("Križci in krožci");
      setVisible(true);  //Prikaz
      setLocationRelativeTo(null);
 
      plosca = new Celica[VRSTICE][STOLPCI]; //Število vrstic in stolpcev
      pricniIgro(); 
   }
 
   //Inicializira igralno ploščo ter njeno vsebino in igralne spremenljivke
   public void pricniIgro() {
      for (int vrstica = 0; vrstica < VRSTICE; ++vrstica) {
         for (int stolpec = 0; stolpec < STOLPCI; ++stolpec) {
            plosca[vrstica][stolpec] = Celica.PRAZNO; //Vse celice nastavi na prazne
         }
      }
      trenutnoStanje = StanjeIgre.IGRANJE; //Priprava na začetek igre
      trenutniIgralec = Celica.KRIZEC; //Z igro začne križec
   }
 
   //Posodobi trenutno stanje celice glede na igralca
   public void posodobiIgro(Celica taCelica, int VRSTICEIzbrane, int STOLPCIIzbrani) {
      if (jeZmagal(taCelica, VRSTICEIzbrane, STOLPCIIzbrani)) {  //Preveri za zmago
         trenutnoStanje = (taCelica == Celica.KRIZEC) ? StanjeIgre.ZMAGA_KRIZEC : StanjeIgre.ZMAGA_KROZEC;
      } else if (jeNeodloceno()) {  //Preveri za neodločen rezultat
         trenutnoStanje = StanjeIgre.NEODLOCENO;
      }
      //Če ni nič od tega, se stanje igre ne spremeni
   }
 
   //Vrne true v primeru neodločenega rezultata
   public boolean jeNeodloceno() {
      for (int vrstica = 0; vrstica < VRSTICE; ++vrstica) {
         for (int stolpec = 0; stolpec < STOLPCI; ++stolpec) {
            if (plosca[vrstica][stolpec] == Celica.PRAZNO) {
               return false; //Če najde še prazno celico
            }
         }
      }
      return true;  //Če ne najde nobene prazne celice, vrne neodločeno
   }
 
   //Vrne true ob primeru da je igralec zmagal
   public boolean jeZmagal(Celica taCelica, int VRSTICEIzbrane, int STOLPCIIzbrani) {
      return (plosca[VRSTICEIzbrane][0] == taCelica  //Če ima 3 v vrsti
            && plosca[VRSTICEIzbrane][1] == taCelica
            && plosca[VRSTICEIzbrane][2] == taCelica
       || plosca[0][STOLPCIIzbrani] == taCelica //Če ima 3 v stolpcu
            && plosca[1][STOLPCIIzbrani] == taCelica
            && plosca[2][STOLPCIIzbrani] == taCelica
       || VRSTICEIzbrane == STOLPCIIzbrani  //Če ima 3 po diagonali
            && plosca[0][0] == taCelica
            && plosca[1][1] == taCelica
            && plosca[2][2] == taCelica
       || VRSTICEIzbrane + STOLPCIIzbrani == 2  //Če ima 3 po desni diagonali
            && plosca[0][2] == taCelica
            && plosca[1][1] == taCelica
            && plosca[2][0] == taCelica);
   }
 
   //Za risanje platna
   class NarisiPlatno extends JPanel {
      @Override
      public void paintComponent(Graphics g) { //Klic z repaint()
         super.paintComponent(g); //Zapolnimo ozadje
         setBackground(Color.LIGHT_GRAY); //Nastavitev barve ozadja
 
         //Narišemo vmesne črte
         g.setColor(Color.BLACK);
         for (int vrstica = 1; vrstica < VRSTICE; ++vrstica) {
            g.fillRoundRect(0, VELIKOST_CELICE * vrstica - POLOVICNA_SIRINA_MREZE,
                  SIRINA_PLATNA - 1, SIRINA_MREZE, SIRINA_MREZE, SIRINA_MREZE);
         }
         for (int stolpec = 1; stolpec < STOLPCI; ++stolpec) {
            g.fillRoundRect(VELIKOST_CELICE * stolpec - POLOVICNA_SIRINA_MREZE, 0,
                  SIRINA_MREZE, VISINA_PLATNA - 1, SIRINA_MREZE, SIRINA_MREZE);
         }
 
         //Uporaba 2D grafike za obrobo
         Graphics2D g2d = (Graphics2D)g;
         g2d.setStroke(new BasicStroke(SIRINA_OBROBE_SIMBOLA, BasicStroke.CAP_ROUND,
               BasicStroke.JOIN_ROUND)); 
         for (int vrstica = 0; vrstica < VRSTICE; ++vrstica) {
            for (int stolpec = 0; stolpec < STOLPCI; ++stolpec) {
               int x1 = stolpec * VELIKOST_CELICE + OBLOGA_CELICE;
               int y1 = vrstica * VELIKOST_CELICE + OBLOGA_CELICE;
               if (plosca[vrstica][stolpec] == Celica.KRIZEC) {
                  g2d.setColor(new Color(255,20,147));
                  int x2 = (stolpec + 1) * VELIKOST_CELICE - OBLOGA_CELICE;
                  int y2 = (vrstica + 1) * VELIKOST_CELICE - OBLOGA_CELICE;
                  g2d.drawLine(x1, y1, x2, y2);
                  g2d.drawLine(x2, y1, x1, y2);
               } else if (plosca[vrstica][stolpec] == Celica.KROZEC) {
                  g2d.setColor(Color.green);
                  g2d.drawOval(x1, y1, VELIKOST_SIMBOLA, VELIKOST_SIMBOLA);
               }
            }
         }
 
         //Izpis sporočila v vrstici stanja
         if (trenutnoStanje == StanjeIgre.IGRANJE) {
            vrsticaStanja.setForeground(new Color(138,43,226));
            if (trenutniIgralec == Celica.KRIZEC) {
               vrsticaStanja.setText("Na vrsti je križec!");
            } else {
               vrsticaStanja.setText("Na vrsti je krožec!");
            }
         } else if (trenutnoStanje == StanjeIgre.NEODLOCENO) {
            vrsticaStanja.setForeground(new Color(138,43,226));
            vrsticaStanja.setText("Neodloceno! Kliknite za ponovno igro.");
         } else if (trenutnoStanje == StanjeIgre.ZMAGA_KRIZEC) {
            vrsticaStanja.setForeground(new Color(138,43,226));
            vrsticaStanja.setText("Krizec je zmagal! Kliknite za ponovno igro.");
         } else if (trenutnoStanje == StanjeIgre.ZMAGA_KROZEC) {
            vrsticaStanja.setForeground(new Color(138,43,226));
            vrsticaStanja.setText("Krozec je zmagal! Kliknite za ponovno igro.");
         }
      }

      public void setLocationRelativeTo(Object object) {
      }
   }
 
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new PlayerVsAi(); 
         }
      });
   }
}