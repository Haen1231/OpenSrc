
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Searcher {
    public static HashMap<String, Integer> query = new HashMap<>();
    public static Double[][] innerResult = new Double[2][5];
    public static Double[][] calcResult = new Double[2][5];
    public static int[] maxIndex = new int[3]; // 3개 index 저장
    public static ArrayList<String> titleList = new ArrayList<>();
    public static String path = "";
    public static String queryString="";

    Searcher(String path, String querySting) throws IOException, ClassNotFoundException {
        this.path = path;
        this.queryString = querySting;
    }

    public static void calcSim() throws IOException, ClassNotFoundException {
        System.out.println(queryString);
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(queryString, true);
        for (int i = 0; i < kl.size(); i++) {
            Keyword kwrd = kl.get(i);
            String key = kwrd.getString();
            int value = kwrd.getCnt();
            query.put(key, value);
        }
        FileInputStream fileStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap) object;  //index.post 정보 저장

        //innerProduct
        double sim = 0.0;
        double sim2 = 0.0;
        for(int i = 0; i< 5; i++) { //문서 인덱스
            innerResult[0][i] = Double.valueOf(i); // index저장
            calcResult[0][i] = Double.valueOf(i);
            innerResult[1][i] = 0.0;
            Iterator<String> it = query.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (hashMap.containsKey(key)) {
                    String[] value = (String[]) hashMap.get(key);
                    int queryvalue = query.get(key);
                    String s = value[i];
                    innerResult[1][i] += (queryvalue * Double.parseDouble(s));
                    sim += Math.pow(queryvalue, 2);
                    sim2 += Math.pow(Double.parseDouble(s), 2);
                } else innerResult[1][i] += 0.0;
            }
            calcResult[1][i] = innerResult[1][i] / (Math.pow(sim, 1 / 2) * Math.pow(sim2, 1 / 2));
        }

        //유사도 상위 3개 index 저장
        for (int i=0; i< 3; i++){
            double tmp = calcResult[0][i];  // max index 저장
            int index = i;
            for(int j = 0; j<5; j++){
                if (tmp < calcResult[1][j]){
                    tmp = calcResult[0][j];
                    index = j;
                }
            }
            calcResult[1][index] = -1.0;
            maxIndex[i] = index;
        }
        printTitle();


    }

    public static void printTitle(){
        try {
            File file = new File("./collection.xml");
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

        System.out.println("유사도 검색 결과");

        for (int i=0; i<maxIndex.length; i++){
            for (int j=0; j<titleList.size(); j++){
                if(maxIndex[i] == j){
                    System.out.println(i+1+"등 : "+titleList.get(j));
                }
            }
        }
    }
}