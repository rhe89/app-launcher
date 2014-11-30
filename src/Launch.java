import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import javax.swing.border.*;
import java.util.concurrent.CountDownLatch;
import java.io.*;
import com.apple.eawt.FullScreenUtilities;
import java.awt.Window;
import java.lang.reflect.Method;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import com.apple.eawt.Application;
import java.util.*;

public class Launch extends JFrame {
	private NewImage plexUnmarked, plexMarked, tv2Unmarked, tv2Marked, nrkUnmarked, nrkMarked, csportsUnmarked, csportsMarked,
		viaplayUnmarked, viaplayMarked, netflixMarked, netflixUnmarked, hboMarked, hboUnmarked;
	private File markedFolder, unmarkedFolder, appsFolder;
	private JPanel plexPanel, tv2Panel, nrkPanel, csportsPanel, viaplayPanel, netflixPanel, hboPanel;
	private ArrayList<JPanel> logoList;
	private ArrayList<File> appsList;
	private ArrayList<NewImage> markedImages, unmarkedImages;
	private final int IMAGE_HEIGHT = 86, IMAGE_WIDTH = 300;
	private int counter;

	Launch(File markedFolder, File unmarkedFolder, File appsFolder) {
		this.markedFolder = markedFolder;
		this.unmarkedFolder = unmarkedFolder;
		this.appsFolder = appsFolder;
		setMarkedImages();
		setUnmarkedImages();
		setApps();
		setPanels();
		addImages();
	}

	private void setPanels() {
		plexPanel = new JPanel();
		tv2Panel = new JPanel();
		nrkPanel = new JPanel();
		csportsPanel = new JPanel();
		viaplayPanel = new JPanel();
		netflixPanel = new JPanel();
		hboPanel = new JPanel();
		logoList = new ArrayList<JPanel>();
		

		CountDownLatch cdl = new CountDownLatch(7);

		AddImagePanel plexThread = new AddImagePanel(cdl, plexUnmarked, plexPanel);
		logoList.add(plexPanel);
		AddImagePanel csportsThread = new AddImagePanel(cdl, csportsUnmarked, csportsPanel);
		logoList.add(csportsPanel);
		AddImagePanel netflixThread = new AddImagePanel(cdl, netflixUnmarked, netflixPanel);
		logoList.add(netflixPanel);
		AddImagePanel viaplayThread = new AddImagePanel(cdl, viaplayUnmarked, viaplayPanel);
		logoList.add(viaplayPanel);		
		AddImagePanel nrkThread = new AddImagePanel(cdl, nrkUnmarked, nrkPanel);
		logoList.add(nrkPanel);
		AddImagePanel tv2Thread = new AddImagePanel(cdl, tv2Unmarked, tv2Panel);
		logoList.add(tv2Panel);	
		AddImagePanel hboThread = new AddImagePanel(cdl, hboUnmarked, hboPanel);
		logoList.add(hboPanel);

		new Thread(plexThread).start();
		new Thread(csportsThread).start();
		new Thread(netflixThread).start();
		new Thread(viaplayThread).start();
		new Thread(nrkThread).start();
		new Thread(tv2Thread).start();	
		new Thread(hboThread).start();

		try {
			cdl.await();
		} catch (InterruptedException e) {
			System.out.println("Noe gikk fryktelig galt");
		}
	}
	private void addImages() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		int xValue = 0, yValue = 0;
		
		getContentPane().setBackground(Color.BLACK);
		setExtendedState(Frame.MAXIMIZED_BOTH); 
		setVisible(true);

		c.gridx = 0;
		c.gridy = 0;
		add(plexPanel, c);
	
		c.gridx = 0;
		c.gridy = 1;
		add(csportsPanel, c);

		c.gridx = 0;
		c.gridy = 2;
		add(netflixPanel, c);

		c.gridx = 0;
		c.gridy = 3;
		add(viaplayPanel, c);

		c.gridx = 0;
		c.gridy = 4;
		add(nrkPanel, c);
		
		c.gridx = 0;
		c.gridy = 5;
		add(tv2Panel, c);
		
		c.gridx = 0;
		c.gridy = 6;
		add(hboPanel, c);
	
		addKeyListeners(this, true); 

		revalidate();
		repaint();
		pack();

		enableOSXFullscreen(this);

	}
	private void setApps() {
			appsList = new ArrayList<File>();
			File plex;
			
			plex = new File("/Applications/Plex Home Theater.app");
			
			if (!plex.exists()) {
				plex = new File("/Users/Roar/Applications/Plex Home Theater.app");
			}
			appsList.add(0, plex);	
			appsList.add(1, new File(appsFolder.getAbsolutePath()+"/C Sports.webloc"));	
			appsList.add(2, new File(appsFolder.getAbsolutePath()+"/Netflix.webloc"));
			appsList.add(3, new File(appsFolder.getAbsolutePath()+"/ViaPlay.webloc"));
			appsList.add(4, new File(appsFolder.getAbsolutePath()+"/NRK.webloc"));
			appsList.add(5, new File(appsFolder.getAbsolutePath()+"/TV 2 Sumo.webloc"));
			appsList.add(6, new File(appsFolder.getAbsolutePath()+"/HBO Nordic.webloc"));
		
	}
	private void addKeyListeners(final JFrame window, boolean start) {
		if (start) {
		    setSelected(logoList.get(0), markedImages.get(0));
		    start = false;
		}
	    addKeyListener(new KeyListener() {
	    	@Override
	    	public void keyTyped(KeyEvent e) {
	    	}
	    	@Override
	    	public void keyReleased(KeyEvent e) {
	    	}
	    	@Override
	    	public void keyPressed(KeyEvent e) {
		    	int keyCode = e.getKeyCode();
		    	switch(keyCode) { 
			        case KeyEvent.VK_UP:
			        	if (counter == 0) {
			        		counter = logoList.size() - 1;
			        		setSelected(logoList.get(counter), markedImages.get(counter));
			        		setUnselected(logoList.get(0), unmarkedImages.get(0));
			        	} else {
			        		setUnselected(logoList.get(counter), unmarkedImages.get(counter));
			     			setSelected(logoList.get(--counter), markedImages.get(counter));
			        	}
			            break;
			        case KeyEvent.VK_DOWN:
			            if (counter == (logoList.size() - 1)) {
			        		counter = 0;
			        		setUnselected(logoList.get(logoList.size()-1), unmarkedImages.get(logoList.size()-1));
			        		setSelected(logoList.get(counter), markedImages.get(counter));
			        	} else {
			        		setUnselected(logoList.get(counter), unmarkedImages.get(counter));
			     			setSelected(logoList.get(++counter), markedImages.get(counter));
			        	}
			            break;
			        case KeyEvent.VK_ENTER:
				        try {
				     		Desktop.getDesktop().open(appsList.get(counter));
				       	} catch (IOException f) {
				       		System.out.println("Appen finnes ikke!");
				       	}
				       	break;
			        }
	    	}
	    });	
	}

	private void setSelected(JPanel panel, NewImage image) {

		AddImagePanel panelToAddTo = new AddImagePanel(image, panel);

		Thread t1 = new Thread(panelToAddTo);
		t1.start();
		
		try {
			t1.join();
		} catch (InterruptedException f) {
			System.out.println("Noe gikk fryktelig galt");
		}
		revalidate();
		repaint();
	}

	private void setUnselected(JPanel panel, NewImage image) {

		AddImagePanel panelToAddTo = new AddImagePanel(image, panel);

		Thread t1 = new Thread(panelToAddTo);
		t1.start();
		
		try {
			t1.join();
		} catch (InterruptedException f) {
			System.out.println("Noe gikk fryktelig galt");
		}
		revalidate();
		repaint();
	}

	private class AddImagePanel implements Runnable {
		private CountDownLatch cdl;
		private NewImage image;
		private JPanel panel;

		AddImagePanel(CountDownLatch cdl, NewImage image, JPanel panel) {
			this.cdl = cdl;
			this.image = image;
			this.panel = panel;
		}

		AddImagePanel(NewImage image, JPanel panel) {
			this.image = image;
			this.panel = panel;
		}

		public void run() {
			panel.removeAll();
			panel.add(image);
			panel.setBackground(Color.BLACK);
			if (cdl != null) {
				cdl.countDown();

			}
		}
	}
	/*
	plexThread
csportsThread
netflixThread
viaplayThread
nrkThread
tv2Thread
hboThread*/

	private void setMarkedImages() {
		plexMarked = new NewImage(new ImageIcon(markedFolder.getAbsolutePath()+"/plex.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		tv2Marked = new NewImage(new ImageIcon(markedFolder.getAbsolutePath()+"/tv2.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		nrkMarked = new NewImage(new ImageIcon(markedFolder.getAbsolutePath()+"/nrk.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		csportsMarked = new NewImage(new ImageIcon(markedFolder.getAbsolutePath()+"/csports.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		viaplayMarked = new NewImage(new ImageIcon(markedFolder.getAbsolutePath()+"/viaplay.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		netflixMarked = new NewImage(new ImageIcon(markedFolder.getAbsolutePath()+"/netflix.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		hboMarked = new NewImage(new ImageIcon(markedFolder.getAbsolutePath()+"/hbo.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		markedImages = new ArrayList<NewImage>();
		markedImages.add(plexMarked);
		markedImages.add(csportsMarked);
		markedImages.add(netflixMarked);
		markedImages.add(viaplayMarked);
		markedImages.add(nrkMarked);
		markedImages.add(tv2Marked);
		markedImages.add(hboMarked);
	} 
	private void setUnmarkedImages() {
		plexUnmarked = new NewImage(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/plex.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		tv2Unmarked = new NewImage(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/tv2.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		nrkUnmarked = new NewImage(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/nrk.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		csportsUnmarked = new NewImage(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/csports.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		viaplayUnmarked = new NewImage(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/viaplay.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		netflixUnmarked = new NewImage(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/netflix.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);
		hboUnmarked = new NewImage(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/hbo.png").getImage(), IMAGE_WIDTH, IMAGE_HEIGHT);

		unmarkedImages = new ArrayList<NewImage>();
		unmarkedImages.add(plexUnmarked);
		unmarkedImages.add(csportsUnmarked);
		unmarkedImages.add(netflixUnmarked);
		unmarkedImages.add(viaplayUnmarked);
		unmarkedImages.add(nrkUnmarked);
		unmarkedImages.add(tv2Unmarked);
		unmarkedImages.add(hboUnmarked);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void enableOSXFullscreen(Launch window) {
	    try {
	        Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
	        Class params[] = new Class[]{Window.class, Boolean.TYPE};
	        Method method = util.getMethod("setWindowCanFullScreen", params);
	        method.invoke(util, window, true);
	    } catch (ClassNotFoundException e1) {
	    } catch (Exception e) {
	    }
	    toggleOSXFullscreen(window);
	}
	@SuppressWarnings({"unchecked", "rawtypes"})
    public static void toggleOSXFullscreen(Window window) {
        try {
            Class appClass = Class.forName("com.apple.eawt.Application");

            Method method = appClass.getMethod("getApplication");
            Object appInstance = method.invoke(appClass);

            Class params[] = new Class[]{Window.class};
            method = appClass.getMethod("requestToggleFullScreen", params);
            method.invoke(appInstance, window);
        } catch (ClassNotFoundException e1) {
        } catch (Exception e) {
            System.out.println("Failed to toggle Mac Fullscreen: "+e);
        }
    }
}


class NewImage extends JPanel {
	private Image img;

	public NewImage(Image img, int lengde, int bredde) {
		this.img = img;
		Dimension size = new Dimension(lengde, bredde);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0,0,null);
	}
}