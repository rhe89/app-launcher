import java.io.*;
import java.util.*;

class AppLauncher {
	private ArrayList<String> appsList;
    private File appsFolder;

    public static void main (String [] args) {
        File markedFolder = new File("../img/Markert");
        File unmarkedFolder = new File("../img/Umarkert");
        File appsFolder = new File("../links/");
        new AppLauncher(markedFolder, unmarkedFolder, appsFolder);
    }

    AppLauncher(File markedFolder, File unmarkedFolder, File appsFolder) {
        this.appsFolder = appsFolder;
        setApps();
        new GUI(appsList, markedFolder, unmarkedFolder);

    }

	private void setApps() {
        appsList = new ArrayList<String>();

        appsList.add(0, "http://www.csports.no/");
        appsList.add(1, "http://www.netflix.com/");
        appsList.add(2, "http://www.viaplay.no/");
        appsList.add(3, "http://tv.nrk.no/");
        appsList.add(4, "https://sumo.tv2.no/");

	}
}


