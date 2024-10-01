// Kuneris.java
// written by mnagaku

import java.util.*;
import java.awt.event.*;


/** Kunerisクラス */
public class Kuneris extends Game2D {


/**
 * コンストラクタ。
 * 画面サイズとメインループの速度、キーリピートの設定を行う。
 */
	public Kuneris() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 120;
		KEY_SPEED = 60;
		KEY_DELAY = 3;
	}


/**
 * アプリケーションとして動作する場合の開始位置。
 * Game2DクラスのstartGame()を呼ぶ。
 */
	public static void main(String args[]) {
		startGame("Kuneris");
	}



/**
 * KunerisMainクラス<br>
 * ゲーム本体の処理を行う。
 * @author mnagaku
 */
	public class KunerisMain extends Game2DMain {

/** 画像ファイルの拡張子 */
		static final String GRP_EXTENSION = ".gif";
/** 落ちてくるブロックの形のデータ表現。
    くねくね以外のブロックの全ての姿勢は、
    単位矩形3x3マスの中に収まる */
		final int blockChips[][][] = {
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{5, 5, 5}, {0, 0, 5}, {0, 0, 0}},
			{{5, 5, 0}, {5, 0, 0}, {5, 0, 0}},
			{{5, 0, 0}, {5, 5, 5}, {0, 0, 0}},
			{{0, 5, 0}, {0, 5, 0}, {5, 5, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{0, 0,10},{10,10,10}, {0, 0, 0}},
			{{10,10,0}, {0,10, 0}, {0,10, 0}},
			{{10,10,10},{10,0, 0}, {0, 0, 0}},
			{{10,0, 0}, {10,0, 0}, {10,10,0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{15,15,15},{0,15, 0}, {0, 0, 0}},
			{{15,0, 0}, {15,15,0}, {15,0, 0}},
			{{0,15, 0},{15,15,15}, {0, 0, 0}},
			{{0,15, 0},{15,15, 0}, {0,15, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{18,18,0}, {0,18,18}, {0, 0, 0}},
			{{0,18, 0}, {18,18,0}, {18,0, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{0,21,21}, {21,21,0}, {0, 0, 0}},
			{{21,0, 0}, {21,21,0}, {0,21, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{23,0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
		};
/** ブロックの積まれ具合を表す */
		int[][] fieldMap;
/** 落ちている途中のブロックの座標 */
		int x, y;
/** ハイスコア */
		int hiScore = 0;
/** プレイ中のスコア */
		int score = 0;
/** 残機数 */
		int last = 2;
/** くねくねブロックを成す単位矩形それぞれの座標 */
		int[] tailX, tailY;
/** メインループが回る度にカウントアップ */
		long loopCount = 1;
/** 落ちてくるブロック全6種を記憶 */
		BlockDataVector[] blockDatas;
/** 落ちている途中のブロックの種類 */
		int nowBlockNo = -1;
/** 次に落ちてくるブロックの種類 */
		int nextBlockNo = -1;
/** 落ちている途中のブロックのデータ */
		BlockData nowBlock;
/** ゲーム進行上のモード。0:タイトル 1:ゲーム -1:ゲームオーバ */
		int mode = 0;
/** 飛び散るブロックのリスト */
		CrashBlockList crashBlockList;



/** 落ちてくるブロックを表すBlockDataクラス */
		class BlockData {

/** ブロックの大きさ */
			int width, height;
/** ブロックが単位矩形をどのように組み合わせたものか */
			int[][] chips;
/** ブロックの画像の番号 */
			int grpNo;


/** コンストラクタ */
			BlockData(int grpNo, int width, int height, int[][] array) {
				this.grpNo = grpNo;
				this.width = width;
				this.height = height;
				chips = array;
			}
		}



/** ブロックの種類を表すBlockDataVectorクラス */
		class BlockDataVector extends Vector {

/** ブロックの回転状態 */
			int nowIndex = 0;


/** ブロックの回転状態を初期化する */
			void clearIndex() {
				nowIndex = 0;
			}


/** 現在の姿勢のブロックを取得する */
			BlockData getData() {
				BlockData ret = (BlockData)elementAt(nowIndex);
				nowIndex = ((nowIndex + 1) % size());
				return ret;
			}


/** 1つ前の姿勢のブロックが取得できる状態に戻す */
			void rollBack() {
				nowIndex = (nowIndex - 2 + size()) % size();
			}
		}



/** 弾け飛ぶ単位矩形のリストを表すCrashBlockList */
		class CrashBlockList {

/** 弾け飛ぶ単位矩形のリストを保持 */
			Vector blocks;
/** メインループ1回前の時点での弾け飛ぶ単位矩形の数を記録 */
			int prevCount = 0;


/** コンストラクタ */
			CrashBlockList() {
				blocks = new Vector();
			}


/** 消えるラインを成す単位矩形を弾け飛ぶ単位矩形リストに登録する */
			void setCrash(int line) {
				int i;
				for(i = 1; i < 9; i++)
					if(fieldMap[i][line] != 0)
						blocks.addElement(new CrashBlock((i - 1) * 24 + 8,
							line * 24 + 8, fieldMap[i][line]));
			}


/** 弾け飛ばせる。画面外に出てしまったものは消す */
			void crashBlockEffect() {
				int i;
				CrashBlock now;
				for(i = 0; i < blocks.size(); i++) {
					now = (CrashBlock)blocks.elementAt(i);
					if(now.x < -24 || now.x > 300 || now.y > 400) {
						blocks.removeElementAt(i);
						i--;
						continue;
					}
					sprite.setPlaneGrp(200 + i, 0, now.grpNo);
					sprite.setPlanePos(200 + i, now.x, now.y);
					now.x += now.vx;
					now.y += now.vy;
					now.vy++;
				}
				for(; i < prevCount; i++)
					sprite.setPlaneView(200 + i, false);
				prevCount = blocks.size();
			}


/** 現存する弾け飛ぶ単位矩形の数を返す */
			int crashBlockCount() {
				return blocks.size();
			}
		}



/** 弾け飛ぶ単位矩形を表すCrashBlockクラス */
		class CrashBlock {

/** 画面上の座標 */
			int x, y;
/** 飛ぶ速度 */
			int vx, vy;
/** 画像の番号 */
			int grpNo;


/** コンストラクタ。速度はランダムに初期化 */
			CrashBlock(int x, int y, int grpNo) {
				this.x = x;
				this.y = y;
				this.grpNo = grpNo;
				vx = (int)(Math.random() * 33.) - 16;
				vy = -(int)(Math.random() * 16.);
			}
		}



/** コンストラクタ */
		public KunerisMain() {
			int i, j;

			crashBlockList = new CrashBlockList();

			tailX = new int[8];
			tailY = new int[8];

// 落ちブロックデータ生成
			blockDatas = new BlockDataVector[6];
			for(i = 0; i < 6; i++)
				blockDatas[i] = new BlockDataVector();
			blockDatas[0].addElement(new BlockData(1, 2, 3, blockChips[1]));
			blockDatas[0].addElement(new BlockData(2, 3, 2, blockChips[2]));
			blockDatas[0].addElement(new BlockData(3, 2, 3, blockChips[3]));
			blockDatas[0].addElement(new BlockData(4, 3, 2, blockChips[4]));
			blockDatas[1].addElement(new BlockData(6, 2, 3, blockChips[6]));
			blockDatas[1].addElement(new BlockData(7, 3, 2, blockChips[7]));
			blockDatas[1].addElement(new BlockData(8, 2, 3, blockChips[8]));
			blockDatas[1].addElement(new BlockData(9, 3, 2, blockChips[9]));
			blockDatas[2].addElement(new BlockData(11, 2, 3, blockChips[11]));
			blockDatas[2].addElement(new BlockData(12, 3, 2, blockChips[12]));
			blockDatas[2].addElement(new BlockData(13, 2, 3, blockChips[13]));
			blockDatas[2].addElement(new BlockData(14, 3, 2, blockChips[14]));
			blockDatas[3].addElement(new BlockData(16, 2, 3, blockChips[16]));
			blockDatas[3].addElement(new BlockData(17, 3, 2, blockChips[17]));
			blockDatas[4].addElement(new BlockData(19, 2, 3, blockChips[19]));
			blockDatas[4].addElement(new BlockData(20, 3, 2, blockChips[20]));
			blockDatas[5].addElement(new BlockData(22, 1, 1, blockChips[22]));
// フィールドの埋まり具合データ生成
			fieldMap = new int[10][17];
			for(i = 1; i < 9; i++)
				for(j = 0; j < 16; j++)
					fieldMap[i][j] = 0;
			for(i = 0; i < 10; i++)
				fieldMap[i][16] = 32;
			for(i = 0; i < 17; i++) {
				fieldMap[0][i] = 32;
				fieldMap[9][i] = 32;
			}
// 画像の読み込み
			for(i = 0; i < 24; i++)
				sprite.addGrp(i, "grp"+(i/10)+(i%10)+GRP_EXTENSION);
			sprite.waitLoad();
// 背景画像の設定
			sprite.setPlaneGrp(0, 0, 0);
// ブロックの表示準備
			getBlock();
			sprite.setPlanePos(140, x * 24 + 8, y * 24 + 8);
// 文字表示の準備
			sprite.setPlaneString(150, "NextBlock");
			sprite.setPlanePos(150, 214, 9);

			sprite.setPlaneString(151, "HiScore");
			sprite.setPlanePos(151, 216, 110);
			sprite.setPlaneString(152, scoreString(7, hiScore));
			sprite.setPlanePos(152, 216, 128);

			sprite.setPlaneString(153, "Score");
			sprite.setPlanePos(153, 216, 160);
			sprite.setPlaneString(154, scoreString(7, score));
			sprite.setPlanePos(154, 216, 178);

			sprite.setPlaneString(155, "Last");
			sprite.setPlanePos(155, 216, 210);
			sprite.setPlaneString(156, Integer.toString(last));
			sprite.setPlanePos(156, 248, 228);
// BGMとSE
			sp.addBgm(1, "09.au");
			sp.addSe(1, "01_s01.au");
			sp.addSe(2, "02_s02.au");
			sp.addSe(3, "03_s03.au");
			sp.addSe(4, "04_t01.au");
			sp.addSe(5, "05_t02.au");
			sp.addSe(6, "06_t03.au");
			sp.addSe(7, "07_t04.au");
			sp.addSe(8, "08_t05.au");
			sp.addSe(9, "09_e01.au");
			sp.addSe(10, "10_e02.au");
			sp.addSe(11, "11_e03.au");
			sp.playBgm(1);
		}


/** メインループ。モードに応じて処理を変える */
		public boolean mainLoop() {
			boolean ret = true;

			switch(mode) {
				case 0:
					ret = titleMode();
					break;
				case 1:
					ret = gameMode();
					break;
				case -1:
					ret = gameOverMode();
					break;
			}
// モードに関わらず弾け飛ぶ単位矩形がある場合、処理する
			crashBlockList.crashBlockEffect();
			return ret;
		}



/** タイトル表示 */
		boolean titleMode() {
			InputEventTiny ket;
			int i, j;

			sprite.setPlaneString(160, "Kuneris");
			sprite.setPlaneFont(160, null, -1, 55);
// メインループを回す度に色を徐々に変化させる
			sprite.setPlaneColor(160, 255 - (int)(loopCount & 0xff),
				255 - (int)((loopCount & 0xff00) >> 8),
				255 - (int)((loopCount & 0xff0000) >> 16));
			sprite.setPlanePos(160, 10, 150);

			sprite.setPlaneString(161, "Push Space Key to Start");
			sprite.setPlaneFont(161, null, -1, 17);
			sprite.setPlaneColor(161, 255, 255, 255);
			sprite.setPlanePos(161, 14, 230);

			loopCount *= 2;
			loopCount &= 0xffffff;
			if(loopCount == 0)
				loopCount = 1;

			mouseQ.removeAllElements();
// スペースキーが押されたらゲームプレイの準備をしてmodeを1へ
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED
					|| ket.getKeyCode() != KeyEvent.VK_SPACE)
					continue;

				sprite.setPlaneView(160, false);
				sprite.setPlaneView(161, false);
				mode = 1;

				sp.playSe(1);

				loopCount = 0;
				score = 0;
				last = 2;
				nowBlockNo = -1;
				nextBlockNo = -1;

				for(i = 1; i < 9; i++)
					for(j = 0; j < 16; j++)
						fieldMap[i][j] = 0;

				getBlock();
				buildBrocks();
				sprite.setPlanePos(140, x * 24 + 8, y * 24 + 8);

				sprite.setPlaneString(154, scoreString(7, score));
				sprite.setPlanePos(154, 216, 178);

				sprite.setPlaneString(156, Integer.toString(last));
				sprite.setPlanePos(156, 248, 228);
			}
			return true;
		}


/** ゲームプレイ中 */
		boolean gameMode() {
// キー入力に応じて、画を動かす
			InputEventTiny ket;
			int i;

			if(loopCount % (20 - loopCount / 100) == 0) {
				if(dropBlock() == false) {
					mode = -1;
					loopCount = 1;
				}
				if(loopCount == 1900)
					loopCount--;
			}
			loopCount++;
// マウス入力は捨てる(使わない)
			mouseQ.removeAllElements();
// キー入力処理
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED)
					continue;
				switch(ket.getKeyCode()) {
// 下が押されたらブロックを1段落とす
					case KeyEvent.VK_DOWN:
						if(dropBlock() == false) {
							mode = -1;
							loopCount = 1;
						}
						break;
// 上が押されたらブロックを回転
					case KeyEvent.VK_UP:
						if(nowBlockNo == 5)
							break;
						nowBlock = blockDatas[nowBlockNo].getData();
						if(hitBlockMap()) {
							blockDatas[nowBlockNo].rollBack();
							nowBlock = blockDatas[nowBlockNo].getData();
						} else {
							sprite.setPlaneGrp(140, 0, nowBlock.grpNo);
							sp.playSe(5);
						}
						break;
// ブロックを左右に移動
					case KeyEvent.VK_RIGHT:
						x++;
						if(hitBlockMap())
							x--;
						else if(nowBlockNo == 5) {
							for(i = 7; i > 0; i--) {
								tailX[i] = tailX[i - 1];
								tailY[i] = tailY[i - 1];
							}
							tailX[0] = x;
							tailY[0] = y;
						}
						break;
					case KeyEvent.VK_LEFT:
							x--;
						if(hitBlockMap())
							x++;
						else if(nowBlockNo == 5) {
							for(i = 7; i > 0; i--) {
								tailX[i] = tailX[i - 1];
								tailY[i] = tailY[i - 1];
							}
							tailX[0] = x;
							tailY[0] = y;
						}
						break;
				}
			}
// 操作をブロック描画に反映させる
			sprite.setPlanePos(140, x * 24 + 8, y * 24 + 8);
			if(nowBlockNo == 5)
				for(i = 1; i < 8; i++)
					sprite.setPlanePos(140 + i, tailX[i] * 24 + 8,
						tailY[i] * 24 + 8);
			return true;
		}


/** ゲームオーバ */
		boolean gameOverMode() {
			InputEventTiny ket;
			int i, j;

			sprite.setPlaneString(160, "Game Over");
			sprite.setPlaneFont(160, null, -1, 35);
			sprite.setPlaneColor(160, (int)(loopCount & 0xff),
				(int)((loopCount & 0xff00) >> 8),
				(int)((loopCount & 0xff0000) >> 16));
			sprite.setPlanePos(160, 14, 150);

			loopCount *= 2;
			loopCount &= 0xffffff;
			if(loopCount == 0)
				loopCount = 1;

			mouseQ.removeAllElements();
// スペースキーが押されたらタイトル画面の準備をしてmodeを0へ
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED
					|| ket.getKeyCode() != KeyEvent.VK_SPACE)
					continue;

				sprite.setPlaneView(160, false);
				sprite.setPlaneView(161, false);
				mode = 0;
				loopCount = 1;
			}
			return true;
		}


/** 落とすブロックを取得 */
		void getBlock() {
			int i, j;
			i = (int)(Math.random() * 5.);
			if(nextBlockNo < 0) {
				nextBlockNo = i;
				i = (int)(Math.random() * 5.);
			}
			nowBlockNo = nextBlockNo;
			nextBlockNo = i;

			if((int)(Math.random() * 6.) == 0) {
				nextBlockNo = 5;
				for(i = 0; i < 2; i++)
					for(j = 0; j < 3; j++) {
						sprite.setPlaneGrp(130 + i * 3 + j, 0,
							((BlockData)blockDatas[nextBlockNo]
							.elementAt(0)).grpNo);
						sprite.setPlanePos(130+i*3+j, 226+i*24, 26+j*24);
					}
			} else {
				sprite.setPlaneGrp(130, 0,
					((BlockData)blockDatas[nextBlockNo].elementAt(0)).grpNo);
				sprite.setPlanePos(130, 226, 26);
				for(i = 1; i < 6; i++)
					sprite.setPlaneView(130 + i, false);
			}

			nowBlock = (BlockData)blockDatas[nowBlockNo].elementAt(0);
			blockDatas[nowBlockNo].clearIndex();
			sprite.setPlaneGrp(140, 0, blockDatas[nowBlockNo].getData().grpNo);
			x = 3;
			y = 0;

			if(nowBlockNo == 5) {
				tailX[0] = 3;
				tailY[0] = 0;
				for(i = 1; i < 8; i++) {
					sprite.setPlaneGrp(140 + i, 0, nowBlock.grpNo);
					tailX[i] = 3;
					tailY[i] = 0;
					sprite.setPlanePos(140 + i, tailX[i], tailY[i]);
				}
				sp.playSe(6);
			}
			else
				for(i = 1; i < 8; i++)
					sprite.setPlaneView(140 + i, false);
		}


/** 落ちてきたブロックが積まれたブロックにぶつかったかどうか調べる */
		boolean hitBlockMap() {
			int i, j;
			for(i = 0; i < nowBlock.width; i++)
				for(j = 0; j < nowBlock.height; j++)
					if(fieldMap[x + i + 1][y + j] != 0
						&& nowBlock.chips[i][j] != 0)
						return true;
			return false;
		}


/** 積まれているブロックの描画準備 */
		void buildBrocks() {
			int i, j;
			for(i = 0; i < 8; i++)
				for(j = 0; j < 16; j++)
					if(fieldMap[i + 1][j] != 0) {
						sprite.setPlaneGrp(1+j*8+i, 0, fieldMap[i + 1][j]);
						sprite.setPlanePos(1+j*8+i, 8 + i * 24, 8 + j * 24);
					} else
						sprite.setPlaneView(1+j*8+i, false);
		}


/** ブロックを1段落とす */
		boolean dropBlock() {
			int i, j, k, top, bottom, scoreValue = 0, bounasValue = 0;
			y++;
// ブロックが積まれているブロックにぶつかったら
			if(hitBlockMap()) {
				y--;
// ブロックを積む
				if(nowBlockNo == 5) {
					tailX[0] = x;
					tailY[0] = y;
					for(i = 0; i < 8; i++)
						fieldMap[tailX[i]+1][tailY[i]] = nowBlock.chips[0][0];
					top = tailY[7];
					bottom = y + 1;
					for(i = 0; i < 6; i++)
						if(tailX[i] != tailX[i + 2]
							&& tailY[i] != tailY[i + 2])
							bounasValue++;
				} else {
					for(i = 0; i < nowBlock.width; i++)
						for(j = 0; j < nowBlock.height; j++)
							if(nowBlock.chips[i][j] != 0)
								fieldMap[x+i+1][y+j] = nowBlock.chips[i][j];
					top = y;
					bottom = y + nowBlock.height;
				}
// 消えるラインがないかcheck
				for(j = top; j < bottom; j++) {
					for(i = 1; i < 9; i++)
						if(fieldMap[i][j] == 0)
							break;
					if(i == 9) {
						crashBlockList.setCrash(j);
						for(k = j; k > 0; k--)
							for(i = 1; i < 9; i++)
								fieldMap[i][k] = fieldMap[i][k - 1];
						for(i = 1; i < 9; i++)
							fieldMap[i][0] = 0;
						scoreValue++;
					}
				}
// 点数計算
				if(scoreValue != 0) {
					score += Math.pow(2, scoreValue + bounasValue - 1);
					score += crashBlockList.crashBlockCount();
					sprite.setPlaneString(154, scoreString(7, score));
					sprite.setPlanePos(154, 216, 178);
					sp.playSe(7);
				}
// 次に落とすブロックの準備
				buildBrocks();
				getBlock();
// ブロックが積み上がっていたら
				if(hitBlockMap()) {
					last--;
					loopCount = 0;

					for(i = 0; i < 16; i++)
						crashBlockList.setCrash(i);

					for(i = 1; i < 9; i++)
						for(j = 0; j < 16; j++)
							fieldMap[i][j] = 0;

					buildBrocks();
// 3回積み上げてしまうとゲームオーバ
					if(last < 0) {
						if(score > hiScore) {
							hiScore = score;
							sprite.setPlaneString(152,scoreString(7,hiScore));
							sprite.setPlanePos(152, 216, 128);
							sprite.setPlaneString(161, "!! Top Score !!");
							sprite.setPlaneFont(161, null, -1, 19);
							sprite.setPlaneColor(161, 255, 255, 255);
							sprite.setPlanePos(161, 40, 230);
							sp.playSe(9);
						} else
							sp.playSe(11);
						return false;
					} else {
						sprite.setPlaneString(156, Integer.toString(last));
						sprite.setPlanePos(156, 248, 228);
						sp.playSe(11);
					}

					getBlock();

				}
// くねくねブロックを1段落とす
			} else if(nowBlockNo == 5) {
				for(i = 7; i > 0; i--) {
					tailX[i] = tailX[i - 1];
					tailY[i] = tailY[i - 1];
				}
				tailX[0] = x;
				tailY[0] = y;
			}
			return true;
		}


/** 点数を表す文字列の生成 */
		String scoreString(int w, int c) {
			String ret;
			ret = Integer.toString(c);
			while(ret.length() < w)
				ret = "0" + ret;
			return ret;
		
		}
	}
}

