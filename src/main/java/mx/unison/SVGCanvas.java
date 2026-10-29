package mx.unison;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SVGCanvas extends JPanel {
    private List<SVGElement> elements;

    public SVGCanvas(List<SVGElement> elements, int width, int height) {
        this.elements = elements;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (elements != null) {
            for (SVGElement element : elements) {
                element.draw(g2d);
            }
        }
    }
}