import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.joda.time.*;


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
    private JPanel tv2Panel, nrkPanel, csportsPanel, viaplayPanel, netflixPanel, datePanel, timePanel;
    private JodaLabel dateLabel, timeLabel;
    private int counter = 0;
    private boolean initialized = false;

    private final int IMAGE_HEIGHT = 86, IMAGE_WIDTH = 300;

    GUI(ArrayList<String> appsList, File markedFolder, File unmarkedFolder) {
        this.appsList = appsList;
        this.markedFolder = markedFolder;
        this.unmarkedFolder = unmarkedFolder;

        setMarkedImages();
        setUnmarkedImages();
        setDateAndTime();
        setPanels();
        addPanels();
        new Thread(new UpdateTimeAndDate()).start();

    }

    private void setDateAndTime() {
        LocalTime localTime = new LocalTime();
        LocalDate localDate = new LocalDate();
        String minutes, seconds, hours, months, days;

        minutes = ""+localTime.getMinuteOfHour();
        hours = ""+localTime.getHourOfDay();
        seconds = ""+localTime.getSecondOfMinute();
        months = ""+localDate.getMonthOfYear();
        days = ""+localDate.getDayOfMonth();

        if (Integer.parseInt(minutes) < 10) {
            minutes = "0"+localTime.getMinuteOfHour();
        }
        if (Integer.parseInt(seconds) < 10) {
            seconds = "0"+localTime.getSecondOfMinute();
        }
        if (Integer.parseInt(hours) < 10) {
            hours = "0"+localTime.getHourOfDay();
        }
        if (Integer.parseInt(months) < 10) {
            months = "0"+localDate.getMonthOfYear();
        }
        if (Integer.parseInt(days) < 10) {
            days = "0"+localDate.getDayOfMonth();
        }

        String time =  hours + " : " + minutes + " : " + seconds;
        String date = days + " / " + months + " / " + localDate.getYear();
        if (!initialized) {
            datePanel = new JPanel();
            timePanel = new JPanel();
            datePanel.setOpaque(false);
            timePanel.setOpaque(false);
            dateLabel = new JodaLabel(date);
            timeLabel = new JodaLabel(time);
            dateLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 30));
            timeLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 60));
            datePanel.add(dateLabel);
            timePanel.add(timeLabel);
            initialized = true;
        } else {
            timeLabel.setText(time);
            dateLabel.setText(date);
            datePanel.revalidate();
            datePanel.repaint();
            timePanel.repaint();
            timePanel.repaint();

        }

        revalidate();
        repaint();
    }
    private void setMarkedImages() {
        tv2Marked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/tv2.png").getImage(), 390, 150);
        nrkMarked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/nrk.png").getImage(), 421, 150);
        csportsMarked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/csports.png").getImage(), 542, 150);
        viaplayMarked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/viaplay.png").getImage(), 567, 150);
        netflixMarked = new Logo(new ImageIcon(markedFolder.getAbsolutePath()+"/netflix.png").getImage(), 440, 150);
        markedImages = new ArrayList<Logo>();

        markedImages.add(csportsMarked);
        markedImages.add(netflixMarked);
        markedImages.add(viaplayMarked);
        markedImages.add(nrkMarked);
        markedImages.add(tv2Marked);
    }
    private void setUnmarkedImages() {
        tv2Unmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/tv2.png").getImage(), 390, 150);
        nrkUnmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/nrk.png").getImage(), 421, 150);
        csportsUnmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/csports.png").getImage(), 542, 150);
        viaplayUnmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/viaplay.png").getImage(), 567, 150);
        netflixUnmarked = new Logo(new ImageIcon(unmarkedFolder.getAbsolutePath()+"/netflix.png").getImage(), 440, 150);

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
    private void addPanels() {
        GridBagConstraints c = new GridBagConstraints();
        int xValue = 0, yValue = 0;

        getContentPane().setBackground(Color.BLACK);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setVisible(true);

        JPanel logos = new JPanel();
        logos.setLayout(new GridBagLayout());
        logos.setBackground(Color.BLACK);

        c.gridx = xValue;
        c.gridy = yValue++;
        logos.add(csportsPanel, c);

        c.gridx = xValue;
        c.gridy = yValue++;
        logos.add(netflixPanel, c);

        c.gridx = xValue;
        c.gridy = yValue++;
        logos.add(viaplayPanel, c);

        c.gridx = xValue;
        c.gridy = yValue++;
        logos.add(nrkPanel, c);

        c.gridx = xValue++;
        c.gridy = yValue;
        logos.add(tv2Panel, c);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        c.gridx = 0;
        c.gridy = 0;
        panel.add(datePanel, c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(timePanel, c);

        add(panel, BorderLayout.EAST);
        add(logos, BorderLayout.CENTER);


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
    private class UpdateTimeAndDate implements Runnable {

        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                setDateAndTime();
                System.out.println("Hei");
            }
        }
    }
    private class JodaLabel extends JLabel {
        JodaLabel(String text) {
            super(text);
            setForeground(Color.WHITE);
            setOpaque(false);

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
