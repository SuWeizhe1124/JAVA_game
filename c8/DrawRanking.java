// DrawRanking.java
// written by mnagaku

import java.awt.*;

/**
 * DrawRankingクラス<br>
 * ランキング表を描く。
 * @author mnagaku
 */
class DrawRanking implements Draw {
	int x = 70;
	int y_base = 70;
	int y_offset = 13;

	String names[] = null;

	DrawRanking() {}


/**
 * ランキング表の設定。
 * ランキング表に描画する文字列のリストを設定する。
 * @param names ランキング表に描画する文字列のリスト
 */
	void setRanking(String[] names) {
		this.names = names;
	}


/**
 * 描画。
 * 保持している情報に基づいて、ランキング表を描画する。
 * @param g 描画面
 * @param pln プレーン
 */
	public boolean drawing(Graphics g, Plane pln) {
		if(names == null)
			return true;
		g.setColor(Color.white);
		g.setFont(new Font("Monospaced", Font.PLAIN, 12));
		for(int i = 0; i < names.length; i++)
			g.drawString(names[i], x, y_base + i * y_offset);
		return true;
	}
}
