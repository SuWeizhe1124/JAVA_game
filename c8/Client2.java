// Client2.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Client2
 */
public class Client2 extends Thread {
// 接続先
	static final String DEFAULT_SERVER = "localhost";
// 接続先のポート
	static final int DEFAULT_PORT = 4001;
// 送信するメッセージ
	static final String DEFAULT_MESSAGE = "Hello, I am Client2.";
// メッセージを送信する回数。受信する回数も同じ。
	static final int MESSAGE_COUNT = 10;

	static Socket usocket;
	static int recivedCount = 0;


// main()メソッド
	public static void main(String args[]) {
// コマンドライン引数をparamsに格納
		Hashtable params = new Hashtable();
		for(int i = 0; i < args.length; i++) {
			if(args[i].indexOf("-") == 0) {
				if(i + 1 < args.length && args[i + 1].indexOf("-") != 0)
					params.put(args[i].substring(1), args[i + 1]);
				else
					params.put(args[i].substring(1), args[i].substring(1));
			}
		}
// コマンドライン引数から接続先を取得
		String serverName = DEFAULT_SERVER;
		String tmpStr;
		if((tmpStr = (String)(params.get("server"))) != null)
			serverName = tmpStr;
		System.out.println("接続先server : " + serverName);
// コマンドライン引数から接続先ポート番号を取得
		int portNo = DEFAULT_PORT;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("接続先ポート番号 : " + portNo);
// コマンドライン引数から送信メッセージを取得
		String sendString = DEFAULT_MESSAGE;
		if((tmpStr = (String)(params.get("message"))) != null)
			sendString = tmpStr;
		System.out.println("送信メッセージ : " + sendString);
// 乱数の初期化
		Math.random();
		try {
// 接続
			usocket = new Socket(serverName, portNo);
// 受信開始
		(new Client2()).start();
// 送信
			ObjectOutputStream os
				= new ObjectOutputStream(usocket.getOutputStream());
			for(int i = 0; i < MESSAGE_COUNT; i++) {
				sleep((long)(Math.random() * 1000.));
				os.writeObject(sendString + " : count " + i);
				os.flush();
				System.out.println("送信完了 : count " + i);
			}
		} catch(Exception e) {e.printStackTrace();}
	}


// 受信専用スレッド
	public void run() {
		String recivedString = null;
		try {
// 受信
			ObjectInputStream is
				= new ObjectInputStream(usocket.getInputStream());
			while(recivedCount < MESSAGE_COUNT) {
				recivedString = (String)is.readObject();
				System.out.println("受信メッセージ : " + recivedString);
				recivedCount++;
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}
