// Audio.java
// written by mnagaku

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Audio extends Applet {

	Image img, img2;
	int x = 0, y = 0;
	AudioClip bgm, se;

	public void init() {

// 鳴らす音の準備
		bgm = getAudioClip(getClass().getResource("09.au"));
		se = getAudioClip(getClass().getResource("05_t02.au"));
// BGMはループ再生
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
// 効果音を鳴らす
			se.play();
		}
	}

	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
		g.drawImage(img2, x, y, this);
	}
}

