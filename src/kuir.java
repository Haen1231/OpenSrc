import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class kuir {

    public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, ClassNotFoundException {

        String command = args[0];
        String path = args[1];

        if(command.equals("-c")) {
            makeCollection collection = new makeCollection(path);
            collection.makeFile();
        }
        else if(command.equals("-k")) {
            makeKeyword keyword = new makeKeyword(path);
            keyword.convertXml();
        }else if(command.equals("-i")){
            Indexer indexer = new Indexer(path);
            indexer.init();
        }else if(command.equals("-s")){
            String postRoute = "src/" + args[1];
            if(args[2].equals("-q")){
                String query = args[3];
                Searcher sr = new Searcher(postRoute, query);
                sr.CalcSim();
            }else{
                System.out.println("Query doesn't exist.");
            }
        }

    }

}


// -s
//./index.post
//-q
//"라면에는 면과 스프가 있다. 아이스크림은 유럽에서 먹는 아이스크림이 맛있으며 면에는 국수, 라면, 짜장면 등이 존재한다. 떡은 한국의 고유 음식이다"

//test test

//hello