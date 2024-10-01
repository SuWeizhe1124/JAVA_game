// PenApp.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;

public class PenApp extends Frame {

	static final int CANVAS_SIZE = 256; // ウィンドウ内の描画領域の大きさ

	Image img, img2;
	int x = 0, y = 0;

	public static void main(String args[]) {
		new PenApp();
	}

	public PenApp() {
// ウィンドウのタイトルを「PenApp」に設定
		super("PenApp");
// ウィンドウの外観を確定させる
		pack();
		setVisible(true);
		setVisible(false);
		pack();
// ウィンドウをリサイズできなくする。Windowsではここで枠の幅が確定する
		setResizable(false);
		pack();
// 画面のサイズを取得し、ウィンドウを画面中央付近に置く
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - CANVAS_SIZE) / 2, (d.height - CANVAS_SIZE) / 2);
// 描画可能な領域がCANVAS_SIZExCANVAS_SIZEになるようにウィンドウのサイズを指定
		setSize(CANVAS_SIZE + getInsets().left + getInsets().right,
			CANVAS_SIZE + getInsets().top + getInsets().bottom);
// ウィンドウを閉じた時に終了するように設定
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});

		img = Toolkit.getDefaultToolkit().getImage
			(getClass().getResource("penguin.gif"));
		img2 = Toolkit.getDefaultToolkit().getImage
			(getClass().getResource("kuwa.gif"));
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		x = getInsets().left;
		y = getInsets().top;

// ウィンドウを表示
		setVisible(true);
	}

	public void processMouseEvent(MouseEvent e) {
		if(e.getID() == MouseEvent.MOUSE_PRESSED) {
			x = e.getX();
			y = e.getY();
			repaint();
		}
	}

	public void paint(Graphics g) {
		g.drawImage(img, getInsets().left, getInsets().top, this);
		g.drawImage(img2, x, y, this);
	}
}

