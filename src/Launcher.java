import java.io.*;


class Launcher {
	public static void main (String [] args) {
		File markedFolder = new File("../img/Markert");
		File unmarkedFolder = new File("../img/Umarkert");
		File appsFolder = new File("../links/");
		new Launch(markedFolder, unmarkedFolder, appsFolder);
	}
}
