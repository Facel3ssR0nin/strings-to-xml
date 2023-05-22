import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class Main {
    public static void main(String[] args) {
        try {
            // Provide path to .strings while that you want to convert to .xml
            File inputFile = new File("/Users/Facel3ssR0nin/yourstringsfile.strings");
            RunConverter.convertToXml(inputFile);
            System.out.println("Convert proceeded successfully!");
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error during reading file!");
            e.printStackTrace();
        }
    }
}