import javax.swing.*;
import java.awt.*;

/**
 * Created by Roar on 30.11.14.
 */
public class Logo extends JPanel {
    Image img;

    public Logo(Image img, int lengde, int bredde) {
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

