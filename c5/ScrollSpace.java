// ScrollSpace.java
// written by mnagaku

import java.awt.*;

/**
 * ScrollSpace�N���X<br>
 * �w�i�p�ɃX�N���[�����鐯���`���B
 * @author mnagaku
 */
class ScrollSpace implements Draw {

/** ���̈ʒu���L�^ */
	int dot[] = new int[40];
/** ���̃X�N���[����Ԃ��L�^ */
	int count = 0;
/** �`��ʂ̑傫�� */
	int w, h;


/**
 * �R���X�g���N�^�B
 * �`�揀���B
 * @param w �`��ʂ̕�
 * @param h �`��ʂ̍���
 */
	ScrollSpace(int w, int h) {
		this.w = w;
		this.h = h - 1;
		for(int i = 0; i < 40; i++) {
			dot[i] = (int)(Math.random() * w);
		}
	}


/**
 * �`��B
 * �Ă΂��x�ɐ��̕\���ʒu�����炵�Ă������ƂŃX�N���[������������B
 * @param g �`���
 * @param pln �v���[��
 */
	public boolean drawing(Graphics g, Plane pln) {
		dot[count] = (int)(Math.random() * w);
		count++;
		count %= 40;

		g.setColor(Color.black);
		g.fillRect(0, 0, w, h);

		g.setColor(Color.white);
		for(int i = 0; i < 40; i++, count = (count + 1) % 40) {
			int size = dot[count] % 3;
			g.drawLine(dot[count] - size, h - (i * 10),
				dot[count] + size, h - (i * 10));
			g.drawLine(dot[count], h - (i * 10) - size,
				dot[count], h - (i * 10) + size);
		}

		return true;
	}
}
