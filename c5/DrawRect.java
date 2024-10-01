// DrawRect.java
// written by mnagaku

import java.awt.*;

/**
 * DrawRect�N���X<br>
 * �l�p��`���B
 * @author mnagaku
 */
class DrawRect implements Draw {

/** �l�p�̑傫�� */
	int w, h;
/** �l�p�̐F */
	Color color;


/**
 * �R���X�g���N�^�B
 * �`�揀���B
 * @param w �l�p�̕�
 * @param h �l�p�̍���
 * @param color �l�p�̐F
 */
	DrawRect(int w, int h, Color color) {
		this.w = w;
		this.h = h;
		this.color = color;
	}


/**
 * �`��B
 * �ێ����Ă�����Ɋ�Â��āA�v���[���̈ʒu�Ɏl�p��`�悷��B
 * @param g �`���
 * @param pln �v���[��
 */
	public boolean drawing(Graphics g, Plane pln) {
		g.setColor(color);
		g.fillRect(pln.posX, pln.posY, w, h);
		return true;
	}


/**
 * �ێ����Ă���F�����Â�����B
 */
	public void darker() {
		color = color.darker();
	}
}
