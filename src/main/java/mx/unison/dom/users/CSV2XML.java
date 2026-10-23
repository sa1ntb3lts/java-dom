package mx.unison.dom.users;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.StringTokenizer;

public class CSV2XML {

    public static void main(String[] args) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();

        final String[] elementName = { "ID", "FIRST_NAME", "LAST_NAME","EMAIL","IP"};

        // crea elemento raiz
        Element rootElement = doc.createElement("USERS-DATA");
        doc.appendChild(rootElement);

        FileReader fin = null;
        BufferedReader in = null;

        try {
            fin = new FileReader("usuarios.csv");
            in = new BufferedReader(fin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String registro = null;

        try {
            while ((registro = in.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(registro, ",");

                Element userElement = doc.createElement("USER");
                rootElement.appendChild( userElement );
                int fieldId = 0;
                while ( st.hasMoreTokens() ) {
                    String value = st.nextToken();

                    Element element = doc.createElement( elementName[fieldId] );
                    userElement.appendChild(element);
                    element.insertBefore( doc.createTextNode(value) ,
                            element.getLastChild() );
                    fieldId++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        prettyPrint(doc);
    }

    public static final void prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        System.out.println(out.toString());
    }

}
