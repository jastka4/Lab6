package tb.sockets.client;

import java.awt.EventQueue;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import tb.sockets.client.kontrolki.KKButton;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;

public class MainFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblHost = new JLabel("Host:");
		lblHost.setBounds(10, 14, 26, 14);
		contentPane.add(lblHost);
		
		JFormattedTextField frmtdtxtfldIp;
		try {
			frmtdtxtfldIp = new JFormattedTextField(new MaskFormatter("###.###.###.###"));
			frmtdtxtfldIp.setBounds(43, 11, 90, 20);
			frmtdtxtfldIp.setText("xxx.xxx.xxx.xxx");
			contentPane.add(frmtdtxtfldIp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(10, 70, 75, 23);
		contentPane.add(btnConnect);
		
		JFormattedTextField frmtdtxtfldXxxx = new JFormattedTextField();
		frmtdtxtfldXxxx.setText("xxxx");
		frmtdtxtfldXxxx.setBounds(43, 39, 90, 20);
		contentPane.add(frmtdtxtfldXxxx);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(10, 42, 26, 14);
		contentPane.add(lblPort);
		
		OrderPane panel = new OrderPane();
		panel.setBounds(145, 14, 487, 448);
		contentPane.add(panel);
		
		JLabel lblNotConnected = new JLabel("Not Connected");
		lblNotConnected.setForeground(new Color(255, 255, 255));
		lblNotConnected.setBackground(new Color(128, 128, 128));
		lblNotConnected.setOpaque(true);
		lblNotConnected.setBounds(10, 104, 123, 23);
		contentPane.add(lblNotConnected);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
		panel_1.setBounds(10, 138, 123, 123);
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(2, 2, 5, 5));
		
		JButton btn1_1 = new KKButton();
		panel_1.add(btn1_1);

		JButton btn1_2 = new KKButton();
		panel_1.add(btn1_2);

		JButton btn2_1 = new KKButton();
		panel_1.add(btn2_1);

		JButton btn2_2 = new KKButton();
		panel_1.add(btn2_2);

	}
}
