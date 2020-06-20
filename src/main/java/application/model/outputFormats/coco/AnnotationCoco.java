package application.model.outputFormats.coco;


import application.model.Annotations;
import lombok.Data;

import java.util.ArrayList;

/**
 * Represents the Annotation Field in Coco. The names of each attribute will be printed via JSON Objectmapper
 * in Coco.class. So dont change names if the format doesnt change.
 */
@Data
public class AnnotationCoco {
    private long id;
    private double area;
    private int iscrowd;
    private String caption;
    private ArrayList <ArrayList <Double>> segmentation;
    private long image_id;
    private long category_id;
    private Double[] bbox;

    public AnnotationCoco(Annotations annotation) {
        this.id = annotation.getId();
        this.area = -1;
        this.iscrowd = -1;
        this.caption = "unknown";
        this.segmentation = annotation.getPolygon().getPolygon();
        this.image_id = annotation.getImage().getId();
        this.category_id = annotation.getCategory().getId();
        this.bbox = new Double[]{annotation.getBbox().getXMin(), annotation.getBbox().getYMax(), annotation.getBbox().getWidth(), annotation.getBbox().getHeight()};
    }
}
