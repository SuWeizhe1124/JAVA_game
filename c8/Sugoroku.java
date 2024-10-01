// Sugoroku.java
// written by mnagaku

import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;

/**
 * Sugoroku
 */
public class Sugoroku extends Game2D {
	static String params[];

	public Sugoroku() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 80;
		KEY_SPEED = 40;
		KEY_DELAY = 3;
	}

/**
 * Gameのコンストラクタの実体。
*/
	public static void main(String args[]) {
		params = args;
		startGame("Sugoroku");
	}


/**
 * SugorokuMainクラス<br>
 * ゲーム本体の処理を行います。
 * @author mnagaku
 */
	public class SugorokuMain extends Game2DMain {
// 同時に存在できるプレイヤ数の上限
		final int MAX_MEMBER = 8;
// サーバ名(もしくはIPアドレス)
		String sName = "127.0.0.1";
// サーバの待ち受けポート番号
		int portNo = 4001;
// clientID = time(msec) + IPアドレス
		long myID[] = {System.currentTimeMillis(), 0};

		boolean demo = false;
		boolean nowKomaView[] = new boolean[MAX_MEMBER];
		int nowMove = 0, nowForward, clearCount = 1, mode = 0;
		String keyword[] = {"MAP", "NEXT", "RANKING",
			"NEWPLAYER", "GOALPLAYER", "USER"};
		Color colorList[] = {Color.red, Color.green, Color.blue,
			Color.cyan, Color.magenta, Color.yellow, Color.orange, Color.pink};
		KomaVector komaList = new KomaVector();
		Socket usocket;
		ObjectOutputStream os;
		ObjectInputStream is;


// 受信専用スレッド
		class ReciveThread extends Thread {
			public void run() {
				while(true) {
					SugorokuMain.this.recivedString
						= SugorokuMain.this.reciveString();
				}
			}
		}


// 受信文字列から表示する盤面の状態を取得
		int[] setKomaView(String str) {
			boolean recivedKomaView[] = new boolean[MAX_MEMBER];
			int i, j, ret[] = {0, 0}, readMode = -1;
			StringTokenizer st = new StringTokenizer(str);

			for(i = 0; i < MAX_MEMBER; i++)
				recivedKomaView[i] = false;

			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				for(i = 0; i < keyword.length; i++)
					if(token.indexOf(keyword[i]) == 0) {
						readMode = i;
						break;
					}
				if(i < keyword.length)
					continue;
				switch(readMode) {
// MAP
					case 0:
						int id = Integer.parseInt(token);
						int pos = Integer.parseInt(st.nextToken());
						int endPos = Integer.parseInt(st.nextToken());
						String name = st.nextToken();
						recivedKomaView[id] = true;
						Koma tmpKoma = komaList.saerch(id);
						if(tmpKoma != null)
							tmpKoma.nowPos = pos;
						if(tmpKoma == null || tmpKoma.endPos != endPos
							|| tmpKoma.name.indexOf(name) != 0) {
							if(nowKomaView[id])
								tmpKoma.preDelete(
									tmpKoma.name + "が退出しました");
							Koma k = new Koma(id,colorList[id],pos,endPos,name);
							sprite.setPlaneDraw(200 + id * 2, k);
							komaList.addElement(k);
						}
						break;
// NEXT
					case 1:
						ret[0] = Integer.parseInt(token);
						ret[1] = Integer.parseInt(st.nextToken());
						break;
// RANKING
					case 2:
						String names[] = new String[10];
						names[0] = token;
						st.nextToken();
						for(i = 1; i < 10; i++) {
							names[i] = st.nextToken();
							st.nextToken();
						}
						drawRanking.setRanking(names);
						break;
// NEWPLAYER
					case 3:
						st.nextToken();
						st.nextToken();
						break;
// GOALPLAYER
					case 4:
						st.nextToken();
						st.nextToken();
						st.nextToken();
						break;
// USER
					case 5:
						break;
				}
			}
// いなくなったユーザに関する処理
			for(i = 0; i < MAX_MEMBER; i++)
				if(nowKomaView[i] && !recivedKomaView[i]) {
					Koma tmpKoma = komaList.saerch(i);
					if(tmpKoma != null)
						tmpKoma.preDelete(tmpKoma.name + "が退出しました");
					komaList.removeElement(tmpKoma);
				}
			nowKomaView = recivedKomaView;

			return ret;
		}


		DrawRanking drawRanking;
		boolean debugFlag = false;

// コンストラクタ
		public SugorokuMain() {
// 背景
			sprite.setPlaneDraw(0, new Ripple(CANVAS_SIZE_W, CANVAS_SIZE_H));
			DrawField field = new DrawField(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(1, field);

			for(int i = 0; i < MAX_MEMBER; i++)
				nowKomaView[i] = false;
// コマンドライン引数からdebugモードで起動するかを取得
		if(getParameter("debug") != null) {
			debugFlag = true;
			System.out.println("デバッグモードで起動します");
		}

// connect
			String str = connectServer();
			if(str == "OK") {
				drawRanking = new DrawRanking();
				sprite.setPlaneDraw(2, drawRanking);
// 受信スレッド開始
				new ReciveThread().start();
			} else {
// error 表示
				sprite.setPlaneString(300, str);
				sprite.setPlanePos(300, 60, 60);
				sprite.setPlaneColor(300, 255, 255, 255);

				str = "MAP ";
				for(int i = 0; i < MAX_MEMBER; i++) {
					str += "" + i + " " + (i * 24 / MAX_MEMBER + 1) + " "
						+ (i * 24 / MAX_MEMBER) + " demo" + i + " ";
				}
				str += "NEXT 0 1";
				setKomaView(str);
				demo = true;
			}
		}


		String recivedString = null;
		boolean movePhase = true;

// メインループ
		public boolean mainLoop() {
// 入力イベントは捨てる
			mouseQ.removeAllElements();
			keyQ.removeAllElements();

			if(mode == 1)
				return true;

			int i;

// 動きが止まっているかcheck
			for(i = 0; i < komaList.size(); i++)
				if(komaList.getKoma(i).isForward()) {
					nowMove = i;
					break;
				}
// 止まっていたら
			if(i >= komaList.size()) {
// 追突check
				for(i = 0; nowMove < komaList.size()
					&& i < komaList.size(); i++) {
					if(i != nowMove && komaList.getKoma(i).nowPos
						== komaList.getKoma(nowMove).nowPos) {
						nowMove = i;
						komaList.getKoma(nowMove).forward(nowForward);
						return true;
					}
				}

// 次を動かす
				if(movePhase) {
					if(demo) {
						nowMove++;
						nowMove %= komaList.size();
						nowForward = (int)(Math.random() * 5.99) + 1;
						komaList.getKoma(nowMove).forward(nowForward);
						movePhase = false;
// 受信check
					} else if(recivedString != null) {
						int[] moveData = setKomaView(recivedString);
						nowMove = moveData[0];
						nowForward = moveData[1];
						recivedString = null;
						komaList.getKoma(nowMove).forward(nowForward);
						movePhase = false;
					}
// クリアcheck
				} else {
					String ss = "OK MAP", goaledString = " GOALPLAYER ";
					for(i = 0; i < komaList.size(); i++) {
						Koma tmpKoma = komaList.getKoma(i);
						if(tmpKoma.nowPos == tmpKoma.endPos) {
							goaledString += " " + tmpKoma.id;
							tmpKoma.preDelete(tmpKoma.name+"がゴールしました");
							nowKomaView[tmpKoma.id] = false;
							komaList.removeElementAt(i);
							i--;
						} else if(nowKomaView[tmpKoma.id])
							ss += " "+tmpKoma.id+" "+tmpKoma.nowPos;
					}
					if(!demo) {
						if(komaList.size() > 0)
							ss += " NEXT "+((nowMove+1)%komaList.size())+" "
								+ ((int)(Math.random() * 5.99) + 1);
						ss += goaledString;
						sendString(ss);
					}
					else if(komaList.size() == 0)
						mode = 1;
					movePhase = true;
				}
			}
			return true;
		}


		boolean parsedArgs = false;
		Hashtable params;

// 実行時引数を取得
		public String getParameter(String name) {
			if(Sugoroku.this.appletFlag) {
				return Sugoroku.this.getParameter(name);
			}
			else if(parsedArgs == false) {
				params = new Hashtable();
				for(int i = 0; i < Sugoroku.this.params.length; i++) {
					if(Sugoroku.this.params[i].indexOf("-") == 0) {
						if(i + 1 < Sugoroku.this.params.length
							&& Sugoroku.this.params[i + 1].indexOf("-") != 0)
							params.put(Sugoroku.this.params[i].substring(1),
								Sugoroku.this.params[i + 1]);
						else
							params.put(Sugoroku.this.params[i].substring(1),
								Sugoroku.this.params[i].substring(1));
					}
				}
				if(params.size() <= 0)
					params = null;
				parsedArgs = true;
			}
			if(params != null)
				return (String)(params.get(name));
			return null;
		}


// サーバに接続
		String connectServer() {
			String ret = "NG", tmpStr;
			try{
// サーバの名前、接続ポート番号を用意し、ソケットを用意
				if(Sugoroku.this.appletFlag)
					sName = getCodeBase().getHost();
				else if((tmpStr = getParameter("server")) != null)
					sName = tmpStr;
				if((tmpStr = getParameter("port")) != null)
					portNo = Integer.parseInt(tmpStr);
				usocket = new Socket(sName, portNo);
			} catch(Exception e) {
				if(debugFlag)
					e.printStackTrace();
				return "サーバへの接続に失敗しました";
			}
			System.out.println(sName + ":" + portNo + "に接続しました");
			byte myAddress[] = usocket.getLocalAddress().getAddress();
			myID[1] = myAddress[0] << 24 | myAddress[1] << 16
				| myAddress[2] << 8 | myAddress[3];
// JOINを送信
			String sendStr = "join noname";
			if(getParameter("name") != null)
				sendStr = "join " + getParameter("name");
			sendStr = sendStr + " " + myID[0] + " " + myID[1];
			if(!sendString(sendStr)) {
				return "joinの送信に失敗しました";
			}
			return "OK";
		}


// サーバから受信
		String reciveString() {
			try {
				is = new ObjectInputStream(usocket.getInputStream());
				String ret = (String)is.readObject();
				if(debugFlag)
					System.out.println("recive: " + ret);
				return ret;
			} catch(Exception e) {
				System.out.println("サーバからの受信に失敗しました");
				if(debugFlag)
					e.printStackTrace();
			}
			return null;
		}


// サーバに送信
		boolean sendString(String str) {
			try {
				os = new ObjectOutputStream(usocket.getOutputStream());
				os.writeObject(str);
				os.flush();
				if(debugFlag)
					System.out.println("send: " + str);
				return true;
			} catch(Exception e) {
				System.out.println("サーバへの送信に失敗しました");
				if(debugFlag)
					e.printStackTrace();
			}
			return false;
		}
	}



// Komaのプール KomaVector
	class KomaVector extends Vector {
		KomaVector() {
			super();
		}

		Koma getKoma(int no) {
			return (Koma)elementAt(no);
		}

		Koma saerch(int id) {
			int i;
			Koma tmpKoma = null;
			for(i = 0; i < size(); i++) {
				tmpKoma = getKoma(i);
				if(tmpKoma.id == id)
					break;
			}
			if(i < size())
				return tmpKoma;
			else
				return null;
		}
	}



// コマを描く
	class Koma implements Draw {
		int id, endPos, nowPos, oldPos, count = 0, targetCount = 0;
		boolean moveMode = false;
		Color color;
		String name = "nothing";
		DrawRect dr;

		int jumpOffset[] = {0, 32, 48, 56, 60, 62, 60, 56, 48, 32, 0};

		Koma(int id, Color color, int startPos, int endPos, String name) {
			this.id = id;
			this.color = color;
			nowPos = startPos;
			this.endPos = endPos;
			this.name = name;

			sprite.setPlaneDraw(100 + id,
				dr = new DrawRect(50, 50, color.darker()));
			Point p = getPoint(endPos);
			sprite.setPlanePos(100 + id, p.x, p.y);

			sprite.setPlaneCenterString(200 + id * 2 + 1, name);
			sprite.setPlaneColor(200 + id * 2 + 1, 255 - color.getRed(),
				255 - color.getGreen(), 255 - color.getBlue());
		}


// コマを描画
		public boolean drawing(Graphics g, Plane pln) {
			g.setColor(color);
			Point p = getPoint(nowPos);
			if(moveMode) {
				Point op = getPoint(oldPos);
				p.x = (int)(op.x + (double)(p.x - op.x) / 10 * count);
				p.y = (int)(op.y+(double)(p.y-op.y)/10*count-jumpOffset[count]);
				count++;
				if(count > 10)
					moveMode = false;
			}
			g.fillOval(p.x, p.y, 50, 50);

			sprite.setPlanePos(200 + id * 2 + 1, p.x + 25, p.y + 17);

			return true;
		}


// コマを消してメッセージを表示
		void preDelete(String info) {
			sprite.delPlane(200 + id * 2);
			sprite.delPlane(200 + id * 2 + 1);
			sprite.delPlane(100 + id);
			sprite.setPlaneDraw(300 + id,
				new DrawInfo(info, color, 300 + id));
		}


// 移動中?
		public boolean isForward() {
			return moveMode;
		}

// 移動
		public boolean forward(int c) {
			if(moveMode)
				return false;
			moveMode = true;
			count = 0;
			targetCount = c;
			oldPos = nowPos;
			nowPos += c;
			nowPos = (nowPos + 24) % 24;
			return true;
		}

// マスの座標を計算
		Point getPoint(int no) {
			no = (no + 24) % 24;
			Point ret = new Point(no * 50, 0);
			if(no >= 6 && no < 13)
				ret.setLocation(250, (no - 5) * 50);
			else if(no >= 13 && no < 18)
				ret.setLocation((17 - no) * 50, 350);
			else if(no >= 18)
				ret.setLocation(0, (24 - no) * 50);
			return ret;
		}
	}



	int drawInfoLine = 0;

// 一定時間お知らせ表示
	class DrawInfo implements Draw {
		int x = 150;
		int y_base = 70;
		int y_offset = 13;

		int count = SPEED, plnNo, ownLine;
		String info;
		Color color;

		DrawInfo(String info, Color color, int plnNo) {
			this.info = info;
			this.color = color;
			this.plnNo = plnNo;
			ownLine = drawInfoLine;
			drawInfoLine = (drawInfoLine + 1 % 25);
		}

		public boolean drawing(Graphics g, Plane pln) {
			g.setColor(color);
			g.setFont(new Font("Monospaced", Font.PLAIN, 12));
			g.drawString(info, x, y_base + ownLine * y_offset);
			if(--count < 0)
				sprite.delPlane(plnNo);
			return true;
		}
	}
}
