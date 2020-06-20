package application.model.outputFormats.coral;


import application.model.Annotations;
import application.model.outputFormats.ProcessValuesToString;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents the ImageCLEFcoral Format and consists of an ArrayList of Objects, which represent the annotations in
 * that format.
 */
@Data
public class Coral {
    private ArrayList <CoralAnnotation> coralAnnotationArrayList = new ArrayList <>();

    public Coral(ArrayList <Annotations> annotationsList, boolean anlo, boolean dev) {
        this.createCoralineArrayList(annotationsList, anlo, dev);
    }

    /**
     * Transforms the saved data into the structure of ImageCLEFcoral depending on which format is required.
     *
     * @param annotationsList, common list of annotations
     * @param anlo,            true when Annotation and Localisation- Format is required, else false (PixelWise Parsing Task)
     * @param dev,             true when development format is required and false if submission
     */
    private void createCoralineArrayList(ArrayList <Annotations> annotationsList, boolean anlo, boolean dev) {
        for (Annotations annotations : annotationsList) {
            ArrayList <ArrayList <Double>> bbox = new ArrayList <>();
            if (anlo) {
                ArrayList <Double> inner = new ArrayList <>();
                if (dev) {
                    inner.add(annotations.getBbox().getXMin());//xmin
                    inner.add(annotations.getBbox().getYMax() - annotations.getBbox().getHeight());//ymin
                    inner.add(annotations.getBbox().getXMin() + annotations.getBbox().getWidth());//xmax
                    inner.add(annotations.getBbox().getYMax());//ymax
                } else {
                    inner.add(annotations.getBbox().getWidth());//width
                    inner.add(annotations.getBbox().getHeight());//height
                    inner.add(annotations.getBbox().getXMin());//xmin
                    inner.add(annotations.getBbox().getYMax() - annotations.getBbox().getHeight());//ymin
                }
                bbox.add(inner);
                this.coralAnnotationArrayList.add(new CoralAnnotation(annotations.getImage().getFileName(), annotations.getCategory().getName(), bbox));
            } else {
                if (annotations.getPolygon().getPolygon().get(0).size() > 1) {
                    this.coralAnnotationArrayList.add(new CoralAnnotation(annotations.getImage().getFileName(), annotations.getCategory().getName(), annotations.getPolygon().getPolygon()));
                }
            }
        }
        if (dev) {
            setCounts();
        }
        this.coralAnnotationArrayList.sort(Comparator.comparing(CoralAnnotation::getImageName).thenComparing(CoralAnnotation::getSubstrate));
    }

    /**
     * For ImageCLEFcoral development format there are count the qnnotations per image and printed in text file.
     * So that is, what is done here.
     */
    private void setCounts() {
        this.coralAnnotationArrayList.sort(Comparator.comparing(CoralAnnotation::getImageName).thenComparing(CoralAnnotation::getSubstrate));
        int count = 0;
        for (int i = 0; i < this.coralAnnotationArrayList.size() - 1; i++) {
            if (this.coralAnnotationArrayList.get(i).getImageName().equals(this.coralAnnotationArrayList.get(i + 1).getImageName())) {
                this.coralAnnotationArrayList.get(i + 1).setCount(++count);
            } else {
                count = 0;
                this.coralAnnotationArrayList.get(i + 1).setCount(count);
            }
        }
    }

    /**
     * Prints the created object according to the required type of format. Current format illustrated in
     *
     * @param dev,  true if development format, for submission false
     * @param anlo, true if Annotation and Localisation Task, false if PixelWise parsing
     * @return String in ImageCLEFcoral format
     * @see application.services.parsingServices.CoralService
     */
    public String printTxt(boolean dev, boolean anlo) {
        String coralTxt;
        if (dev) {
            coralTxt = printDev();
        } else {
            coralTxt = printSubm(anlo);
        }
        return coralTxt;
    }

    private String printDev() {
        ProcessValuesToString processValuesToString = new ProcessValuesToString();
        StringBuilder coralTxt = new StringBuilder();
        for (int j = 0; j < this.coralAnnotationArrayList.size(); j++) {
            coralTxt.append(this.coralAnnotationArrayList.get(j).getImageName()).append(" ").append(this.coralAnnotationArrayList.get(j).getCount()).append(" ").append(this.coralAnnotationArrayList.get(j).getSubstrate()).append(" ").append(this.coralAnnotationArrayList.get(j).getConfidence()).append(" ");
            for (int i = 0; i < this.coralAnnotationArrayList.get(j).getPolygonOrBbox().get(0).size(); i++) {
                if (i < this.coralAnnotationArrayList.get(j).getPolygonOrBbox().get(0).size() - 1) {
                    coralTxt.append(processValuesToString.getValueString(this.coralAnnotationArrayList.get(j).getPolygonOrBbox().get(0).get(i))).append(" ");
                } else {
                    coralTxt.append(processValuesToString.getValueString(this.coralAnnotationArrayList.get(j).getPolygonOrBbox().get(0).get(i)));
                }
            }
            if (j < this.coralAnnotationArrayList.size() - 1) {
                coralTxt.append("\n");
            }
        }
        return coralTxt.toString();
    }

    private String printSubm(boolean anlo) {
        StringBuilder coralTxt = new StringBuilder(this.coralAnnotationArrayList.get(0).getImageName() + ";" + this.coralAnnotationArrayList.get(0).getSubstrate() + " " + buildAnnotation(anlo, 0));

        for (int i = 1; i < this.coralAnnotationArrayList.size(); i++) {
            if (this.coralAnnotationArrayList.get(i - 1).getImageName().equals(this.coralAnnotationArrayList.get(i).getImageName()) && this.coralAnnotationArrayList.get(i - 1).getSubstrate().equals(this.coralAnnotationArrayList.get(i).getSubstrate())) {
                coralTxt.append(",").append(buildAnnotation(anlo, i));
            } else {
                coralTxt.append("\n");
                coralTxt.append(this.coralAnnotationArrayList.get(i).getImageName()).append(";").append(this.coralAnnotationArrayList.get(i).getSubstrate()).append(" ").append(buildAnnotation(anlo, i));
            }
        }
        return coralTxt.toString();
    }

    private String buildAnnotation(boolean anlo, int i) {
        ProcessValuesToString processValuesToString = new ProcessValuesToString();
        StringBuilder anno = new StringBuilder("-1:");
        if (anlo) {
            anno.append(processValuesToString.getValueString(this.coralAnnotationArrayList.get(i).getPolygonOrBbox().get(0).get(0))).append("x").append(processValuesToString.getValueString(this.coralAnnotationArrayList.get(i).getPolygonOrBbox().get(0).get(1))).append("+").append(processValuesToString.getValueString(this.coralAnnotationArrayList.get(i).getPolygonOrBbox().get(0).get(2))).append("+").append(processValuesToString.getValueString(this.coralAnnotationArrayList.get(i).getPolygonOrBbox().get(0).get(3)));
        } else {
            for (int j = 0; j < this.coralAnnotationArrayList.get(i).getPolygonOrBbox().get(0).size(); j++) {
                anno.append(processValuesToString.getValueString(this.coralAnnotationArrayList.get(i).getPolygonOrBbox().get(0).get(j)));
                if (j < this.coralAnnotationArrayList.get(i).getPolygonOrBbox().get(0).size() - 1) {
                    anno.append("+");
                }
            }
        }
        return anno.toString();
    }
}
