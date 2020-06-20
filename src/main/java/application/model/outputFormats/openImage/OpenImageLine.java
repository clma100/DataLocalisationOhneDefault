package application.model.outputFormats.openImage;

import application.model.Annotations;
import lombok.Data;

/**
 * Represents each line in the OpenImageV5 format and includes all required attributes.
 */
@Data
public class OpenImageLine {
    private int confidence, isOccluded, isTruncated, isGroupOf, isDepiction, isInside;
    private String imageId, source, labelName;
    private double xMin, xMax, yMin, yMax;

    public OpenImageLine(Annotations annotations) {
        this.imageId = annotations.getImage().getFileName();
        this.confidence = -1;
        this.isOccluded = -1;
        this.isTruncated = -1;
        this.isGroupOf = -1;
        this.isDepiction = -1;
        this.isInside = -1;
        this.source = "unknown";
        this.labelName = annotations.getCategory().getName();
        this.xMin = annotations.getBbox().getXMin();
        this.yMax = annotations.getBbox().getYMax();
        this.xMax = this.xMin + annotations.getBbox().getWidth();
        this.yMin = this.yMax - annotations.getBbox().getHeight();
    }
}
