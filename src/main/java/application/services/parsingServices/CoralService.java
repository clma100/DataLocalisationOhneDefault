package application.services.parsingServices;

import application.model.Annotations;
import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.model.subsets.Polygon;
import application.services.parentClass.PrepService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static application.model.subsets.Categories.createCategory;
import static application.model.subsets.Images.createImage;

/**
 * Reads all files in ImageCLEFcoral- Format and stores the passed data in common lists.
 * Current Formats:
 * annotation and localisation
 * - submission: [image_name];[substrate1] [[confidence1,1]:][width1,1]x[height1,1]+[xmin1,1]+[ymin1,1],[[confidence1,2]:][width1,2]x[height1,2]+[xmin1,2]+[ymin1,2],...;
 * [substrate2] ...   (pro image und klasse eine spalte)
 * - development: [image_name] [count p.Image] [substrate1] [1] [xmin] [ymin] [xmax] [ymax] /n
 * [image_name] [count p.Image] [substrate2] [1] [xmin] [ymin] [xmax] [ymax] ...
 * <p>
 * pixel-wise parsing task
 * - submission: [image_name];[substrate1] [[confidence1,1]:][x1,1]+[y1,1]+[x2,1]+[y2,1]+….+[xn,1]+[yn,1],[[confidence1,2][x1,2]+[y1,2]+[x2,2]+[y2,2]+….+[xn,2]+[yn,2];
 * [substrate2] ...
 * - development: [image_name] [count p.Image] [substrate1] [1] [x1] [y1] [x2] [y2]...[xn] [yn] \n
 * [image_name] [count p.Image] [substrate2] [1] [x1] [y1] [x2] [y2]...[xn] [yn] ...
 */
public class CoralService extends PrepService {

    public CoralService(ArrayList <Images> imagesList, ArrayList <Categories> categoriesList, ArrayList <Annotations> annotationsList, ArrayList <ArrayList <String>> infoLog) {
        super(imagesList, categoriesList, annotationsList, infoLog);
    }

    /**
     * Reads the contained information in file and stores it into the common lists depending on the format.
     *
     * @param filePath file path of passed Multipartfile
     * @throws IOException if IO error occurs by reading bytes from file
     */
    public void readCoralTxt(String filePath) throws IOException {
        String txt = new String(Files.readAllBytes(Paths.get(filePath)));
        splitIntoRows(txt);
        this.infoLog.get(infoLog.size() - 1).add("eingelesen. ");
    }

    private void splitIntoRows(String txt) {
        int beginn = 0;
        int end = txt.indexOf("\n");
        String row;

        if (!txt.substring(beginn).contains("\n")) {
            row = txt.substring(beginn);
            readRow(row);
        }
        while (txt.substring(beginn).contains("\n")) {
            row = txt.substring(beginn, end);
            readRow(row);
            beginn = end + 1;
            if (!txt.substring(beginn).contains("\n")) {
                row = txt.substring(beginn);
                readRow(row);
            }
            end = beginn + txt.substring(beginn).indexOf("\n");
        }
    }

    private void readRow(String row) {
        if (row.contains("x") || row.contains(";")) {
            extractAnnotationSub(row);
        } else {
            readAnnotation(row, true);
        }
    }

    private void readAnnotation(String row, boolean dev) {
        String seperator1;
        if (!dev) {
            seperator1 = ";";
        } else {
            seperator1 = " ";
        }

        if (!row.contains(seperator1)) {
            return;
        }

        int beginn = 0;
        int end = row.indexOf(seperator1);

        String imageName = row.substring(beginn, end);
        Images image = createImage(imageName, imagesList);               //save imageName and image

        beginn = end + 1;
        end = beginn + row.substring(beginn).indexOf(" ");
        if (dev) {
            beginn = end + 1;
            end = beginn + row.substring(beginn).indexOf(" ");
        }

        String categoryName = row.substring(beginn, end);
        Categories category = createCategory(categoryName, categoriesList);    //save categoryName and category / substrate

        if (dev) {
            beginn = end + 1;
            end = beginn + row.substring(beginn).indexOf(" ");
            beginn = end + 1;
        } else {
            beginn = end + 1 + row.substring(end + 1).indexOf(":") + 1;
            end = beginn + row.substring(beginn).indexOf(",");
        }

        if (!row.substring(beginn).contains(",")) {            //seperate bbox informations, parse and save in annotations
            ArrayList <Double> listValues = substractValues(row.substring(beginn), dev);
            if (listValues.size() == 4) {
                saveBbox(image, category, listValues);
            } else {
                savePolygon(image, category, listValues);
            }
        } else {
            while (row.substring(beginn).contains(",")) {
                ArrayList <Double> listValues = substractValues(row.substring(beginn, end), dev);
                if (listValues.size() == 4) {
                    saveBbox(image, category, listValues);
                } else {
                    savePolygon(image, category, listValues);
                }
                beginn = end + 1 + row.substring(end + 1).indexOf(":") + 1;
                if (!row.substring(beginn).contains(",")) {
                    listValues = substractValues(row.substring(beginn), dev);
                    if (listValues.size() == 4) {
                        saveBbox(image, category, listValues);
                    } else {
                        savePolygon(image, category, listValues);
                    }
                }
                end = beginn + row.substring(beginn).indexOf(",");
            }
        }
    }

    private void extractAnnotationSub(String row) {
        String seperator1 = ";";
        int beginn = 0;
        int end = row.indexOf(seperator1);

        String imageName = row.substring(beginn, end);

        while (row.substring(beginn).contains(";")) {
            beginn = end + 1;
            if (row.substring(beginn).contains(";")) {
                end = beginn + row.substring(beginn).indexOf(";");
            } else {
                end = row.length();
            }
            readAnnotation(imageName + ";" + row.substring(beginn, end), false);
        }
    }

    private void savePolygon(Images image, Categories category, ArrayList <Double> listValues) {
        ArrayList <ArrayList <Double>> polygonList = new ArrayList <>();
        polygonList.add(listValues);
        Polygon polygon = new Polygon(polygonList);
        Bbox bbox = this.createBboxFromPolygon(polygon);
        Annotations annotation = new Annotations(image, category, bbox, annotationsList.size() + 1, polygon);
        annotation.setAbsoluteValuePolygon();
        annotationsList.add(annotation);
    }

    private void saveBbox(Images image, Categories category, ArrayList <Double> listValues) {
        Bbox bbox = new Bbox();
        bbox.setXMin(listValues.get(0));
        bbox.setYMax(listValues.get(1));
        bbox.setWidth(listValues.get(2));
        bbox.setHeight(listValues.get(3));
        Annotations annotation = new Annotations(image, category, bbox, annotationsList.size() + 1);
        annotation.setAbsoluteValueBbox();
        annotationsList.add(annotation);
    }

    private ArrayList <Double> substractValues(String annotationRow, boolean dev) {                     //gets raw information conc. bbox as string and parses into common bbox format (=> xmin, ymax, width, height)
        String sep1;
        String sep2;
        if (dev) {
            sep1 = " ";
            sep2 = " ";
        } else {
            sep1 = "x";
            sep2 = "+";
        }
        int beginn = 0;
        int end;

        if (annotationRow.contains(sep1)) {
            end = annotationRow.indexOf(sep1);
        } else {
            end = annotationRow.indexOf(sep2);
        }
        ArrayList <Double> list = new ArrayList <>();
        while (annotationRow.substring(beginn).contains(sep2)) {
            list.add(Double.valueOf(annotationRow.substring(beginn, end)));
            beginn = end + 1;
            if (!annotationRow.substring(beginn).contains(sep2)) {
                String x = annotationRow.substring(beginn);
                if (!x.isEmpty()) {
                    try {
                        list.add(Double.valueOf(x));
                    } catch (Exception e) {
                        break;
                    }
                }
            }
            end = beginn + annotationRow.substring(beginn).indexOf(sep2);
        }
        if (list.size() == 4) {
            if (dev) {
                double help = list.get(1);              //list pos: 0:xmin 1:ymin 2:xmax 3:ymax => common format: xmin,ymax,width,height
                list.set(1, list.get(3));
                list.set(2, list.get(2) - list.get(0) + 1);
                list.set(3, list.get(3) - help + 1);
            } else {
                Double help = list.get(0);                  //list pos: 0:width 1:height 2:xmin 3:ymin => common format: xmin,ymax,width,height
                list.set(0, list.get(2));
                list.set(1, list.get(3) + list.get(1));
                list.set(2, help);
                list.set(3, list.get(1) - list.get(3));
            }
        }
        return list;
    }
}