// SoundPalette.java
// written by mnagaku

import java.util.*;
import java.applet.*;

/**
 * �T�E���h���Ǘ�����SoundPalette�N���X<br>
 * BGM��SE��o�^(�ǂݍ���)���Ă����A�K�v�Ȏ��ɖ点��悤�������Ă����B
 * AudioClip�ŃT�|�[�g�����`���̂ݖ点��B
 * @author mnagaku
 */
public class SoundPalette {

/** BGM��ǂݍ���Œu���Ă����v�[�� */
	Hashtable bgms;

/** SE��ǂݍ���Œu���Ă����v�[�� */
	Hashtable ses;

/** JVM�̃o�[�V���� */
	int javaVersion;

/** ���t����BGM */
	int nowBgm;

/** �Đ�����SE */
	AudioClip nowSe = null;

/** �v���O�������s��ԁA�A�v���b�g���삩�ۂ� */
	boolean isApplet;

/** SoundPalette���Ăяo���A�v���b�g */
	Applet owner;


/**
 * �R���X�g���N�^�B
 * �v�[���𐶐��B�����Ȃ��̏ꍇ�͔�A�v���b�g�Ƃ��čl����B
*/
	public SoundPalette() {
		this(null);
	}


/**
 * �R���X�g���N�^�B
 * �v�[���𐶐��B���s���̔ŁAApplet���ǂ��������肵�Ă����B
 * @param owner SoundPalette���Ǘ�����I�[�i�[�ƂȂ�A�v���b�g
*/
	public SoundPalette(Applet owner) {
		this.owner = null;
		this.isApplet = false;
		if(owner instanceof Applet) {
			this.owner = owner;
			this.isApplet = true;
		}

		bgms = new Hashtable();
		ses = new Hashtable();

		String javaVersionStr = System.getProperty("java.version");
		if(javaVersionStr.compareTo("1.1.0") < 0)
			javaVersion = 10;
		else if(javaVersionStr.compareTo("1.2.0") < 0)
			javaVersion = 11;
		else
			javaVersion = 12;
	}

/**
 * �v�[����data��ǂݍ��ށB
 * �v�[���̉��Ԗڂ̈ʒu�ɁA���ƌ������O��data��ǂݍ��ނ��w�肷��B
 * @param no �ǂݍ���data���i�[����A�v�[���̏ꏊ(�C���f�b�N�X)
 * @param file �ǂݍ���data�t�@�C����
 * @param pool �ΏۂƂȂ�pool
 * @return �ǂݍ��݂�����ɏI�������ꍇ��true
*/
	boolean loadData(int no, String file, Hashtable pool) {
		AudioClip ac = null;
		try {
			switch(javaVersion) {
				case 11:
					if(isApplet)
						ac = owner.getAudioClip(getClass().getResource(file));
					else
						ac = newAudioClip4Sun(file);
					break;
				case 12:
					ac = Applet.newAudioClip(getClass().getResource(file));
					break;
			}
		} catch(Exception e) {
			System.out.println("Warning : SoundPalette is unplayable.");
			System.out.println("java.version : "
				+ System.getProperty("java.version"));
			System.out.println("java.vendor : "
				+ System.getProperty("java.vendor"));
			e.printStackTrace();
		}
		if(ac == null)
			return false;
		pool.put(new Integer(no), ac);
		return true;
	}

/**
 * sun�̓����N���X��p���ĉ�data��ǂݍ��ށB
 * Java1.1.x�ł�JVM���applet�ȊO�̃Q�[�����痘�p����ꍇ�̏������s���B
 * @param file �ǂݍ���data�t�@�C����
 * @return �ǂݍ���AudioClip
*/
	AudioClip newAudioClip4Sun(String file) {
		AudioClip ret;
		try {
			Class.forName("sun.applet.AppletAudioClip");
			ret = new sun.applet.AppletAudioClip(getClass().getResource(file));
		} catch(Exception e) {
			ret = null;
		}
		return ret;
	}


/**
 * BGM�v�[����BGM�f�[�^��ǂݍ��ށB
 * �v�[���̉��Ԗڂ̈ʒu�ɁA���ƌ������O��BGM��ǂݍ��ނ��w�肷��B
 * @param no �ǂݍ���BGM���i�[����A�v�[���̏ꏊ(�C���f�b�N�X)
 * @param file �ǂݍ���BGM�t�@�C����
 * @return �ǂݍ��݂�����ɏI�������ꍇ��true
*/
	public boolean addBgm(int no, String file) {
		return loadData(no, file, bgms);
	}


/**
 * SE�v�[����SE�f�[�^��ǂݍ��ށB
 * �v�[���̉��Ԗڂ̈ʒu�ɁA���ƌ������O��SE��ǂݍ��ނ��w�肷��B
 * @param no �ǂݍ���SE���i�[����A�v�[���̏ꏊ(�C���f�b�N�X)
 * @param file �ǂݍ���SE�t�@�C����
 * @return �ǂݍ��݂�����ɏI�������ꍇ��true
*/
	public boolean addSe(int no, String file) {
		return loadData(no, file, ses);
	}


/** pool����ac���擾 */
	AudioClip getAc(int no, Hashtable pool) {
		AudioClip ac = null;
		ac = (AudioClip)pool.get(new Integer(no));
		return ac;
	}


/** �����ꎞ��~ */
	public boolean pause() {
		AudioClip ac = getAc(nowBgm, bgms);
		if(ac == null)
			return false;
		ac.stop();
		return true;
	}


/** �����ĊJ */
	public boolean restart() {
		AudioClip ac = getAc(nowBgm, bgms);
		if(ac == null)
			return false;
		ac.loop();
		return true;
	}


/** BGM��炷 */
	public boolean playBgm(int no) {
		AudioClip ac = getAc(nowBgm, bgms);
		if(ac != null)
		ac.stop();
		nowBgm = no;
		ac = getAc(nowBgm, bgms);
		if(ac == null)
			return false;
		ac.loop();
		return true;
	}


/** SE��炷 */
	public boolean playSe(int no) {
		if(nowSe != null)
			nowSe.stop();
		nowSe = getAc(no, ses);
		if(nowSe == null)
			return false;
		nowSe.play();
		return true;
	}
}

