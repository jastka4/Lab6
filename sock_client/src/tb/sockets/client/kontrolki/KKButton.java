/**
 * 
 */
package tb.sockets.client.kontrolki;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

/**
 * @author tb
 *
 */
@SuppressWarnings("serial")
public class KKButton extends JButton {

	private BufferedImage[] rysunki = new BufferedImage[3];
	private int stan = 0;
	
	public KKButton() {
		super("");
		for (int i=0;i<3; i++) {
			BufferedImage tI = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
			tI.getGraphics().setColor(new Color(100, i, i, 0));
			tI.getGraphics().drawOval(3, 3, 17, 17);
			rysunki[i] = tI;
		}
		
	}
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(rysunki[stan], 0, 0, null);
	}
}
