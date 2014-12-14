import java.io.*;
import java.util.*;

class AppLauncher {
	private ArrayList<String> appsList;

    public static void main (String [] args) {
        File markedFolder = new File("../img/marked");
        File unmarkedFolder = new File("../img/notmarked");
        new AppLauncher(markedFolder, unmarkedFolder);
    }

    AppLauncher(File markedFolder, File unmarkedFolder) {
        setApps();
        new GUI(appsList, markedFolder, unmarkedFolder);

    }

	private void setApps() {
        appsList = new ArrayList<String>();

        appsList.add(0, "http://www.csports.no/spilleskjema");
        appsList.add(1, "http://www.netflix.com/");
        appsList.add(2, "http://viaplay.no/sport");
        appsList.add(3, "http://tv.nrk.no/");
        appsList.add(4, "https://sumo.tv2.no/");

	}
}


