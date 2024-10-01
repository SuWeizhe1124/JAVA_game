// Sprite.java
// written by mnagaku

import java.util.*;
import java.awt.*;

/**
 * �X�v���C�g�V�X�e������������Sprite�N���X<br>
 * �摜�𕔕i�Ƃ��āA�`��A�A�j���[�V�������s���ׂ̋@�\��񋟂��܂��B
 * �ǂݍ��񂾉摜���v�[�����Ă����A�K�v�Ȏ��ɕK�v�ȏꏊ�ɔz�u���܂��B
 * �摜�Ɠ����悤�ɁA��������\���o����悤�ɂ��܂��B
 * �摜�╶���́A�X�v���C�g�v���[���ɏ悹���A
 * �v���[�����S�ďd�Ȃ����`�ŕ`�悳��܂�
 * @author mnagaku
 */
public class Sprite {
/** �X�v���C�g�v���[���̃��[�h */
	static final int NULL_MODE = 0, GRP_MODE = 1, STR_MODE = 2, DRW_MODE = 8,
		CENTER_STR_MODE = 6;
/** �`��ΏۂƂȂ�ʂ̑傫�� */
	int canvasWidth, canvasHeight;
/** �`��ΏۂƂȂ鐶�������L�����Ă����ꏊ */
	Container owner;
/** �摜��ǂݍ���Œu���Ă����摜�v�[�� */
	Hashtable grp;
/** �o�b�N�o�b�t�@ */
	Image backGrp = null;
/** �摜�̓ǂݍ��ݏ�Ԃ��Ď����� */
	MediaTracker tracker;
/** �X�v���C�g�v���[�� */
	Hashtable planes;
/** �X�v���C�g�`�掞�̕`�揇 */
	Integer spriteList[];


/**
 * �R���X�g���N�^�B
 * �摜�v�[���A�X�v���C�g�v���[���𐶐����A�`��ʏ����L���B
 * @param canvasWidth �`��Ώۖʂ̕�
 * @param canvasHeight �`��Ώۖʂ̍���
 * @param owner �`��ΏۂƂȂ鐶����
*/
	public Sprite(int canvasWidth, int canvasHeight, Container owner) {
		int i, j;
// �摜�v�[���̐���
		grp = new Hashtable();
// �X�v���C�g�v���[���̐���
		planes = new Hashtable();
// �`��ʏ��̋L��
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.owner = owner;
// �摜�Ǎ���Ԃ��Ǘ�����MediaTracker�̐���
		tracker = new MediaTracker(owner);
	}


/**
 * �G���[�A�x���̕\���B
*/
	void infomation(String info, Exception e) {
		System.out.println(info);
		System.out.println("java.version : "
			+ System.getProperty("java.version"));
		System.out.println("java.vendor : "
			+ System.getProperty("java.vendor"));
		if(e != null)
			e.printStackTrace();
	}


/**
 * �摜�v�[���ɉ摜��ǂݍ��ށB
 * �摜�v�[���̉��Ԗڂ̈ʒu�ɁA���ƌ������O�̉摜��ǂݍ��ނ��w�肷��B
 * �ǂݍ��񂾉摜�́A�X�v���C�g�v���[���ɏ悹�Ďg�����Ƃ��ł���悤�ɂȂ�B
 * @param no �ǂݍ��񂾉摜���i�[����A�摜�v�[���̏ꏊ(�C���f�b�N�X�A0�`)
 * @param file �ǂݍ��މ摜�t�@�C����
 * @return �ǂݍ��݂�����ɏI�������ꍇ��true
*/
	public boolean addGrp(int no, String file) {
		try {
			grp.put(new Integer(no),
				Toolkit.getDefaultToolkit()
					.getImage(getClass().getResource(file)));
		} catch (Exception e) {
			infomation("Warning : Do not create image data.", e);
			return false;
		}
		tracker.addImage((Image)(grp.get(new Integer(no))), 1);
		return true;
	}


/**
 * �摜�̓ǂݍ��݂�҂B
 * �摜�v�[���ɉ摜��ǂݍ���ł��A�ǂݍ��݂��������Ȃ��Ɖ摜���g���Ȃ��̂�
 * �ǂݍ��ݒ��̉摜���S�ēǂݍ��ݏI���܂ő҂B
 * @return �ǂݍ��ݒ��ɗ�O������������false
*/
	public boolean waitLoad() {
		try {
			tracker.waitForID(1);
		} catch (InterruptedException e) {
			infomation("Warning : Problem occurred in waitLoad().", e);
			return false;
		}
		return true;
	}


/**
 * �摜�̓ǂݍ��ݏ�Ԃ𒲂ׂ�B
 * �摜�v�[���ɉ摜��ǂݍ���ł��A�ǂݍ��݂��������Ȃ��Ɖ摜���g���Ȃ��̂�
 * �ǂݍ��ݒ��̉摜���S�ēǂݍ��ݏI����������ׂ�B
 * �摜�̗��ǂݍ��݂��s���ꍇ�Ɏg���B
 * @return �ǂݍ��ݐ���I���Ȃ�1�A�ǂݍ��ݒ��Ȃ�0�A�G���[������������-1
*/
	public int isLoaded() {
		if(tracker.checkID(1) == false)
			return 0;
		if(tracker.isErrorID(1) == false)
			return 1;
		return -1;
	}


/**
 * �X�v���C�g�v���[���ɉ摜��o�^�B
 * �摜�v�[�����̉摜���A���ۂɕ\������ΏۂƂ��ăv���[���ɓo�^����B
 * �v���[���ɂ̓A�j���[�V�������s�킹�邽�߂ɁA�����̉摜��o�^�ł���B
 * �摜��o�^����ƁA�v���[���͉摜�\���p�ɐݒ肳���B
 * @param planeNo �o�^����v���[���̔ԍ�(0�`)
 * @param animeNo �A�j���[�V����������ꍇ�A���Ԗڂ̉摜���B�����Ȃ��Ȃ�0�B
 * @param grpNo �o�^����摜�B�摜�v�[�����ł̔ԍ�(0�`)
 * @return �o�^������������true
*/
	public boolean setPlaneGrp(int planeNo, int animeNo, int grpNo) {
		Integer pno = new Integer(planeNo);
		Plane pln;
		if((pln = (Plane)(planes.get(pno))) == null) {
			pln = new Plane();
			planes.put(pno, pln);
		}
		pln.animeNo = new Integer(animeNo);
		pln.grp.put(pln.animeNo, grp.get(new Integer(grpNo)));
		pln.planeMode = GRP_MODE;
		pln.view = true;
		pln.str = null;
		pln.font = null;
		pln.color = null;
		pln.draw = null;
		return true;
	}


/**
 * �X�v���C�g�v���[���ɉ摜��o�^�B
 * �摜�v�[�����̉摜���A���ۂɕ\������ΏۂƂ��ăv���[���ɓo�^����B
 * �摜��o�^����ƁA�v���[���͉摜�\���p�ɐݒ肳���B
 * �A�j���𗘗p���Ȃ��ꍇ�Ɏg�p����B
 * @param planeNo �o�^����v���[���̔ԍ�(0�`)
 * @param grpNo �o�^����摜�B�摜�v�[�����ł̔ԍ�(0�`)
 * @return �o�^������������true
*/
	public boolean setPlaneGrp(int planeNo, int grpNo) {
		Integer pno = new Integer(planeNo);
		Plane pln;
		if((pln = (Plane)(planes.get(pno))) == null) {
			pln = new Plane();
			planes.put(pno, pln);
		}
		pln.animeNo = new Integer(0);
		pln.grp.put(pln.animeNo, grp.get(new Integer(grpNo)));
		pln.planeMode = GRP_MODE;
		pln.view = true;
		pln.str = null;
		pln.font = null;
		pln.color = null;
		pln.draw = null;
		return true;
	}


/**
 * �X�v���C�g�v���[���̍��W��ݒ�B
 * �X�v���C�g�v���[���̉�ʏ�ł̕\���ʒu���w�肷��B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @param x x���W
 * @param y y���W
 * @return ����������true
*/
	public boolean setPlanePos(int planeNo, int x, int y) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return false;
		pln.posX = x;
		pln.posY = y;
		return true;
	}


/**
 * �X�v���C�g�v���[���̍��W�ɉ��Z�B
 * �X�v���C�g�v���[���̉�ʏ�ł̕\���ʒu���A���݂̈ʒu������Z���Ĉړ�����B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @param x x�����̈ړ���
 * @param y y�����̈ړ���
 * @return ����������true
*/
	public boolean setPlaneMov(int planeNo, int x, int y) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return false;
		pln.posX += x;
		pln.posY += y;
		return true;
	}


/**
 * �X�v���C�g�v���[����x���W��Ԃ��B
 * �X�v���C�g�v���[���̉�ʏ�ł̕\���ʒu��Ԃ��B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @return x�����̕\���ʒu
*/
	public int getPlanePosX(int planeNo) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return 0xffff;
		return pln.posX;
	}


/**
 * �X�v���C�g�v���[����y���W��Ԃ��B
 * �X�v���C�g�v���[���̉�ʏ�ł̕\���ʒu��Ԃ��B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @return y�����̕\���ʒu
*/
	public int getPlanePosY(int planeNo) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return 0xffff;
		return pln.posY;
	}


/**
 * �X�v���C�g�v���[���̃A�j�����[�h��ݒ�B
 * �ΏۂƂȂ�v���[�����A�j���[�V���������邩�ǂ����ݒ肷��B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @param mode �A�j���[�V����������Ȃ�true�A�����Ȃ��Ȃ�false
 * @return �ݒ�ɐ���������true
*/
	public boolean setPlaneAnime(int planeNo, boolean mode) {
		int i, j;
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return false;
		if(pln.planeMode != GRP_MODE)
			return false;
		if((pln.anime = mode) == true) {
			pln.animeList = new Integer[pln.grp.size()];
			Enumeration enum = pln.grp.keys();
			for(i = 0; enum.hasMoreElements(); i++)
				pln.animeList[i] = (Integer)(enum.nextElement());
// Java1.1.x�ɂ�sort()���Ȃ��̂ŁA�����ŕ��בւ���
//			Arrays.sort(pln.animeList);
			Integer tmp;
			for(i = 0; i < pln.animeList.length - 1; i++)
				for(j = i + 1; j < pln.animeList.length; j++)
					if(pln.animeList[i].intValue()
						> pln.animeList[j].intValue()) {
						tmp = pln.animeList[i];
						pln.animeList[i] = pln.animeList[j];
						pln.animeList[j] = tmp;
					}
		}
		else
			pln.animeList = null;
		return true;
	}


/**
 * �X�v���C�g�v���[���ɕ������ݒ�B
 * �ΏۂƂȂ�v���[���𕶎���\���p�ɐݒ肷��B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @param str ���̃v���[���ŕ\�������镶����
 * @return �ݒ�ɐ���������true
*/
	public boolean setPlaneString(int planeNo, String str) {
		Integer pno = new Integer(planeNo);
		Plane pln;
		if((pln = (Plane)(planes.get(pno))) == null) {
			pln = new Plane();
			planes.put(pno, pln);
		}
		pln.font = new Font("Monospaced", Font.PLAIN, 16);
		pln.color = new Color(0, 0, 0);
		pln.str = str;
		pln.planeMode = STR_MODE;
		pln.view = true;
		pln.grp.clear();
		pln.anime = false;
		pln.animeNo = null;
		pln.draw = null;
		return true;
	}


/**
 * �X�v���C�g�v���[���ɃZ���^�����O�\���������ݒ�B
 * �ΏۂƂȂ�v���[���𕶎���\���p�ɐݒ肷��B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @param str ���̃v���[���ŕ\�������镶����
 * @return �ݒ�ɐ���������true
*/
	public boolean setPlaneCenterString(int planeNo, String str) {
		Integer pno = new Integer(planeNo);
		Plane pln;
		if((pln = (Plane)(planes.get(pno))) == null) {
			pln = new Plane();
			planes.put(pno, pln);
		}
		pln.font = new Font("Monospaced", Font.PLAIN, 16);
		pln.color = new Color(0, 0, 0);
		pln.str = str;
		pln.planeMode = CENTER_STR_MODE;
		pln.view = true;
		pln.grp.clear();
		pln.anime = false;
		pln.animeNo = null;
		pln.draw = null;
		return true;
	}


/**
 * �X�v���C�g�v���[����Font������ݒ�B
 * �v���[���̕����\���Ɏg���t�H���g��ݒ肷��B
 * �^�������Font�N���X�̐����Ɏg����B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @param name �t�H���g��
 * @param style �X�^�C��
 * @param size �T�C�Y
 * @return �ݒ�ɐ���������true
*/
	public boolean setPlaneFont(int planeNo,String name,int style,int size) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return false;
		if((pln.planeMode & STR_MODE) == 0)
			return false;
		if(name == null)
			name = "Monospaced";
		if(style < 0)
			style = Font.PLAIN;
		if(size < 0)
			size = 16;
		pln.font = new Font(name, style, size);
		return true;
	}


/**
 * �X�v���C�g�v���[���̐F������ݒ�B
 * RGB�l���v���[���ɋL������B�F�͕����̕`��Ɏg����B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @param r ��(0�`255)
 * @param g ��(0�`255)
 * @param b ��(0�`255)
 * @return �ݒ�ɐ���������true
*/
	public boolean setPlaneColor(int planeNo, int r, int g, int b) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return false;
		if((pln.planeMode & STR_MODE) == 0)
			return false;
		pln.color = new Color(r, g, b);
		return true;
	}


/**
 * �X�v���C�g�v���[���ɕ`�惋�[�`����ݒ�B
 * �ΏۂƂȂ�v���[����`�惋�[�`���\���p�ɐݒ肷��B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @param draw ���̃v���[���ŕ`�悷�郋�[�`����\��Draw�N���X�̎���
 * @return �ݒ�ɐ���������true
*/
	public boolean setPlaneDraw(int planeNo, Draw draw) {
		Integer pno = new Integer(planeNo);
		Plane pln;
		if((pln = (Plane)(planes.get(pno))) == null) {
			pln = new Plane();
			planes.put(pno, pln);
		}
		pln.font = null;
		pln.color = null;
		pln.str = null;
		pln.planeMode = DRW_MODE;
		pln.view = true;
		pln.grp.clear();
		pln.anime = false;
		pln.animeNo = null;
		pln.draw = draw;
		return true;
	}


/**
 * �X�v���C�g�v���[���̕\�����I��/�I�t�B
 * �\����Ԃ��I���ɂ�����I�t�ɂ����肷��B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @param view �I���Ȃ�true�A�I�t�Ȃ�false
 * @return �ݒ�ɐ���������true
*/
	public boolean setPlaneView(int planeNo, boolean view) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return false;
		pln.view = view;
		return true;
	}


/**
 * �X�v���C�g�v���[���̕ێ�������������B
 * �g��Ȃ��Ȃ����X�v���C�g�v���[�����������A�V�����p�r�ɗ��p�ł���悤�ɂ���B
 * @param planeNo �ΏۂƂȂ�v���[���̔ԍ�(0�`)
 * @return �����ɐ���������true
*/
	public boolean delPlane(int planeNo) {
		Integer pno = new Integer(planeNo);
		planes.remove(pno);
		return true;
	}


/**
 * �S�ẴX�v���C�g�v���[���̕ێ�������������B
 * �S�ẴX�v���C�g�v���[�����������A�܂�����ȏ�Ԃŗ��p�ł���悤�ɂ���B
 * @return �����ɐ���������true
*/
	public boolean delPlaneAll() {
		planes.clear();
		return true;
	}


/**
 * �ێ��������e�Ɋ�Â��A��ʂɕ`��B
 * Sprite�N���X�ɕێ����ꂽ���Ɋ�Â��A�`����s���B
 * @param g �`��Ώۂ�Graphics�R���e�L�X�g
 * @return �`��ɐ���������true
*/
	public boolean paintScreen(Graphics g) {
		int i, j;
		Graphics gbg;
		Plane pln;

		if(backGrp == null) {
			backGrp = owner.createImage(canvasWidth, canvasHeight);
		}
		gbg = backGrp.getGraphics();

		spriteList = new Integer[planes.size()];
		Enumeration enum = planes.keys();
		for(i = 0; enum.hasMoreElements(); i++)
			spriteList[i] = (Integer)(enum.nextElement());
// Java1.1.x�ɂ�sort()���Ȃ��̂ŁA�����ŕ��בւ���
//		Arrays.sort(spriteList);
		Integer tmp;
		for(i = 0; i < spriteList.length - 1; i++)
			for(j = i + 1; j < spriteList.length; j++)
				if(spriteList[i].intValue() > spriteList[j].intValue()) {
					tmp = spriteList[i];
					spriteList[i] = spriteList[j];
					spriteList[j] = tmp;
				}

		for(i = 0; i < spriteList.length; i++) {
			pln = (Plane)(planes.get(spriteList[i]));
			if(pln.view == false)
				continue;
			if(pln.planeMode == GRP_MODE) {
				gbg.drawImage((Image)(pln.grp.get(pln.animeNo)),
					pln.posX, pln.posY, owner);
				if(pln.anime == true) {
					for(j = 0; pln.animeList[j] != pln.animeNo; j++);
					j = (j + 1) % pln.animeList.length;
					pln.animeNo = pln.animeList[j];
				}
// �����ŕ�����\��
			} else if(pln.planeMode == STR_MODE) {
				gbg.setFont(pln.font);
				gbg.setColor(pln.color);
				gbg.drawString(pln.str, pln.posX, pln.posY+pln.font.getSize());
// ��Ӓ�����ŕ�����\��
			} else if(pln.planeMode == CENTER_STR_MODE) {
				gbg.setFont(pln.font);
				gbg.setColor(pln.color);
				gbg.drawString(pln.str,
					pln.posX - gbg.getFontMetrics().stringWidth(pln.str) / 2,
					pln.posY + pln.font.getSize());
			} else if(pln.planeMode == DRW_MODE)
				pln.draw.drawing(gbg, pln);
		}
		gbg.dispose();
		g.drawImage(backGrp, owner.getInsets().left, owner.getInsets().top,
			owner);
		return true;
	}
}


/**
 * �X�v���C�g�v���[��1�����̏���ێ�����Plane�N���X<br>
 * �������A���ɏ���������������Ȃ��̂ŁA�f�t�H���g�R���X�g���N�^���g�p�B
 * @author mnagaku
 */
class Plane {
/** �\�������邩�ۂ��̃t���O */
	boolean view = false;
/** �A�j���[�V���������邩�ۂ��̃t���O */
	boolean anime = false;
/** �v���[���̍��W */
	int posX = 0, posY = 0;
/** �A�j���[�V�������A���Ԗڂ̉摜��\�����Ȃ̂���ێ� */
	Integer animeNo = null;
/** �A�j���[�V�����p�̉摜���X�g */
	Integer animeList[] = null;
/** �v���[���̃��[�h */
	int planeMode = 0;
/** �v���[���Ɋ֘A�t����ꂽ�摜�̔ԍ� */
	Hashtable grp = new Hashtable();
/** �\���Ɏg�p���镶�����ێ� */
	String str = null;
/** �\���Ɏg�p����t�H���g */
	Font font = null;
/** �����\���Ɏg�p����F */
	Color color = null;
/** draw���[�h���ɕ`�揈�����s��Draw�C���X�^���X��ێ� */
	Draw draw = null;
}

