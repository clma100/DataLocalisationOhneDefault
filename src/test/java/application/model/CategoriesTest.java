package application.model;

import application.model.subsets.Categories;
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
public class CategoriesTest {

    ArrayList <Categories> listCategories;
    Categories categorie1;
    Categories categorie2;
    Categories categorie3;

    @Before
    public void setUp() {
        categorie1 = new Categories();
        categorie1.setName("categorie1");
        categorie1.setId(1);
        categorie2 = new Categories();
        categorie2.setName("categorie2");
        categorie2.setId(2);
        categorie3 = new Categories();
        categorie3.setName("categorie3");
        categorie3.setId(3);
        listCategories = new ArrayList <>();
        listCategories.add(categorie1);
        listCategories.add(categorie2);
        listCategories.add(categorie3);
    }

    @After
    public void delete() {
        listCategories.clear();
    }

    @Test
    public void containsCategorieAtIndexContains() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Categories categories = new Categories();
        Method method = Categories.class.getDeclaredMethod("containsCategoryAtIndex", String.class, ArrayList.class);
        method.setAccessible(true);
        Assert.assertEquals(1, (int) method.invoke(categories, "categorie2", listCategories));
    }

    @Test
    public void containsCategorieAtIndexDoesntContain() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Categories categories = new Categories();
        Method method = Categories.class.getDeclaredMethod("containsCategoryAtIndex", String.class, ArrayList.class);
        method.setAccessible(true);
        Assert.assertEquals(-1, (int) method.invoke(categories, "categorie10", listCategories));
    }

    @Test
    public void createNewCategory() {
        Assert.assertEquals(3, listCategories.size());
        Categories.createCategory("category10", listCategories);
        Assert.assertEquals(4, listCategories.size());
    }
}
