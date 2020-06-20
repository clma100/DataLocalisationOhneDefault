package application.model;

import application.model.subsets.Images;
import application.model.subsets.Polygon;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
public class PolygonTest {

    @Test
    public void setRelativ() {
        ArrayList <ArrayList <Double>> list = new ArrayList <>();
        ArrayList <Double> innerList = new ArrayList <>();
        innerList.add(1.0);
        innerList.add(1.0);
        innerList.add(2.0);
        innerList.add(2.0);
        innerList.add(3.0);
        innerList.add(3.0);
        list.add(innerList);
        Polygon polygonAbs = new Polygon(list);
        Images image = new Images(3, 4, "image1", 1);

        polygonAbs.setRelativValues(image);
        Assert.assertEquals(0.3, polygonAbs.getPolygon().get(0).get(0), 0.1);
        Assert.assertEquals(0.25, polygonAbs.getPolygon().get(0).get(1), 0.1);
        Assert.assertEquals(0.6, polygonAbs.getPolygon().get(0).get(2), 0.1);
        Assert.assertEquals(0.5, polygonAbs.getPolygon().get(0).get(3), 0.1);
        Assert.assertEquals(1.0, polygonAbs.getPolygon().get(0).get(4), 0.1);
        Assert.assertEquals(0.75, polygonAbs.getPolygon().get(0).get(5), 0.1);
    }

    @Test
    public void setAbsolut() {
        ArrayList <ArrayList <Double>> list = new ArrayList <>();
        ArrayList <Double> innerList = new ArrayList <>();
        innerList.add(0.8);
        innerList.add(0.6);
        innerList.add(0.1);
        innerList.add(0.4);
        innerList.add(0.3);
        innerList.add(0.3);
        list.add(innerList);
        Polygon polygonRel = new Polygon(list);
        Images image = new Images(3, 4, "image1", 2);

        polygonRel.setAbsoluteValue(image);
        Assert.assertEquals(2.4, polygonRel.getPolygon().get(0).get(0), 0.1);
        Assert.assertEquals(2.4, polygonRel.getPolygon().get(0).get(1), 0.1);
        Assert.assertEquals(0.3, polygonRel.getPolygon().get(0).get(2), 0.1);
        Assert.assertEquals(1.6, polygonRel.getPolygon().get(0).get(3), 0.1);
        Assert.assertEquals(0.9, polygonRel.getPolygon().get(0).get(4), 0.1);
        Assert.assertEquals(1.2, polygonRel.getPolygon().get(0).get(5), 0.1);
    }
}
