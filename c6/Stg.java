// Stg.java
// written by mnagaku

import java.io.*;
import java.util.*;
import java.awt.event.*;


/** Stg�N���X */
public class Stg extends Game2D {


/**
 * �R���X�g���N�^�B
 * ��ʃT�C�Y�ƃ��C�����[�v�̑��x�A�L�[���s�[�g�̐ݒ���s���B
 */
	public Stg() {
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
		startGame("Stg");
	}



/**
 * StgMain�N���X<br>
 * �Q�[���{�̂̏������s���B
 * @author mnagaku
 */
	public class StgMain extends Game2DMain {

/** �摜�t�@�C���̊g���q�B */
		static final String GRP_EXTENSION = ".gif";
/** �n�C�X�R�A */
		int hiScore = 0;
/** �v���C���̃X�R�A */
		int score = 0;
/** �c�@�� */
		int last;
/** �Q�[���i�s��̃��[�h�B
    1�`3:�Q�[��(��) 0:�Q�[���J�n -1:�^�C�g�� -2:�Q�[���I�[�o -3:�Q�[���N���A */
		int mode = -1;
/** ������ */
		String str = "";
/** �Q�[�����ɓo�ꂷ�镨�̂��Ǘ� */
		GameObjects gos;
/** ���C�����[�v�����x�ɃJ�E���g�A�b�v */
		int mainLoopCount = 0;


/** �R���X�g���N�^ */
		public StgMain() {
			int i, j;

			gos = new GameObjects(10, sprite, keyQ);
// �摜�̓ǂݍ���
			sprite.addGrp(1, "enemy1"+GRP_EXTENSION);
			sprite.addGrp(2, "enemy2"+GRP_EXTENSION);
			sprite.addGrp(3, "enemy3"+GRP_EXTENSION);
			sprite.addGrp(4, "menemy1"+GRP_EXTENSION);
			sprite.addGrp(5, "benemy1"+GRP_EXTENSION);
			sprite.addGrp(6, "own"+GRP_EXTENSION);

			sprite.addGrp(11, "benemy2"+GRP_EXTENSION);

			sprite.addGrp(16, "benemy3"+GRP_EXTENSION);
			sprite.addGrp(17, "enemy10"+GRP_EXTENSION);

			sprite.waitLoad();

// �w�i�摜�̐ݒ�
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
// BGM
			sp.addBgm(1, "09.au");
			sp.playBgm(1);
		}


/** ���C�����[�v�B���[�h�ɉ����ď�����ς��� */
		public boolean mainLoop() {
			InputEventTiny ket;
/* 2002�ԃv���[������莞�ԕ\����ɔ�\���ɂ��� */
			mainLoopCount++;
			if(mainLoopCount == 20)
				sprite.setPlaneView(2002, false);

			mouseQ.removeAllElements();
			switch(mode) {
// �Q�[���N���A
				case -3:
					gos.clearGOS();

					sprite.setPlaneString(2002, "Game clear");
					sprite.setPlanePos(2002, 100, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					sprite.setPlaneString(2001, "Hit any key");
					sprite.setPlanePos(2001, 100, 250);
					sprite.setPlaneColor(2001, 255, 255, 255);
					mainLoopCount = 0;

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = -1;
					break;
// �Q�[���I�[�o
				case -2:
					gos.clearGOS();

					sprite.setPlaneString(2002, "Game over");
					sprite.setPlanePos(2002, 100, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					sprite.setPlaneString(2001, "Hit any key");
					sprite.setPlanePos(2001, 100, 250);
					sprite.setPlaneColor(2001, 255, 255, 255);
					mainLoopCount = 0;

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = -1;
					break;
// �^�C�g���\��
				case -1:
					sprite.setPlaneString(2002, "An ordinary shooting game");
					sprite.setPlanePos(2002, 50, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					sprite.setPlaneString(2001, "Hit any key");
					sprite.setPlanePos(2001, 100, 250);
					sprite.setPlaneColor(2001, 255, 255, 255);
					mainLoopCount = 0;

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = 0;
					break;
// �Q�[���J�n
				case 0:
					sprite.setPlaneView(2002, false);
					gos.addGOOwn("Own", CANVAS_SIZE_W / 2 - 16, 300, 1000);
					mode = 1;
					openSequence("stage" + mode + ".txt");
					str = "Stage: " + mode;
					sprite.setPlaneString(2002, str);
					sprite.setPlanePos(2002, 100, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					mainLoopCount = 0;
					break;
// �Q�[����
				default:
					if(gos.isAliveOwn() == false) {
						mode = -2;
						sprite.setPlaneView(2000, false);
						sprite.setPlaneView(2001, false);
						keyQ.removeAllElements();
						break;
					}
					if(readSequence() == false) {
						mode++;

						if(mode > 3) {
							mode = -3;
							keyQ.removeAllElements();
							break;
						}

						openSequence("stage" + mode + ".txt");
						str = "Stage: " + mode;
						sprite.setPlaneString(2002, str);
						sprite.setPlanePos(2002, 100, 200);
						sprite.setPlaneColor(2002, 255, 255, 255);
						mainLoopCount = 0;
						keyQ.removeAllElements();
						break;
					}

					str = "Score: " + toString(gos.getScore(), 5, "0");
					sprite.setPlaneString(2000, str);
					str = "";
					sprite.setPlaneColor(2000, 255, 255, 255);
					for(int i = 0; i < gos.getLast() - 1; i++)
						str += "A";
					sprite.setPlaneString(2001, str);
					sprite.setPlanePos(2001, 0, 384);
					sprite.setPlaneColor(2001, 255, 0, 0);

					gos.moveAll();
					gos.hitCheckOwn();
					gos.hitCheckOwnBow();
			}
			return true;
		}

// �V�[�P���X�t�@�C���ǂݍ��݊֘A ------------------------------------

/** �V�[�P���X�t�@�C����̎Q�ƈʒu */
		int sequenceCount = 0;
/** �V�[�P���X�t�@�C���ǂݍ��ݗp */
		BufferedReader sequenceReader = null;


/** �V�[�P���X�t�@�C�����J�� */
		boolean openSequence(String filename) {
			sequenceCount = 0;

			try {
				if(sequenceReader != null)
					sequenceReader.close();
				InputStream is = getClass().getResource(filename).openStream();
				InputStreamReader isr = new InputStreamReader(is);
				sequenceReader = new BufferedReader(isr);
			} catch(Exception e) {e.printStackTrace();}
			return true;
		}


/** �V�[�P���X�t�@�C����ǂ� */
		boolean readSequence() {

			if(sequenceCount < 0) {
				GameObject go;
				Enumeration enum = gos.elements();
				while(enum.hasMoreElements()) {
					go = ((GameObject)(enum.nextElement()));
					if(go.attribute == GameObject.ENEMY)
						return true;
				}
				sequenceCount = 0;
			}

			if(sequenceCount > 0) {
				sequenceCount--;
				return true;
			}

			try {
				String str = sequenceReader.readLine();
				if(str.length() == 0)
					return true;
				StringTokenizer st = new StringTokenizer(str);
				String cmd = st.nextToken();
				if(cmd.equals("sleep") == true) {
					sequenceCount = Integer.parseInt(st.nextToken());
					return true;
				} else if(cmd.equals("wait") == true) {
					sequenceCount = -1;
					return true;
				} else {
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					gos.addGO(cmd, x, y, 1000);
					return true;
				}
			} catch(Exception e) {
				return false;
			}
		}
//--------------------------------------------------------------------


/** �_����\��������̐��� */
		String toString(int number, int length, String head) {
			String ret = new String("" + number);
			for(int i = ret.length(); i < length; i++)
				ret = head + ret;
			return ret;
		}
	}
}
