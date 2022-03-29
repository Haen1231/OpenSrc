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
        }
    }
}
