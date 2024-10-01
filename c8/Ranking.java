// Ranking.java
// written by mnagaku

/**
 * Ranking�N���X<br>
 * �����L���O����ێ�����B
 * @author mnagaku
 */
class Ranking {
	String names[];
	int points[], max, sortParam = 1;


/**
 * �R���X�g���N�^�B
 * �����L���O����ێ����鏀���B
 * @param max ���ʂ܂ł̃����L���O����ێ����邩
 */
	Ranking(int max) {
		this.max = max;
		names = new String[max];
		points = new int[max];
		for(int i = 0; i < max; i++) {
			names[i] = "player" + (i + 1);
			points[i] = (i + 1) * 10;
		}
	}


/**
 * �����L���O��\��������𐶐�����B
 * @return �����L���O��\��������
 */
	String makeString() {
		String ret = "RANKING ";
		for(int i = 0; i < max; i++)
			ret += names[i] + " " + points[i] + " ";
		return ret;
	}


/**
 * �����L���O�ɒǉ�����B
 * @param name ���O
 * @param point ���_
 * @return ���ʂɑ}�����ꂽ���B�����L���O�ɓ���Ȃ������ꍇ��max
 */
	int addRanking(String name, int point) {
		int i, j;
		for(i = 0; i < max && points[i] * sortParam >= point * sortParam; i++);
		if(i >= max)
			return i;
		for(j = max - 1; j > i; j--) {
			points[j] = points[j - 1];
			names[j] = names[j - 1];
		}
		points[i] = point;
		names[i] = name;
		return i;
	}


/**
 * �����L���O�����~���Ƀ\�[�g����B
 */
	void big2small() {
		sortParam = 1;
		sort();
	}


/**
 * �����L���O���������Ƀ\�[�g����B
 */
	void small2big() {
		sortParam = -1;
		sort();
	}


/**
 * �\�[�g����B
 */
	void sort() {
		if(max < 2)
			return;
		int i, j, tmpPoint;
		String tmpName;
		for(i = 0; i < max - 1; i++)
			for(j = i + 1; j < max; j++)
				if(points[i] * sortParam < points[j] * sortParam) {
					tmpPoint = points[i];
					points[i] = points[j];
					points[j] = tmpPoint;
					tmpName = names[i];
					names[i] = names[j];
					names[j] = tmpName;
				}
	}
}
