// CS.java
// written by mnagaku

import java.net.*;
import java.util.*;

/**
 * CS
 */
public class CS {
// 使用するポート
	static final int DEFAULT_PORT = 4001;

	int portNo;
	String sendString;


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
		new CS(params);
	}


// コンストラクタ
	CS(Hashtable params) {
// コマンドライン引数からポート番号を取得
		portNo = DEFAULT_PORT;
		String tmpStr;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
// 乱数の初期化
		Math.random();
// コマンドライン引数から接続先を取得
		String serverName = (String)(params.get("server"));
		try {
// 接続先指定があった場合はclientモード
			if(serverName != null) {
				System.out.println("接続先server : " + serverName);
				System.out.println("ポート番号 : " + portNo);
				new Communicate(new Socket(serverName, portNo));
// 接続先指定がなかった場合はserverモード
			} else {
				System.out.println("待ち受けポート番号 : " + portNo);
// 接続待ち受け
				ServerSocket serversocket = new ServerSocket(portNo);
				while(true)
					new Communicate(serversocket.accept());
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}
