package application.services;

import application.model.Annotations;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.services.parsingServices.CocoService;
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
public class CocoTest {

    ArrayList <Images> listImages;
    ArrayList <Categories> listCat;
    ArrayList <Annotations> listAnn;
    ArrayList <ArrayList <String>> infoLog;
    CocoService cocoService;
    String path;

    @Before
    public void setUp() {
        listImages = new ArrayList <>();
        listCat = new ArrayList <>();
        listAnn = new ArrayList <>();
        infoLog = new ArrayList <>();
        ArrayList <String> infoInner = new ArrayList <>();
        infoLog.add(infoInner);
        cocoService = new CocoService(listImages, listCat, listAnn, infoLog);
        path = "src/test/resources/coco_test.json";
    }

    @After
    public void delete() {
        listImages.clear();
        listCat.clear();
        listAnn.clear();
    }

    @Test
    public void readCocoJsonTest() {
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        cocoService.readCocoJson(path);
        Assert.assertEquals(2, listImages.size());
        Assert.assertEquals(1, listCat.size());
        Assert.assertEquals(2, listAnn.size());

        Assert.assertEquals(1, listImages.get(0).getId());
        Assert.assertEquals("2008_000210", listImages.get(0).getFileName());
        Assert.assertEquals(500, listImages.get(0).getWidth());
        Assert.assertEquals(333, listImages.get(0).getHeight());
        Assert.assertEquals(2, listImages.get(1).getId());
        Assert.assertEquals("2008_000216", listImages.get(1).getFileName());
        Assert.assertEquals(500, listImages.get(1).getWidth());
        Assert.assertEquals(371, listImages.get(1).getHeight());

        Assert.assertEquals(1, listCat.get(0).getId());
        Assert.assertEquals("person", listCat.get(0).getName());

        Assert.assertEquals(1, listAnn.get(0).getId());
        Assert.assertEquals(21.0, listAnn.get(0).getBbox().getXMin(), 0.1);
        Assert.assertEquals(333.0, listAnn.get(0).getBbox().getYMax(), 0.1);
        Assert.assertEquals(418.0, listAnn.get(0).getBbox().getWidth(), 0.1);
        Assert.assertEquals(332.0, listAnn.get(0).getBbox().getHeight(), 0.1);
        Assert.assertEquals(0.0, listAnn.get(0).getPolygon().getPolygon().get(0).get(0), 0.1);
        Assert.assertEquals(45.0, listAnn.get(0).getPolygon().getPolygon().get(0).get(1), 0.1);
        Assert.assertEquals(1, listAnn.get(0).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(0).getImage().getId());

        Assert.assertEquals(2, listAnn.get(1).getId());
        Assert.assertEquals(60.0, listAnn.get(1).getBbox().getXMin(), 0.1);
        Assert.assertEquals(371.0, listAnn.get(1).getBbox().getYMax(), 0.1);
        Assert.assertEquals(137.0, listAnn.get(1).getBbox().getWidth(), 0.1);
        Assert.assertEquals(319.0, listAnn.get(1).getBbox().getHeight(), 0.1);
        Assert.assertEquals(1, listAnn.get(1).getCategory().getId());
        Assert.assertEquals(2, listAnn.get(1).getImage().getId());
    }
}
