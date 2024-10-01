// Client1.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Client1
 */
public class Client1 {
// �ڑ���
	static final String DEFAULT_SERVER = "localhost";
// �ڑ���̃|�[�g
	static final int DEFAULT_PORT = 4001;
// ���M���郁�b�Z�[�W
	static final String DEFAULT_MESSAGE = "Hello, I am Client1.";


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
// �R�}���h���C����������ڑ�����擾
		String serverName = DEFAULT_SERVER;
		String tmpStr;
		if((tmpStr = (String)(params.get("server"))) != null)
			serverName = tmpStr;
		System.out.println("�ڑ���server : " + serverName);
// �R�}���h���C����������ڑ���|�[�g�ԍ����擾
		int portNo = DEFAULT_PORT;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("�ڑ���|�[�g�ԍ� : " + portNo);
// �R�}���h���C���������瑗�M���b�Z�[�W���擾
		String sendString = DEFAULT_MESSAGE;
		if((tmpStr = (String)(params.get("message"))) != null)
			sendString = tmpStr;
		System.out.println("���M���b�Z�[�W : " + sendString);
		String recivedString = null;
		try {
// �ڑ�
			Socket usocket = new Socket(serverName, portNo);
// ���M
			ObjectOutputStream os
				= new ObjectOutputStream(usocket.getOutputStream());
			os.writeObject(sendString);
			os.flush();
			System.out.println("���M����");
// ��M
			ObjectInputStream is
				= new ObjectInputStream(usocket.getInputStream());
			recivedString = (String)is.readObject();
			System.out.println("��M����");
		} catch(Exception e) {e.printStackTrace();}
		System.out.println("��M���b�Z�[�W : " + recivedString);
	}
}
