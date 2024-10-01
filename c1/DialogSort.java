// DialogSort.java
// written by mnagaku

import java.io.*;
import java.util.*;

class DialogSort {

	public static void main(String args[]) throws IOException {
		int i, j, tmp;
		int[] dataArray;
		String str;
		Vector data = new Vector();
		BufferedReader br
			= new BufferedReader(new InputStreamReader(System.in));

		while(true) {
			System.out.println("現在、入力されている数値");
			if(data.isEmpty())
				System.out.println("なし");
			else {
				for(i = 0; i < data.size(); i++) {
					if(i % 4 == 3)
						System.out.println("["+i+"]"+data.elementAt(i));
					else
						System.out.print("["+i+"]"+data.elementAt(i)+", ");
				}
				if(i % 4 != 0)
					System.out.println("");
			}
			System.out.println("a:数値を追加");
			System.out.println("d:数値を削除");
			System.out.println("s:数値をソート");

			str = br.readLine();
			if(str.compareTo("d") == 0) {
				System.out.println("削除する数値の番号を入力して下さい");
				str = br.readLine();
				tmp = Integer.parseInt(str);
				data.removeElementAt(tmp);
			}
			else if(str.compareTo("a") == 0) {
				System.out.println("追加する数値を入力して下さい");
				str = br.readLine();
				tmp = Integer.parseInt(str);
				data.addElement(new Integer(tmp));
			}
			else
				break;
		}

		if(data.size() == 0) {
			System.out.println("数値がありません");
			return;
		}

		System.out.println("ソート結果");

		dataArray = new int[data.size()];
		for(i = 0; i < data.size(); i++)
			dataArray[i] = ((Integer)data.elementAt(i)).intValue();
		for(i = 0; i < dataArray.length - 1; i++) {
			for(j = i; j < dataArray.length; j++)
				if(dataArray[i] > dataArray[j]) {
					tmp = dataArray[i];
					dataArray[i] = dataArray[j];
					dataArray[j] = tmp;
				}
			System.out.println(dataArray[i]);
		}
		System.out.println(dataArray[i]);
	}
}

