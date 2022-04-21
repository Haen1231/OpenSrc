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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MidTerm {
    //public static String path="";
    public static String path="./collection.xml";
    public static String queryString="밀가루 넣은 반죽";
    public static ArrayList<String> bodyContent = new ArrayList<>();
    public static ArrayList<String> titleList = new ArrayList<>();
    public static ArrayList<String> snippet = new ArrayList<>();
    public static ArrayList<String> query = new ArrayList<>();
    public static int[][] result;
    public static String[][] resulttxt;

    MidTerm(String path, String query){
        this.path = path;
        queryString = query;
        showSnippet();
    }

    public static void showSnippet(){
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(queryString, true);
        for (int i = 0; i < kl.size(); i++) {
            Keyword kwrd = kl.get(i);
            String key = kwrd.getString();
            query.add(key);
        }

        // title, body 내용 받아오기
        try {
            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            Document document = db.parse(file);

            NodeList nList = document.getElementsByTagName("doc");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String txtString =  eElement.getElementsByTagName("body").item(0).getTextContent();
                    bodyContent.add(txtString);
                    String title =  eElement.getElementsByTagName("title").item(0).getTextContent();
                    titleList.add(title);
                }
            }
        }
        catch(IOException e) {
            System.out.println(e);
        } catch (SAXException e) {
            e.printStackTrace();
        }

        for (int i=0; i< titleList.size(); i++){
            String bodytxt = bodyContent.get(i);
            int matchNum = 0;
            for(int j=0; j<bodytxt.length() - 30; j++){
                String txt = bodytxt.substring(0+j, 30+j);
                //System.out.println(titleList.get(i)+" : "+txt);
                String[] txtword = txt.split(" ");
                for(int n=0; n< query.size(); n++){ // 키워드 별 매칭
                    for(int w = 0; w< txtword.length; w++){
                        if(txtword.equals(query)){
                            matchNum++;
                        }
                    }

                }
                if(matchNum !=0){
                    result[i][j] = matchNum;
                    resulttxt[i][j] =txt;
                }
            }

        }

        for (int i=0; i< result.length;i++){
            for(int j=0; j< result[i].length; j++){
                System.out.println(resulttxt[i][j]+","+result[i][j]);
            }
        }

    }
}
