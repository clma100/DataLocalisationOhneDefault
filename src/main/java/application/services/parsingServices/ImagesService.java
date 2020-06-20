package application.services.parsingServices;

import application.model.Annotations;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.services.parentClass.PrepService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static application.model.subsets.Images.createImage;

/**
 * This class extracts the information of a passed image and saves it.
 */
public class ImagesService extends PrepService {

    public ImagesService(ArrayList <Images> imagesList, ArrayList <Categories> categoriesList, ArrayList <Annotations> annotationsList, ArrayList <ArrayList <String>> infoLog) {
        super(imagesList, categoriesList, annotationsList, infoLog);
    }

    public void readImage(String filePath) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(filePath));
            String imageName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.lastIndexOf("."));       //ohne .jpg
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            createImage(imageName, imagesList, width, height);
            this.infoLog.get(infoLog.size() - 1).add("eingelesen. ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
