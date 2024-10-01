// Server1.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Server1
 */
public class Server1 {
// �҂��󂯂Ɏg�p����|�[�g
	static final int DEFAULT_PORT = 4001;
// ���M���郁�b�Z�[�W
	static final String DEFAULT_MESSAGE = "Hello, I am Server1.";


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
		String recivedString = null;
		try {
// �ڑ��҂���
			ServerSocket serversocket = new ServerSocket(portNo);
			Socket usocket = serversocket.accept();
// ��M
			ObjectInputStream is
				= new ObjectInputStream(usocket.getInputStream());
			recivedString = (String)is.readObject();
			System.out.println("��M����");
// ���M
			ObjectOutputStream os
				= new ObjectOutputStream(usocket.getOutputStream());
			os.writeObject(sendString);
			os.flush();
			System.out.println("���M����");
		} catch(Exception e) {e.printStackTrace();}
		System.out.println("��M���b�Z�[�W : " + recivedString);
	}
}
