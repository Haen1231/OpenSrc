import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class makeCollection {
    public static String path = "";
    makeCollection(String path){
        this.path = path;
    }
    public static File[] makeFileList(String path) {
        File dir = new File(path);
        return dir.listFiles();
    }

    public static void makeFile() throws ParserConfigurationException, IOException, TransformerException{

        File[] file = makeFileList(path);

        DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docbuilder = docfactory.newDocumentBuilder();


        Document document = docbuilder.newDocument();

        Element docu = document.createElement("docs");
        document.appendChild(docu);

        for (int i = 0; i < 5; i++) {

            org.jsoup.nodes.Document html = null;
            try {
                html = Jsoup.parse(file[i], "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String titleData = html.title();
            String bodyData = html.body().text();

            Element doc = document.createElement("doc");
            docu.appendChild(doc);

            doc.setAttribute("id", "" + i + "");

            Element title = document.createElement("title");
            title.appendChild(document.createTextNode(titleData));
            doc.appendChild(title);

            Element body = document.createElement("body");
            body.appendChild(document.createTextNode(bodyData));
            doc.appendChild(body);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");


        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(new File("./collection.xml")));

        transformer.transform(source, result);
        }
}
