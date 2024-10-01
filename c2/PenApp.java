// PenApp.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;

public class PenApp extends Frame {

	static final int CANVAS_SIZE = 256; // �E�B���h�E���̕`��̈�̑傫��

	Image img, img2;
	int x = 0, y = 0;

	public static void main(String args[]) {
		new PenApp();
	}

	public PenApp() {
// �E�B���h�E�̃^�C�g�����uPenApp�v�ɐݒ�
		super("PenApp");
// �E�B���h�E�̊O�ς��m�肳����
		pack();
		setVisible(true);
		setVisible(false);
		pack();
// �E�B���h�E�����T�C�Y�ł��Ȃ�����BWindows�ł͂����Řg�̕����m�肷��
		setResizable(false);
		pack();
// ��ʂ̃T�C�Y���擾���A�E�B���h�E����ʒ����t�߂ɒu��
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - CANVAS_SIZE) / 2, (d.height - CANVAS_SIZE) / 2);
// �`��\�ȗ̈悪CANVAS_SIZExCANVAS_SIZE�ɂȂ�悤�ɃE�B���h�E�̃T�C�Y���w��
		setSize(CANVAS_SIZE + getInsets().left + getInsets().right,
			CANVAS_SIZE + getInsets().top + getInsets().bottom);
// �E�B���h�E��������ɏI������悤�ɐݒ�
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

// �E�B���h�E��\��
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

