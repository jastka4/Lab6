package tb.sockets.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public class ClientApp extends JFrame implements ActionListener {
	private static final long serialVersionUID = -7165003859696110622L;

	private String ipSerwera = "localhost";

	private JPanel contentPane;
	private JPanel opponentsBattlefield = new JPanel(); // plansza przeciwnika - ta po lewej - ta kt√≥rƒÖ odgadujƒô
	private JPanel myBattlefield = new JPanel(); // plansza moja - po prawej
	private JButton[][] opponentsBattleFieldCells = new JButton[10][10]; // grid button√≥w 10x10 na planszy przeciwnika
	private JButton[][] myBattleFieldCells = new JButton[10][10]; // grid button√≥w 10x10 na mojej planszy
	private JButton btnNieodkryte = new JButton("");
	private JButton btnPudlo = new JButton("");
	private JButton btnTrafiony = new JButton("");
	private JButton btnZatopiony = new JButton("");
	private JButton btnStatek = new JButton("");
	private JButton btnMorze = new JButton("");
	private JButton btnStartGame = new JButton("Start!");
	private JLabel lblNieodkryte = new JLabel("nieodkryte");
	private JLabel lblPudlo = new JLabel("pud\u0142o");
	private JLabel lblTrafiony = new JLabel("trafiony");
	private JLabel lblZatopiony = new JLabel("zatopiony");
	private JLabel lblPlanszaPrzeciwnika = new JLabel("PLANSZA PRZECIWNIKA");
	private JLabel lblPlanszaTwoja = new JLabel("PLANSZA TWOJA");
	private JLabel lblStatek = new JLabel("statek");
	private JLabel lblMorze = new JLabel("morze");
	private JLabel labelWhosTurn = new JLabel("");

	private Socket socket;
	private DataInputStream in;
	private BufferedReader is;
	private DataOutputStream os;
	private String myBoard = "";
	private String shotCoordinates = "";
	private boolean isItMyTurn;
	private boolean doIStart;
	private String responseLine;
	private String yourTurnMsg = "Teraz Twoja kolej!";
	private String opponentsTurnMsg = "Teraz kolej przeciwnika!";

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

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int choose = JOptionPane.showConfirmDialog(e.getComponent(), "Do you really want to close the game?",
						"Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (choose == JOptionPane.YES_OPTION) {
					try {
						System.out.println("/quit");
						os.writeBytes("/quit");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.getWindow().dispose();
				}
			}
		});
	}

	private void connectToServer() throws UnknownHostException, IOException {
		socket = new Socket(ipSerwera, 6666);
		in = new DataInputStream(socket.getInputStream());
		is = new BufferedReader(new InputStreamReader(in));
		os = new DataOutputStream(socket.getOutputStream());
		myBoard = is.readLine();

		if (myBoard.charAt(0) == '0') { // first char in myBoard determines who starts game 1 - you, 0 - opponent
			doIStart = isItMyTurn = false;
			System.out.println("I go second!");
		} else {
			System.out.println("I go first!");
			doIStart = isItMyTurn = true;
		}
		myBoard = myBoard.substring(1);
	}

	private String gameRun() {
		if (isItMyTurn) {
			labelWhosTurn.setText(yourTurnMsg);
		} else {
			labelWhosTurn.setText(opponentsTurnMsg);
			return waitingForOpponentsMove();
		}
		return null;
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
				tmp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
				tmp.setBorderPainted(true);
				if (myBoard.charAt(i * myBattleFieldCells.length + j) == '1') {
					tmp.setBackground(new Color(105, 105, 105));
				} else {
					tmp.setBackground(new Color(30, 144, 255));
				}
				tmp.setEnabled(false);
				myBattleFieldCells[i][j] = tmp;
				myBattlefield.add(myBattleFieldCells[i][j]);
			}
		}
	}

	public String waitingForOpponentsMove() {
		// hang the turn, don't allow player to click and wait for server's info about
		// opponents move
		do {
			SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

				@Override
				protected String doInBackground() throws Exception {
					responseLine = is.readLine();
					return responseLine;
				}

				@Override
				protected void done() {
					processResponse(responseLine);
					System.out.println("Recieved response from server - " + responseLine);
				}
			};
			worker.execute();
		} while (responseLine.charAt(1) != 'm');

		return null;
	}

	public void actionPerformed(ActionEvent e) {
		JButton src = (JButton) e.getSource();
		for (int i = 0; i < opponentsBattleFieldCells.length; i++) {
			for (int j = 0; j < opponentsBattleFieldCells[0].length; j++) {
				if (src == opponentsBattleFieldCells[i][j]) {
					opponentsBattleFieldCells[i][j].setEnabled(false);
					shotCoordinates = i + "" + j + "\n";
					try {
						os.writeBytes(shotCoordinates);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					try {
						responseLine = is.readLine();
						processResponse(responseLine);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		gameRun();
	}

	private void processResponse(String responseLine) {
		System.out.println(isItMyTurn + ", " + responseLine);
		// if (isItMyTurn) {
		// contentPane.setEnabled(true);
		// } else {
		// contentPane.setEnabled(false);
		// }

		if (responseLine.charAt(0) == '.') {
			// game continues
			if (isItMyTurn) {
				// my turn
				updateOpponentsBattleField(responseLine);
				labelWhosTurn.setText(opponentsTurnMsg);
			} else {
				// opponents turn
				updateMyBattleField(responseLine);
				labelWhosTurn.setText(yourTurnMsg);
			}
		} else if (responseLine.charAt(0) == 'L') {
			// game is lost
			JOptionPane.showMessageDialog(this, "You've lost the game :(");
		} else if (responseLine.charAt(0) == 'W') {
			// game is won
			JOptionPane.showMessageDialog(this, "You've won the game :)");
		}
	}

	private void toggleLabelWhosTurn() {
		if (labelWhosTurn.getText().equals(yourTurnMsg)) {
			labelWhosTurn.setText(opponentsTurnMsg);
		} else {
			labelWhosTurn.setText(yourTurnMsg);
		}
	}

	private void updateOpponentsBattleField(String responseLine) {
		if (responseLine.charAt(1) == 'm') {
			int x = responseLine.charAt(2) - 48;
			int y = responseLine.charAt(3) - 48;
			opponentsBattleFieldCells[x][y].setBackground(Color.WHITE);
			isItMyTurn = !isItMyTurn;
			toggleLabelWhosTurn();
		} else if (responseLine.charAt(1) == 'h') {
			int x = responseLine.charAt(2) - 48;
			int y = responseLine.charAt(3) - 48;
			opponentsBattleFieldCells[x][y].setBackground(Color.ORANGE);
		} else if (responseLine.charAt(1) == 's') {
			int lengthOfSunkenShip = (responseLine.length() - 2) / 2;
			for (int i = 1; i <= lengthOfSunkenShip; i++) {
				int x = responseLine.charAt(i * 2) - 48;
				int y = responseLine.charAt(1 + i * 2) - 48;
				opponentsBattleFieldCells[x][y].setBackground(Color.RED);
			}
		}
	}

	private void updateMyBattleField(String responseLine) {
		if (responseLine.charAt(1) == 'm') {
			int x = responseLine.charAt(2) - 48;
			int y = responseLine.charAt(3) - 48;
			myBattleFieldCells[x][y].setBackground(Color.WHITE);
			isItMyTurn = !isItMyTurn;
			toggleLabelWhosTurn();
		} else if (responseLine.charAt(1) == 'h') {
			int x = responseLine.charAt(2) - 48;
			int y = responseLine.charAt(3) - 48;
			myBattleFieldCells[x][y].setBackground(Color.ORANGE);
		} else if (responseLine.charAt(1) == 's') {
			int lengthOfSunkenShip = (responseLine.length() - 2) / 2;
			for (int i = 1; i <= lengthOfSunkenShip; i++) {
				int x = responseLine.charAt(i * 2) - 48;
				int y = responseLine.charAt(1 + i * 2) - 48;
				myBattleFieldCells[x][y].setBackground(Color.RED);
			}
		}
	}

	private void drawGUI() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

		labelWhosTurn.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelWhosTurn.setHorizontalAlignment(SwingConstants.CENTER);
		labelWhosTurn.setBounds(500, 491, 400, 69);
		contentPane.add(labelWhosTurn);

		btnStartGame.setBounds(418, 511, 89, 23);
		contentPane.add(btnStartGame);
		if (doIStart) {
			labelWhosTurn.setText("Naciúnij Start aby rozpoczπÊ");
		} else {
			labelWhosTurn.setText("Naciúnij Start i poczekaj na swojπ turÍ");
		}

		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (doIStart) {
					labelWhosTurn.setText(yourTurnMsg);
				} else {
					labelWhosTurn.setText(opponentsTurnMsg);
				}
				btnStartGame.setEnabled(false);

				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

					@Override
					protected String doInBackground() throws Exception {
						String tmp = gameRun();
						return tmp;
					}

					@Override
					protected void done() {
					}
				};
				worker.execute();
			}
		});
	}
}