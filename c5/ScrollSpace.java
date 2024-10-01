// ScrollSpace.java
// written by mnagaku

import java.awt.*;

/**
 * ScrollSpaceクラス<br>
 * 背景用にスクロールする星空を描く。
 * @author mnagaku
 */
class ScrollSpace implements Draw {

/** 星の位置を記録 */
	int dot[] = new int[40];
/** 星のスクロール状態を記録 */
	int count = 0;
/** 描画面の大きさ */
	int w, h;


/**
 * コンストラクタ。
 * 描画準備。
 * @param w 描画面の幅
 * @param h 描画面の高さ
 */
	ScrollSpace(int w, int h) {
		this.w = w;
		this.h = h - 1;
		for(int i = 0; i < 40; i++) {
			dot[i] = (int)(Math.random() * w);
		}
	}


/**
 * 描画。
 * 呼ばれる度に星の表示位置をずらしていくことでスクロールを実現する。
 * @param g 描画面
 * @param pln プレーン
 */
	public boolean drawing(Graphics g, Plane pln) {
		dot[count] = (int)(Math.random() * w);
		count++;
		count %= 40;

		g.setColor(Color.black);
		g.fillRect(0, 0, w, h);

		g.setColor(Color.white);
		for(int i = 0; i < 40; i++, count = (count + 1) % 40) {
			int size = dot[count] % 3;
			g.drawLine(dot[count] - size, h - (i * 10),
				dot[count] + size, h - (i * 10));
			g.drawLine(dot[count], h - (i * 10) - size,
				dot[count], h - (i * 10) + size);
		}

		return true;
	}
}
