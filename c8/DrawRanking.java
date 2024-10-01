// DrawRanking.java
// written by mnagaku

import java.awt.*;

/**
 * DrawRanking�N���X<br>
 * �����L���O�\��`���B
 * @author mnagaku
 */
class DrawRanking implements Draw {
	int x = 70;
	int y_base = 70;
	int y_offset = 13;

	String names[] = null;

	DrawRanking() {}


/**
 * �����L���O�\�̐ݒ�B
 * �����L���O�\�ɕ`�悷�镶����̃��X�g��ݒ肷��B
 * @param names �����L���O�\�ɕ`�悷�镶����̃��X�g
 */
	void setRanking(String[] names) {
		this.names = names;
	}


/**
 * �`��B
 * �ێ����Ă�����Ɋ�Â��āA�����L���O�\��`�悷��B
 * @param g �`���
 * @param pln �v���[��
 */
	public boolean drawing(Graphics g, Plane pln) {
		if(names == null)
			return true;
		g.setColor(Color.white);
		g.setFont(new Font("Monospaced", Font.PLAIN, 12));
		for(int i = 0; i < names.length; i++)
			g.drawString(names[i], x, y_base + i * y_offset);
		return true;
	}
}
