// Game2D.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.lang.reflect.*;

/**
 * 2D�Q�[���t���[�����[�N�N���X Game2D<br>
 * 2D�Q�[���쐬�ɕK�v�ȁA�X�v���C�g�A���A���͂������v���O�����̃t���[�����[�N�B
 * �����extends����2D�Q�[�������B
 * MacOS9�ȑO�̊�MRJ2.2.6��MSVM�ł����삷��悤�AJava1.1.x����Ώۂ�
 * �X���b�h�ɂ��^�C�}�[���荞�݂������B
 * <br>
 * ���̃N���X��extends����Game�N���X�́A
 * ���̗l�ɋL�q���ꂽhtml�t�@�C������Ăяo�����B
 * (�A�v���b�g��jar�t�@�C��������Ă���ꍇ)
 * <br>
 * <pre>
 * &lt;html&gt;
 * &lt;head&gt;
 * &lt;title&gt;Game&lt;/title&gt;
 * &lt;/head&gt;
 * &lt;applet code=Game archive=Game.jar width=200 height=200&gt;
 * &lt;/applet&gt;
 * &lt;/html&gt;
 * </pre>
 * @author mnagaku
 */
abstract public class Game2D extends Applet {

/** �Q�[���̖��O�BGame2D�N���X���p�����č��ꂽ�N���X�����琶������� */
	static String GAME_NAME;

/** Game2DMain�N���X���p�����č��ꂽ�N���X�� */
	static String GAME_MAIN_NAME;

/** �E�B���h�E�̕`��ʂ̕� */
	int CANVAS_SIZE_W = 320;
/** �E�B���h�E�̕`��ʂ̍��� */
	int CANVAS_SIZE_H = 240;

/** ��ʍĕ`��A���C�����[�v�����𔭐�������Ԋu�B�P�ʂ̓~���b */
	int SPEED = 100;

/** �J�[�\���L�[�̃L�[���s�[�g���擾����Ԋu�B�P�ʂ̓~���b */
	int KEY_SPEED = 50;
/** �J�[�\���L�[�̃L�[���s�[�g�J�n�̒x��B�P�ʂ�GET_KEY_SPEED�̉� */
	int KEY_DELAY = 3;

/** �L�[�̉���������Ԃ�ێ� */
	boolean pressUp = false, pressDown = false,
		pressLeft = false, pressRight = false;
/** �}�E�X���̈���ɂ��邩�ǂ����̏�Ԃ�ێ� */
	boolean mouseOnFrame = false;

/** �A�v���b�g�N�����A�A�v���P�[�V�����N���� */
	boolean appletFlag = true;

/** Sprite�N���X�B�摜�Ɋւ��鏈����Sprite�N���X�ɔC���� */
	Sprite sprite;

/** SoundPalette�N���X�BBGM�ASE�Ɋւ��鏈����SoundPalette�N���X�ɔC���� */
	SoundPalette sp;

/** Game2DMain�N���X�B�Q�[���{�̂̏�����Game2DMain�N���X�ɔC���� */
	Game2DMain gm;

/** �L�[�{�[�h�C�x���g��Queue�B�J�[�\���L�[�̓L�[���s�[�g����� */
	Queue keyQ;
/** �}�E�X�C�x���g��Queue */
	Queue mouseQ;

/** �ĕ`��E���C�����[�v����������MainLoop�N���X�̃I�u�W�F�N�g��ێ� */
	MainLoop timerTask;
/** �L�[���s�[�g����������KeyRepeater�N���X�̃I�u�W�F�N�g��ێ� */
	KeyRepeater keyTimerTask;


/**
 * �R���X�g���N�^�B
 * GAME_MAIN_NAME��Game2DMain���p�����č��ꂽ�N���X�̖��O��ݒ肷��B
 * ��{�I�ɂ͎����I�Ɍ������邪�AJava1.1.x�ł͌����ł��Ȃ��̂ŁA
 * GAME_MAIN_NAME = GAME_NAME + "$" + GAME_NAME + "Main";
 * �Ƃ���B
 */
	public Game2D() {
		try {
			GAME_NAME = getClass().getName();
			Class[] mbrs = getClass().getClasses();
			if(mbrs.length == 0) {
				GAME_MAIN_NAME = GAME_NAME + "$" + GAME_NAME + "Main";
				infomation("Warning : I can not getClasses().", null);
			}
			for(int i = 0; i < mbrs.length; i++)
				if(mbrs[i].getSuperclass().getName()
					.compareTo("Game2D$Game2DMain") == 0)
					GAME_MAIN_NAME = mbrs[i].getName();
		} catch (Exception e) {
			infomation("Error : I can not finish Game2D constructor.", e);
		}
	}


/**
 * �G���[�A�x���̕\���B
 * @param info �\�����镶����B�uError�v�Ŏn�܂镶���񂪓n�����Ƌ����I������
 * @param e ��O���������Ă���ꍇ�́A��O���n���Ə���\������B
 * ��O��n���Ȃ��ꍇ��null�ɂ���
 */
	static void infomation(String info, Exception e) {
		System.out.println(info);
		System.out.println("java.version : "
			+ System.getProperty("java.version"));
		System.out.println("java.vendor : "
			+ System.getProperty("java.vendor"));
		if(e != null) {
			if(e.getClass().getName().compareTo(
				"java.lang.reflect.InvocationTargetException") == 0)
				((InvocationTargetException)e).getTargetException()
					.printStackTrace();
			else
				e.printStackTrace();
		}
		if(info.indexOf("Error") == 0)
			System.exit(0);
	}


/**
 * ��applet���Amain()����Ăяo���R���X�g���N�^�̃��b�p�B
 * @param game2dClassName �����ΏۂƂȂ�Game2D�N���X�̔h���N���X�̖��O
 */
	static void startGame(String game2dClassName) {
		GAME_NAME = game2dClassName;
		try {
			Game2D game2D = (Game2D)(Class
				.forName(game2dClassName).newInstance());
			game2D.newGame2D();
		} catch (Exception e) {
			infomation("Error : I can not create Game2D or newGame2D().", e);
		}
	}


/**
 * Game2D����Ăяo��Game2DMain�̃R���X�g���N�^�̃��b�p�B
 * ���ۂɃC���X�^���X�����������
 * Game2DMain���p�������N���X�̖��O���A
 * GAME_MAIN_NAME�ɐݒ肳��Ă�����̂Ƃ��āA�R���X�g���N�^���Ăяo���B
 * @return �������ꂽGame2DMain���p�������N���X�̃I�u�W�F�N�g
 */
	Game2DMain newGame2DMain() {
		try {
			Class argClass[] = {getClass()};
			Constructor g2dmCon
				= Class.forName(GAME_MAIN_NAME).getConstructor(argClass);
			Object initArgs[] = {this};
			return (Game2DMain)(g2dmCon.newInstance(initArgs));
		} catch(Exception e) {
			infomation("Error : I can not create Game2DMain.", e);
		}
		return null;
	}


/**
 * �A�v���P�[�V�����̊J�n�p���\�b�h�̎��́B
 * main()���\�b�h���Ŏ��g�̃N���X�𐶐�������Ŏ��s�B
 * �E�B���h�E�𐶐����A�����ɃA�v���b�g��\���B
 */
	public void newGame2D() {
// �A�v���b�g�����s�J�n
		appletFlag = false;
		init();

// �A�v���P�[�V�����Ƃ��ẴE�B���h�E���쐬
		Frame frame = new Frame(GAME_NAME);
		frame.pack();
		frame.setVisible(true);
		frame.setVisible(false);
		frame.pack();

// �E�B���h�E�̕\���ʒu���A�قډ�ʒ�����
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width-CANVAS_SIZE_W)/2,
			(d.height-CANVAS_SIZE_H)/2);

// �E�B���h�E�̑傫����ݒ�
		int left, right, top, bottom;
		left = frame.getInsets().left;
		right = frame.getInsets().right;
		top = frame.getInsets().top;
		bottom = frame.getInsets().bottom;
		frame.setSize(CANVAS_SIZE_W + left + right,
			CANVAS_SIZE_H + top + bottom);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
// �E�B���h�E���A�C�R�������ꂽ�特���~
			public void windowIconified(WindowEvent e) {stop();}
// �E�B���h�E���A�C�R�����畜�A������BGM���ĊJ
			public void windowDeiconified(WindowEvent e) {start();}
		});
		frame.setResizable(false);
// �E�B���h�E�ɃA�v���b�g�𒣂荞��
		frame.add(this);
// �E�B���h�E��\��
		frame.setVisible(true);
// �E�B���h�E�̑傫�����m�F
		if(left != frame.getInsets().left
			|| right != frame.getInsets().right
			|| top != frame.getInsets().top
			|| bottom != frame.getInsets().bottom) {
			left = frame.getInsets().left;
			right = frame.getInsets().right;
			top = frame.getInsets().top;
			bottom = frame.getInsets().bottom;
			frame.setSize(CANVAS_SIZE_W + left + right,
				CANVAS_SIZE_H + top + bottom);
		}
	}


/**
 * �A�v���b�g�̊J�n�B
 * �Q�[���{�̂̏����������B
 * �ĕ`��E���C�����[�v�A�L�[�{�[�h���s�[�^�̊J�n�B
 */
	public void init() {
// �L�[�C�x���g�擾��ݒ�
		enableEvents(AWTEvent.MOUSE_EVENT_MASK |
			AWTEvent.MOUSE_MOTION_EVENT_MASK |
			AWTEvent.KEY_EVENT_MASK);
// �X�v���C�g�A���A���͂��Ǘ�����N���X�𐶐�
		keyQ = new Queue();
		mouseQ = new Queue();
		if(appletFlag)
			sp = new SoundPalette(this);
		else
			sp = new SoundPalette();
		sprite = new Sprite(CANVAS_SIZE_W, CANVAS_SIZE_H, this);
// �Q�[���{�̂���������N���X�𐶐�
		gm = newGame2DMain();
// �ĕ`��E���C�����[�v�̐����A�J�n
		timerTask = new MainLoop();
		timerTask.start();
// �L�[�{�[�h���s�[�^�����A�J�n
		keyTimerTask = new KeyRepeater();
		keyTimerTask.start();
// �t�H�[�J�X�擾
		requestFocus();
	}


/** �A�v���b�g���A���ABGM���Đ� */
	public void start() {
		sp.restart();
		timerTask.threadSuspended = false;
		keyTimerTask.threadSuspended = false;
	}


/** �A�v���b�g��~���A�����~ */
	public void stop() {
		sp.pause();
		timerTask.threadSuspended = true;
		keyTimerTask.threadSuspended = true;
	}


/** �A�v���b�g�I�����A�X���b�h���I�� */
	public void destroy() {
		timerTask.threadStoped = true;
		keyTimerTask.threadStoped = true;
		try {
			timerTask.join();
			keyTimerTask.join();
		} catch (Exception e) {
			infomation("Error : I can not finish destroy().", e);
		}
	}


/**
 * �}�E�X�C�x���g(�E�B���h�E�ォ�A������Ă��邩)�������B
 * �{�^���������ꂽ���A�����ꂽ���AQueue�ɓ����B
 * @param e �}�E�X�C�x���g
 */
	public void processMouseEvent(MouseEvent e) {
		switch(e.getID()) {
			case MouseEvent.MOUSE_ENTERED:
				mouseOnFrame = true;
				break;
			case MouseEvent.MOUSE_EXITED:
				mouseOnFrame = false;
				break;
			case MouseEvent.MOUSE_PRESSED:
			case MouseEvent.MOUSE_RELEASED:
				if(mouseOnFrame)
					mouseQ.enqueue(new InputEventTiny(e.getID(),
						 e.getX() - getInsets().left,
						 e.getY() - getInsets().top));
				break;
		}
	}


/**
 * �}�E�X�C�x���g(�ړ�)�������B
 * �}�E�X�̍��W���ω��������AQueue�ɓ����B
 * @param e �}�E�X�C�x���g
 */
	public void processMouseMotionEvent(MouseEvent e) {
		if(!mouseOnFrame || e.getID() != MouseEvent.MOUSE_MOVED
			|| e.getX() < getInsets().left
			|| e.getX() > CANVAS_SIZE_W + getInsets().left
			|| e.getY() < getInsets().top
			|| e.getY() > CANVAS_SIZE_H + getInsets().top)
			return;
		mouseQ.enqueue(new InputEventTiny(e.getID(),
			e.getX() - getInsets().left, e.getY() - getInsets().top));
	}


/**
 * �L�[�{�[�h�C�x���g�������B
 * �L�[�{�[�h�C�x���g��Queue�ɓ����B
 * �J�[�\���L�[������KeyRepeater�ƘA�g���āA�L�[���s�[�g�������B
 * @param e �L�[�C�x���g
 */
	public void processKeyEvent(KeyEvent e) {
		switch(e.getID()) {
			case KeyEvent.KEY_PRESSED:
				switch(e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						if(!pressDown)
							keyQ.enqueue(new InputEventTiny(e.getID(),
								e.getKeyCode()));
						pressDown = true;
						break;
					case KeyEvent.VK_UP:
						if(!pressUp)
							keyQ.enqueue(new InputEventTiny(e.getID(),
								e.getKeyCode()));
						pressUp = true;
						break;
					case KeyEvent.VK_RIGHT:
						if(!pressRight)
							keyQ.enqueue(new InputEventTiny(e.getID(),
								e.getKeyCode()));
						pressRight = true;
						break;
					case KeyEvent.VK_LEFT:
						if(!pressLeft)
							keyQ.enqueue(new InputEventTiny(e.getID(),
								e.getKeyCode()));
						pressLeft = true;
						break;
					default:
						keyQ.enqueue(new InputEventTiny(e.getID(),
							e.getKeyCode()));
						break;
				}
				break;
			case KeyEvent.KEY_RELEASED:
				switch(e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						pressDown = false;
						break;
					case KeyEvent.VK_UP:
						pressUp = false;
						break;
					case KeyEvent.VK_RIGHT:
						pressRight = false;
						break;
					case KeyEvent.VK_LEFT:
						pressLeft = false;
						break;
				}
				keyQ.enqueue(new InputEventTiny(e.getID(), e.getKeyCode()));
				break;
		}
	}


/**
 * �`��B
 * Sprite�N���X�ɕ`���Ă��炤
 * ���荞�ݏ��������repaint()�ȊO�̕`�掞�A�܂�ŏ�������̕��A����A
 * �B��Ă�����Ԃ���L���ȃE�B���h�E�Ƃ��ĕ\�Ƀs�b�N�A�b�v���ꂽ����
 * �t�H�[�J�X���擾���A�L�[���͂��󂯕t������悤�ɂ���B
 * �t�H�[�J�X�������I�Ɏ擾����̂́A�A�v���b�g���}�E�X�N���b�N���Ă���łȂ���
 * �L�[���͂��󂯕t�����Ȃ����߁B
 * @param g �y�C���g��ƂȂ�Graphics�R���e�L�X�g
 */
	public void paint(Graphics g) {
		sprite.paintScreen(g);
		requestFocus();
	}


/**
 * ��ʍX�V�B
 * repaint()�ŌĂ΂��̂ŁASprite�N���X�ɕ`���Ă��炤�B
 * �����ł̓t�H�[�J�X�̎擾�͍s��Ȃ��B
 * @param g �y�C���g��ƂȂ�Graphics�R���e�L�X�g
 */
	public void update(Graphics g) {
		sprite.paintScreen(g);
	}


/**
 * �L�[���s�[�g����������KeyRepeater�N���X<br>
 * �J�[�\���L�[�̓K�؂ȃL�[���s�[�g�������B
 * �L�[���s�[�g�̎��ԊԊu��A�J�n�̒x��́A
 * Game2D�N���X��KEY_SPEED�AKEY_DELAY���g���B
 * @author mnagaku
 */
	class KeyRepeater extends Thread {
/** true�ɐݒ肳���ƃL�[���s�[�g�̈ꎞ��~���s���� */
		boolean threadSuspended = false;
/** true�ɐݒ肳���ƃL�[���s�[�g����~����� */
		boolean threadStoped = false;

/**
 * �^�C�}�[���荞�ݏ����{�́B
 * ��莞�Ԗ��ɃL�[���s�[�g�𔭐������鏈�����s����B
 */
		public void run() {
			long processTime, pressDownCount = 0, pressUpCount = 0,
				pressRightCount = 0, pressLeftCount = 0;
			while(!threadStoped) {
				try {
					processTime = System.currentTimeMillis();
// �����𖞂����Ă���΃L�[���s�[�g�𔭐�������BQueue�ɓ����
					if(pressDown) {
					 	if(pressDownCount > KEY_DELAY)
							keyQ.enqueue(new InputEventTiny(
								KeyEvent.KEY_PRESSED, KeyEvent.VK_DOWN));
						else
							pressDownCount++;
					}
					else
						pressDownCount = 0;
					if(pressUp) {
					 	if(pressUpCount > KEY_DELAY)
							keyQ.enqueue(new InputEventTiny(
								KeyEvent.KEY_PRESSED, KeyEvent.VK_UP));
						else
							pressUpCount++;
					}
					else
						pressUpCount = 0;
					if(pressRight) {
					 	if(pressRightCount > KEY_DELAY)
							keyQ.enqueue(new InputEventTiny(
								KeyEvent.KEY_PRESSED, KeyEvent.VK_RIGHT));
						else
							pressRightCount++;
					}
					else
						pressRightCount = 0;
					if(pressLeft) {
					 	if(pressLeftCount > KEY_DELAY)
							keyQ.enqueue(new InputEventTiny(
								KeyEvent.KEY_PRESSED, KeyEvent.VK_LEFT));
						else
							pressLeftCount++;
					}
					else
						pressLeftCount = 0;
// KEY_SPEED�̎��ԑ҂�
					processTime = System.currentTimeMillis() - processTime;
					if(KEY_SPEED - processTime < 0)
						infomation("Warning : Processing delay in KeyRepeater.",
							null);
					else
						sleep(KEY_SPEED - processTime);

					while(threadSuspended && !threadStoped)
						yield();

				} catch (Exception e) {
					infomation("Error : Problem occurred in KeyRepeater.", e);
				}
			}
		}
	}


/**
 * �ĕ`��E���C�����[�v����������MainLoop�N���X<br>
 * run()�����̃��[�v�����̎��ԊԊu�ŉ񂷂��ƂŁA�^�C�}�[���荞�݂������B
 * @author mnagaku
 */
	class MainLoop extends Thread {
/** true�ɐݒ肳���ƃ��C�����[�v�̈ꎞ��~���s���� */
		boolean threadSuspended = false;
/** true�ɐݒ肳���ƃ��C�����[�v����~����� */
		boolean threadStoped = false;

/**
 * �^�C�}�[���荞�ݏ����{�́B
 * ��莞�Ԗ��ɌĂяo����A
 * Game2DMain�N���X(�̔h���N���X)��mainLoop()���Ăяo����
 * ���C�����[�v�̏������s���A
 * �X�v���C�g�ɕ`�惊�N�G�X�g���o���B
 */
		public void run() {
			long processTime = 0;
			while(!threadStoped) {
				try {
					processTime = System.currentTimeMillis();
// ���C�����[�v1�񕪂�����
					gm.mainLoop();
// �X�V�������Ɋ�Â��ĕ`��
					repaint();
// SPEED�̎��ԑ҂�
					processTime = System.currentTimeMillis() - processTime;
					if(SPEED - processTime < 0)
						infomation("Warning : Processing delay in MainLoop.",
							null);
					else
						sleep(SPEED - processTime);

					while(threadSuspended && !threadStoped)
						yield();

				} catch (Exception e) {
					infomation("Error : Problem occurred in MainLoop.", e);
					System.exit(0);
				}
			}
		}
	}


/**
 * Game2DMain�N���X<br>
 * �Q�[���{�̂̏������s���B
 * @author mnagaku
 */
	abstract public class Game2DMain {

/**
 * �R���X�g���N�^�B
 * �Q�[���{�̂̏����̏��������s���܂��B
*/
//		Game2DMain() {}


/**
 * ���C�����[�v1�񕪂̏����B
 * ��莞�Ԗ��ɌĂяo����鏈�����L�q����B
 * ��ʂ̃����_�����O�͂��̃��\�b�h�����s���ꂽ��A�����I�ɍs����B
 */
		abstract public boolean mainLoop();
	}
}
