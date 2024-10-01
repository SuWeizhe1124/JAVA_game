// SugorokuServer.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * SugorokuServer
 */
public class SugorokuServer extends Thread {
// �����ɑ��݂ł���v���C�����̏��
	static final int MAX_MEMBER = 8;
// �}�X�ڂ̐�
	static final int MAX_MAP = 24;
// �T�[�o���҂��󂯂Ɏg�p����|�[�g
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


// main()���\�b�h
	public static void main(String args[]) {
// �R�}���h���C��������params�Ɋi�[
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


// �R���X�g���N�^
	SugorokuServer() {
// �ϐ��̏�����
		for(int i = 0; i < MAX_MEMBER; i++)
			usedID[i] = false;
		ranking.small2big();
	}


// ���[�U�ڑ��҂���
	public void run() {
		int portNo = DEFAULT_PORT;
		String tmpStr;
// �R�}���h���C���������烆�[�U�ڑ��҂��󂯃|�[�g�ԍ����擾
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("���[�U�ڑ��҂��󂯃|�[�g�ԍ� : " + portNo);
// �R�}���h���C����������debug���[�h�ŋN�����邩���擾
		if(params.get("debug") != null) {
			debugFlag = true;
			System.out.println("�f�o�b�O���[�h�ŋN�����܂�");
		}
		params = null;
// ���[�U�ڑ��҂���
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
			System.out.println("���[�U�ڑ��҂��󂯂Ŗ�肪�������܂���");
		}
	}


// �V�K���[�U�̐ڑ��v��������
	boolean connectClient(Socket usocket) {
		byte[] recivedBytes;
		String recived;
// �ڑ��v��������̎�M
		try {
			ObjectInputStream is
				= new ObjectInputStream((usocket.getInputStream()));
			recived = (String)is.readObject();
		} catch(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			return false;
		}
// �ڑ��v��������"join name cID[0] cID[1]"������𒊏o
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
// �d�����郆�[�U�������Ɏ����Ă���ꍇ�͐ڑ��������Ȃ�
		for(int i = 0; i < prePlayer.size(); i++) {
			Koma tmpKoma = prePlayer.getKoma(i);
			if(tmpKoma.name == name
				|| (tmpKoma.cID[0]==cID[0] && tmpKoma.cID[1]==cID[1]))
				return false;
		}
// execute()�̃X���b�h�ƂԂ���Ȃ��悤�Ƀ��[�U����ǉ�
		preAddFlag = true;
		while(execFlag)
			yield();
		prePlayer.addElement(new Koma(usocket, name, cID));
		System.out.println(name + "(" + cID[0] + "," + cID[1]
			+ ")�������܂���");
		System.out.println("���݂̃��[�U�� "
			+ (player.size() + prePlayer.size()) + " �l");
		preAddFlag = false;
		return true;
	}


	String keyword[] = {"MAP", "NEXT", "GOALPLAYER"};

// �Q�[����i�s������
// �����Imain()����
	public void execute() {
		int i, j;
		String ss;
		System.out.println("SugorokuServer �N��");
// ���[�U�ڑ��҂��󂯂��J�n
		start();
// �Q�[����i�s������
		while(!exitFlag) {
// ���[�U�ǉ���ƒ��͑҂�
			while(preAddFlag)
				yield();
// ���[�U����̕ԐM���S�đ������玟�̈ړ����𐶐��A�z�M
			if(reciveAllOK()) {
// ���[�U�ǉ���Ƃɑ҂��Ă��炤
				execFlag = true;
// ��M������̎擾
				ss = popRecivedString();
// ��M������"OK MAP id pos �c NEXT id forward GOALPLAYER goaledId �c"�����
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
										+ ")���S�[�����܂���");
									if(rank < MAX_RANKING)
										System.out.println("���ʂ� "+ (rank + 1)
											+ " �ʂł�");
									goalPlayerString += tmpKoma.name + " "
										+ tmpKoma.cID[0] + " " + tmpKoma.cID[1]
										+ " " + rank + " ";
								}
								break;
						}
					}
				}
// �ڑ��������������[�U������
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
						+ "," + tmpKoma.cID[1] + ")���o�čs���܂���");
				}
				if(i > 0)
					System.out.println("���݂̃��[�U�� "
						+ (player.size() + prePlayer.size()) + " �l");
				removeMember.removeAllElements();
// �V����player�ɂȂ�l������I�o
				newPlayerString = "";
				for(i = player.size(); prePlayer.size() > 0
					&& i < MAX_MEMBER; i++) {
					Koma tmpKoma = prePlayer.getKoma(0);
					prePlayer.removeElementAt(0);
					joinMap(tmpKoma);
					newPlayerString += tmpKoma.name + " " + tmpKoma.cID[0] + " "
						+ tmpKoma.cID[1] + " ";
				}
// ���̏��𐶐��A�S���[�U�ɑ��M����
				if(player.size() > 0) {
					nextMoveKoma %= player.size();
					player.getKoma(nextMoveKoma).forwardCount++;
					ss = makeSendString(nextMoveKoma, moveCount);
					for(i = 0; i < prePlayer.size(); i++)
						prePlayer.getKoma(i).sendString(ss);
					for(i = 0; i < player.size(); i++)
						player.getKoma(i).sendString(ss);
				}
// ���[�U�ǉ���Ƃɑ΂���u���b�N������
				execFlag = false;
			}
			yield();
		}
	}


// �S�Ẵ��[�U�����̏�����M�ł����Ԃ����ׂ�
	boolean reciveAllOK() {
		int i, j;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
// ���[�U�����Ȃ���Ύ�M�ł����Ԃł͂Ȃ��ƌ��Ȃ�
		if(player.size() + prePlayer.size() <= 0)
			return false;
// �S�Ẵ��[�U�ɂ��āA��M������̐擪���A�V�K���[�U������"NEW"�A
// ���[�U�����̏�����M�ł����Ԃł��邱�Ƃ�����"OK"�A
// �ڑ��������������[�U������"DEL"�̉��ꂩ�ł��邱�Ƃ��m�F����
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


// ��M�ł�����������擾���A���̎�M�ɔ����ă_�~�[�̕������ݒ肷��
	String popRecivedString() {
		int i, j;
		String ret = null;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
		for(j = 0; j < 2; j++)
			for(i = 0; i < users[j].size(); i++) {
				tmpKoma = users[j].getKoma(i);
// ��M������"OK MAP id pos �c NEXT id forward GOALPLAYER goaledId �c"���擾
				if(ret == null && tmpKoma.recivedString.indexOf("OK") == 0)
					ret = tmpKoma.recivedString;
// ���̎�M�ɔ����ă_�~�[�̕������ݒ肷��
				tmpKoma.recivedString = "NO_STRING";
			}
		return ret;
	}


// �V�Kplayer�̒ǉ�
	boolean joinMap(Koma koma) {
		int i, j;
// �R�}�̐F�����߂�
		for(i = 0; i < MAX_MEMBER && usedID[i]; i++);
		koma.id = i;
		usedID[i] = true;
// �R�}�̏����ʒu�ݒ�
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


// ���݂̃t�B�[���h��map�𐶐�
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


// map��񐶐�&move
	String makeSendString(int nextMoveKoma, int moveCount) {
		int i;
// map��񐶐�
		String ret = "MAP ";
		for(i = 0; i < player.size(); i++) {
			Koma tmpKoma = player.getKoma(i);
			ret += Integer.toString(tmpKoma.id) + " ";
			ret += Integer.toString(tmpKoma.pos) + " ";
			ret += Integer.toString(tmpKoma.endPos) + " ";
			ret += tmpKoma.name + " ";
		}
// �ړ���񐶐�
		ret += "NEXT " + Integer.toString(nextMoveKoma) + " ";
		ret += Integer.toString(moveCount) + " ";
// ranking��񐶐�
		ret += ranking.makeString();
// �V�K�Q���v���C���[��񐶐�
		ret += "NEWPLAYER " + newPlayerString;
// �S�[�������v���C���[�̏��𐶐�
		ret += "GOALPLAYER " + goalPlayerString;
// �S���[�U��
		ret += "USER " + (player.size() + prePlayer.size());
		return ret;
	}



// user�Ǘ��p�N���XKoma�̃v�[�� KomaVector
	class KomaVector extends Vector {
		KomaVector() {
			super();
		}


// �v�[�����̏ꏊ(�ԍ�)���w�肵��Koma��Ԃ�
		Koma getKoma(int no) {
			return (Koma)elementAt(no);
		}


// Koma�̎����Ă����ӂ�ID���w�肵�Č���
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



// user�Ǘ��p�N���X Koma
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


// ���[�U�ɑ��M
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


// ���[�U�����M
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


// �ʐM�����ŗ�O������������A���̃��[�U��j��
		void errorSocket(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			stopFlag = true;
			recivedString = "DEL";
			removeMember.add(this);
		}


// ��M��p�X���b�h
		public void run() {
			while(!stopFlag)
				reciveString();
		}
	}
}
