package mx.unison.dom.users;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.StringTokenizer;

public class CSVaXML {

    public static final int USR_ID = 0;
    public static final int USR_FIRSTNAME = 1;
    public static final int USR_LASTNAME = 2;
    public static final int USR_EMAIL = 3;
    public static final int USR_IP = 4;

    public static void main(String[] args) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();

        // crea elemento raiz
        Element rootElement = doc.createElement("users-data");
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
        Element idElement = null;
        Element firstNameElement = null;
        Element lastNameElement = null;
        Element emaiElement = null;
        Element ipElement = null;

        try {
            while ((registro = in.readLine()) != null) {
                int fieldId = 0;
                StringTokenizer st = new StringTokenizer(registro, ",");
                System.out.println(registro);

                Element userElement = doc.createElement("user");
                rootElement.appendChild(userElement);

                while ( st.hasMoreTokens() ) {
                    String value = st.nextToken();

                    switch (fieldId) {
                        case USR_ID:
                            idElement = doc.createElement("usr_id");
                            userElement.appendChild(idElement);
                            idElement.insertBefore(doc.createTextNode(value),
                                    idElement.getLastChild());
                            break;
                        case USR_FIRSTNAME:
                            firstNameElement = doc.createElement("first_name");
                            userElement.appendChild(firstNameElement);
                            firstNameElement.insertBefore(doc.createTextNode(value),
                                    firstNameElement.getLastChild());
                            break;
                        case USR_LASTNAME:
                            lastNameElement = doc.createElement("last_name");
                            userElement.appendChild(lastNameElement);
                                         lastNameElement.insertBefore(doc.createTextNode(value),
                                    lastNameElement.getLastChild());
                            break;
                        case USR_EMAIL:
                            emaiElement = doc.createElement("email");

                            userElement.appendChild(emaiElement);
                            emaiElement.insertBefore(doc.createTextNode(value),
                                    emaiElement.getLastChild());
                            break;
                        case USR_IP:
                            ipElement = doc.createElement("usr_ip");
                            userElement.appendChild(ipElement);
                            ipElement.insertBefore(doc.createTextNode(value),
                                    ipElement.getLastChild());
                            break;
                    }
                    fieldId++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        prettyPrint(doc);

        if( args.length > 0 ) {
            File file = new File(args[0]);
            saveDocument(doc, file);
        }

    }

    public static final void saveDocument(Document xml, File file) {

        Transformer tf = null;

        FileWriter out = null;
        try {
            tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            out = new FileWriter(file);
            tf.transform(new DOMSource(xml), new StreamResult(out));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        System.out.println(out.toString());
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
