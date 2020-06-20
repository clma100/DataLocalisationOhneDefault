package application.services;


import application.model.Annotations;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.services.parsingServices.PascalService;
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
public class PascalTest {

    ArrayList <Images> listImages;
    ArrayList <Categories> listCat;
    ArrayList <Annotations> listAnn;
    ArrayList <ArrayList <String>> infoLog;
    PascalService pascalService;
    String path;

    @Before
    public void setUp() {
        listImages = new ArrayList <>();
        listCat = new ArrayList <>();
        listAnn = new ArrayList <>();
        infoLog = new ArrayList <>();
        ArrayList <String> infoInner = new ArrayList <>();
        infoLog.add(infoInner);
        pascalService = new PascalService(listImages, listCat, listAnn, infoLog);
        path = "src/test/resources/2008_000200.xml";
    }

    @After
    public void delete() {
        listImages.clear();
        listCat.clear();
        listAnn.clear();
    }

    @Test
    public void readPascalXmlTest() {
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        pascalService.readPascalXml(path);
        Assert.assertEquals(1, listImages.size());
        Assert.assertEquals(1, listCat.size());
        Assert.assertEquals(2, listAnn.size());

        Assert.assertEquals(1, listImages.get(0).getId());
        Assert.assertEquals("2008_000200", listImages.get(0).getFileName());
        Assert.assertEquals(375, listImages.get(0).getHeight());
        Assert.assertEquals(500, listImages.get(0).getWidth());

        Assert.assertEquals(1, listCat.get(0).getId());
        Assert.assertEquals("person", listCat.get(0).getName());

        Assert.assertEquals(1, listAnn.get(0).getId());
        Assert.assertEquals(119, listAnn.get(0).getBbox().getXMin(), 0.1);
        Assert.assertEquals(311, listAnn.get(0).getBbox().getYMax(), 0.1);
        Assert.assertEquals(65, listAnn.get(0).getBbox().getWidth(), 0.1);
        Assert.assertEquals(235, listAnn.get(0).getBbox().getHeight(), 0.1);
        Assert.assertEquals(1, listAnn.get(0).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(0).getImage().getId());

        Assert.assertEquals(2, listAnn.get(1).getId());
        Assert.assertEquals(266, listAnn.get(1).getBbox().getXMin(), 0.1);
        Assert.assertEquals(323, listAnn.get(1).getBbox().getYMax(), 0.1);
        Assert.assertEquals(72, listAnn.get(1).getBbox().getWidth(), 0.1);
        Assert.assertEquals(280, listAnn.get(1).getBbox().getHeight(), 0.1);
        Assert.assertEquals(1, listAnn.get(1).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(1).getImage().getId());
    }
}
