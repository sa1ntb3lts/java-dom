package mx.unison;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SVGParser {
    private List<SVGElement> elements = new ArrayList<>();
    private int canvasWidth = 500;
    private int canvasHeight = 400;

    public void parseSVG(String filePath) {
        elements.clear();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filePath));
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            
            if (root.hasAttribute("width")) {
                canvasWidth = Integer.parseInt(root.getAttribute("width").replaceAll("[^0-9]", ""));
            }
            if (root.hasAttribute("height")) {
                canvasHeight = Integer.parseInt(root.getAttribute("height").replaceAll("[^0-9]", ""));
            }

            parseNodes(root.getChildNodes());

        } catch (Exception e) {
            System.err.println("Error al procesar el documento SVG: " + e.getMessage());
        }
    }

    private void parseNodes(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String tagName = element.getTagName();
                SVGElement svgElement = null;

                switch (tagName) {
                    case "line":
                        svgElement = new LineElement(
                            getDouble(element, "x1", 0),
                            getDouble(element, "y1", 0),
                            getDouble(element, "x2", 0),
                            getDouble(element, "y2", 0)
                        );
                        break;
                    case "circle":
                        svgElement = new CircleElement(
                            getDouble(element, "cx", 0),
                            getDouble(element, "cy", 0),
                            getDouble(element, "r", 0)
                        );
                        break;
                    case "rect":
                        svgElement = new RectElement(
                            getDouble(element, "x", 0),
                            getDouble(element, "y", 0),
                            getDouble(element, "width", 0),
                            getDouble(element, "height", 0)
                        );
                        break;
                    case "ellipse":
                        svgElement = new EllipseElement(
                            getDouble(element, "cx", 0),
                            getDouble(element, "cy", 0),
                            getDouble(element, "rx", 0),
                            getDouble(element, "ry", 0)
                        );
                        break;
                    case "polyline":
                        svgElement = new PolylineElement(element.getAttribute("points"));
                        break;
                    case "text":
                        svgElement = new TextElement(
                            getDouble(element, "x", 0),
                            getDouble(element, "y", 0),
                            element.getTextContent(),
                            element.getAttribute("font-family"),
                            element.getAttribute("font-size")
                        );
                        break;
                    case "g":
                        parseNodes(element.getChildNodes());
                        break;
                }

                if (svgElement != null) {
                    svgElement.setStroke(element.getAttribute("stroke"));
                    svgElement.setFill(element.getAttribute("fill"));
                    svgElement.setStrokeWidth(element.getAttribute("stroke-width"));
                    elements.add(svgElement);
                }
            }
        }
    }

    private double getDouble(Element el, String attr, double defaultValue) {
        String val = el.getAttribute(attr);
        if (val == null || val.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(val.replaceAll("[^0-9.-]", ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public List<SVGElement> getElements() {
        return elements;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }
}