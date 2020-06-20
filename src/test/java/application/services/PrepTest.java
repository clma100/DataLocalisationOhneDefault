package application.services;

import application.model.Annotations;
import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.model.subsets.Polygon;
import application.services.parentClass.PrepService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringBootConfiguration;

import java.util.ArrayList;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PrepService.class)
@SpringBootConfiguration
public class PrepTest {
    PrepService prepService;
    Annotations annotation1;
    Annotations annotation2;
    Annotations annotation3;
    Images image1;
    Images image2;
    Images image3;

    @Before
    public void setUp() {
        prepService = new PrepService();

        Categories categorie1 = new Categories();
        categorie1.setId(1);
        Categories categorie2 = new Categories();
        categorie2.setId(2);
        Categories categorie3 = new Categories();
        categorie2.setId(3);
        prepService.getCategoriesList().add(categorie1);
        prepService.getCategoriesList().add(categorie2);
        prepService.getCategoriesList().add(categorie3);
        image1 = new Images();
        image1.setId(1);
        image2 = new Images();
        image2.setId(2);
        image3 = new Images();
        image3.setId(3);
        Images image4 = new Images();
        image4.setId(4);
        Images image5 = new Images();
        image5.setId(5);
        Images image6 = new Images();
        image6.setId(6);
        Images image7 = new Images();
        image7.setId(7);
        Images image8 = new Images();
        image8.setId(8);
        prepService.getImagesList().add(image1);
        prepService.getImagesList().add(image2);
        prepService.getImagesList().add(image3);
        prepService.getImagesList().add(image4);
        prepService.getImagesList().add(image5);
        prepService.getImagesList().add(image6);
        prepService.getImagesList().add(image7);
        prepService.getImagesList().add(image8);
        annotation1 = new Annotations();
        annotation1.setId(1);
        annotation1.setImage(image1);
        annotation1.setCategory(categorie1);
        annotation1.setAbsoluteValue(1);
        annotation2 = new Annotations();
        annotation2.setId(2);
        annotation2.setImage(image1);
        annotation2.setCategory(categorie1);
        annotation2.setAbsoluteValue(0);
        annotation3 = new Annotations();
        annotation3.setId(3);
        annotation3.setImage(image2);
        annotation3.setCategory(categorie2);
        annotation3.setAbsoluteValue(1);
        prepService.getAnnotationsList().add(annotation1);
        prepService.getAnnotationsList().add(annotation2);
        prepService.getAnnotationsList().add(annotation3);
        prepService.getInfoLog().add(new ArrayList <>());
    }

    @After
    public void delete() {
        prepService.clear();
    }

    @Test
    public void splitDataNoneTest() {
        int[] abs = {2, 1, 0};
        ArrayList <Annotations> annotations = prepService.splitData("none", abs, 0, 0);
        Assert.assertEquals(annotations.size(), 3);
        Assert.assertEquals(1, annotations.get(0).getId());
        Assert.assertEquals(2, annotations.get(1).getId());
        Assert.assertEquals(3, annotations.get(2).getId());
    }

    @Test
    public void splitDataAnnoTest() {
        int[] abs = {2, 1, 0};
        ArrayList <Annotations> annotations = prepService.splitData("annotation", abs, 0, 0);
        Assert.assertEquals(annotations.size(), 1);
        Assert.assertEquals(1, annotations.get(0).getId());
    }

    @Test
    public void splitDataClassTest() {
        int[] abs = {2, 1, 0};
        ArrayList <Annotations> annotations = prepService.splitData("class", abs, 0, 0);
        Assert.assertEquals(annotations.size(), 2);
        Assert.assertEquals(1, annotations.get(0).getCategory().getId());
        Assert.assertEquals(1, annotations.get(1).getCategory().getId());
    }

    @Test
    public void splitDataImageTest() {
        int[] abs = {3, 0, 0};
        ArrayList <Annotations> annotations = prepService.splitData("image", abs, 0, 0);
        Assert.assertEquals(annotations.size(), 2);
        Assert.assertEquals(1, annotations.get(0).getImage().getId());
        Assert.assertEquals(1, annotations.get(1).getImage().getId());
    }

    @Test
    public void splitDataSplitTest() {
        int[] abs = {1, 1, 0};
        ArrayList <Annotations> annotations1 = prepService.splitData("split", abs, 0, 0);
        Assert.assertEquals(2, annotations1.size());
        Assert.assertEquals(1, annotations1.get(0).getImage().getId());
        Assert.assertEquals(1, annotations1.get(1).getImage().getId());
        ArrayList <Annotations> annotations2 = prepService.splitData("split", abs, 1, 2);
        Assert.assertEquals(1, annotations2.size());
        Assert.assertEquals(2, annotations2.get(0).getImage().getId());
        ArrayList <Annotations> annotations3 = prepService.splitData("split", abs, 2, 3);
        Assert.assertEquals(0, annotations3.size());
    }

    @Test
    public void setNumberAbsThreeTest() {
        double[] splitNumbers = {40, 30, 30};
        int[] absNumbers = prepService.setAbsNumberOfSplit(splitNumbers);
        Assert.assertEquals(3, absNumbers[0]);
        Assert.assertEquals(2, absNumbers[1]);
        Assert.assertEquals(3, absNumbers[2]);
    }

    @Test
    public void setNumberAbsThreeOver100Test() {
        double[] splitNumbers = {80, 40, 90};
        int[] absNumbers = prepService.setAbsNumberOfSplit(splitNumbers);
        Assert.assertEquals(8, absNumbers[0]);
        Assert.assertEquals(0, absNumbers[1]);
        Assert.assertEquals(0, absNumbers[2]);
    }

    @Test
    public void setNumberAbsTwoTest() {
        double[] splitNumbers = {60, 40};
        int[] absNumbers = prepService.setAbsNumberOfSplit(splitNumbers);
        Assert.assertEquals(5, absNumbers[0]);
        Assert.assertEquals(3, absNumbers[1]);
        Assert.assertEquals(0, absNumbers[2]);
    }

    @Test
    public void setNumberAbsTwoOver100Test() {
        double[] splitNumbers = {80, 40};
        int[] absNumbers = prepService.setAbsNumberOfSplit(splitNumbers);
        Assert.assertEquals(8, absNumbers[0]);
        Assert.assertEquals(0, absNumbers[1]);
        Assert.assertEquals(0, absNumbers[2]);
    }

    @Test
    public void createBboxFromPolygonTest() {
        ArrayList <ArrayList <Double>> list = new ArrayList <>();
        ArrayList <Double> innerList = new ArrayList <>();
        innerList.add(1.0);
        innerList.add(1.0);
        innerList.add(2.0);
        innerList.add(2.0);
        innerList.add(3.0);
        innerList.add(3.0);
        innerList.add(3.0);
        innerList.add(1.0);
        innerList.add(4.0);
        innerList.add(2.0);
        list.add(innerList);
        Polygon polygon = new Polygon(list);
        Bbox bbox = prepService.createBboxFromPolygon(polygon);
        Assert.assertEquals(1, bbox.getXMin(), 0.1);
        Assert.assertEquals(3, bbox.getYMax(), 0.1);
        Assert.assertEquals(3, bbox.getWidth(), 0.1);
        Assert.assertEquals(2, bbox.getHeight(), 0.1);
    }

    @Test
    public void createBboxFromPolygonSquareTest() {
        ArrayList <ArrayList <Double>> list = new ArrayList <>();
        ArrayList <Double> innerList = new ArrayList <>();
        innerList.add(1.0);
        innerList.add(1.0);
        innerList.add(1.0);
        innerList.add(3.0);
        innerList.add(4.0);
        innerList.add(3.0);
        innerList.add(4.0);
        innerList.add(1.0);
        list.add(innerList);
        Polygon polygon = new Polygon(list);
        Bbox bbox = prepService.createBboxFromPolygon(polygon);
        Assert.assertEquals(1, bbox.getXMin(), 0.1);
        Assert.assertEquals(3, bbox.getYMax(), 0.1);
        Assert.assertEquals(3, bbox.getWidth(), 0.1);
        Assert.assertEquals(2, bbox.getHeight(), 0.1);
    }

    @Test
    public void setKindOfValueRelativWithoutImageTest() {
        prepService.setKindOfValue("relativ", prepService.getAnnotationsList());
        Assert.assertEquals(-1, annotation1.getAbsoluteValue());
        Assert.assertEquals(0, annotation2.getAbsoluteValue());
        Assert.assertEquals(-1, annotation3.getAbsoluteValue());
    }

    @Test
    public void setKindOfValueAbsolutWithoutImageTest() {
        prepService.setKindOfValue("absolut", prepService.getAnnotationsList());
        Assert.assertEquals(1, annotation1.getAbsoluteValue());
        Assert.assertEquals(-1, annotation2.getAbsoluteValue());
        Assert.assertEquals(1, annotation3.getAbsoluteValue());
    }

    @Test
    public void setKindOfValueRelativWithImageTest() {
        Polygon polygon = mock(Polygon.class);
        Annotations annotations = mock(Annotations.class);
        when(annotations.getPolygon()).thenReturn(polygon);
        when(annotations.getAbsoluteValue()).thenReturn(0);
        image1.setWidth(100);
        image1.setHeight(200);
        image2.setHeight(23);
        image2.setWidth(89);
        image3.setWidth(289000);
        image3.setHeight(2466);
        prepService.getAnnotationsList().add(annotations);
        prepService.setKindOfValue("relativ", prepService.getAnnotationsList());
        Assert.assertEquals(0, annotation1.getAbsoluteValue());
        Assert.assertEquals(0, annotation2.getAbsoluteValue());
        Assert.assertEquals(0, annotation3.getAbsoluteValue());
    }

    @Test
    public void setKindOfValueAbsolutWithImageTest() {
        image1.setWidth(100);
        image1.setHeight(200);
        image2.setHeight(23);
        image2.setWidth(89);
        image3.setWidth(289000);
        image3.setHeight(2466);
        prepService.setKindOfValue("absolut", prepService.getAnnotationsList());
        Assert.assertEquals(1, annotation1.getAbsoluteValue());
        Assert.assertEquals(1, annotation2.getAbsoluteValue());
        Assert.assertEquals(1, annotation3.getAbsoluteValue());
    }
}
