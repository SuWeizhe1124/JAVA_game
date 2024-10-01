// Stg.java
// written by mnagaku

import java.io.*;
import java.util.*;
import java.awt.event.*;


/** Stgクラス */
public class Stg extends Game2D {


/**
 * コンストラクタ。
 * 画面サイズとメインループの速度、キーリピートの設定を行う。
 */
	public Stg() {
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
		startGame("Stg");
	}



/**
 * StgMainクラス<br>
 * ゲーム本体の処理を行う。
 * @author mnagaku
 */
	public class StgMain extends Game2DMain {

/** 画像ファイルの拡張子。 */
		static final String GRP_EXTENSION = ".gif";
/** ハイスコア */
		int hiScore = 0;
/** プレイ中のスコア */
		int score = 0;
/** 残機数 */
		int last;
/** ゲーム進行上のモード。
    1～3:ゲーム(面) 0:ゲーム開始 -1:タイトル -2:ゲームオーバ -3:ゲームクリア */
		int mode = -1;
/** 文字列 */
		String str = "";
/** ゲーム内に登場する物体を管理 */
		GameObjects gos;
/** メインループが回る度にカウントアップ */
		int mainLoopCount = 0;


/** コンストラクタ */
		public StgMain() {
			int i, j;

			gos = new GameObjects(10, sprite, keyQ);
// 画像の読み込み
			sprite.addGrp(1, "enemy1"+GRP_EXTENSION);
			sprite.addGrp(2, "enemy2"+GRP_EXTENSION);
			sprite.addGrp(3, "enemy3"+GRP_EXTENSION);
			sprite.addGrp(4, "menemy1"+GRP_EXTENSION);
			sprite.addGrp(5, "benemy1"+GRP_EXTENSION);
			sprite.addGrp(6, "own"+GRP_EXTENSION);

			sprite.addGrp(11, "benemy2"+GRP_EXTENSION);

			sprite.addGrp(16, "benemy3"+GRP_EXTENSION);
			sprite.addGrp(17, "enemy10"+GRP_EXTENSION);

			sprite.waitLoad();

// 背景画像の設定
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
// BGM
			sp.addBgm(1, "09.au");
			sp.playBgm(1);
		}


/** メインループ。モードに応じて処理を変える */
		public boolean mainLoop() {
			InputEventTiny ket;
/* 2002番プレーンを一定時間表示後に非表示にする */
			mainLoopCount++;
			if(mainLoopCount == 20)
				sprite.setPlaneView(2002, false);

			mouseQ.removeAllElements();
			switch(mode) {
// ゲームクリア
				case -3:
					gos.clearGOS();

					sprite.setPlaneString(2002, "Game clear");
					sprite.setPlanePos(2002, 100, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					sprite.setPlaneString(2001, "Hit any key");
					sprite.setPlanePos(2001, 100, 250);
					sprite.setPlaneColor(2001, 255, 255, 255);
					mainLoopCount = 0;

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = -1;
					break;
// ゲームオーバ
				case -2:
					gos.clearGOS();

					sprite.setPlaneString(2002, "Game over");
					sprite.setPlanePos(2002, 100, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					sprite.setPlaneString(2001, "Hit any key");
					sprite.setPlanePos(2001, 100, 250);
					sprite.setPlaneColor(2001, 255, 255, 255);
					mainLoopCount = 0;

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = -1;
					break;
// タイトル表示
				case -1:
					sprite.setPlaneString(2002, "An ordinary shooting game");
					sprite.setPlanePos(2002, 50, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					sprite.setPlaneString(2001, "Hit any key");
					sprite.setPlanePos(2001, 100, 250);
					sprite.setPlaneColor(2001, 255, 255, 255);
					mainLoopCount = 0;

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = 0;
					break;
// ゲーム開始
				case 0:
					sprite.setPlaneView(2002, false);
					gos.addGOOwn("Own", CANVAS_SIZE_W / 2 - 16, 300, 1000);
					mode = 1;
					openSequence("stage" + mode + ".txt");
					str = "Stage: " + mode;
					sprite.setPlaneString(2002, str);
					sprite.setPlanePos(2002, 100, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					mainLoopCount = 0;
					break;
// ゲーム中
				default:
					if(gos.isAliveOwn() == false) {
						mode = -2;
						sprite.setPlaneView(2000, false);
						sprite.setPlaneView(2001, false);
						keyQ.removeAllElements();
						break;
					}
					if(readSequence() == false) {
						mode++;

						if(mode > 3) {
							mode = -3;
							keyQ.removeAllElements();
							break;
						}

						openSequence("stage" + mode + ".txt");
						str = "Stage: " + mode;
						sprite.setPlaneString(2002, str);
						sprite.setPlanePos(2002, 100, 200);
						sprite.setPlaneColor(2002, 255, 255, 255);
						mainLoopCount = 0;
						keyQ.removeAllElements();
						break;
					}

					str = "Score: " + toString(gos.getScore(), 5, "0");
					sprite.setPlaneString(2000, str);
					str = "";
					sprite.setPlaneColor(2000, 255, 255, 255);
					for(int i = 0; i < gos.getLast() - 1; i++)
						str += "A";
					sprite.setPlaneString(2001, str);
					sprite.setPlanePos(2001, 0, 384);
					sprite.setPlaneColor(2001, 255, 0, 0);

					gos.moveAll();
					gos.hitCheckOwn();
					gos.hitCheckOwnBow();
			}
			return true;
		}

// シーケンスファイル読み込み関連 ------------------------------------

/** シーケンスファイル上の参照位置 */
		int sequenceCount = 0;
/** シーケンスファイル読み込み用 */
		BufferedReader sequenceReader = null;


/** シーケンスファイルを開く */
		boolean openSequence(String filename) {
			sequenceCount = 0;

			try {
				if(sequenceReader != null)
					sequenceReader.close();
				InputStream is = getClass().getResource(filename).openStream();
				InputStreamReader isr = new InputStreamReader(is);
				sequenceReader = new BufferedReader(isr);
			} catch(Exception e) {e.printStackTrace();}
			return true;
		}


/** シーケンスファイルを読む */
		boolean readSequence() {

			if(sequenceCount < 0) {
				GameObject go;
				Enumeration enum = gos.elements();
				while(enum.hasMoreElements()) {
					go = ((GameObject)(enum.nextElement()));
					if(go.attribute == GameObject.ENEMY)
						return true;
				}
				sequenceCount = 0;
			}

			if(sequenceCount > 0) {
				sequenceCount--;
				return true;
			}

			try {
				String str = sequenceReader.readLine();
				if(str.length() == 0)
					return true;
				StringTokenizer st = new StringTokenizer(str);
				String cmd = st.nextToken();
				if(cmd.equals("sleep") == true) {
					sequenceCount = Integer.parseInt(st.nextToken());
					return true;
				} else if(cmd.equals("wait") == true) {
					sequenceCount = -1;
					return true;
				} else {
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					gos.addGO(cmd, x, y, 1000);
					return true;
				}
			} catch(Exception e) {
				return false;
			}
		}
//--------------------------------------------------------------------


/** 点数を表す文字列の生成 */
		String toString(int number, int length, String head) {
			String ret = new String("" + number);
			for(int i = ret.length(); i < length; i++)
				ret = head + ret;
			return ret;
		}
	}
}
