// Sample.java
// written by mnagaku

import java.awt.event.*;

/**
 * Sampleクラス<br>
 * Game2Dクラスの使い方のサンプル。
 * @author mnagaku
 */
public class Sample extends Game2D {


/**
 * コンストラクタ。
 * 画面サイズとメインループの速度、キーリピートの設定を行う。
 */
	public Sample() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 80;
		KEY_SPEED = 40;
		KEY_DELAY = 3;
	}


/**
 * アプリケーションとして動作する場合の開始位置。
 * Game2DクラスのstartGame()を呼ぶ。
 */
	public static void main(String args[]) {
		startGame("Sample");
	}



/**
 * SampleMainクラス<br>
 * ゲーム本体の処理を行う。
 * @author mnagaku
 */
	public class SampleMain extends Game2DMain {

/** 画像ファイルの拡張子。 */
		static final String GRP_EXTENSION = ".gif";


/**
 * コンストラクタ。
 * 画像や音データの読み込みなどを行う。
 */
		public SampleMain() {
// GRP読み込み
			sprite.addGrp(1, "own" + GRP_EXTENSION);
			sprite.addGrp(2, "benemy2" + GRP_EXTENSION);
			sprite.waitLoad();
// GRP表示
			sprite.setPlaneGrp(1, 0, 1);
			sprite.setPlanePos(1, 100, 300);
			sprite.setPlaneGrp(2, 0, 2);
			sprite.setPlanePos(2, 200, 100);
// 背景
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
// 文字列の表示
			sprite.setPlaneString(3, "string");
			sprite.setPlanePos(3, 100, 200);
			sprite.setPlaneColor(3, 255, 255, 255);
// BGM
			sp.addBgm(1, "09.au");
			sp.playBgm(1);
		}


/**
 * メインループ1回分の処理。
 * マウス、キーの入力を処理して、キャラクタの描画位置を変える。
 */
		public boolean mainLoop() {
			InputEventTiny ket;

			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED)
					continue;
				switch(ket.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						sprite.setPlaneMov(1, 0, 2);
						break;
					case KeyEvent.VK_UP:
						sprite.setPlaneMov(1, 0, -2);
						break;
					case KeyEvent.VK_RIGHT:
						sprite.setPlaneMov(1, 2, 0);
						break;
					case KeyEvent.VK_LEFT:
						sprite.setPlaneMov(1, -2, 0);
						break;
				}
			}
			while((ket = (InputEventTiny)(mouseQ.dequeue())) != null) {
				if(ket.getID() != MouseEvent.MOUSE_PRESSED)
					continue;
				sprite.setPlanePos(2, ket.getX(), ket.getY());
			}
			return true;
		}
	}
}
