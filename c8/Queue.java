// Queue.java
// written by mnagaku

import java.util.*;

/**
 * Queue�N���X<br>
 * Vector���g������Queue�������B
 * MacOS9�ȑO�̊�MRJ2.2.6��MSVM�ł����삷��悤�AJava1.1.x����Ώۂ�
 * Vector�N���X�̌Â����\�b�h���g���ċL�q�B
 * �������A���ɏ���������������Ȃ��̂ŁA�f�t�H���g�R���X�g���N�^���g�p�B
 * @author mnagaku
 */
public class Queue extends Vector {

/**
 * Queue�ɓ����
 * @param element �����v�f
 * @return ���ꂽ�v�f
*/
	public Object enqueue(Object element) {
		addElement(element);
		return element;
	}


/**
 * Queue����o��
 * @return ���o�����v�f�BQueue�ɗv�f���Ȃ����null
*/
	public Object dequeue() {
		Object ret;
		if(isEmpty())
			return null;
		ret = elementAt(0);
		removeElementAt(0);
		return ret;
	}
}

