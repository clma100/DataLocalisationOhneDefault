package application.model.subsets;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Images {

    private long id;
    private int width;
    private int height;
    private String fileName;


    public Images() {
        this.id = -1;
        this.width = -1;
        this.height = -1;
        this.fileName = "unknown";
    }

    public Images(int width, int height, String fileName, long id) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.fileName = fileName;
    }

    public Images(long id) {
        this.id = id;
        this.width = -1;
        this.height = -1;
        this.fileName = "unknown";
    }

    public static Images createImage(String imageName, ArrayList <Images> imagesList, int... widthheight) { //1.args:width 2.args:height
        Images image;
        int containsImage = containsImageAtIndex(imageName, imagesList, widthheight);
        int width = -1;
        int height = -1;
        if (widthheight.length == 2) {
            width = widthheight[0];
            height = widthheight[1];
        }
        if (containsImage == -1) {
            image = new Images(width, height, imageName, imagesList.size() + 1);
            imagesList.add(image);
        } else {
            image = imagesList.get(containsImage);
            image.setHeight(height);
            image.setWidth(width);
        }
        return image;
    }

    private static int containsImageAtIndex(String imageName, ArrayList <Images> imagesList, int... widthheight) {
        for (int i = 0; i < imagesList.size(); i++) {
            if (imagesList.get(i).getFileName() != null && imagesList.get(i).getFileName().equals(imageName)) {
                if (widthheight.length == 2) {
                    imagesList.get(i).setWidth(widthheight[0]);
                    imagesList.get(i).setHeight(widthheight[1]);
                }
                return i;
            }
        }
        return -1;
    }
}
