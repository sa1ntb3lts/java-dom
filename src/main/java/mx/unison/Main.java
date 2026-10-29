package mx.unison;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "xml/test_svg_dom.svg";
        if (args.length > 0) {
            filePath = args[0];
        }

        SVGParser parser = new SVGParser();
        parser.parseSVG(filePath);

        List<SVGElement> elements = parser.getElements();
        int width = parser.getCanvasWidth();
        int height = parser.getCanvasHeight();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Visualizador SVG (DOM + Java 2D)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            SVGCanvas canvas = new SVGCanvas(elements, width, height);

            JScrollPane scrollPane = new JScrollPane(canvas);
            frame.add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}