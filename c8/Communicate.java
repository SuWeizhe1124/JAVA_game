// Communicate.java
// written by mnagaku

import java.net.*;
import java.io.*;

/**
 * Communicate
 */
public class Communicate extends Thread {
// ���b�Z�[�W�𑗐M����񐔁B��M����񐔂������B
	static final int MESSAGE_COUNT = 10;

	int recivedCount = 0;
	Socket usocket;


// �R���X�g���N�^
	Communicate(Socket usocket) {
		this.usocket = usocket;
		start();
		(new SendMessages()).start();
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
				System.out.println("from "
					+ usocket.toString() + " " + recivedString);
				recivedCount++;
			}
		} catch(Exception e) {e.printStackTrace();}
	}



// ���M��p�X���b�h
	class SendMessages extends Thread {
		SendMessages() {}

		public void run() {
			try {
				ObjectOutputStream os
					= new ObjectOutputStream(usocket.getOutputStream());
				long wait;
				for(int i = 0; i < MESSAGE_COUNT; i++) {
					wait = (long)(Math.random() * 1000.);
					sleep(wait);
					os.writeObject("" + wait + " : No." + i);
					os.flush();
					System.out.println("to " + usocket.toString()
						+ " " + wait + " No." + i);
				}
			} catch(Exception e) {e.printStackTrace();}
		}
	}
}
