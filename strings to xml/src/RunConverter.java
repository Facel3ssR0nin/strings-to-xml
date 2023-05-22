import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RunConverter {
    public static void convertToXml(File inputFile) throws ParserConfigurationException,
            IOException, TransformerConfigurationException, TransformerException {
        // Create a factory to create the XML parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        // Create the root element for our XML document
        Element resources = doc.createElement("resources");
        doc.appendChild(resources);

        // Open aninput stream to read the .string file
        try (InputStream input = new FileInputStream(inputFile)) {
            // Read the lines from the file
            byte[] buffer = new byte[1024];
            StringBuilder stringBuilder = new StringBuilder();
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }
            String[] lines = stringBuilder.toString().split("\\n");

            // Create elements for each line and add them to the root element
            for (String line : lines) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String value = parts[1].trim();
                    Element stringElement = doc.createElement("string");
                    stringElement.setAttribute("name", name);
                    stringElement.setTextContent(value);
                    resources.appendChild(stringElement);
                }
            }
        }

        // Create a transformer and configure it to write to the file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Create a data source from our XML document
        DOMSource source = new DOMSource(doc);

        // Create an output stream to write the strings.xml file
        try (OutputStream output = new FileOutputStream("strings.xml")) {
            // Create an object to write the XML to the file
            StreamResult result = new StreamResult(output);

            // Write the XML to the file
            transformer.transform(source, result);
        }
    }
}