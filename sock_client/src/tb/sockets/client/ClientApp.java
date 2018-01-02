package tb.sockets.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ClientApp extends JFrame implements ActionListener {
    private static final long serialVersionUID = -7165003859696110622L;

    private String ipSerwera = "localhost";

    private JPanel contentPane;
    private JPanel opponentsBattlefield = new JPanel(); // plansza przeciwnika - ta po lewej - ta którą odgaduję
    private JButton[][] opponentsBattleFieldCells = new JButton[10][10]; // grid buttonów 10x10 na planszy przeciwnika
    private JPanel myBattlefield = new JPanel(); // plansza moja - po prawej
    private JButton[][] myBattleFieldCells = new JButton[10][10]; // grid buttonów 10x10 na mojej planszy
    private final JButton btnNieodkryte = new JButton("");
    private final JButton btnPudlo = new JButton("");
    private final JButton btnTrafiony = new JButton("");
    private final JButton btnZatopiony = new JButton("");
    private final JLabel lblNieodkryte = new JLabel("nieodkryte");
    private final JLabel lblPudlo = new JLabel("pud\u0142o");
    private final JLabel lblTrafiony = new JLabel("trafiony");
    private final JLabel lblZatopiony = new JLabel("zatopiony");
    private final JLabel lblPlanszaPrzeciwnika = new JLabel("PLANSZA PRZECIWNIKA");
    private final JLabel lblPlanszaTwoja = new JLabel("PLANSZA TWOJA");
    private final JButton btnStatek = new JButton("");
    private final JButton btnMorze = new JButton("");
    private final JLabel lblStatek = new JLabel("statek");
    private final JLabel lblMorze = new JLabel("morze");
    private Socket socket;
    private DataInputStream in;
    private BufferedReader is;
    private DataOutputStream os;
    private String myBoard = "";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientApp frame = new ClientApp();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ClientApp() {
        try {
            connectToServer();
        } catch (IOException e) {
            System.out.println("Unable to connect to game server :( ");
            e.printStackTrace();
        }
        drawGUI();
    }

    private void connectToServer() throws UnknownHostException, IOException {
        socket = new Socket(ipSerwera, 6666);
        in = new DataInputStream(socket.getInputStream());
        is = new BufferedReader(new InputStreamReader(in));
        os = new DataOutputStream(socket.getOutputStream());
        myBoard = is.readLine();
        System.out.println(myBoard);
    }

    private void drawGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(930, 600);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setResizable(false);

        opponentsBattlefield.setBounds(20, 80, 400, 400);
        opponentsBattlefield.setLayout(new GridLayout(10, 10));
        contentPane.add(opponentsBattlefield);
        setupOpponentsBattlefield();

        myBattlefield.setBounds(500, 80, 400, 400);
        myBattlefield.setLayout(new GridLayout(10, 10));
        contentPane.add(myBattlefield);
        setupMyBattlefield();
        btnNieodkryte.setEnabled(false);

        btnNieodkryte.setBackground(Color.LIGHT_GRAY);
        btnNieodkryte.setHorizontalAlignment(SwingConstants.LEFT);
        btnNieodkryte.setBounds(20, 490, 30, 30);
        contentPane.add(btnNieodkryte);

        btnPudlo.setHorizontalAlignment(SwingConstants.LEFT);
        btnPudlo.setEnabled(false);
        btnPudlo.setBackground(Color.WHITE);
        btnPudlo.setBounds(20, 530, 30, 30);
        contentPane.add(btnPudlo);
        btnTrafiony.setEnabled(false);

        btnTrafiony.setHorizontalAlignment(SwingConstants.LEFT);
        btnTrafiony.setBackground(Color.ORANGE);
        btnTrafiony.setBounds(160, 490, 30, 30);
        contentPane.add(btnTrafiony);
        btnZatopiony.setEnabled(false);

        btnZatopiony.setHorizontalAlignment(SwingConstants.LEFT);
        btnZatopiony.setBackground(Color.RED);
        btnZatopiony.setBounds(161, 530, 30, 30);
        contentPane.add(btnZatopiony);

        lblNieodkryte.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
        lblNieodkryte.setBounds(55, 497, 68, 14);
        contentPane.add(lblNieodkryte);

        lblPudlo.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
        lblPudlo.setBounds(55, 538, 53, 14);
        contentPane.add(lblPudlo);

        lblTrafiony.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
        lblTrafiony.setBounds(195, 497, 53, 14);
        contentPane.add(lblTrafiony);

        lblZatopiony.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
        lblZatopiony.setBounds(195, 538, 60, 14);
        contentPane.add(lblZatopiony);

        lblPlanszaPrzeciwnika.setFont(new Font("Trebuchet MS", Font.BOLD, 36));
        lblPlanszaPrzeciwnika.setHorizontalAlignment(SwingConstants.CENTER);
        lblPlanszaPrzeciwnika.setBounds(20, 11, 400, 58);
        contentPane.add(lblPlanszaPrzeciwnika);

        lblPlanszaTwoja.setHorizontalAlignment(SwingConstants.CENTER);
        lblPlanszaTwoja.setFont(new Font("Trebuchet MS", Font.BOLD, 36));
        lblPlanszaTwoja.setBounds(500, 11, 400, 58);
        contentPane.add(lblPlanszaTwoja);

        btnStatek.setHorizontalAlignment(SwingConstants.LEFT);
        btnStatek.setEnabled(false);
        btnStatek.setBackground(new Color(105, 105, 105));
        btnStatek.setBounds(280, 490, 30, 30);
        contentPane.add(btnStatek);

        btnMorze.setHorizontalAlignment(SwingConstants.LEFT);
        btnMorze.setEnabled(false);
        btnMorze.setBackground(new Color(30, 144, 255));
        btnMorze.setBounds(280, 530, 30, 30);
        contentPane.add(btnMorze);

        lblStatek.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
        lblStatek.setBounds(315, 497, 53, 14);
        contentPane.add(lblStatek);

        lblMorze.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
        lblMorze.setBounds(315, 538, 53, 14);
        contentPane.add(lblMorze);
    }

    private void setupOpponentsBattlefield() {
        for (int i = 0; i < opponentsBattleFieldCells.length; i++) {
            for (int j = 0; j < opponentsBattleFieldCells[0].length; j++) {
                JButton tmp = new JButton();
                tmp.addActionListener(this);
                tmp.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                tmp.setBackground(Color.LIGHT_GRAY);
                opponentsBattleFieldCells[i][j] = tmp;
                opponentsBattlefield.add(opponentsBattleFieldCells[i][j]);
            }
        }
    }

    private void setupMyBattlefield() {
        for (int i = 0; i < myBattleFieldCells.length; i++) {
            for (int j = 0; j < myBattleFieldCells[0].length; j++) {
                JButton tmp = new JButton();
                //tmp.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                tmp.setBorderPainted(false);
                if (myBoard.charAt(i * myBattleFieldCells.length + j) == '1') {
                    tmp.setBackground(new Color(150, 150, 150));
                } else {
                    tmp.setBackground(new Color(158, 226, 226));
                }
                tmp.setEnabled(false);
                myBattleFieldCells[i][j] = tmp;
                myBattlefield.add(myBattleFieldCells[i][j]);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object src = (JButton) e.getSource();
        for (int i = 0; i < opponentsBattleFieldCells.length; i++) {
            for (int j = 0; j < opponentsBattleFieldCells[0].length; j++) {
                if (src == opponentsBattleFieldCells[i][j]) {
                    opponentsBattleFieldCells[i][j].setBackground(new Color(0, 0, 0));
                    opponentsBattleFieldCells[i][j].setEnabled(false);
                    // opponentsBattleFieldCells[i][j].setText("X");

                    String coordinates = i + "" + i +"\n";
                    try {
                        os.writeBytes(coordinates);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    String responseLine;
                    try {
                        responseLine = is.readLine();
                        System.out.println(responseLine);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }
    }
}
