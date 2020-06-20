package application.model.outputFormats.pascal;

import application.model.Annotations;
import application.model.outputFormats.ProcessValuesToString;
import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;

import static application.model.outputFormats.pascal.ImagesPascal.containsPascalImage;

/**
 * Represents the PASCAL- Format and contains an ArrayList of objects representing each image with its annotations.
 */
@Data
public class Pascal {
    private ArrayList <ImagesPascal> imagesPascalList = new ArrayList <>();

    public Pascal(ArrayList <Annotations> annotationsList) {
        ArrayList <ImagesPascal> ImagesPascals = new ArrayList <>();
        for (Annotations annotations : annotationsList) {
            if (containsPascalImage(annotations.getImage().getFileName(), ImagesPascals) == -1) {
                ImagesPascal imagePascal = new ImagesPascal(annotations.getImage().getFileName(), annotations.getImage().getWidth(), annotations.getImage().getHeight(), getAnnotationsWithImageId(annotations.getImage().getId(), annotationsList));
                ImagesPascals.add(imagePascal);
            }
        }
        this.imagesPascalList = ImagesPascals;
    }

    private ArrayList <Annotations> getAnnotationsWithImageId(long id, ArrayList <Annotations> annotationsList) {
        ArrayList <Annotations> annotationsPascal = new ArrayList <>();
        for (Annotations annotations : annotationsList) {
            if (annotations.getImage().getId() == id) {
                annotationsPascal.add(annotations);
            }
        }
        return annotationsPascal;
    }

    /**
     * Prints the created object to the required format of PASCAL as XML- File. Current format illustrated in
     *
     * @return String in PASCAL format
     * @throws ParserConfigurationException parsing doesnt work
     * @throws TransformerException         transform doesnt work
     * @see application.services.parsingServices.PascalService
     */
    public ArrayList <String> printXml() throws ParserConfigurationException, TransformerException {
        ProcessValuesToString processValuesToString = new ProcessValuesToString();
        ArrayList <String> StringXml = new ArrayList <>();
        for (ImagesPascal imagesPascal : this.imagesPascalList) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("annotation");
            document.appendChild(root);

            Element folder = document.createElement("folder");
            folder.appendChild(document.createTextNode("unknown"));
            root.appendChild(folder);
            Element filename = document.createElement("filename");
            filename.appendChild(document.createTextNode(imagesPascal.getFileName() + ".jpg"));
            root.appendChild(filename);
            Element source = document.createElement("source");
            root.appendChild(source);
            Element database = document.createElement("database");
            database.appendChild(document.createTextNode("unknown"));
            source.appendChild(database);
            Element annotation2 = document.createElement("annotation");
            annotation2.appendChild(document.createTextNode("unknown"));
            source.appendChild(annotation2);
            Element image = document.createElement("image");
            image.appendChild(document.createTextNode("unknown"));
            source.appendChild(image);

            Element size = document.createElement("size");
            root.appendChild(size);
            Element width = document.createElement("width");
            width.appendChild(document.createTextNode(String.valueOf(imagesPascal.getWidth())));
            size.appendChild(width);
            Element height = document.createElement("height");
            height.appendChild(document.createTextNode(String.valueOf(imagesPascal.getHeight())));
            size.appendChild(height);
            Element depth = document.createElement("depth");
            depth.appendChild(document.createTextNode(String.valueOf(imagesPascal.getDepth())));
            size.appendChild(depth);

            Element segmented = document.createElement("segmented");
            segmented.appendChild(document.createTextNode("-1"));
            root.appendChild(segmented);
            for (Annotations annotations : imagesPascal.getAnnotationsPascal()) {
                Element object = document.createElement("object");
                root.appendChild(object);
                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(annotations.getCategory().getName()));
                object.appendChild(name);
                Element bndbox = document.createElement("bndbox");
                object.appendChild(bndbox);
                Element xmin = document.createElement("xmin");
                xmin.appendChild(document.createTextNode(processValuesToString.getValueString(annotations.getBbox().getXMin())));
                bndbox.appendChild(xmin);
                Element ymin = document.createElement("ymin");
                ymin.appendChild(document.createTextNode(processValuesToString.getValueString(annotations.getBbox().getYMax() - annotations.getBbox().getHeight())));
                bndbox.appendChild(ymin);
                Element xmax = document.createElement("xmax");
                xmax.appendChild(document.createTextNode(processValuesToString.getValueString(annotations.getBbox().getXMin() + annotations.getBbox().getWidth())));
                bndbox.appendChild(xmax);
                Element ymax = document.createElement("ymax");
                ymax.appendChild(document.createTextNode(processValuesToString.getValueString(annotations.getBbox().getYMax())));
                bndbox.appendChild(ymax);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource domSource = new DOMSource(document);
            transformer.transform(domSource, result);
            String string = result.getWriter().toString();
            StringXml.add(string);
        }
        return StringXml;
    }
}
