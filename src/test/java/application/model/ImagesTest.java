package application.model;

import application.model.subsets.Images;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
public class ImagesTest {

    ArrayList <Images> listImages;
    Images image1;
    Images image2;
    Images image3;

    @Before
    public void setUp() {
        image1 = new Images();
        image1.setFileName("image1");
        image1.setId(1);
        image2 = new Images();
        image2.setFileName("image2");
        image2.setId(2);
        image3 = new Images();
        image3.setFileName("image3");
        image3.setId(3);
        listImages = new ArrayList <>();
        listImages.add(image1);
        listImages.add(image2);
        listImages.add(image3);
    }

    @After
    public void clear() {
        listImages.clear();
    }

    @Test
    public void containsImageAtIndexContains() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Images images = new Images();
        Method method = Images.class.getDeclaredMethod("containsImageAtIndex", String.class, ArrayList.class, int[].class);
        method.setAccessible(true);
        int[] ints = {};
        Assert.assertEquals(1, (int) method.invoke(images, "image2", listImages, ints));
    }

    @Test
    public void containsImageAtIndexSetWidthHeight() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Images images = new Images();
        Method method = Images.class.getDeclaredMethod("containsImageAtIndex", String.class, ArrayList.class, int[].class);
        method.setAccessible(true);
        int[] ints = {2, 3};
        int index = (int) method.invoke(images, "image2", listImages, ints);
        Assert.assertEquals(2, listImages.get(index).getWidth());
        Assert.assertEquals(3, listImages.get(index).getHeight());
    }

    @Test
    public void containsImageAtIndexDoesntContain() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Images images = new Images();
        Method method = Images.class.getDeclaredMethod("containsImageAtIndex", String.class, ArrayList.class, int[].class);
        method.setAccessible(true);
        int[] ints = {};
        Assert.assertEquals(-1, (int) method.invoke(images, "image9", listImages, ints));
    }

    @Test
    public void createNewImage() {
        Assert.assertEquals(3, listImages.size());
        Images.createImage("image10", listImages, 10, 9);
        Assert.assertEquals(4, listImages.size());
    }
}
