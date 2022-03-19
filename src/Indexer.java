import org.jsoup.Jsoup;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Indexer {
    public static void makeFile() throws ParserConfigurationException, TransformerException, IOException{
        String path = "src/data";
        File [] file = makeFileList(path);

        DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docbuilder = docfactory.newDocumentBuilder();

        Document document = docbuilder.newDocument();

        Element docs = document.createElement("docs");
        document.appendChild(docs);

        for(int i =0; i<5; i++) {

            org.jsoup.nodes.Document html = Jsoup.parse(file[i], "UTF-8");
            String titleData = html.title();
            String bodyData = html.body().text();

            Element doc = document.createElement("doc");
            docs.appendChild(doc);

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
    public static File[] makeFileList(String path){
        File dir = new File(path);
        return dir.listFiles();
    }
    public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException {
        //makeFile();
        String testString ="꼬꼬마형테소분석기를 테스트 하고 있어여. 테스트 결과를 볼게요";

        try {
            File file = new File("collection.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document document = db.parse(file);

            NodeList nList = document.getElementsByTagName("doc");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String txtString =  eElement.getElementsByTagName("body").item(0).getTextContent();
                    KeywordExtractor ke = new KeywordExtractor();
                    KeywordList kl = ke.extractKeyword(txtString, true);
                    String content = "";
                    for(int i = 0; i<kl.size(); i++){
                        Keyword kwrd = kl.get(i);
                        content += kwrd.getString()+":"+kwrd.getCnt()+"#";
                    }
                    eElement.getElementsByTagName("body").item(0).setTextContent(content);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");


            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(new File("./index.xml")));

            transformer.transform(source, result);
        }
        catch(IOException e) {
            System.out.println(e);
        } catch (SAXException e) {
            e.printStackTrace();
        }


    }
}