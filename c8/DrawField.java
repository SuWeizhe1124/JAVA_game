// DrawField.java
// written by mnagaku

import java.awt.*;

/**
 * DrawField�N���X<br>
 * �����Ń}�X�ڂ�`���B
 * @author mnagaku
 */
class DrawField implements Draw {

/** �`��͈͂̑傫�� */
	int w, h;


/**
 * �R���X�g���N�^�B
 * �`�揀���B
 * @param w �`��͈͂̕�
 * @param h �`��͈͂̍���
 */
	DrawField(int w, int h) {
		this.w = w;
		this.h = h;
	}


/**
 * �`��B
 * �ێ����Ă�����Ɋ�Â��āA�}�X�ڂ�`�悷��B
 * @param g �`���
 * @param pln �v���[��
 */
	public boolean drawing(Graphics g, Plane pln) {
		g.setColor(Color.white);
		g.drawLine(50, 0, 50, 400);
		g.drawLine(250, 0, 250, 400);
		g.drawLine(0, 50, 300, 50);
		g.drawLine(0, 350, 300, 350);
		for(int i = 0; i < 3; i++) {
			g.drawLine(100 + i * 50, 0, 100 + i * 50, 50);
			g.drawLine(100 + i * 50, 350, 100 + i * 50, 400);
		}
		for(int i = 0; i < 5; i++) {
			g.drawLine(0, 100 + i * 50, 50, 100 + i * 50);
			g.drawLine(250, 100 + i * 50, 300, 100 + i * 50);
		}
		return true;
	}
}
