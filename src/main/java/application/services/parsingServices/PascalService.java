package application.services.parsingServices;

import application.model.Annotations;
import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.services.parentClass.PrepService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

import static application.model.subsets.Categories.createCategory;
import static application.model.subsets.Images.createImage;

/**
 * Reads all files in PASCAL- Format and stores the passed data in common lists.
 * Current Format:
 * <annotation>
 * <folder>..</folder>
 * <filename>..</filename>
 * <source>
 * <database>..</database>
 * <annotation>..</annotation>
 * <image>..</image>
 * </source>
 * <size>
 * <width>..</width>
 * <height>..</height>
 * <depth>..</depth>
 * </size>
 * <segmented>..</segmented>
 * <object>
 * <name>..</name>
 * <bndbox>
 * <xmin>..</xmin>
 * <ymin>..</ymin>
 * <xmax>..</xmax>
 * <ymax>..</ymax>
 * </bndbox>
 * </object>
 * <object>
 * ...
 * </object>
 * ...
 * </annotation>
 */
public class PascalService extends PrepService {

    public PascalService(ArrayList <Images> imagesList, ArrayList <Categories> categoriesList, ArrayList <Annotations> annotationsList, ArrayList <ArrayList <String>> infoLog) {
        super(imagesList, categoriesList, annotationsList, infoLog);
    }

    /**
     * Extracts the contained information out of the XML- File by going through the tree.
     *
     * @param filePath filepath of passed Multipartfile
     */
    public void readPascalXml(String filePath) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File file = new File(filePath);
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            String imageName = doc.getElementsByTagName("filename").item(0).getTextContent();
            if (imageName.contains(".")) {
                imageName = imageName.substring(0, imageName.lastIndexOf("."));
            }
            int width = Integer.parseInt(doc.getElementsByTagName("width").item(0).getTextContent());
            int height = Integer.parseInt(doc.getElementsByTagName("height").item(0).getTextContent());
            Images image = createImage(imageName, imagesList, width, height);

            NodeList nodeList = doc.getElementsByTagName("object");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String categoryName = element.getElementsByTagName("name").item(0).getTextContent();
                    Categories category = createCategory(categoryName, categoriesList);
                    double xmin = Double.parseDouble(element.getElementsByTagName("xmin").item(0).getTextContent());
                    double ymin = Double.parseDouble(element.getElementsByTagName("ymin").item(0).getTextContent());
                    double xmax = Double.parseDouble(element.getElementsByTagName("xmax").item(0).getTextContent());
                    double ymax = Double.parseDouble(element.getElementsByTagName("ymax").item(0).getTextContent());
                    Bbox bbox = new Bbox(xmin, ymax, (xmax - xmin), (ymax - ymin));
                    Annotations annotation = new Annotations(image, category, bbox, annotationsList.size() + 1);
                    annotation.setAbsoluteValueBbox();
                    annotationsList.add(annotation);
                }
            }
            this.infoLog.get(infoLog.size() - 1).add("eingelesen. ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
