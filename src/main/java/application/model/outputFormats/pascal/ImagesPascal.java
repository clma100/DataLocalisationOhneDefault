package application.model.outputFormats.pascal;

import application.model.Annotations;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ImagesPascal {

    private String fileName;
    private int width;
    private int height;
    private int depth;
    private ArrayList <Annotations> annotationsPascal = new ArrayList <>();

    public ImagesPascal(String fileName, int width, int height, ArrayList <Annotations> annotations) {
        this.fileName = fileName;
        this.width = width;
        this.height = height;
        this.depth = -1;
        this.annotationsPascal = annotations;
    }

    public static int containsPascalImage(String imageName, ArrayList <ImagesPascal> imagesList) {
        for (int i = 0; i < imagesList.size(); i++) {
            if (imagesList.get(i).getFileName() != null && imagesList.get(i).getFileName().equals(imageName)) {
                return i;
            }
        }
        return -1;
    }
}
