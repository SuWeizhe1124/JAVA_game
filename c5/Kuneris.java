// Kuneris.java
// written by mnagaku

import java.util.*;
import java.awt.event.*;


/** Kuneris�N���X */
public class Kuneris extends Game2D {


/**
 * �R���X�g���N�^�B
 * ��ʃT�C�Y�ƃ��C�����[�v�̑��x�A�L�[���s�[�g�̐ݒ���s���B
 */
	public Kuneris() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 120;
		KEY_SPEED = 60;
		KEY_DELAY = 3;
	}


/**
 * �A�v���P�[�V�����Ƃ��ē��삷��ꍇ�̊J�n�ʒu�B
 * Game2D�N���X��startGame()���ĂԁB
 */
	public static void main(String args[]) {
		startGame("Kuneris");
	}



/**
 * KunerisMain�N���X<br>
 * �Q�[���{�̂̏������s���B
 * @author mnagaku
 */
	public class KunerisMain extends Game2DMain {

/** �摜�t�@�C���̊g���q */
		static final String GRP_EXTENSION = ".gif";
/** �����Ă���u���b�N�̌`�̃f�[�^�\���B
    ���˂��ˈȊO�̃u���b�N�̑S�Ă̎p���́A
    �P�ʋ�`3x3�}�X�̒��Ɏ��܂� */
		final int blockChips[][][] = {
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{5, 5, 5}, {0, 0, 5}, {0, 0, 0}},
			{{5, 5, 0}, {5, 0, 0}, {5, 0, 0}},
			{{5, 0, 0}, {5, 5, 5}, {0, 0, 0}},
			{{0, 5, 0}, {0, 5, 0}, {5, 5, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{0, 0,10},{10,10,10}, {0, 0, 0}},
			{{10,10,0}, {0,10, 0}, {0,10, 0}},
			{{10,10,10},{10,0, 0}, {0, 0, 0}},
			{{10,0, 0}, {10,0, 0}, {10,10,0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{15,15,15},{0,15, 0}, {0, 0, 0}},
			{{15,0, 0}, {15,15,0}, {15,0, 0}},
			{{0,15, 0},{15,15,15}, {0, 0, 0}},
			{{0,15, 0},{15,15, 0}, {0,15, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{18,18,0}, {0,18,18}, {0, 0, 0}},
			{{0,18, 0}, {18,18,0}, {18,0, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{0,21,21}, {21,21,0}, {0, 0, 0}},
			{{21,0, 0}, {21,21,0}, {0,21, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{23,0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
		};
/** �u���b�N�̐ς܂���\�� */
		int[][] fieldMap;
/** �����Ă���r���̃u���b�N�̍��W */
		int x, y;
/** �n�C�X�R�A */
		int hiScore = 0;
/** �v���C���̃X�R�A */
		int score = 0;
/** �c�@�� */
		int last = 2;
/** ���˂��˃u���b�N�𐬂��P�ʋ�`���ꂼ��̍��W */
		int[] tailX, tailY;
/** ���C�����[�v�����x�ɃJ�E���g�A�b�v */
		long loopCount = 1;
/** �����Ă���u���b�N�S6����L�� */
		BlockDataVector[] blockDatas;
/** �����Ă���r���̃u���b�N�̎�� */
		int nowBlockNo = -1;
/** ���ɗ����Ă���u���b�N�̎�� */
		int nextBlockNo = -1;
/** �����Ă���r���̃u���b�N�̃f�[�^ */
		BlockData nowBlock;
/** �Q�[���i�s��̃��[�h�B0:�^�C�g�� 1:�Q�[�� -1:�Q�[���I�[�o */
		int mode = 0;
/** ��юU��u���b�N�̃��X�g */
		CrashBlockList crashBlockList;



/** �����Ă���u���b�N��\��BlockData�N���X */
		class BlockData {

/** �u���b�N�̑傫�� */
			int width, height;
/** �u���b�N���P�ʋ�`���ǂ̂悤�ɑg�ݍ��킹�����̂� */
			int[][] chips;
/** �u���b�N�̉摜�̔ԍ� */
			int grpNo;


/** �R���X�g���N�^ */
			BlockData(int grpNo, int width, int height, int[][] array) {
				this.grpNo = grpNo;
				this.width = width;
				this.height = height;
				chips = array;
			}
		}



/** �u���b�N�̎�ނ�\��BlockDataVector�N���X */
		class BlockDataVector extends Vector {

/** �u���b�N�̉�]��� */
			int nowIndex = 0;


/** �u���b�N�̉�]��Ԃ����������� */
			void clearIndex() {
				nowIndex = 0;
			}


/** ���݂̎p���̃u���b�N���擾���� */
			BlockData getData() {
				BlockData ret = (BlockData)elementAt(nowIndex);
				nowIndex = ((nowIndex + 1) % size());
				return ret;
			}


/** 1�O�̎p���̃u���b�N���擾�ł����Ԃɖ߂� */
			void rollBack() {
				nowIndex = (nowIndex - 2 + size()) % size();
			}
		}



/** �e����ԒP�ʋ�`�̃��X�g��\��CrashBlockList */
		class CrashBlockList {

/** �e����ԒP�ʋ�`�̃��X�g��ێ� */
			Vector blocks;
/** ���C�����[�v1��O�̎��_�ł̒e����ԒP�ʋ�`�̐����L�^ */
			int prevCount = 0;


/** �R���X�g���N�^ */
			CrashBlockList() {
				blocks = new Vector();
			}


/** �����郉�C���𐬂��P�ʋ�`��e����ԒP�ʋ�`���X�g�ɓo�^���� */
			void setCrash(int line) {
				int i;
				for(i = 1; i < 9; i++)
					if(fieldMap[i][line] != 0)
						blocks.addElement(new CrashBlock((i - 1) * 24 + 8,
							line * 24 + 8, fieldMap[i][line]));
			}


/** �e����΂���B��ʊO�ɏo�Ă��܂������̂͏��� */
			void crashBlockEffect() {
				int i;
				CrashBlock now;
				for(i = 0; i < blocks.size(); i++) {
					now = (CrashBlock)blocks.elementAt(i);
					if(now.x < -24 || now.x > 300 || now.y > 400) {
						blocks.removeElementAt(i);
						i--;
						continue;
					}
					sprite.setPlaneGrp(200 + i, 0, now.grpNo);
					sprite.setPlanePos(200 + i, now.x, now.y);
					now.x += now.vx;
					now.y += now.vy;
					now.vy++;
				}
				for(; i < prevCount; i++)
					sprite.setPlaneView(200 + i, false);
				prevCount = blocks.size();
			}


/** ��������e����ԒP�ʋ�`�̐���Ԃ� */
			int crashBlockCount() {
				return blocks.size();
			}
		}



/** �e����ԒP�ʋ�`��\��CrashBlock�N���X */
		class CrashBlock {

/** ��ʏ�̍��W */
			int x, y;
/** ��ԑ��x */
			int vx, vy;
/** �摜�̔ԍ� */
			int grpNo;


/** �R���X�g���N�^�B���x�̓����_���ɏ����� */
			CrashBlock(int x, int y, int grpNo) {
				this.x = x;
				this.y = y;
				this.grpNo = grpNo;
				vx = (int)(Math.random() * 33.) - 16;
				vy = -(int)(Math.random() * 16.);
			}
		}



/** �R���X�g���N�^ */
		public KunerisMain() {
			int i, j;

			crashBlockList = new CrashBlockList();

			tailX = new int[8];
			tailY = new int[8];

// �����u���b�N�f�[�^����
			blockDatas = new BlockDataVector[6];
			for(i = 0; i < 6; i++)
				blockDatas[i] = new BlockDataVector();
			blockDatas[0].addElement(new BlockData(1, 2, 3, blockChips[1]));
			blockDatas[0].addElement(new BlockData(2, 3, 2, blockChips[2]));
			blockDatas[0].addElement(new BlockData(3, 2, 3, blockChips[3]));
			blockDatas[0].addElement(new BlockData(4, 3, 2, blockChips[4]));
			blockDatas[1].addElement(new BlockData(6, 2, 3, blockChips[6]));
			blockDatas[1].addElement(new BlockData(7, 3, 2, blockChips[7]));
			blockDatas[1].addElement(new BlockData(8, 2, 3, blockChips[8]));
			blockDatas[1].addElement(new BlockData(9, 3, 2, blockChips[9]));
			blockDatas[2].addElement(new BlockData(11, 2, 3, blockChips[11]));
			blockDatas[2].addElement(new BlockData(12, 3, 2, blockChips[12]));
			blockDatas[2].addElement(new BlockData(13, 2, 3, blockChips[13]));
			blockDatas[2].addElement(new BlockData(14, 3, 2, blockChips[14]));
			blockDatas[3].addElement(new BlockData(16, 2, 3, blockChips[16]));
			blockDatas[3].addElement(new BlockData(17, 3, 2, blockChips[17]));
			blockDatas[4].addElement(new BlockData(19, 2, 3, blockChips[19]));
			blockDatas[4].addElement(new BlockData(20, 3, 2, blockChips[20]));
			blockDatas[5].addElement(new BlockData(22, 1, 1, blockChips[22]));
// �t�B�[���h�̖��܂��f�[�^����
			fieldMap = new int[10][17];
			for(i = 1; i < 9; i++)
				for(j = 0; j < 16; j++)
					fieldMap[i][j] = 0;
			for(i = 0; i < 10; i++)
				fieldMap[i][16] = 32;
			for(i = 0; i < 17; i++) {
				fieldMap[0][i] = 32;
				fieldMap[9][i] = 32;
			}
// �摜�̓ǂݍ���
			for(i = 0; i < 24; i++)
				sprite.addGrp(i, "grp"+(i/10)+(i%10)+GRP_EXTENSION);
			sprite.waitLoad();
// �w�i�摜�̐ݒ�
			sprite.setPlaneGrp(0, 0, 0);
// �u���b�N�̕\������
			getBlock();
			sprite.setPlanePos(140, x * 24 + 8, y * 24 + 8);
// �����\���̏���
			sprite.setPlaneString(150, "NextBlock");
			sprite.setPlanePos(150, 214, 9);

			sprite.setPlaneString(151, "HiScore");
			sprite.setPlanePos(151, 216, 110);
			sprite.setPlaneString(152, scoreString(7, hiScore));
			sprite.setPlanePos(152, 216, 128);

			sprite.setPlaneString(153, "Score");
			sprite.setPlanePos(153, 216, 160);
			sprite.setPlaneString(154, scoreString(7, score));
			sprite.setPlanePos(154, 216, 178);

			sprite.setPlaneString(155, "Last");
			sprite.setPlanePos(155, 216, 210);
			sprite.setPlaneString(156, Integer.toString(last));
			sprite.setPlanePos(156, 248, 228);
// BGM��SE
			sp.addBgm(1, "09.au");
			sp.addSe(1, "01_s01.au");
			sp.addSe(2, "02_s02.au");
			sp.addSe(3, "03_s03.au");
			sp.addSe(4, "04_t01.au");
			sp.addSe(5, "05_t02.au");
			sp.addSe(6, "06_t03.au");
			sp.addSe(7, "07_t04.au");
			sp.addSe(8, "08_t05.au");
			sp.addSe(9, "09_e01.au");
			sp.addSe(10, "10_e02.au");
			sp.addSe(11, "11_e03.au");
			sp.playBgm(1);
		}


/** ���C�����[�v�B���[�h�ɉ����ď�����ς��� */
		public boolean mainLoop() {
			boolean ret = true;

			switch(mode) {
				case 0:
					ret = titleMode();
					break;
				case 1:
					ret = gameMode();
					break;
				case -1:
					ret = gameOverMode();
					break;
			}
// ���[�h�Ɋւ�炸�e����ԒP�ʋ�`������ꍇ�A��������
			crashBlockList.crashBlockEffect();
			return ret;
		}



/** �^�C�g���\�� */
		boolean titleMode() {
			InputEventTiny ket;
			int i, j;

			sprite.setPlaneString(160, "Kuneris");
			sprite.setPlaneFont(160, null, -1, 55);
// ���C�����[�v���񂷓x�ɐF�����X�ɕω�������
			sprite.setPlaneColor(160, 255 - (int)(loopCount & 0xff),
				255 - (int)((loopCount & 0xff00) >> 8),
				255 - (int)((loopCount & 0xff0000) >> 16));
			sprite.setPlanePos(160, 10, 150);

			sprite.setPlaneString(161, "Push Space Key to Start");
			sprite.setPlaneFont(161, null, -1, 17);
			sprite.setPlaneColor(161, 255, 255, 255);
			sprite.setPlanePos(161, 14, 230);

			loopCount *= 2;
			loopCount &= 0xffffff;
			if(loopCount == 0)
				loopCount = 1;

			mouseQ.removeAllElements();
// �X�y�[�X�L�[�������ꂽ��Q�[���v���C�̏���������mode��1��
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED
					|| ket.getKeyCode() != KeyEvent.VK_SPACE)
					continue;

				sprite.setPlaneView(160, false);
				sprite.setPlaneView(161, false);
				mode = 1;

				sp.playSe(1);

				loopCount = 0;
				score = 0;
				last = 2;
				nowBlockNo = -1;
				nextBlockNo = -1;

				for(i = 1; i < 9; i++)
					for(j = 0; j < 16; j++)
						fieldMap[i][j] = 0;

				getBlock();
				buildBrocks();
				sprite.setPlanePos(140, x * 24 + 8, y * 24 + 8);

				sprite.setPlaneString(154, scoreString(7, score));
				sprite.setPlanePos(154, 216, 178);

				sprite.setPlaneString(156, Integer.toString(last));
				sprite.setPlanePos(156, 248, 228);
			}
			return true;
		}


/** �Q�[���v���C�� */
		boolean gameMode() {
// �L�[���͂ɉ����āA��𓮂���
			InputEventTiny ket;
			int i;

			if(loopCount % (20 - loopCount / 100) == 0) {
				if(dropBlock() == false) {
					mode = -1;
					loopCount = 1;
				}
				if(loopCount == 1900)
					loopCount--;
			}
			loopCount++;
// �}�E�X���͎͂̂Ă�(�g��Ȃ�)
			mouseQ.removeAllElements();
// �L�[���͏���
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED)
					continue;
				switch(ket.getKeyCode()) {
// ���������ꂽ��u���b�N��1�i���Ƃ�
					case KeyEvent.VK_DOWN:
						if(dropBlock() == false) {
							mode = -1;
							loopCount = 1;
						}
						break;
// �オ�����ꂽ��u���b�N����]
					case KeyEvent.VK_UP:
						if(nowBlockNo == 5)
							break;
						nowBlock = blockDatas[nowBlockNo].getData();
						if(hitBlockMap()) {
							blockDatas[nowBlockNo].rollBack();
							nowBlock = blockDatas[nowBlockNo].getData();
						} else {
							sprite.setPlaneGrp(140, 0, nowBlock.grpNo);
							sp.playSe(5);
						}
						break;
// �u���b�N�����E�Ɉړ�
					case KeyEvent.VK_RIGHT:
						x++;
						if(hitBlockMap())
							x--;
						else if(nowBlockNo == 5) {
							for(i = 7; i > 0; i--) {
								tailX[i] = tailX[i - 1];
								tailY[i] = tailY[i - 1];
							}
							tailX[0] = x;
							tailY[0] = y;
						}
						break;
					case KeyEvent.VK_LEFT:
							x--;
						if(hitBlockMap())
							x++;
						else if(nowBlockNo == 5) {
							for(i = 7; i > 0; i--) {
								tailX[i] = tailX[i - 1];
								tailY[i] = tailY[i - 1];
							}
							tailX[0] = x;
							tailY[0] = y;
						}
						break;
				}
			}
// ������u���b�N�`��ɔ��f������
			sprite.setPlanePos(140, x * 24 + 8, y * 24 + 8);
			if(nowBlockNo == 5)
				for(i = 1; i < 8; i++)
					sprite.setPlanePos(140 + i, tailX[i] * 24 + 8,
						tailY[i] * 24 + 8);
			return true;
		}


/** �Q�[���I�[�o */
		boolean gameOverMode() {
			InputEventTiny ket;
			int i, j;

			sprite.setPlaneString(160, "Game Over");
			sprite.setPlaneFont(160, null, -1, 35);
			sprite.setPlaneColor(160, (int)(loopCount & 0xff),
				(int)((loopCount & 0xff00) >> 8),
				(int)((loopCount & 0xff0000) >> 16));
			sprite.setPlanePos(160, 14, 150);

			loopCount *= 2;
			loopCount &= 0xffffff;
			if(loopCount == 0)
				loopCount = 1;

			mouseQ.removeAllElements();
// �X�y�[�X�L�[�������ꂽ��^�C�g����ʂ̏���������mode��0��
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED
					|| ket.getKeyCode() != KeyEvent.VK_SPACE)
					continue;

				sprite.setPlaneView(160, false);
				sprite.setPlaneView(161, false);
				mode = 0;
				loopCount = 1;
			}
			return true;
		}


/** ���Ƃ��u���b�N���擾 */
		void getBlock() {
			int i, j;
			i = (int)(Math.random() * 5.);
			if(nextBlockNo < 0) {
				nextBlockNo = i;
				i = (int)(Math.random() * 5.);
			}
			nowBlockNo = nextBlockNo;
			nextBlockNo = i;

			if((int)(Math.random() * 6.) == 0) {
				nextBlockNo = 5;
				for(i = 0; i < 2; i++)
					for(j = 0; j < 3; j++) {
						sprite.setPlaneGrp(130 + i * 3 + j, 0,
							((BlockData)blockDatas[nextBlockNo]
							.elementAt(0)).grpNo);
						sprite.setPlanePos(130+i*3+j, 226+i*24, 26+j*24);
					}
			} else {
				sprite.setPlaneGrp(130, 0,
					((BlockData)blockDatas[nextBlockNo].elementAt(0)).grpNo);
				sprite.setPlanePos(130, 226, 26);
				for(i = 1; i < 6; i++)
					sprite.setPlaneView(130 + i, false);
			}

			nowBlock = (BlockData)blockDatas[nowBlockNo].elementAt(0);
			blockDatas[nowBlockNo].clearIndex();
			sprite.setPlaneGrp(140, 0, blockDatas[nowBlockNo].getData().grpNo);
			x = 3;
			y = 0;

			if(nowBlockNo == 5) {
				tailX[0] = 3;
				tailY[0] = 0;
				for(i = 1; i < 8; i++) {
					sprite.setPlaneGrp(140 + i, 0, nowBlock.grpNo);
					tailX[i] = 3;
					tailY[i] = 0;
					sprite.setPlanePos(140 + i, tailX[i], tailY[i]);
				}
				sp.playSe(6);
			}
			else
				for(i = 1; i < 8; i++)
					sprite.setPlaneView(140 + i, false);
		}


/** �����Ă����u���b�N���ς܂ꂽ�u���b�N�ɂԂ��������ǂ������ׂ� */
		boolean hitBlockMap() {
			int i, j;
			for(i = 0; i < nowBlock.width; i++)
				for(j = 0; j < nowBlock.height; j++)
					if(fieldMap[x + i + 1][y + j] != 0
						&& nowBlock.chips[i][j] != 0)
						return true;
			return false;
		}


/** �ς܂�Ă���u���b�N�̕`�揀�� */
		void buildBrocks() {
			int i, j;
			for(i = 0; i < 8; i++)
				for(j = 0; j < 16; j++)
					if(fieldMap[i + 1][j] != 0) {
						sprite.setPlaneGrp(1+j*8+i, 0, fieldMap[i + 1][j]);
						sprite.setPlanePos(1+j*8+i, 8 + i * 24, 8 + j * 24);
					} else
						sprite.setPlaneView(1+j*8+i, false);
		}


/** �u���b�N��1�i���Ƃ� */
		boolean dropBlock() {
			int i, j, k, top, bottom, scoreValue = 0, bounasValue = 0;
			y++;
// �u���b�N���ς܂�Ă���u���b�N�ɂԂ�������
			if(hitBlockMap()) {
				y--;
// �u���b�N��ς�
				if(nowBlockNo == 5) {
					tailX[0] = x;
					tailY[0] = y;
					for(i = 0; i < 8; i++)
						fieldMap[tailX[i]+1][tailY[i]] = nowBlock.chips[0][0];
					top = tailY[7];
					bottom = y + 1;
					for(i = 0; i < 6; i++)
						if(tailX[i] != tailX[i + 2]
							&& tailY[i] != tailY[i + 2])
							bounasValue++;
				} else {
					for(i = 0; i < nowBlock.width; i++)
						for(j = 0; j < nowBlock.height; j++)
							if(nowBlock.chips[i][j] != 0)
								fieldMap[x+i+1][y+j] = nowBlock.chips[i][j];
					top = y;
					bottom = y + nowBlock.height;
				}
// �����郉�C�����Ȃ���check
				for(j = top; j < bottom; j++) {
					for(i = 1; i < 9; i++)
						if(fieldMap[i][j] == 0)
							break;
					if(i == 9) {
						crashBlockList.setCrash(j);
						for(k = j; k > 0; k--)
							for(i = 1; i < 9; i++)
								fieldMap[i][k] = fieldMap[i][k - 1];
						for(i = 1; i < 9; i++)
							fieldMap[i][0] = 0;
						scoreValue++;
					}
				}
// �_���v�Z
				if(scoreValue != 0) {
					score += Math.pow(2, scoreValue + bounasValue - 1);
					score += crashBlockList.crashBlockCount();
					sprite.setPlaneString(154, scoreString(7, score));
					sprite.setPlanePos(154, 216, 178);
					sp.playSe(7);
				}
// ���ɗ��Ƃ��u���b�N�̏���
				buildBrocks();
				getBlock();
// �u���b�N���ςݏオ���Ă�����
				if(hitBlockMap()) {
					last--;
					loopCount = 0;

					for(i = 0; i < 16; i++)
						crashBlockList.setCrash(i);

					for(i = 1; i < 9; i++)
						for(j = 0; j < 16; j++)
							fieldMap[i][j] = 0;

					buildBrocks();
// 3��ςݏグ�Ă��܂��ƃQ�[���I�[�o
					if(last < 0) {
						if(score > hiScore) {
							hiScore = score;
							sprite.setPlaneString(152,scoreString(7,hiScore));
							sprite.setPlanePos(152, 216, 128);
							sprite.setPlaneString(161, "!! Top Score !!");
							sprite.setPlaneFont(161, null, -1, 19);
							sprite.setPlaneColor(161, 255, 255, 255);
							sprite.setPlanePos(161, 40, 230);
							sp.playSe(9);
						} else
							sp.playSe(11);
						return false;
					} else {
						sprite.setPlaneString(156, Integer.toString(last));
						sprite.setPlanePos(156, 248, 228);
						sp.playSe(11);
					}

					getBlock();

				}
// ���˂��˃u���b�N��1�i���Ƃ�
			} else if(nowBlockNo == 5) {
				for(i = 7; i > 0; i--) {
					tailX[i] = tailX[i - 1];
					tailY[i] = tailY[i - 1];
				}
				tailX[0] = x;
				tailY[0] = y;
			}
			return true;
		}


/** �_����\��������̐��� */
		String scoreString(int w, int c) {
			String ret;
			ret = Integer.toString(c);
			while(ret.length() < w)
				ret = "0" + ret;
			return ret;
		
		}
	}
}

