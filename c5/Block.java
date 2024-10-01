// Block.java
// written by mnagaku

import java.awt.event.*;
import java.awt.*;

/**
 * Blockクラス<br>
 * ブロックの落ちる処理を実現してみる
 * @author mnagaku
 */
public class Block extends Game2D {


/**
 * コンストラクタ。
 * 画面サイズとメインループの速度、キーリピートの設定を行う。
 */
	public Block() {
		CANVAS_SIZE_W = 200;
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
		startGame("Block");
	}



/**
 * BlockMainクラス<br>
 * ゲーム本体の処理を行う。
 * @author mnagaku
 */
	public class BlockMain extends Game2DMain {

/** 新規ブロック生成フラグ。trueの時、新規作成 */
		boolean newBlock = true;
/** ブロックを何個まで作ったか示す値 */
		int blockCount = 1;
/** ブロックを1段落とすまでの時間をカウントする */
		int blockWait = 0;
/** ブロックの積まれ具合を表す */
		int map[][] = new int[10][17];
/** ブロックの描画に使われる色を表す */
		Color colorList[] = {Color.red, Color.green, Color.blue};
/**
 * ブロックの描画に使われる
 * Drawインターフェイスを備えたDrawRectクラスのインスタンス
 */
		DrawRect nowBlock;


/**
 * コンストラクタ。
 * mapの初期化と背景設定。
 */
		public BlockMain() {
// blockの積み具合を管理するmap[][]の初期化
			for(int i = 0; i < 10; i++)
				for(int j = 0; j < 17; j++)
					map[i][j] = 1;
			for(int i = 1; i < 9; i++)
				for(int j = 0; j < 16; j++)
					map[i][j] = 0;
// 背景の設定
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
		}


/**
 * メインループ1回分の処理。
 * コンストラクタ。mapの初期化と背景設定。
 */
		public boolean mainLoop() {
// マウスイベントは捨てる
			mouseQ.removeAllElements();
// 新しいblockを作る
			if(newBlock) {
				sprite.setPlaneDraw(blockCount, nowBlock = new DrawRect(25, 25,
					colorList[(int)(Math.random() * 3)]));
				sprite.setPlanePos(blockCount,
					(int)(Math.random() * 8) * 25, 0);
				newBlock = false;
				blockWait = 0;
			}
// mainLoop()10回毎に1マス落とす
			blockWait++;
			if(blockWait % 10 == 0) {
				if(map[sprite.getPlanePosX(blockCount)/25+1]
					[sprite.getPlanePosY(blockCount)/25+1] != 0) {
					map[sprite.getPlanePosX(blockCount)/25+1]
						[sprite.getPlanePosY(blockCount)/25] = 1;
					nowBlock.darker();
					newBlock = true;
					blockCount++;
					keyQ.removeAllElements();
					return true;
				}
				sprite.setPlaneMov(blockCount, 0, 25);
			}
// キー入力処理
			InputEventTiny ket;
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED)
					continue;
				switch(ket.getKeyCode()) {
// 下が押されたら1マス落とす
					case KeyEvent.VK_DOWN:
					if(map[sprite.getPlanePosX(blockCount)/25+1]
						[sprite.getPlanePosY(blockCount)/25+1] == 0)
						sprite.setPlaneMov(blockCount, 0, 25);
						break;
					case KeyEvent.VK_UP:
						break;
// 左右に移動
					case KeyEvent.VK_RIGHT:
						if(map[sprite.getPlanePosX(blockCount)/25+2]
							[sprite.getPlanePosY(blockCount)/25] == 0)
							sprite.setPlaneMov(blockCount, 25, 0);
						break;
					case KeyEvent.VK_LEFT:
						if(map[sprite.getPlanePosX(blockCount)/25]
							[sprite.getPlanePosY(blockCount)/25] == 0)
							sprite.setPlaneMov(blockCount, -25, 0);
						break;
				}
			}
			return true;
		}
	}
}
