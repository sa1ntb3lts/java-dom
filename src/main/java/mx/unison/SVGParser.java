package mx.unison;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;

public class SVGParser {

    public void parseSVG(String filePath) {
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
            System.out.println("Raíz SVG: " + root.getNodeName());
            System.out.println("Ancho: " + root.getAttribute("width") + ", Alto: " + root.getAttribute("height"));

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

                switch (tagName) {
                    case "line":
                        System.out.println("Elemento line -> x1: " + element.getAttribute("x1") +
                                ", y1: " + element.getAttribute("y1") +
                                ", x2: " + element.getAttribute("x2") +
                                ", y2: " + element.getAttribute("y2") +
                                ", stroke: " + element.getAttribute("stroke") +
                                ", stroke-width: " + element.getAttribute("stroke-width"));
                        break;
                    case "circle":
                        System.out.println("Elemento circle -> cx: " + element.getAttribute("cx") +
                                ", cy: " + element.getAttribute("cy") +
                                ", r: " + element.getAttribute("r") +
                                ", fill: " + element.getAttribute("fill") +
                                ", stroke: " + element.getAttribute("stroke"));
                        break;
                    case "polyline":
                        System.out.println("Elemento polyline -> points: " + element.getAttribute("points") +
                                ", stroke: " + element.getAttribute("stroke") +
                                ", fill: " + element.getAttribute("fill"));
                        break;
                    case "rect":
                        System.out.println("Elemento rect -> x: " + element.getAttribute("x") +
                                ", y: " + element.getAttribute("y") +
                                ", width: " + element.getAttribute("width") +
                                ", height: " + element.getAttribute("height") +
                                ", fill: " + element.getAttribute("fill"));
                        break;
                    case "ellipse":
                        System.out.println("Elemento ellipse -> cx: " + element.getAttribute("cx") +
                                ", cy: " + element.getAttribute("cy") +
                                ", rx: " + element.getAttribute("rx") +
                                ", ry: " + element.getAttribute("ry") +
                                ", fill: " + element.getAttribute("fill"));
                        break;
                    case "text":
                        System.out.println("Elemento text -> x: " + element.getAttribute("x") +
                                ", y: " + element.getAttribute("y") +
                                ", font-family: " + element.getAttribute("font-family") +
                                ", font-size: " + element.getAttribute("font-size") +
                                ", texto: " + element.getTextContent());
                        break;
                    case "g":
                        parseNodes(element.getChildNodes());
                        break;
                    default:
                        break;
                }
            }
        }
    }
}