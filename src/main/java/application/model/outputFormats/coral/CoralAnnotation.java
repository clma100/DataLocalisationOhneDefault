package application.model.outputFormats.coral;


import lombok.Data;

import java.util.ArrayList;

/**
 * Represents a annotation in the ImageCLEFcoral with its attributes. As the presentation (Bounding Box or Polygon)
 * differs between the different formats in ImageCLEFcoral, the polygonOrBbox Variable presents either a Bounding Box
 * with four values or a polygon with several.
 */
@Data
public class CoralAnnotation {
    private long count;
    private String imageName;
    private String substrate;
    private int confidence;
    private ArrayList <ArrayList <Double>> polygonOrBbox;


    public CoralAnnotation(String imageName, String substrate, ArrayList <ArrayList <Double>> polygonOrBbox) {
        this.count = 0;
        this.imageName = imageName;
        this.substrate = substrate;
        this.confidence = -1;
        this.polygonOrBbox = polygonOrBbox;
    }
}
