// SugorokuServer.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * SugorokuServer
 */
public class SugorokuServer extends Thread {
// 同時に存在できるプレイヤ数の上限
	static final int MAX_MEMBER = 8;
// マス目の数
	static final int MAX_MAP = 24;
// サーバが待ち受けに使用するポート
	static final int DEFAULT_PORT = 4001;

	static final int MAX_RANKING = 10;

	boolean execFlag = false;
	boolean preAddFlag = false;
	boolean usedID[] = new boolean[MAX_MEMBER];
	boolean debugFlag = false;
	boolean exitFlag = false;
	int nextMoveKoma = 0;
	int reciveOKCount = 0;
	int moveKomaNo = 0;
	int moveCount = (int)(Math.random() * 5.99) + 1;
	String newPlayerString = "", goalPlayerString = "";
	static Hashtable params = new Hashtable();
	KomaVector prePlayer = new KomaVector();
	KomaVector player = new KomaVector();
	KomaVector removeMember = new KomaVector();
	Ranking ranking = new Ranking(MAX_RANKING);


// main()メソッド
	public static void main(String args[]) {
// コマンドライン引数をparamsに格納
		for(int i = 0; i < args.length; i++) {
			if(args[i].indexOf("-") == 0) {
				if(i + 1 < args.length && args[i + 1].indexOf("-") != 0)
					params.put(args[i].substring(1), args[i + 1]);
				else
					params.put(args[i].substring(1), args[i].substring(1));
			}
		}
		(new SugorokuServer()).execute();
	}


// コンストラクタ
	SugorokuServer() {
// 変数の初期化
		for(int i = 0; i < MAX_MEMBER; i++)
			usedID[i] = false;
		ranking.small2big();
	}


// ユーザ接続待ち受け
	public void run() {
		int portNo = DEFAULT_PORT;
		String tmpStr;
// コマンドライン引数からユーザ接続待ち受けポート番号を取得
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("ユーザ接続待ち受けポート番号 : " + portNo);
// コマンドライン引数からdebugモードで起動するかを取得
		if(params.get("debug") != null) {
			debugFlag = true;
			System.out.println("デバッグモードで起動します");
		}
		params = null;
// ユーザ接続待ち受け
		try {
			ServerSocket serversocket = new ServerSocket(portNo);
			while(!exitFlag) {
				Socket usocket = serversocket.accept();
				connectClient(usocket);
			}
		} catch(Exception e) {
			exitFlag = true;
			if(debugFlag)
				e.printStackTrace();
			System.out.println("ユーザ接続待ち受けで問題が発生しました");
		}
	}


// 新規ユーザの接続要求を処理
	boolean connectClient(Socket usocket) {
		byte[] recivedBytes;
		String recived;
// 接続要求文字列の受信
		try {
			ObjectInputStream is
				= new ObjectInputStream((usocket.getInputStream()));
			recived = (String)is.readObject();
		} catch(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			return false;
		}
// 接続要求文字列"join name cID[0] cID[1]"から情報を抽出
		if(recived.indexOf("join") != 0)
			return false;
		StringTokenizer st = new StringTokenizer(recived);
		if(st.countTokens() != 4)
			return false;
		st.nextToken();
		String name = st.nextToken();
		long cID[] = {0, 0};
		cID[0] = Long.parseLong(st.nextToken());
		cID[1] = Long.parseLong(st.nextToken());
// 重複するユーザ情報を既に持っている場合は接続を許可しない
		for(int i = 0; i < prePlayer.size(); i++) {
			Koma tmpKoma = prePlayer.getKoma(i);
			if(tmpKoma.name == name
				|| (tmpKoma.cID[0]==cID[0] && tmpKoma.cID[1]==cID[1]))
				return false;
		}
// execute()のスレッドとぶつからないようにユーザ情報を追加
		preAddFlag = true;
		while(execFlag)
			yield();
		prePlayer.addElement(new Koma(usocket, name, cID));
		System.out.println(name + "(" + cID[0] + "," + cID[1]
			+ ")が加わりました");
		System.out.println("現在のユーザ数 "
			+ (player.size() + prePlayer.size()) + " 人");
		preAddFlag = false;
		return true;
	}


	String keyword[] = {"MAP", "NEXT", "GOALPLAYER"};

// ゲームを進行させる
// 実質的main()処理
	public void execute() {
		int i, j;
		String ss;
		System.out.println("SugorokuServer 起動");
// ユーザ接続待ち受けを開始
		start();
// ゲームを進行させる
		while(!exitFlag) {
// ユーザ追加作業中は待つ
			while(preAddFlag)
				yield();
// ユーザからの返信が全て揃ったら次の移動情報を生成、配信
			if(reciveAllOK()) {
// ユーザ追加作業に待ってもらう
				execFlag = true;
// 受信文字列の取得
				ss = popRecivedString();
// 受信文字列"OK MAP id pos … NEXT id forward GOALPLAYER goaledId …"を解析
				if(player.size() > 0 && ss != null) {
					StringTokenizer st = new StringTokenizer(ss);
					int mode = -1;
					goalPlayerString = "";
					while(st.hasMoreTokens()) {
						String token = st.nextToken();
						for(i = 0; i < keyword.length; i++)
							if(token.indexOf(keyword[i]) == 0) {
								mode = i;
								break;
							}
						if(i < keyword.length)
							continue;
						switch(mode) {
// MAP
							case 0:
								Koma tmpKoma
									= player.saerch(Integer.parseInt(token));
								if(tmpKoma != null)
									tmpKoma.pos
										= Integer.parseInt(st.nextToken());
								break;
// NEXT
							case 1:
								nextMoveKoma = Integer.parseInt(token);
								moveCount = Integer.parseInt(st.nextToken());
								break;
// GOALPLAYER
							case 2:
								int id = Integer.parseInt(token);
								usedID[id] = false;
								tmpKoma = player.saerch(id);
								if(tmpKoma != null) {
									int rank = ranking.addRanking(tmpKoma.name,
										tmpKoma.forwardCount);
									prePlayer.addElement(tmpKoma);
									player.removeElement(tmpKoma);
									tmpKoma.forwardCount = 0;
									System.out.println(tmpKoma.name + "("
										+ tmpKoma.cID[0] + "," + tmpKoma.cID[1]
										+ ")がゴールしました");
									if(rank < MAX_RANKING)
										System.out.println("順位は "+ (rank + 1)
											+ " 位です");
									goalPlayerString += tmpKoma.name + " "
										+ tmpKoma.cID[0] + " " + tmpKoma.cID[1]
										+ " " + rank + " ";
								}
								break;
						}
					}
				}
// 接続を解除したユーザを消す
				for(i = 0; i < removeMember.size(); i++) {
					Koma tmpKoma = removeMember.getKoma(i);
					if(player.indexOf(tmpKoma) >= 0) {
						usedID[tmpKoma.id] = false;
						if(nextMoveKoma >= tmpKoma.id)
							nextMoveKoma--;
						player.removeElement(tmpKoma);
					} else
						prePlayer.removeElement(tmpKoma);
					System.out.println(tmpKoma.name + "(" + tmpKoma.cID[0]
						+ "," + tmpKoma.cID[1] + ")が出て行きました");
				}
				if(i > 0)
					System.out.println("現在のユーザ数 "
						+ (player.size() + prePlayer.size()) + " 人");
				removeMember.removeAllElements();
// 新しくplayerになる人たちを選出
				newPlayerString = "";
				for(i = player.size(); prePlayer.size() > 0
					&& i < MAX_MEMBER; i++) {
					Koma tmpKoma = prePlayer.getKoma(0);
					prePlayer.removeElementAt(0);
					joinMap(tmpKoma);
					newPlayerString += tmpKoma.name + " " + tmpKoma.cID[0] + " "
						+ tmpKoma.cID[1] + " ";
				}
// 次の情報を生成、全ユーザに送信する
				if(player.size() > 0) {
					nextMoveKoma %= player.size();
					player.getKoma(nextMoveKoma).forwardCount++;
					ss = makeSendString(nextMoveKoma, moveCount);
					for(i = 0; i < prePlayer.size(); i++)
						prePlayer.getKoma(i).sendString(ss);
					for(i = 0; i < player.size(); i++)
						player.getKoma(i).sendString(ss);
				}
// ユーザ追加作業に対するブロックを解除
				execFlag = false;
			}
			yield();
		}
	}


// 全てのユーザが次の情報を受信できる状態か調べる
	boolean reciveAllOK() {
		int i, j;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
// ユーザがいなければ受信できる状態ではないと見なす
		if(player.size() + prePlayer.size() <= 0)
			return false;
// 全てのユーザについて、受信文字列の先頭が、新規ユーザを示す"NEW"、
// ユーザが次の情報を受信できる状態であることを示す"OK"、
// 接続を解除したユーザを示す"DEL"の何れかであることを確認する
		for(j = 0; j < 2; j++) {
			for(i = 0; i < users[j].size(); i++) {
				tmpKoma = users[j].getKoma(i);
				if(tmpKoma.recivedString.indexOf("OK") != 0
					&& tmpKoma.recivedString.indexOf("NEW") != 0
					&& tmpKoma.recivedString.indexOf("DEL") != 0)
					break;
			}
			if(i < users[j].size())
				return false;
		}
		return true;
	}


// 受信できた文字列を取得し、次の受信に備えてダミーの文字列を設定する
	String popRecivedString() {
		int i, j;
		String ret = null;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
		for(j = 0; j < 2; j++)
			for(i = 0; i < users[j].size(); i++) {
				tmpKoma = users[j].getKoma(i);
// 受信文字列"OK MAP id pos … NEXT id forward GOALPLAYER goaledId …"を取得
				if(ret == null && tmpKoma.recivedString.indexOf("OK") == 0)
					ret = tmpKoma.recivedString;
// 次の受信に備えてダミーの文字列を設定する
				tmpKoma.recivedString = "NO_STRING";
			}
		return ret;
	}


// 新規playerの追加
	boolean joinMap(Koma koma) {
		int i, j;
// コマの色を決める
		for(i = 0; i < MAX_MEMBER && usedID[i]; i++);
		koma.id = i;
		usedID[i] = true;
// コマの初期位置設定
		int map[] = createMap();
		int pos = (int)(Math.random() * (MAX_MAP - .01));
		for(j = 1; j < MAX_MAP; j++)
			for(i = pos; i != (pos + MAX_MAP - 1) % MAX_MAP;
				i = (i + 1) % MAX_MAP)
				if((map[i] & 2) == 0 && (map[(i + j) % MAX_MAP] & 1) == 0) {
					koma.endPos = i;
					koma.pos = (i+j) % MAX_MAP;
					player.addElement(koma);
					return true;
				}
		return false;
	}


// 現在のフィールド状況mapを生成
	int[] createMap() {
		int ret[] = new int[MAX_MAP], i;
		for(i = 0; i < MAX_MAP; i++)
			ret[i] = 0;
		for(i = 0; i < player.size(); i++) {
			Koma tmpKoma = player.getKoma(i);
			ret[tmpKoma.pos] += 1;
			ret[tmpKoma.endPos] += 2;
		}
		return ret;
	}


// map情報生成&move
	String makeSendString(int nextMoveKoma, int moveCount) {
		int i;
// map情報生成
		String ret = "MAP ";
		for(i = 0; i < player.size(); i++) {
			Koma tmpKoma = player.getKoma(i);
			ret += Integer.toString(tmpKoma.id) + " ";
			ret += Integer.toString(tmpKoma.pos) + " ";
			ret += Integer.toString(tmpKoma.endPos) + " ";
			ret += tmpKoma.name + " ";
		}
// 移動情報生成
		ret += "NEXT " + Integer.toString(nextMoveKoma) + " ";
		ret += Integer.toString(moveCount) + " ";
// ranking情報生成
		ret += ranking.makeString();
// 新規参入プレイヤー情報生成
		ret += "NEWPLAYER " + newPlayerString;
// ゴールしたプレイヤーの情報を生成
		ret += "GOALPLAYER " + goalPlayerString;
// 全ユーザ数
		ret += "USER " + (player.size() + prePlayer.size());
		return ret;
	}



// user管理用クラスKomaのプール KomaVector
	class KomaVector extends Vector {
		KomaVector() {
			super();
		}


// プール内の場所(番号)を指定してKomaを返す
		Koma getKoma(int no) {
			return (Koma)elementAt(no);
		}


// Komaの持っている一意のIDを指定して検索
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



// user管理用クラス Koma
	class Koma extends Thread {
		int id = -1, pos, endPos, forwardCount = 0;
		long cID[] = {0, 0};
		boolean stopFlag = false;
		String name, recivedString = "NEW";
		Socket soc;
		ObjectInputStream is;
		ObjectOutputStream os;


		Koma(Socket soc, String name, long[] cID) {
			this.soc = soc;
			this.name = name;
			this.cID = cID;
			start();
		}


// ユーザに送信
		synchronized boolean sendString(String data) {
			try {
				os = new ObjectOutputStream(soc.getOutputStream());
				os.writeObject(data);
				os.flush();
				if(debugFlag)
					System.out.println("send: " + data);
				return true;
			} catch(Exception e) {
				errorSocket(e);
				return false;
			}
		}


// ユーザから受信
		String reciveString() {
			try {
				is = new ObjectInputStream(soc.getInputStream());
				recivedString = (String)is.readObject();
				if(debugFlag)
					System.out.println("recive: " + recivedString);
				return recivedString;
			} catch(Exception e) {
				errorSocket(e);
				return null;
			}
		}


// 通信処理で例外が発生したら、そのユーザを破棄
		void errorSocket(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			stopFlag = true;
			recivedString = "DEL";
			removeMember.add(this);
		}


// 受信専用スレッド
		public void run() {
			while(!stopFlag)
				reciveString();
		}
	}
}
