package application.model.outputFormats.coco;

import application.model.subsets.Images;
import lombok.Data;

/**
 * Represents the Images Field in Coco. The names of each attribute will be printed via JSON Objectmapper
 * in Coco.class. So dont change names if the format doesnt change.
 */
@Data
public class ImagesCoco {
    private long id;
    private int width;
    private int height;
    private String file_name;
    private int license;
    private String flickr_url;
    private String coco_url;
    private String date_captured;

    public ImagesCoco(Images images) {
        this.id = images.getId();
        this.width = images.getWidth();
        this.height = images.getHeight();
        this.file_name = images.getFileName() + ".jpg";
        this.license = -1;
        this.flickr_url = "unknown";
        this.coco_url = "unknown";
        this.date_captured = "unknown";
    }
}
