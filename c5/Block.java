// Block.java
// written by mnagaku

import java.awt.event.*;
import java.awt.*;

/**
 * Block�N���X<br>
 * �u���b�N�̗����鏈�����������Ă݂�
 * @author mnagaku
 */
public class Block extends Game2D {


/**
 * �R���X�g���N�^�B
 * ��ʃT�C�Y�ƃ��C�����[�v�̑��x�A�L�[���s�[�g�̐ݒ���s���B
 */
	public Block() {
		CANVAS_SIZE_W = 200;
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
		startGame("Block");
	}



/**
 * BlockMain�N���X<br>
 * �Q�[���{�̂̏������s���B
 * @author mnagaku
 */
	public class BlockMain extends Game2DMain {

/** �V�K�u���b�N�����t���O�Btrue�̎��A�V�K�쐬 */
		boolean newBlock = true;
/** �u���b�N�����܂ō�����������l */
		int blockCount = 1;
/** �u���b�N��1�i���Ƃ��܂ł̎��Ԃ��J�E���g���� */
		int blockWait = 0;
/** �u���b�N�̐ς܂���\�� */
		int map[][] = new int[10][17];
/** �u���b�N�̕`��Ɏg����F��\�� */
		Color colorList[] = {Color.red, Color.green, Color.blue};
/**
 * �u���b�N�̕`��Ɏg����
 * Draw�C���^�[�t�F�C�X�������DrawRect�N���X�̃C���X�^���X
 */
		DrawRect nowBlock;


/**
 * �R���X�g���N�^�B
 * map�̏������Ɣw�i�ݒ�B
 */
		public BlockMain() {
// block�̐ς݋���Ǘ�����map[][]�̏�����
			for(int i = 0; i < 10; i++)
				for(int j = 0; j < 17; j++)
					map[i][j] = 1;
			for(int i = 1; i < 9; i++)
				for(int j = 0; j < 16; j++)
					map[i][j] = 0;
// �w�i�̐ݒ�
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
		}


/**
 * ���C�����[�v1�񕪂̏����B
 * �R���X�g���N�^�Bmap�̏������Ɣw�i�ݒ�B
 */
		public boolean mainLoop() {
// �}�E�X�C�x���g�͎̂Ă�
			mouseQ.removeAllElements();
// �V����block�����
			if(newBlock) {
				sprite.setPlaneDraw(blockCount, nowBlock = new DrawRect(25, 25,
					colorList[(int)(Math.random() * 3)]));
				sprite.setPlanePos(blockCount,
					(int)(Math.random() * 8) * 25, 0);
				newBlock = false;
				blockWait = 0;
			}
// mainLoop()10�񖈂�1�}�X���Ƃ�
			blockWait++;
			if(blockWait % 10 == 0) {
				if(map[sprite.getPlanePosX(blockCount)/25+1]
					[sprite.getPlanePosY(blockCount)/25+1] != 0) {
					map[sprite.getPlanePosX(blockCount)/25+1]
						[sprite.getPlanePosY(blockCount)/25] = 1;
					nowBlock.darker();
					newBlock = true;
					blockCount++;
					keyQ.removeAllElements();
					return true;
				}
				sprite.setPlaneMov(blockCount, 0, 25);
			}
// �L�[���͏���
			InputEventTiny ket;
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED)
					continue;
				switch(ket.getKeyCode()) {
// ���������ꂽ��1�}�X���Ƃ�
					case KeyEvent.VK_DOWN:
					if(map[sprite.getPlanePosX(blockCount)/25+1]
						[sprite.getPlanePosY(blockCount)/25+1] == 0)
						sprite.setPlaneMov(blockCount, 0, 25);
						break;
					case KeyEvent.VK_UP:
						break;
// ���E�Ɉړ�
					case KeyEvent.VK_RIGHT:
						if(map[sprite.getPlanePosX(blockCount)/25+2]
							[sprite.getPlanePosY(blockCount)/25] == 0)
							sprite.setPlaneMov(blockCount, 25, 0);
						break;
					case KeyEvent.VK_LEFT:
						if(map[sprite.getPlanePosX(blockCount)/25]
							[sprite.getPlanePosY(blockCount)/25] == 0)
							sprite.setPlaneMov(blockCount, -25, 0);
						break;
				}
			}
			return true;
		}
	}
}
