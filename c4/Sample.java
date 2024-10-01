// Sample.java
// written by mnagaku

import java.awt.event.*;

/**
 * Sample�N���X<br>
 * Game2D�N���X�̎g�����̃T���v���B
 * @author mnagaku
 */
public class Sample extends Game2D {


/**
 * �R���X�g���N�^�B
 * ��ʃT�C�Y�ƃ��C�����[�v�̑��x�A�L�[���s�[�g�̐ݒ���s���B
 */
	public Sample() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 80;
		KEY_SPEED = 40;
		KEY_DELAY = 3;
	}


/**
 * �A�v���P�[�V�����Ƃ��ē��삷��ꍇ�̊J�n�ʒu�B
 * Game2D�N���X��startGame()���ĂԁB
 */
	public static void main(String args[]) {
		startGame("Sample");
	}



/**
 * SampleMain�N���X<br>
 * �Q�[���{�̂̏������s���B
 * @author mnagaku
 */
	public class SampleMain extends Game2DMain {

/** �摜�t�@�C���̊g���q�B */
		static final String GRP_EXTENSION = ".gif";


/**
 * �R���X�g���N�^�B
 * �摜�≹�f�[�^�̓ǂݍ��݂Ȃǂ��s���B
 */
		public SampleMain() {
// GRP�ǂݍ���
			sprite.addGrp(1, "own" + GRP_EXTENSION);
			sprite.addGrp(2, "benemy2" + GRP_EXTENSION);
			sprite.waitLoad();
// GRP�\��
			sprite.setPlaneGrp(1, 0, 1);
			sprite.setPlanePos(1, 100, 300);
			sprite.setPlaneGrp(2, 0, 2);
			sprite.setPlanePos(2, 200, 100);
// �w�i
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
// ������̕\��
			sprite.setPlaneString(3, "string");
			sprite.setPlanePos(3, 100, 200);
			sprite.setPlaneColor(3, 255, 255, 255);
// BGM
			sp.addBgm(1, "09.au");
			sp.playBgm(1);
		}


/**
 * ���C�����[�v1�񕪂̏����B
 * �}�E�X�A�L�[�̓��͂��������āA�L�����N�^�̕`��ʒu��ς���B
 */
		public boolean mainLoop() {
			InputEventTiny ket;

			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED)
					continue;
				switch(ket.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						sprite.setPlaneMov(1, 0, 2);
						break;
					case KeyEvent.VK_UP:
						sprite.setPlaneMov(1, 0, -2);
						break;
					case KeyEvent.VK_RIGHT:
						sprite.setPlaneMov(1, 2, 0);
						break;
					case KeyEvent.VK_LEFT:
						sprite.setPlaneMov(1, -2, 0);
						break;
				}
			}
			while((ket = (InputEventTiny)(mouseQ.dequeue())) != null) {
				if(ket.getID() != MouseEvent.MOUSE_PRESSED)
					continue;
				sprite.setPlanePos(2, ket.getX(), ket.getY());
			}
			return true;
		}
	}
}
