// Relation.java
// written by mnagaku

import java.awt.*;

// 領主同士の仲良さ
class Relation {

	int arr[][], gLine, bLine;

	Relation(int no) {
		arr = new int[no - 1][];
		for(int i = 0; i < no - 1; i++)
			arr[i] = new int[no - 1 - i];
	}

// 領主間の関係を乱数で初期化
	void init() {
		int bad = 100;
		for(int i = 0; i < arr.length; i++)
			for(int j = 0; j < arr[i].length; j++) {
				arr[i][j] = (int)(Math.random() * 100.);
				if(i != 0 && arr[i][j] < bad) {
					bad = arr[i][j];
					gLine = i;
					bLine = j + 1;
				}
			}
	}

// 2人の関係を取得する
	int getRelation(int no1, int no2) {
		int line = no1 < no2 ? no1 : no2;
		int no = no1 > no2 ? no1 - line - 1 : no2 - line - 1;
		if(no1 == no2 || line < 0 || line >= arr.length
			|| no < 0 || no >= arr[line].length)
			return -1;
		return arr[line][no];
	}

// 2人の関係を良くする
	int incRelation(int no1, int no2) {
		int line = no1 < no2 ? no1 : no2;
		int no = no1 > no2 ? no1 - 1 : no2 - 1;
		if(no1 == no2 || line < 0 || line >= arr.length
			|| no < 0 || no >= arr[line].length)
			return -1;
		arr[line][no]++;
		if(arr[line][no] > 100)
			arr[line][no] = 100;
		return arr[line][no];
	}

// 2人の関係を悪くする
	int decRelation(int no1, int no2) {
		int line = no1 < no2 ? no1 : no2;
		int no = no1 > no2 ? no1 - 1 : no2 - 1;
		if(no1 == no2 || line < 0 || line >= arr.length
			|| no < 0 || no >= arr[line].length)
			return -1;
		arr[line][no]--;
		if(arr[line][no] < 0)
			arr[line][no] = 0;
		return arr[line][no];
	}

// srcがdistに対して関係改善を働きかけると、2人の関係だけでなく、
// distと仲の良い領主と仲が良くなり、distと仲の悪い領主と仲が悪くなる
	int changeRelation(int src, int dist) {
		for(int i = 0; i < arr.length; i++) {
			if(i != src && i != dist && getRelation(dist, i) > 75)
				incRelation(src, i);
			else if(i != src && i != dist && getRelation(dist, i) < 25)
				decRelation(src, i);
		}
		return incRelation(src, dist);
	}

// マーク図形を塗る色を取得する
	Color getColor(int no) {
		if(no == 0)
			return Color.red;
		else if(no == gLine)
			return new Color(Color.HSBtoRGB((float)(.5-getRelation(0,no)/200.),
				(float)1., (float)1.));
		else if(no == bLine)
			return new Color(Color.HSBtoRGB((float)(.5+getRelation(0,no)/200.),
				(float)1., (float)1.));

		int p = getRelation(0, no);
		int g = getRelation(no, gLine);
		int b = getRelation(no, bLine);

		double h, s;

		h = Math.atan2((g - b) * (Math.sqrt(100. * p - p * p)) / (50. * (g + b)),
			p / 50. - 1.) / (2. * Math.PI);
		if(h < 0.)
			h += 1.;

		s = Math.sqrt(Math.pow(p / 50. - 1., 2.)
			+ Math.pow((g - b) * (Math.sqrt(100. * p - p * p)) / (50. * (g + b)),
			2.));

		return new Color(Color.HSBtoRGB((float)h, (float)s, (float)1.));
	}
}

