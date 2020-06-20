package application.model.output;

import application.model.Annotations;
import application.model.outputFormats.openImage.OpenImage5;
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

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
public class OpenImageTest {
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
    public void checkConstructor1OpenImage() {
        OpenImage5 openImage5 = new OpenImage5(listAnn);

        Assert.assertEquals(1, openImage5.getOpenImageList().size());
        Assert.assertEquals("image1", openImage5.getOpenImageList().get(0).getImageId());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(0).getConfidence());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(0).getIsOccluded());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(0).getIsTruncated());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(0).getIsGroupOf());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(0).getIsDepiction());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(0).getIsInside());
        Assert.assertEquals("unknown", openImage5.getOpenImageList().get(0).getSource());
        Assert.assertEquals("categorie1", openImage5.getOpenImageList().get(0).getLabelName());
        Assert.assertEquals(2, openImage5.getOpenImageList().get(0).getXMin(), 0.1);
        Assert.assertEquals(5, openImage5.getOpenImageList().get(0).getYMax(), 0.1);
        Assert.assertEquals(8, openImage5.getOpenImageList().get(0).getXMax(), 0.1);
        Assert.assertEquals(3, openImage5.getOpenImageList().get(0).getYMin(), 0.1);
    }

    @Test
    public void checkConstructor2OpenImage() {
        Annotations annotation2 = new Annotations();
        listAnn.add(annotation2);
        OpenImage5 openImage5 = new OpenImage5(listAnn);

        Assert.assertEquals(2, openImage5.getOpenImageList().size());
        Assert.assertEquals("image1", openImage5.getOpenImageList().get(0).getImageId());
        Assert.assertEquals("unknown", openImage5.getOpenImageList().get(1).getImageId());
        Assert.assertEquals("categorie1", openImage5.getOpenImageList().get(0).getLabelName());
        Assert.assertEquals("unknown", openImage5.getOpenImageList().get(1).getLabelName());

        Assert.assertEquals(-1, openImage5.getOpenImageList().get(1).getConfidence());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(1).getIsOccluded());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(1).getIsTruncated());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(1).getIsGroupOf());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(1).getIsDepiction());
        Assert.assertEquals(-1, openImage5.getOpenImageList().get(1).getIsInside());
        Assert.assertEquals("unknown", openImage5.getOpenImageList().get(1).getSource());
        Assert.assertEquals(0, openImage5.getOpenImageList().get(1).getXMin(), 0.1);
        Assert.assertEquals(0, openImage5.getOpenImageList().get(1).getYMax(), 0.1);
        Assert.assertEquals(0, openImage5.getOpenImageList().get(1).getXMax(), 0.1);
        Assert.assertEquals(0, openImage5.getOpenImageList().get(1).getYMin(), 0.1);
    }

    @Test
    public void printCsv1Test() {
        OpenImage5 openImage5 = new OpenImage5(listAnn);
        String openImageCsv1 = "ImageID,Source,LabelName,Confidence,XMin,XMax,YMin,YMax,IsOccluded,IsTruncated,IsGroupOf,IsDepiction,IsInside\n" + "image1,unknown,categorie1,-1,2,8,3,5,-1,-1,-1,-1,-1";
        Assert.assertEquals(openImageCsv1, openImage5.printCsv());
    }

    @Test
    public void printCsvRelativ1Test() {
        OpenImage5 openImage5 = new OpenImage5(listAnn);
        openImage5.getOpenImageList().get(0).setXMin(0.0006788);
        openImage5.getOpenImageList().get(0).setXMax(0.02);
        openImage5.getOpenImageList().get(0).setYMin(0.1);
        openImage5.getOpenImageList().get(0).setYMax(0.000000009);
        String openImageCsv1 = "ImageID,Source,LabelName,Confidence,XMin,XMax,YMin,YMax,IsOccluded,IsTruncated,IsGroupOf,IsDepiction,IsInside\n" + "image1,unknown,categorie1,-1,0.0006788,0.02,0.1,0.00000001,-1,-1,-1,-1,-1";
        Assert.assertEquals(openImageCsv1, openImage5.printCsv());
    }

    @Test
    public void printCsv2Test() {
        Annotations annotation2 = new Annotations();
        listAnn.add(annotation2);
        OpenImage5 openimage52 = new OpenImage5(listAnn);
        String openimageCsv2 = "ImageID,Source,LabelName,Confidence,XMin,XMax,YMin,YMax,IsOccluded,IsTruncated,IsGroupOf,IsDepiction,IsInside\n" + "image1,unknown,categorie1,-1,2,8,3,5,-1,-1,-1,-1,-1\n" + "unknown,unknown,unknown,-1,0,0,0,0,-1,-1,-1,-1,-1";
        Assert.assertEquals(openimageCsv2, openimage52.printCsv());
    }
}
