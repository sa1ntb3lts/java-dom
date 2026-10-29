package mx.unison;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Permitir pasar la ruta del archivo por argumentos, o usar por defecto "xml/test_svg_dom.svg"
        String filePath = "xml/test_svg_dom.svg";
        if (args.length > 0) {
            filePath = args[0];
        }

        // 1. Parsear el archivo SVG
        SVGParser parser = new SVGParser();
        parser.parseSVG(filePath);

        List<SVGElement> elements = parser.getElements();
        int width = parser.getCanvasWidth();
        int height = parser.getCanvasHeight();

        // 2. Configurar la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Visualizador SVG (DOM + Java 2D)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // 3. Crear el lienzo gráfico con los elementos parseados
            SVGCanvas canvas = new SVGCanvas(elements, width, height);

            // 4. Agregar el lienzo a un JScrollPane para permitir desplazamiento si es grande
            JScrollPane scrollPane = new JScrollPane(canvas);
            frame.add(scrollPane, BorderLayout.CENTER);

            // 5. Mostrar la ventana
            frame.setVisible(true);
        });
    }
}