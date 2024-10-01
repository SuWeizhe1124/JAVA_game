// Server1.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Server1
 */
public class Server1 {
// 待ち受けに使用するポート
	static final int DEFAULT_PORT = 4001;
// 送信するメッセージ
	static final String DEFAULT_MESSAGE = "Hello, I am Server1.";


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
// コマンドライン引数からユーザ接続待ち受けポート番号を取得
		int portNo = DEFAULT_PORT;
		String tmpStr;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("待ち受けポート番号 : " + portNo);
// コマンドライン引数から送信メッセージを取得
		String sendString = DEFAULT_MESSAGE;
		if((tmpStr = (String)(params.get("message"))) != null)
			sendString = tmpStr;
		System.out.println("送信メッセージ : " + sendString);
		String recivedString = null;
		try {
// 接続待ち受け
			ServerSocket serversocket = new ServerSocket(portNo);
			Socket usocket = serversocket.accept();
// 受信
			ObjectInputStream is
				= new ObjectInputStream(usocket.getInputStream());
			recivedString = (String)is.readObject();
			System.out.println("受信完了");
// 送信
			ObjectOutputStream os
				= new ObjectOutputStream(usocket.getOutputStream());
			os.writeObject(sendString);
			os.flush();
			System.out.println("送信完了");
		} catch(Exception e) {e.printStackTrace();}
		System.out.println("受信メッセージ : " + recivedString);
	}
}
