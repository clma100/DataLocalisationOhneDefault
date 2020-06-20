package application.services;

import application.model.Annotations;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.services.parsingServices.OpenImage5Service;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
public class OpenImageTest {

    ArrayList <Images> listImages;
    ArrayList <Categories> listCat;
    ArrayList <Annotations> listAnn;
    ArrayList <ArrayList <String>> infoLog;
    OpenImage5Service openImage5Service;
    String path;

    @Before
    public void setUp() {
        listImages = new ArrayList <>();
        listCat = new ArrayList <>();
        listAnn = new ArrayList <>();
        infoLog = new ArrayList <>();
        ArrayList <String> infoInner = new ArrayList <>();
        infoLog.add(infoInner);
        openImage5Service = new OpenImage5Service(listImages, listCat, listAnn, infoLog);
        path = "src/test/resources/test_4lines.csv";
    }

    @After
    public void delete() {
        listImages.clear();
        listCat.clear();
        listAnn.clear();
    }

    @Test
    public void readOpenImageCsvRelativTest() throws IOException {
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        openImage5Service.readOpenImageCsv(path);
        Assert.assertEquals(2, listImages.size());
        Assert.assertEquals(2, listCat.size());
        Assert.assertEquals(4, listAnn.size());

        Assert.assertEquals(1, listImages.get(0).getId());
        Assert.assertEquals("000026e7ee790996", listImages.get(0).getFileName());
        Assert.assertEquals(2, listImages.get(1).getId());
        Assert.assertEquals("000062a39995e348", listImages.get(1).getFileName());

        Assert.assertEquals(1, listCat.get(0).getId());
        Assert.assertEquals("/m/07j7r", listCat.get(0).getName());
        Assert.assertEquals(2, listCat.get(1).getId());
        Assert.assertEquals("/m/015p6", listCat.get(1).getName());

        Assert.assertEquals(1, listAnn.get(0).getId());
        Assert.assertEquals(0.071875, listAnn.get(0).getBbox().getXMin(), 0.1);
        Assert.assertEquals(0.39166668, listAnn.get(0).getBbox().getYMax(), 0.1);
        Assert.assertEquals(0.0734375, listAnn.get(0).getBbox().getWidth(), 0.1);
        Assert.assertEquals(0.18541668, listAnn.get(0).getBbox().getHeight(), 0.1);
        Assert.assertEquals(1, listAnn.get(0).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(0).getImage().getId());

        Assert.assertEquals(4, listAnn.get(3).getId());
        Assert.assertEquals(0.20620842, listAnn.get(3).getBbox().getXMin(), 0.1);
        Assert.assertEquals(1, listAnn.get(3).getBbox().getYMax(), 0.1);
        Assert.assertEquals(0.64301558, listAnn.get(3).getBbox().getWidth(), 0.1);
        Assert.assertEquals(0.84536083, listAnn.get(3).getBbox().getHeight(), 0.1);
        Assert.assertEquals(2, listAnn.get(3).getCategory().getId());
        Assert.assertEquals(2, listAnn.get(3).getImage().getId());
    }
}
