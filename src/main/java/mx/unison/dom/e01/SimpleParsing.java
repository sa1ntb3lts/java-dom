package mx.unison.dom.e01;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class SimpleParsing {

    static final String CLASS_NAME = SimpleParsing.class.getSimpleName();
    static final Logger LOG = Logger.getLogger(CLASS_NAME);

    public static void main(String[] argv) {

        if (argv.length != 1) {
            LOG.severe("Falta archivo XML como argumento.");
            System.exit(1);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = null;
        File file = new File(argv[0]);
        try{
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOG.severe(e.getMessage());
            System.exit(2);
        }
        Document document = null;
        if( documentBuilder != null) {

            try {
                document = documentBuilder.parse(new File(argv[0]));
                document.getDocumentElement().normalize();
            } catch (SAXException e) {
                LOG.severe(e.getMessage());
            } catch (IOException e) {
                LOG.severe(e.getMessage());
            }
            Element root = document.getDocumentElement();
            System.out.printf("Root element: %s\n", root.getNodeName());
            if( root.hasAttributes() ) {
                NamedNodeMap attributes = root.getAttributes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    Attr attr = (Attr) attributes.item(j);
                    System.out.printf("\tAttribute: %s = %s\n", attr.getName(), attr.getValue());
                }
            }

            Node first = root.getFirstChild();
            printNode(first);
            NodeList nodeList = root.getChildNodes();

            int len = nodeList.getLength();
            for (int i = 0; i < len; i++) {
                Node nodo = nodeList.item(i);
                int type = nodo.getNodeType();
                if(type == Node.ELEMENT_NODE) {
                    Element element = (Element) nodo;
                    if( element.hasAttributes() ) {
                        NamedNodeMap attributes = element.getAttributes();
                        for (int j = 0; j < attributes.getLength(); j++) {
                            Attr attr = (Attr) attributes.item(j);
                            System.out.printf("\tAttribute: %s = %s\n", attr.getName(), attr.getValue());
                        }
                    }
                    processNode(nodo);
                }
            }

        }

    }

    private static  void printNode(Node node) {
        if (node == null) {
            return;
        }
        int type = node.getNodeType();
        if (type == Node.ELEMENT_NODE) {
            System.out.printf("%s: %s\n", node.getNodeName(), node.getTextContent());
        }
        if (type == Node.TEXT_NODE) {
            System.out.printf("%s: '%s' %d\n", node.getNodeName(), node.getTextContent(),node.getTextContent().length());
        }
        if (type == Node.CDATA_SECTION_NODE) {

        }
        if (type == Node.ATTRIBUTE_NODE) {

        }
    }

    public static void processNode(Node current) {
        System.out.printf("Name: %s \n", current.getNodeName());
        if ( current.hasChildNodes() ) {
            //  System.out.printf("\tName: %s Value: %s\n", current.getNodeName(),  current.getTextContent());
            NodeList children = current.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if( element.hasAttributes() ) {
                        NamedNodeMap attributes = element.getAttributes();
                        for (int j = 0; j < attributes.getLength(); j++) {
                            Attr attr = (Attr) attributes.item(j);
                            System.out.printf("\tAttribute: %s = %s\n", attr.getName(), attr.getValue());
                        }
                    }
                    processNode(element);
                }
                if( node.getNodeType() == Node.TEXT_NODE ) {
                    Text text = (Text) node;
                    if( !text.isElementContentWhitespace() )
                        System.out.printf("\tValue: '%s'\n",    node.getTextContent());
                }
            }
        }
    }
}
