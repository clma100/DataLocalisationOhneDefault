package application.model.output;

import application.model.Annotations;
import application.model.outputFormats.pascal.Pascal;
import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.model.subsets.Polygon;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
public class PascalTest {
    ArrayList <Annotations> listAnn;

    @Before
    public void setUp() {
        listAnn = new ArrayList <>();

        Categories categorie1 = new Categories();
        categorie1.setId(1);
        categorie1.setName("categorie1");

        Images image1 = new Images();
        image1.setId(1);
        image1.setFileName("image1");
        image1.setHeight(400);
        image1.setWidth(300);

        Bbox bbox = new Bbox(2, 5, 6, 2);

        Annotations annotation1 = new Annotations();
        annotation1.setId(1);
        annotation1.setAbsoluteValue(1);
        annotation1.setPolygon(new Polygon());
        annotation1.setBbox(bbox);
        annotation1.setImage(image1);
        annotation1.setCategory(categorie1);
        listAnn.add(annotation1);
    }

    @After
    public void delete() {
        listAnn.clear();
    }

    @Test
    public void checkConstructorPascalTest() {
        Pascal pascal = new Pascal(listAnn);

        Assert.assertEquals(1, pascal.getImagesPascalList().size());
        Assert.assertEquals("image1", pascal.getImagesPascalList().get(0).getFileName());
        Assert.assertEquals(300, pascal.getImagesPascalList().get(0).getWidth());
        Assert.assertEquals(400, pascal.getImagesPascalList().get(0).getHeight());
        Assert.assertEquals(-1, pascal.getImagesPascalList().get(0).getDepth());
        Assert.assertEquals(1, pascal.getImagesPascalList().get(0).getAnnotationsPascal().size());
        Assert.assertEquals(1, pascal.getImagesPascalList().get(0).getAnnotationsPascal().get(0).getId());
    }

    @Test
    public void printXmlTest() throws TransformerException, ParserConfigurationException {
        Pascal pascal = new Pascal(listAnn);
        String pascalXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n" + "<annotation>\r\n" + "    <folder>unknown</folder>\r\n" + "    <filename>image1.jpg</filename>\r\n" + "    <source>\r\n" + "        <database>unknown</database>\r\n" + "        <annotation>unknown</annotation>\r\n" + "        <image>unknown</image>\r\n" + "    </source>\r\n" + "    <size>\r\n" + "        <width>300</width>\r\n" + "        <height>400</height>\r\n" + "        <depth>-1</depth>\r\n" + "    </size>\r\n" + "    <segmented>-1</segmented>\r\n" + "    <object>\r\n" + "        <name>categorie1</name>\r\n" + "        <bndbox>\r\n" + "            <xmin>2</xmin>\r\n" + "            <ymin>3</ymin>\r\n" + "            <xmax>8</xmax>\r\n" + "            <ymax>5</ymax>\r\n" + "        </bndbox>\r\n" + "    </object>\r\n" + "</annotation>\r\n";
        Assert.assertEquals(1, pascal.printXml().size());
        Assert.assertEquals(pascalXml, pascal.printXml().get(0));
    }
}
