import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.io.*;
import java.util.*;

public class Indexer {
    public static HashMap<String, String> result = new HashMap<>();
    public static ArrayList<ArrayList<HashMap<String, String>>> dataList = new ArrayList<>(); //index 저장
    public static HashMap<String,String> keyMap = new HashMap<>();
    public static Set<String> keys;
    public static String path="";

    Indexer(String path){
        this.path = path;
    }

    public static void init() throws IOException, ClassNotFoundException {
        FileOutputStream fileStream = new FileOutputStream("src/index.post");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
        keywordMap();

        objectOutputStream.writeObject(result);
        objectOutputStream.close();

        fileInput();
    }

    public static void keywordMap(){
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
                HashMap<String,String> data = new HashMap<>();
                ArrayList<HashMap<String, String>> indexdata = new ArrayList<>();
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String txtString =  eElement.getElementsByTagName("body").item(0).getTextContent();
                    String[] splitarr = txtString.split("#");
                    for (int i = 0; i< splitarr.length; i++){
                        String[] arr = splitarr[i].split(":");
                        keyMap.put(arr[0], arr[1]); //키만 받아오기 => keyset 생성을 위해
                        data.put(arr[0], arr[1]); //index 별 키 저장
                        indexdata.add(data);
                        keys = keyMap.keySet();
                    }

                }
                dataList.add(indexdata);
            }
            int N = dataList.size(); // 총 문서개수
            for(String key : keys){
                int[] tf = new int[5]; // 키 별 가중치 저장
                double[] weight = new double[5];
                int dfx=0; //
                for(int i=0; i< dataList.size(); i++){ //index마다 돌기
                    if(dataList.get(i).get(0).containsKey(key)){
                        tf[i] = Integer.parseInt(dataList.get(i).get(0).get(key));
                        dfx++;
                    }
                }
                //가중치 계산
                String resulttxt = "";
                for(int j=0; j<N; j++){
                    weight[j] = Math.round( tf[j] * Math.log( N / dfx )* 100) / 100.0;
                    resulttxt += j + " " + weight[j]+ " ";
                }
                result.put(key, resulttxt);

            }

        }
        catch(IOException e) {
            System.out.println(e);
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
    public static void fileInput() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("src/index.post");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap)object;
        Iterator<String> it = hashMap.keySet().iterator();

        while (it.hasNext()){
            String key = it.next();
            String value = (String) hashMap.get(key);
            System.out.println(key+"->"+value);
        }
    }
}