// Anime.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;
import java.math.*;

public class Anime extends Frame implements Runnable {

	static final int CANVAS_SIZE = 256;

	Image img, img2;
	Image[] img3;
	Image backGrp = null;
	int x, y, xv, yv, xa, ya, nx, ny, animeCount;

	public static void main(String args[]) {
		new Thread(new Anime()).start();
	}

	public Anime() {
		super("Anime");
		pack();
		setVisible(true);
		setVisible(false);
		pack();
		setResizable(false);
		pack();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - CANVAS_SIZE) / 2, (d.height - CANVAS_SIZE) / 2);
		setSize(CANVAS_SIZE + getInsets().left + getInsets().right,
			CANVAS_SIZE + getInsets().top + getInsets().bottom);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});

		img = Toolkit.getDefaultToolkit().getImage
			(getClass().getResource("penguin.gif"));
		img2 = Toolkit.getDefaultToolkit().getImage
			(getClass().getResource("kuwa.gif"));
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);

// アニメ用画像の読み込み
		img3 = new Image[4];
		for(int i = 0; i < 4; i++)
			img3[i] = Toolkit.getDefaultToolkit().getImage
				(getClass().getResource("num" + (i + 1) + ".gif"));

// アニメ画像表示位置の初期値
		nx = 0;
		ny = 0;

// 何番目の画像を表示するかを表現する変数の初期値
		animeCount = 0;

		x = 0;
		y = 0;
		xv = 2;
		yv = 2;
		xa = 1;
		ya = 1;

		setVisible(true);
	}

	public void processMouseEvent(MouseEvent e) {
		if(e.getID() == MouseEvent.MOUSE_PRESSED) {
			if(x > e.getX() - getInsets().left)
				xv = -Math.abs(xv);
			else
				xv = Math.abs(xv);
			if(y > e.getY() - getInsets().top)
				yv = -Math.abs(yv);
			else
				yv = Math.abs(yv);
// マウスクリック位置にアニメ画像を移動
			nx = e.getX() - getInsets().left;
			ny = e.getY() - getInsets().top;
		}
	}

	public void paint(Graphics g) {
		Graphics gbg;
		if(backGrp == null)
			backGrp = createImage(CANVAS_SIZE, CANVAS_SIZE);
		gbg = backGrp.getGraphics();
		gbg.drawImage(img, 0, 0, this);
		gbg.drawImage(img2, x, y, this);
// アニメ画像をバッファに描画
		gbg.drawImage(img3[animeCount], nx, ny, this);
		gbg.dispose();
		g.drawImage(backGrp, getInsets().left, getInsets().top, this);
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void run() {
		while(true) {
			x += xv;
			y += yv;
			if(x <= 0 || x + 32 >= CANVAS_SIZE - 1) {
				if(Math.abs(xv) == 2)
					xa = 1;
				else if(Math.abs(xv) == 8)
					xa = -1;
				xv *= -Math.pow(2, xa);
			}
			if(y <= 0 || y + 64 >= CANVAS_SIZE - 1) {
				if(Math.abs(yv) == 2)
					ya = 1;
				else if(Math.abs(yv) == 8)
					ya = -1;
				yv *= -Math.pow(2, ya);
			}
// 何番目の画像を表示するかを表現する変数を1つ進める
// 3の次は0となるようにする
			animeCount = (animeCount + 1) % 4;
			repaint();
			try {
				Thread.sleep(100);
			} catch(Exception e) {}
		}
	}
}

