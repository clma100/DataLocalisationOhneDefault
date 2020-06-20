package application.model;

import application.model.subsets.Bbox;
import application.model.subsets.Images;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
public class BboxTest {

    @Test
    public void setAbsolutTest() {
        Bbox bboxRel = new Bbox(0.3, 0.4, 0.5, 0.6);
        Images image = new Images(3, 4, "image1", 1);
        bboxRel.setAbsoluteValue(image);
        Assert.assertEquals(0.9, bboxRel.getXMin(), 0.1);
        Assert.assertEquals(1.6, bboxRel.getYMax(), 0.1);
        Assert.assertEquals(1.5, bboxRel.getWidth(), 0.1);
        Assert.assertEquals(2.4, bboxRel.getHeight(), 0.1);
    }

    @Test
    public void setRelativTest() {
        Bbox bboxAbs = new Bbox(4, 5, 8, 4);
        Images image = new Images(10, 6, "image1", 2);
        bboxAbs.setRelativValues(image);
        Assert.assertEquals(0.4, bboxAbs.getXMin(), 0.1);
        Assert.assertEquals(0.8, bboxAbs.getYMax(), 0.1);
        Assert.assertEquals(0.8, bboxAbs.getWidth(), 0.1);
        Assert.assertEquals(0.6, bboxAbs.getHeight(), 0.1);
    }
}
