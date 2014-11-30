import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Roar on 30.11.14.
 */
public class GUI extends JFrame {
    private ArrayList<Logo> markedImages, unmarkedImages;
    private ArrayList<String> appsList;
    private File markedFolder, unmarkedFolder;
    private Logo tv2Unmarked, tv2Marked, nrkUnmarked, nrkMarked, csportsUnmarked, csportsMarked,
            viaplayUnmarked, viaplayMarked, netflixMarked, netflixUnmarked;
    private ArrayList<JPanel> logoList;
    private JPanel tv2Panel, nrkPanel, csportsPanel, viaplayPanel, netflixPanel;
    private int counter = 0;
    private final int IMAGE_HEIGHT = 86, IMAGE_WIDTH = 300;

    GUI(ArrayList<String> appsList, File markedFolder, File unmarkedFolder) {
        this.appsList = appsList;
        this.markedFolder = markedFolder;
        this.unmarkedFolder = unmarkedFolder;
        setMarkedImages();
        setUnmarkedImages();
        setPanels();
        addImages();
    }
    private void setMarkedImages() {
        tv2Marked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/tv2.png").getImage(), 488, 150);
        nrkMarked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/nrk.png").getImage(), 526, 150);
        csportsMarked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/csports.png").getImage(), 678, 150);
        viaplayMarked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/viaplay.png").getImage(), 709, 150);
        netflixMarked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/netflix.png").getImage(), 550, 150);
        markedImages = new ArrayList<Logo>();

        markedImages.add(csportsMarked);
        markedImages.add(netflixMarked);
        markedImages.add(viaplayMarked);
        markedImages.add(nrkMarked);
        markedImages.add(tv2Marked);
    }
    private void setUnmarkedImages() {
        tv2Unmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/tv2.png").getImage(), 488, 150);
        nrkUnmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/nrk.png").getImage(), 526, 150);
        csportsUnmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/csports.png").getImage(), 678, 150);
        viaplayUnmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/viaplay.png").getImage(), 709, 150);
        netflixUnmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/netflix.png").getImage(), 550, 150);

        unmarkedImages = new ArrayList<Logo>();
        unmarkedImages.add(csportsUnmarked);
        unmarkedImages.add(netflixUnmarked);
        unmarkedImages.add(viaplayUnmarked);
        unmarkedImages.add(nrkUnmarked);
        unmarkedImages.add(tv2Unmarked);
    }
    private void setPanels() {
        tv2Panel = new JPanel();
        nrkPanel = new JPanel();
        csportsPanel = new JPanel();
        viaplayPanel = new JPanel();
        netflixPanel = new JPanel();
        logoList = new ArrayList<JPanel>();


        CountDownLatch cdl = new CountDownLatch(5);

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

        new Thread(csportsThread).start();
        new Thread(netflixThread).start();
        new Thread(viaplayThread).start();
        new Thread(nrkThread).start();
        new Thread(tv2Thread).start();

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
        c.gridy = yValue++;
        add(csportsPanel, c);

        c.gridx = 0;
        c.gridy = yValue++;
        add(netflixPanel, c);

        c.gridx = 0;
        c.gridy = yValue++;
        add(viaplayPanel, c);

        c.gridx = 0;
        c.gridy = yValue++;
        add(nrkPanel, c);

        c.gridx = 0;
        c.gridy = yValue++;
        add(tv2Panel, c);

        addKeyListeners(this, true);

        revalidate();
        repaint();
        pack();

        enableOSXFullscreen(this);

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
                            Runtime.getRuntime().exec( new String[] { "open" , "-a", "Safari", appsList.get(counter) }) ;

                        } catch (IOException f) {
                            System.out.println("Appen finnes ikke!");
                        }
                        break;
                }
            }
        });
    }

    private void setSelected(JPanel panel, Logo image) {

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

    private void setUnselected(JPanel panel, Logo image) {

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
        private Logo image;
        private JPanel panel;

        AddImagePanel(CountDownLatch cdl, Logo image, JPanel panel) {
            this.cdl = cdl;
            this.image = image;
            this.panel = panel;
        }

        AddImagePanel(Logo image, JPanel panel) {
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
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void enableOSXFullscreen(GUI window) {
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
