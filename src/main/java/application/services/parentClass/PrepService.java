package application.services.parentClass;

import application.model.Annotations;
import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.model.subsets.Polygon;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class includes all the function all classes need to transform and parse the handed data. So it
 * includes all functions to prepare and transform the data. Also it includes the lists where the passed data is
 * stored during the process.
 */
@Data
public class PrepService {

    public ArrayList <Images> imagesList;
    public ArrayList <Categories> categoriesList;
    public ArrayList <Annotations> annotationsList;
    public ArrayList <ArrayList <String>> infoLog;

    public PrepService() {
        this.annotationsList = new ArrayList <>();
        this.categoriesList = new ArrayList <>();
        this.imagesList = new ArrayList <>();
        this.infoLog = new ArrayList <>();
    }

    public PrepService(ArrayList <Images> imagesList, ArrayList <Categories> categoriesList, ArrayList <Annotations> annotationsList, ArrayList <ArrayList <String>> infoLog) {
        this.annotationsList = annotationsList;
        this.categoriesList = categoriesList;
        this.imagesList = imagesList;
        this.infoLog = infoLog;
    }

    /**
     * Creates and returns list of annotations per file according the requirements.
     *
     * @param split,    type of desired split
     * @param splitAbs, Array of the amount of images per file
     * @param i,        number of output file
     * @param annoAt,   index of last passed annotation in common annotation list
     * @return ArrayList of annotations to be listed in file
     */
    public ArrayList <Annotations> splitData(String split, int[] splitAbs, int i, int annoAt) {
        ArrayList <Annotations> annotations = new ArrayList <>();
        switch (split) {
            case "none":
                annotations.addAll(annotationsList);
                return annotations;
            case "annotation":
                annotations.add(annotationsList.get(annoAt));
                return annotations;
            case "class":
                if (i == 0) {
                    this.annotationsList.sort(Comparator.comparing(o -> o.getCategory().getId()));
                }
                long categoryId = annotationsList.get(annoAt).getCategory().getId();
                for (int d = annoAt; d < this.annotationsList.size(); d++) {
                    if (!(categoryId == annotationsList.get(d).getCategory().getId())) {
                        break;
                    }
                    annotations.add(this.annotationsList.get(d));
                }
                break;
            default:
                int j = 1;
                if (i == 0) {
                    this.annotationsList.sort(Comparator.comparing(o -> o.getImage().getId()));
                }
                if (!split.equals("image")) {
                    j = splitAbs[i];
                }
                while (j > 0) {
                    long imageId = annotationsList.get(annoAt).getImage().getId();

                    for (int y = annoAt; y < this.annotationsList.size(); y++) {
                        if (!(imageId == annotationsList.get(y).getImage().getId())) {
                            annoAt = y;
                            break;
                        }
                        annotations.add(annotationsList.get(y));
                    }
                    j--;
                }
                break;
        }
        return annotations;
    }

    /**
     * Sets the amount of images per file in Array: pos1:amount first file, pos2: second file ...
     *
     * @param splitNumbers percentages of images per file (two or three)
     * @return absolute amount of images per file (three)
     */
    public int[] setAbsNumberOfSplit(double[] splitNumbers) {
        int size = this.imagesList.size();
        int[] absNumbers = {size, 0, 0};
        double sum = 0;
        if (splitNumbers != null) {
            for (double d : splitNumbers) {
                sum += d;
            }
            if (sum > 100) {
                this.getInfoLog().get(infoLog.size() - 1).add("Die eingegebenen Prozentangaben übersteigen in der Summe 100%. Keine Verteilung erfolgt.");
                return absNumbers;
            }
            if (splitNumbers.length == 2) {
                absNumbers[0] = (int) ((splitNumbers[0] / 100) * size + 0.5);
                absNumbers[1] = size - absNumbers[0];
            } else if (splitNumbers.length == 3) {
                absNumbers[0] = (int) ((splitNumbers[0] / 100) * size + 0.5);
                absNumbers[1] = (int) ((splitNumbers[1] / 100) * size + 0.5);
                absNumbers[2] = size - absNumbers[0] - absNumbers[1];
            }
        }
        return absNumbers;
    }

    /**
     * Transforms the value of the output either to relativ or absolut (as passed from controller) by calling
     * {@link #doValueOutput(int, Annotations)}
     *
     * @param value,           relativ or absolut
     * @param annotationsList, list of annotation for one output file
     */
    public void setKindOfValue(String value, ArrayList <Annotations> annotationsList) {
        if (value.equals("relativ")) {
            for (Annotations annotations : annotationsList) {
                doValueOutput(0, annotations);
            }
        } else if (value.equals("absolut")) {
            for (Annotations annotations : annotationsList) {
                doValueOutput(1, annotations);
            }
        }
    }

    /**
     * Sets the output value to relativ, absolut or default depending on if image data exist.
     *
     * @param absoluteValue, 0 (relativ) or 1 (absolut)
     * @param annotation     annotation, which values are to be converted
     */
    private void doValueOutput(int absoluteValue, Annotations annotation) {
        if (absoluteValue == annotation.getAbsoluteValue()) {
            return;
        }
        if (annotation.getImage().getWidth() != -1 && annotation.getImage().getHeight() != -1) {
            if (absoluteValue == 1) {
                annotation.getPolygon().setAbsoluteValue(annotation.getImage());
                annotation.getBbox().setAbsoluteValue(annotation.getImage());
                annotation.setAbsoluteValue(1);
            } else if (absoluteValue == 0) {
                annotation.getPolygon().setRelativValues(annotation.getImage());
                annotation.getBbox().setRelativValues(annotation.getImage());
                annotation.setAbsoluteValue(0);
            }
        } else {
            annotation.getPolygon().setDefaultValue();
            annotation.getBbox().setDefaultValue();
            annotation.setAbsoluteValue(-1);
            this.infoLog.get(infoLog.size() - 1).add("Werte der Annotation " + annotation.getId() + " in Bild '" + annotation.getImage().getFileName() + "' können nicht umgerechnet werden.");
        }
    }

    public Bbox createBboxFromPolygon(Polygon polygon) {
        double xmin = -1;
        double ymin = -1;
        double ymax = -1;
        double xmax = -1;

        for (int j = 0; j < polygon.getPolygon().size(); j++) {
            for (int i = 0; i < polygon.getPolygon().get(j).size() - 1; i += 2) {
                double currentX = polygon.getPolygon().get(j).get(i);
                double currentY = polygon.getPolygon().get(j).get(i + 1);

                if ((j == 0 && i == 0) || currentX < xmin) {
                    xmin = currentX;
                }

                if ((j == 0 && i == 0) || currentY < ymin) {
                    ymin = currentY;
                }

                if (currentX > xmax) {
                    xmax = currentX;
                }

                if (currentY > ymax) {
                    ymax = currentY;
                }
            }
        }
        return new Bbox(xmin, ymax, (xmax - xmin), (ymax - ymin));
    }

    public void clear() {
        this.getImagesList().clear();
        this.getCategoriesList().clear();
        this.getAnnotationsList().clear();
        this.getInfoLog().clear();
    }
}
