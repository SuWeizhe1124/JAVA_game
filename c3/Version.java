// Version.java
// written by mnagaku

public class Version {

	public static void main(String args[]) {
		String version = System.getProperty("java.version");
		System.out.println(version);
		if(version.compareTo("1.2.0") < 0)
			System.out.println("Java2ではありません");
		else
			System.out.println("Java2です");
	}
}

