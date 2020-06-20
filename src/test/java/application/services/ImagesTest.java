package application.services;

import application.model.Annotations;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.services.parsingServices.ImagesService;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
public class ImagesTest {
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();
    ArrayList <Images> listImages;
    ArrayList <Categories> listCat;
    ArrayList <Annotations> listAnn;
    ArrayList <ArrayList <String>> infoLog;
    ImagesService imagesService;
    BufferedImage bufferedImage;
    File file;

    @Before
    public void setUp() throws IOException {
        listImages = new ArrayList <>();
        listCat = new ArrayList <>();
        listAnn = new ArrayList <>();
        infoLog = new ArrayList <>();
        ArrayList <String> info = new ArrayList <>();
        infoLog.add(info);
        imagesService = new ImagesService(listImages, listCat, listAnn, infoLog);

        bufferedImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        file = temporaryFolder.newFile("any.jpg");
        ImageIO.write(bufferedImage, "jpg", file);
    }

    @After
    public void delete() {
        this.imagesService.clear();
    }

    @Test
    public void addImageTest() {
        imagesService.readImage(file.getAbsolutePath());

        Assert.assertEquals("any", listImages.get(0).getFileName());
        Assert.assertEquals(100, listImages.get(0).getWidth());
        Assert.assertEquals(200, listImages.get(0).getHeight());
    }

    @Test
    public void modifyImageTest() {
        Images image = new Images();
        image.setFileName("any");
        listImages.add(image);
        Assert.assertEquals(-1, image.getWidth());
        Assert.assertEquals(-1, image.getHeight());
        Assert.assertEquals(1, listImages.size());

        imagesService.readImage(file.getAbsolutePath());

        Assert.assertEquals(1, listImages.size());
        Assert.assertEquals("any", listImages.get(0).getFileName());
        Assert.assertEquals(100, listImages.get(0).getWidth());
        Assert.assertEquals(200, listImages.get(0).getHeight());
    }
}
