package application.model.subsets;

import lombok.Data;

/**
 * Represents a Bounding Box with its x-min, ymax, width and height. Default Value is zero.
 */
@Data
public class Bbox {

    private double xMin;
    private double yMax;
    private double width;
    private double height;

    public Bbox() {
        this.xMin = 0;
        this.yMax = 0;
        this.width = 0;
        this.height = 0;
    }

    public Bbox(double xMin, double yMax, double width, double height) {
        this.xMin = xMin;
        this.yMax = yMax;
        this.width = width;
        this.height = height;
    }

    public void setAbsoluteValue(Images image) {
        this.setXMin(this.getXMin() * image.getWidth());
        this.setYMax(this.getYMax() * image.getHeight());
        this.setWidth(this.getWidth() * image.getWidth());
        this.setHeight(this.getHeight() * image.getHeight());
    }

    public void setRelativValues(Images image) {
        this.setXMin(this.getXMin() / image.getWidth());
        this.setYMax(this.getYMax() / image.getHeight());
        this.setWidth(this.getWidth() / image.getWidth());
        this.setHeight(this.getHeight() / image.getHeight());
    }

    public void setDefaultValue() {
        this.setXMin(0);
        this.setYMax(0);
        this.setWidth(0);
        this.setHeight(0);
    }
}
