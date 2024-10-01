// InputEventTiny.java
// written by mnagaku

/**
 * InputEventTiny�N���X<br>
 * �L�[�{�[�h�A�}�E�X�̏�Ԃ�ێ��B
 * @author mnagaku
 */
public class InputEventTiny {

/** ����(�}�E�X�C�x���g�A�L�[�C�x���g�̓����\��ID������) */
	int id;

/** �}�E�X�C�x���g�̔�������x���W */
	int x;

/** �}�E�X�C�x���g�̔�������y���W */
	int y;

/** �L�[�C�x���g�̔��������L�[�̃R�[�h */
	int keyCode;

/** �R���X�g���N�^�B
 * �}�E�X�C�x���g�p
*/
	InputEventTiny(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.keyCode = 0;
	}


/** �R���X�g���N�^�B
 * �L�[�{�[�h�C�x���g�p
*/
	InputEventTiny(int id, int keyCode) {
		this.id = id;
		this.x = 0;
		this.y = 0;
		this.keyCode = keyCode;
	}


/** ID(����)�擾 */
	public int getID() {
		return id;
	}


/** X���W�擾 */
	public int getX() {
		return x;
	}


/** Y���W�擾 */
	public int getY() {
		return y;
	}


/** KeyCode�擾 */
	public int getKeyCode() {
		return keyCode;
	}
}

