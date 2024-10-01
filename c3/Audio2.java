// Audio2.java
// written by mnagaku

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Audio2 extends Applet {

	Image img, img2;
	int x = 0, y = 0;
	AudioClip bgm, se;

	public void init() {

// –Â‚ç‚·‰¹‚Ì€”õBÅ‰‚ÉApplet‚Å‚ ‚é‚±‚Æ‚ğ“`‚¦‚é
		bgm = new GameAudioClip("09.au", this);
		se = new GameAudioClip("05_t02.au");

		bgm.loop();
		img = getImage(getClass().getResource("penguin.gif"));
		img2 = getImage(getClass().getResource("kuwa.gif"));
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	public void processMouseEvent(MouseEvent e) {
		if(e.getID() == MouseEvent.MOUSE_PRESSED) {
			x = e.getX();
			y = e.getY();
			repaint();
			se.play();
		}
	}

	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
		g.drawImage(img2, x, y, this);
	}
}

