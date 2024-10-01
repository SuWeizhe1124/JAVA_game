// DrawField.java
// written by mnagaku

import java.awt.*;

/**
 * DrawFieldクラス<br>
 * 白線でマス目を描く。
 * @author mnagaku
 */
class DrawField implements Draw {

/** 描画範囲の大きさ */
	int w, h;


/**
 * コンストラクタ。
 * 描画準備。
 * @param w 描画範囲の幅
 * @param h 描画範囲の高さ
 */
	DrawField(int w, int h) {
		this.w = w;
		this.h = h;
	}


/**
 * 描画。
 * 保持している情報に基づいて、マス目を描画する。
 * @param g 描画面
 * @param pln プレーン
 */
	public boolean drawing(Graphics g, Plane pln) {
		g.setColor(Color.white);
		g.drawLine(50, 0, 50, 400);
		g.drawLine(250, 0, 250, 400);
		g.drawLine(0, 50, 300, 50);
		g.drawLine(0, 350, 300, 350);
		for(int i = 0; i < 3; i++) {
			g.drawLine(100 + i * 50, 0, 100 + i * 50, 50);
			g.drawLine(100 + i * 50, 350, 100 + i * 50, 400);
		}
		for(int i = 0; i < 5; i++) {
			g.drawLine(0, 100 + i * 50, 50, 100 + i * 50);
			g.drawLine(250, 100 + i * 50, 300, 100 + i * 50);
		}
		return true;
	}
}
