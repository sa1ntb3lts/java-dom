package mx.unison.dom.users;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateDOMDocument {



    public static void main(String[] args) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        Document doc = builder.newDocument();

        // crea elemento raiz
        Element rootElement = doc.createElement("root");
        doc.appendChild( rootElement );

        // crear nodo de comentario
        Comment comment = doc.createComment("Este es un comentario");
        doc.insertBefore(comment, rootElement);

        // agregar elemento despues del primer hijo de la raiz
        Element itemElement = doc.createElement("saludo");
        rootElement.appendChild(itemElement);

        // agregar atributo al elemento
        itemElement.setAttribute("lenguaje", "sp");

        // crear texto (contenido) para el nodo
        itemElement.insertBefore(doc.createTextNode("Hola mundo."), itemElement.getLastChild());

        prettyPrint(doc);

    }

    public static final void prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();

        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");

        Writer out = new StringWriter();

        tf.transform(new DOMSource(xml), new StreamResult(out));
        System.out.println( out.toString() );
    }
}
