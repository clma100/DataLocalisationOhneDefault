package application.services;

import application.model.Annotations;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.services.parsingServices.CoralService;
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
public class CoralTest {
    ArrayList <Images> listImages;
    ArrayList <Categories> listCat;
    ArrayList <Annotations> listAnn;
    ArrayList <ArrayList <String>> infoLog;
    CoralService coralService;
    String pathAnloSub;
    String pathAnloDev;
    String pathPixSub;
    String pathPixDev;

    @Before
    public void setUp() {
        listImages = new ArrayList <>();
        listCat = new ArrayList <>();
        listAnn = new ArrayList <>();
        infoLog = new ArrayList <>();
        ArrayList <String> infoInner = new ArrayList <>();
        infoLog.add(infoInner);
        coralService = new CoralService(listImages, listCat, listAnn, infoLog);
        pathAnloDev = "src/test/resources/anlodev_test.txt";
        pathAnloSub = "src/test/resources/anlosub_test.txt";
        pathPixDev = "src/test/resources/pixdev_test.txt";
        pathPixSub = "src/test/resources/pixsub_test.txt";
    }

    @After
    public void delete() {
        listImages.clear();
        listCat.clear();
        listAnn.clear();
        infoLog.clear();
    }

    @Test
    public void readCoralAnlodevZweiBilderTest() throws IOException {
        String path = "src/test/resources/zweiBilder.txt";
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        coralService.readCoralTxt(path);
        Assert.assertEquals(2, listImages.size());
        Assert.assertEquals(8, listCat.size());
        Assert.assertEquals(42, listAnn.size());

        Assert.assertEquals("2018_0714_112502_024", listImages.get(0).getFileName());
        Assert.assertEquals("2018_0714_112540_049", listImages.get(1).getFileName());
    }

    @Test
    public void readCoralAnlodevBild24Test() throws IOException {
        String path = "src/test/resources/bild24.txt";
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        coralService.readCoralTxt(path);
        Assert.assertEquals(1, listImages.size());
        Assert.assertEquals(8, listCat.size());
        Assert.assertEquals(44, listAnn.size());

        Assert.assertEquals(1, listImages.get(0).getId());
        Assert.assertEquals("2018_0714_112502_024", listImages.get(0).getFileName());

        Assert.assertEquals("c_sponge_barrel", listCat.get(0).getName());
        Assert.assertEquals("c_soft_coral_gorgonian", listCat.get(1).getName());
        Assert.assertEquals("c_soft_coral", listCat.get(2).getName());
        Assert.assertEquals("c_hard_coral_submassive", listCat.get(3).getName());
        Assert.assertEquals("c_hard_coral_mushroom", listCat.get(4).getName());
        Assert.assertEquals("c_hard_coral_encrusting", listCat.get(5).getName());
        Assert.assertEquals("c_hard_coral_boulder", listCat.get(6).getName());
        Assert.assertEquals("c_hard_coral_branching", listCat.get(7).getName());
    }

    @Test
    public void readCoralAnlodevTest() throws IOException {
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        coralService.readCoralTxt(pathAnloDev);
        Assert.assertEquals(1, listImages.size());
        Assert.assertEquals(3, listCat.size());
        Assert.assertEquals(3, listAnn.size());

        Assert.assertEquals(1, listImages.get(0).getId());
        Assert.assertEquals("2018_0714_112502_024", listImages.get(0).getFileName());

        Assert.assertEquals(1, listCat.get(0).getId());
        Assert.assertEquals("c_sponge_barrel", listCat.get(0).getName());
        Assert.assertEquals(2, listCat.get(1).getId());
        Assert.assertEquals("c_soft_coral_gorgonian", listCat.get(1).getName());
        Assert.assertEquals(3, listCat.get(2).getId());
        Assert.assertEquals("c_soft_coral", listCat.get(2).getName());

        Assert.assertEquals(1, listAnn.get(0).getId());
        Assert.assertEquals(1336.0, listAnn.get(0).getBbox().getXMin(), 0.1);
        Assert.assertEquals(2648.0, listAnn.get(0).getBbox().getYMax(), 0.1);
        Assert.assertEquals(914.0, listAnn.get(0).getBbox().getWidth(), 0.1);
        Assert.assertEquals(701.0, listAnn.get(0).getBbox().getHeight(), 0.1);
        Assert.assertEquals(1, listAnn.get(0).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(0).getImage().getId());

        Assert.assertEquals(2, listAnn.get(1).getId());
        Assert.assertEquals(2286.0, listAnn.get(1).getBbox().getXMin(), 0.1);
        Assert.assertEquals(2858.0, listAnn.get(1).getBbox().getYMax(), 0.1);
        Assert.assertEquals(383.0, listAnn.get(1).getBbox().getWidth(), 0.1);
        Assert.assertEquals(629.0, listAnn.get(1).getBbox().getHeight(), 0.1);
        Assert.assertEquals(2, listAnn.get(1).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(1).getImage().getId());

        Assert.assertEquals(3, listAnn.get(2).getId());
        Assert.assertEquals(3016.0, listAnn.get(2).getBbox().getXMin(), 0.1);
        Assert.assertEquals(3008.0, listAnn.get(2).getBbox().getYMax(), 0.1);
        Assert.assertEquals(819.0, listAnn.get(2).getBbox().getWidth(), 0.1);
        Assert.assertEquals(501.0, listAnn.get(2).getBbox().getHeight(), 0.1);
        Assert.assertEquals(3, listAnn.get(2).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(2).getImage().getId());
    }

    @Test
    public void readCoralAnlosubTest() throws IOException {
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        coralService.readCoralTxt(pathAnloSub);
        Assert.assertEquals(2, listImages.size());
        Assert.assertEquals(1, listCat.size());
        Assert.assertEquals(3, listAnn.size());

        Assert.assertEquals(1, listImages.get(0).getId());
        Assert.assertEquals("2018_0712_073328_036", listImages.get(0).getFileName());
        Assert.assertEquals(2, listImages.get(1).getId());
        Assert.assertEquals("2018_0712_073332_031", listImages.get(1).getFileName());

        Assert.assertEquals(1, listCat.get(0).getId());
        Assert.assertEquals("c_soft_coral", listCat.get(0).getName());

        Assert.assertEquals(1, listAnn.get(0).getId());
        Assert.assertEquals(70.0, listAnn.get(0).getBbox().getXMin(), 0.1);
        Assert.assertEquals(1921.0, listAnn.get(0).getBbox().getYMax(), 0.1);
        Assert.assertEquals(1660.0, listAnn.get(0).getBbox().getWidth(), 0.1);
        Assert.assertEquals(1487.0, listAnn.get(0).getBbox().getHeight(), 0.1);
        Assert.assertEquals(1, listAnn.get(0).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(0).getImage().getId());

        Assert.assertEquals(2, listAnn.get(1).getId());
        Assert.assertEquals(129.0, listAnn.get(1).getBbox().getXMin(), 0.1);
        Assert.assertEquals(1996.0, listAnn.get(1).getBbox().getYMax(), 0.1);
        Assert.assertEquals(1668.0, listAnn.get(1).getBbox().getWidth(), 0.1);
        Assert.assertEquals(1701.0, listAnn.get(1).getBbox().getHeight(), 0.1);
        Assert.assertEquals(1, listAnn.get(1).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(1).getImage().getId());

        Assert.assertEquals(3, listAnn.get(2).getId());
        Assert.assertEquals(2874.0, listAnn.get(2).getBbox().getXMin(), 0.1);
        Assert.assertEquals(2230.0, listAnn.get(2).getBbox().getYMax(), 0.1);
        Assert.assertEquals(397.0, listAnn.get(2).getBbox().getWidth(), 0.1);
        Assert.assertEquals(537.0, listAnn.get(2).getBbox().getHeight(), 0.1);
        Assert.assertEquals(1, listAnn.get(2).getCategory().getId());
        Assert.assertEquals(2, listAnn.get(2).getImage().getId());
    }

    @Test
    public void readCoralPixdevTest() throws IOException {
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        coralService.readCoralTxt(pathPixDev);
        Assert.assertEquals(1, listImages.size());
        Assert.assertEquals(2, listCat.size());
        Assert.assertEquals(3, listAnn.size());

        Assert.assertEquals(1, listImages.get(0).getId());
        Assert.assertEquals("2018_0714_112604_057", listImages.get(0).getFileName());

        Assert.assertEquals(1, listCat.get(0).getId());
        Assert.assertEquals("c_hard_coral_branching", listCat.get(0).getName());
        Assert.assertEquals(2, listCat.get(1).getId());
        Assert.assertEquals("c_soft_coral", listCat.get(1).getName());

        Assert.assertEquals(1, listAnn.get(0).getId());
        Assert.assertEquals(1757.0, listAnn.get(0).getPolygon().getPolygon().get(0).get(0), 0.1);
        Assert.assertEquals(1442.0, listAnn.get(0).getPolygon().getPolygon().get(0).get(6), 0.1);
        Assert.assertEquals(978.0, listAnn.get(0).getPolygon().getPolygon().get(0).get(27), 0.1);
        Assert.assertEquals(1, listAnn.get(0).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(0).getImage().getId());

        Assert.assertEquals(2, listAnn.get(1).getId());
        Assert.assertEquals(12, listAnn.get(1).getPolygon().getPolygon().get(0).size());
        Assert.assertEquals(2724.0, listAnn.get(1).getBbox().getXMin(), 0.1);
        Assert.assertEquals(1507.0, listAnn.get(1).getBbox().getYMax(), 0.1);
        Assert.assertEquals(101, listAnn.get(1).getBbox().getWidth(), 0.1);
        Assert.assertEquals(139.0, listAnn.get(1).getBbox().getHeight(), 0.1);
        Assert.assertEquals(2, listAnn.get(1).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(1).getImage().getId());

        Assert.assertEquals(3, listAnn.get(2).getId());
        Assert.assertEquals(16, listAnn.get(2).getPolygon().getPolygon().get(0).size());
        Assert.assertEquals(2, listAnn.get(2).getCategory().getId());
        Assert.assertEquals(1, listAnn.get(2).getImage().getId());
    }

    @Test
    public void readCoralPixsubTest() throws IOException {
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        coralService.readCoralTxt(pathPixSub);
        Assert.assertEquals(1, listImages.size());
        Assert.assertEquals(2, listCat.size());
        Assert.assertEquals(3, listAnn.size());
    }
}
