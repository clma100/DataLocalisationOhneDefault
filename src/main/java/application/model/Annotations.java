package application.model;

import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.model.subsets.Polygon;
import lombok.Data;

import java.util.ArrayList;


/**
 * Represents an annotation which all its required data.
 */
@Data
public class Annotations {

    private long id;
    private Polygon polygon;
    private Bbox bbox;
    private Images image;
    private Categories category;
    private int absoluteValue;

    public Annotations() {
        this.id = -1;
        this.polygon = new Polygon();
        this.bbox = new Bbox();
        this.image = new Images();
        this.category = new Categories();
        this.absoluteValue = -1;
    }

    public Annotations(Images image, Categories category, Bbox bbox, int id, Polygon... polygon) {
        this.id = id;
        this.image = image;
        this.category = category;
        this.bbox = bbox;
        if (polygon.length > 0) {
            this.polygon = polygon[0];
        } else {
            this.polygon = new Polygon();
        }
    }

    /**
     * Checks according to polygon if present values are absolut or relativ and stores the information
     */
    public void setAbsoluteValuePolygon() {
        for (ArrayList <Double> arrayList : this.polygon.getPolygon()) {
            for (Object d : arrayList) {
                if (isRelativ((Double) d) == 0) {
                    this.setAbsoluteValue(1);
                    return;
                }
            }
        }
        this.setAbsoluteValue(0);
    }

    private int isRelativ(double value) {
        if (value < 1 && value > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * checks according to Bounding Box if present values are absolut or relativ and stores the information
     */
    public void setAbsoluteValueBbox() {
        if (isRelativ(this.bbox.getWidth()) == 0 && isRelativ(this.bbox.getHeight()) == 0 && isRelativ(this.bbox.getXMin()) == 0 && isRelativ(this.bbox.getYMax()) == 0) {
            this.setAbsoluteValue(1);
            return;
        }
        this.setAbsoluteValue(0);
    }
}