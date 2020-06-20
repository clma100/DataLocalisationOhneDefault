package application.model.output;

import application.model.Annotations;
import application.model.outputFormats.coco.Coco;
import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.model.subsets.Polygon;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    Polygon polygon;

    @Before
    public void setUp() {
        listImages = new ArrayList <>();
        listCat = new ArrayList <>();
        listAnn = new ArrayList <>();

        Categories categorie1 = new Categories();
        categorie1.setId(1);
        categorie1.setName("categorie1");
        listCat.add(categorie1);

        Images image1 = new Images();
        image1.setId(1);
        image1.setFileName("image1");
        image1.setHeight(400);
        image1.setWidth(300);
        listImages.add(image1);

        Bbox bbox = new Bbox(2, 5, 6, 7);
        ArrayList <ArrayList <Double>> list = new ArrayList <>();
        ArrayList <Double> listInner = new ArrayList <>();
        listInner.add(3.0);
        listInner.add(5.0);
        listInner.add(7.7);
        listInner.add(5.7);
        listInner.add(2.0);
        listInner.add(7.2);
        list.add(listInner);
        polygon = new Polygon(list);

        Annotations annotation1 = new Annotations();
        annotation1.setId(1);
        annotation1.setAbsoluteValue(1);
        annotation1.setPolygon(polygon);
        annotation1.setBbox(bbox);
        annotation1.setImage(image1);
        annotation1.setCategory(categorie1);
        listAnn.add(annotation1);
    }

    @After
    public void delete() {
        listImages.clear();
        listCat.clear();
        listAnn.clear();
    }

    @Test
    public void checkConstructorTest() {
        Coco coco = new Coco(listImages, listCat, listAnn);

        Assert.assertEquals(1, coco.getCategories().size());
        Assert.assertEquals(1, coco.getCategories().get(0).getId());
        Assert.assertEquals("categorie1", coco.getCategories().get(0).getName());
        Assert.assertEquals("unknown", coco.getCategories().get(0).getSupercategory());

        Assert.assertEquals(1, coco.getImages().size());
        Assert.assertEquals(1, coco.getImages().get(0).getId());
        Assert.assertEquals(300, coco.getImages().get(0).getWidth());
        Assert.assertEquals(400, coco.getImages().get(0).getHeight());
        Assert.assertEquals("image1.jpg", coco.getImages().get(0).getFile_name());
        Assert.assertEquals(-1, coco.getImages().get(0).getLicense());
        Assert.assertEquals("unknown", coco.getImages().get(0).getFlickr_url());
        Assert.assertEquals("unknown", coco.getImages().get(0).getCoco_url());
        Assert.assertEquals("unknown", coco.getImages().get(0).getDate_captured());

        Assert.assertEquals(1, coco.getAnnotations().size());
        Assert.assertEquals(1, coco.getAnnotations().get(0).getId());
        Assert.assertEquals(-1, coco.getAnnotations().get(0).getArea(), 0.1);
        Assert.assertEquals(-1, coco.getAnnotations().get(0).getIscrowd());
        Assert.assertEquals("unknown", coco.getAnnotations().get(0).getCaption());
        Assert.assertEquals(polygon.getPolygon(), coco.getAnnotations().get(0).getSegmentation());
        Assert.assertEquals(1, coco.getAnnotations().get(0).getImage_id());
        Assert.assertEquals(1, coco.getAnnotations().get(0).getCategory_id());
        Double[] bbox = coco.getAnnotations().get(0).getBbox();
        Assert.assertEquals(2, bbox[0], 0.1);
        Assert.assertEquals(5, bbox[1], 0.1);
        Assert.assertEquals(6, bbox[2], 0.1);
        Assert.assertEquals(7, bbox[3], 0.1);

        Assert.assertEquals(-1, coco.getInfo().getYear());
        Assert.assertEquals("unknown", coco.getInfo().getVersion());
        Assert.assertEquals("unknown", coco.getInfo().getDescription());
        Assert.assertEquals("unknown", coco.getInfo().getContributor());
        Assert.assertEquals("unknown", coco.getInfo().getUrl());
        Assert.assertEquals("unknown", coco.getInfo().getDate_created());

        Assert.assertEquals(0, coco.getLicenses().size());
    }

    @Test
    public void printCocoTest() throws JsonProcessingException {
        Bbox bbox = new Bbox(0.02, 0.008, 0.9, 0.02335347);
        listAnn.get(0).setBbox(bbox);
        Coco coco = new Coco(listImages, listCat, listAnn);
        String cocoString = "{\"info\":{" + "\"year\":-1" + ",\"version\":\"unknown\"" + ",\"description\":\"unknown\"" + ",\"contributor\":\"unknown\"" + ",\"url\":\"unknown\"" + ",\"date_created\":\"unknown\"}" + ",\"licenses\":[]" + ",\"categories\":[" + "{\"id\":1" + ",\"name\":\"categorie1\"" + ",\"supercategory\":\"unknown\"}]" + ",\"images\":[" + "{\"id\":1" + ",\"width\":300" + ",\"height\":400" + ",\"file_name\":\"image1.jpg\"" + ",\"license\":-1" + ",\"flickr_url\":\"unknown\"" + ",\"coco_url\":\"unknown\"" + ",\"date_captured\":\"unknown\"}]" + ",\"annotations\":[" + "{\"id\":1" + ",\"area\":-1.0" + ",\"iscrowd\":-1" + ",\"caption\":\"unknown\"" + ",\"segmentation\":[[3.0,5.0,7.7,5.7,2.0,7.2]]" + ",\"image_id\":1" + ",\"category_id\":1" + ",\"bbox\":[0.02,0.008,0.9,0.02335347]}]}";
        Assert.assertEquals(cocoString, coco.printJson());
    }
}
