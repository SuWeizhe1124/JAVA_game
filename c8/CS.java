// CS.java
// written by mnagaku

import java.net.*;
import java.util.*;

/**
 * CS
 */
public class CS {
// �g�p����|�[�g
	static final int DEFAULT_PORT = 4001;

	int portNo;
	String sendString;


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
		new CS(params);
	}


// �R���X�g���N�^
	CS(Hashtable params) {
// �R�}���h���C����������|�[�g�ԍ����擾
		portNo = DEFAULT_PORT;
		String tmpStr;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
// �����̏�����
		Math.random();
// �R�}���h���C����������ڑ�����擾
		String serverName = (String)(params.get("server"));
		try {
// �ڑ���w�肪�������ꍇ��client���[�h
			if(serverName != null) {
				System.out.println("�ڑ���server : " + serverName);
				System.out.println("�|�[�g�ԍ� : " + portNo);
				new Communicate(new Socket(serverName, portNo));
// �ڑ���w�肪�Ȃ������ꍇ��server���[�h
			} else {
				System.out.println("�҂��󂯃|�[�g�ԍ� : " + portNo);
// �ڑ��҂���
				ServerSocket serversocket = new ServerSocket(portNo);
				while(true)
					new Communicate(serversocket.accept());
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}
