package application.model.subsets;

import lombok.Data;

import java.util.ArrayList;

/**
 * Represents a Polygon with its values. The values are stored as follows: x1,y1,x2,y2 ... xn,yn.
 * Default Value is zero.
 */
@Data
public class Polygon {
    private ArrayList <ArrayList <Double>> polygon;

    public Polygon() {
        ArrayList <Double> inner = new ArrayList <>();
        ArrayList <ArrayList <Double>> polygon = new ArrayList <>();
        polygon.add(inner);
        this.polygon = polygon;
    }

    public Polygon(ArrayList <ArrayList <Double>> polygon) {
        this.polygon = polygon;
    }

    public void setRelativValues(Images image) {
        for (ArrayList <Double> arrayList : this.getPolygon()) {
            for (int i = 0; i < arrayList.size() - 1; i += 2) {
                double x = arrayList.get(i) / image.getWidth();
                double y = arrayList.get(i + 1) / image.getHeight();
                arrayList.set(i, x);
                arrayList.set(i + 1, y);
            }
        }
    }

    public void setAbsoluteValue(Images image) {
        for (ArrayList <Double> arrayList : this.getPolygon()) {
            for (int i = 0; i < arrayList.size() - 1; i += 2) {
                double x = arrayList.get(i) * image.getWidth();
                double y = arrayList.get(i + 1) * image.getHeight();
                arrayList.set(i, x);
                arrayList.set(i + 1, y);
            }
        }
    }

    public void setDefaultValue() {
        ArrayList <ArrayList <Double>> polygon = new ArrayList <>();
        ArrayList <Double> inner = new ArrayList <>();
        inner.add(0.0);
        polygon.add(inner);
        this.polygon = polygon;
    }
}
