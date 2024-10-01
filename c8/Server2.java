// Server2.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Server2
 */
public class Server2 extends Thread {
// �҂��󂯂Ɏg�p����|�[�g
	static final int DEFAULT_PORT = 4001;
// ���M���郁�b�Z�[�W
	static final String DEFAULT_MESSAGE = "Hello, I am Server2.";
// ���b�Z�[�W�𑗐M����񐔁B��M����񐔂������B
	static final int MESSAGE_COUNT = 10;

	static Socket usocket;
	static int recivedCount = 0;


// main()���\�b�h
	public static void main(String args[]) {
// �R�}���h���C��������params�Ɋi�[
		Hashtable params = new Hashtable();
		for(int i = 0; i < args.length; i++) {
			if(args[i].indexOf("-") == 0) {
				if(i + 1 < args.length && args[i + 1].indexOf("-") != 0)
					params.put(args[i].substring(1), args[i + 1]);
				else
					params.put(args[i].substring(1), args[i].substring(1));
			}
		}
// �R�}���h���C���������烆�[�U�ڑ��҂��󂯃|�[�g�ԍ����擾
		int portNo = DEFAULT_PORT;
		String tmpStr;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("�҂��󂯃|�[�g�ԍ� : " + portNo);
// �R�}���h���C���������瑗�M���b�Z�[�W���擾
		String sendString = DEFAULT_MESSAGE;
		if((tmpStr = (String)(params.get("message"))) != null)
			sendString = tmpStr;
		System.out.println("���M���b�Z�[�W : " + sendString);
// �����̏�����
		Math.random();
		try {
// �ڑ��҂���
			ServerSocket serversocket = new ServerSocket(portNo);
			usocket = serversocket.accept();
// ��M�J�n
			(new Server2()).start();
// ���M
			ObjectOutputStream os
				= new ObjectOutputStream(usocket.getOutputStream());
			for(int i = 0; i < MESSAGE_COUNT; i++) {
				sleep((long)(Math.random() * 1000.));
				os.writeObject(sendString + " : count " + i);
				os.flush();
				System.out.println("���M���� : count " + i);
			}
		} catch(Exception e) {e.printStackTrace();}
	}


// ��M��p�X���b�h
	public void run() {
		String recivedString = null;
		try {
// ��M
			ObjectInputStream is
				= new ObjectInputStream(usocket.getInputStream());
			while(recivedCount < MESSAGE_COUNT) {
				recivedString = (String)is.readObject();
				System.out.println("��M���b�Z�[�W : " + recivedString);
				recivedCount++;
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}
