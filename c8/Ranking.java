// Ranking.java
// written by mnagaku

/**
 * Rankingクラス<br>
 * ランキング情報を保持する。
 * @author mnagaku
 */
class Ranking {
	String names[];
	int points[], max, sortParam = 1;


/**
 * コンストラクタ。
 * ランキング情報を保持する準備。
 * @param max 何位までのランキング情報を保持するか
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
 * ランキングを表す文字列を生成する。
 * @return ランキングを表す文字列
 */
	String makeString() {
		String ret = "RANKING ";
		for(int i = 0; i < max; i++)
			ret += names[i] + " " + points[i] + " ";
		return ret;
	}


/**
 * ランキングに追加する。
 * @param name 名前
 * @param point 得点
 * @return 何位に挿入されたか。ランキングに入らなかった場合はmax
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
 * ランキング情報を降順にソートする。
 */
	void big2small() {
		sortParam = 1;
		sort();
	}


/**
 * ランキング情報を昇順にソートする。
 */
	void small2big() {
		sortParam = -1;
		sort();
	}


/**
 * ソートする。
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
