package application.model.outputFormats.openImage;

import application.model.Annotations;
import application.model.outputFormats.ProcessValuesToString;
import lombok.Data;

import java.util.ArrayList;

/**
 * Represents the Open Image Version 5 Format and consists of a ArrayList including the required data per line.
 */
@Data
public class OpenImage5 {
    private ArrayList <OpenImageLine> openImageList = new ArrayList <>();

    public OpenImage5(ArrayList <Annotations> annotationsList) {
        for (Annotations annotations : annotationsList) {
            this.openImageList.add(new OpenImageLine(annotations));
        }
    }

    /**
     * Prints the created object in OpenImageV5 format.Current format illustrated in
     *
     * @return String in OpenImage format
     * @see application.services.parsingServices.OpenImage5Service
     */
    public String printCsv() {
        ProcessValuesToString processValuesToString = new ProcessValuesToString();
        StringBuilder csvString = new StringBuilder("ImageID,Source,LabelName,Confidence,XMin,XMax,YMin,YMax,IsOccluded,IsTruncated,IsGroupOf,IsDepiction,IsInside\n");
        for (int i = 0; i < this.openImageList.size(); i++) {
            csvString.append(this.openImageList.get(i).getImageId()).append(",").append(this.openImageList.get(i).getSource()).append(",").append(this.openImageList.get(i).getLabelName()).append(",").append(this.openImageList.get(i).getConfidence()).append(",").append(processValuesToString.getValueString(this.openImageList.get(i).getXMin())).append(",").append(processValuesToString.getValueString(this.openImageList.get(i).getXMax())).append(",").append(processValuesToString.getValueString(this.openImageList.get(i).getYMin())).append(",").append(processValuesToString.getValueString(this.openImageList.get(i).getYMax())).append(",").append(this.openImageList.get(i).getIsOccluded()).append(",").append(this.openImageList.get(i).getIsTruncated()).append(",").append(this.openImageList.get(i).getIsGroupOf()).append(",").append(this.openImageList.get(i).getIsDepiction()).append(",").append(this.openImageList.get(i).getIsInside());
            if (i < this.openImageList.size() - 1) {
                csvString.append("\n");
            }
        }
        return csvString.toString();
    }
}
