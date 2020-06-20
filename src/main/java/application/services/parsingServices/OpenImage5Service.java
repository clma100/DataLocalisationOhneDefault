package application.services.parsingServices;

import application.model.Annotations;
import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.services.parentClass.PrepService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static application.model.subsets.Categories.createCategory;
import static application.model.subsets.Images.createImage;
import static java.lang.Double.parseDouble;

/**
 * Reads all files in OpenImageV5- Format and stores the passed data in common lists.
 * Current Format:
 * <p>
 * ImageID,Source,LabelName,Confidence,XMin,XMax,YMin,YMax,IsOccluded,IsTruncated,IsGroupOf,IsDepiction,IsInside
 * [ImageID],[Source],[LabelName],[Confidence],[XMin],[XMax],[YMin],[YMax],[IsOccluded]..
 * ...
 * Line per Line
 */
public class OpenImage5Service extends PrepService {

    public OpenImage5Service(ArrayList <Images> imagesList, ArrayList <Categories> categoriesList, ArrayList <Annotations> annotationsList, ArrayList <ArrayList <String>> infoLog) {
        super(imagesList, categoriesList, annotationsList, infoLog);
    }

    /**
     * Extracts the contained information and reads the file line per line.
     *
     * @param filePath filepath of passed Multipartfile
     * @throws IOException if action with file doesnt work
     */
    public void readOpenImageCsv(String filePath) throws IOException {
        String row;
        String seperator = ",";
        String[] variables = new String[0];

        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        if ((row = bufferedReader.readLine()) != null) {
            variables = row.split(seperator);
        }

        while ((row = bufferedReader.readLine()) != null) {
            String[] annotationRow = row.split(seperator);
            createAnnotation(annotationRow, variables);
        }
        bufferedReader.close();
        this.infoLog.get(infoLog.size() - 1).add("eingelesen. ");
    }

    private void createAnnotation(String[] annotationRow, String[] variables) {
        int posImageId = getPos(variables, "ImageID");
        int posLabelName = getPos(variables, "LabelName");
        int posXmin = getPos(variables, "XMin");
        int posXmax = getPos(variables, "XMax");
        int posYmin = getPos(variables, "YMin");
        int posYmax = getPos(variables, "YMax");

        String imageName = annotationRow[posImageId];
        Images image = createImage(imageName, imagesList);

        String categoryName = annotationRow[posLabelName];
        Categories category = createCategory(categoryName, categoriesList);

        Bbox bbox = new Bbox(parseDouble(annotationRow[posXmin]), parseDouble(annotationRow[posYmax]), parseDouble(annotationRow[posXmax]) - parseDouble(annotationRow[posXmin]), parseDouble(annotationRow[posYmax]) - parseDouble(annotationRow[posYmin]));
        Annotations annotation = new Annotations(image, category, bbox, annotationsList.size() + 1);
        annotation.setAbsoluteValueBbox();
        annotationsList.add(annotation);
    }

    private int getPos(String[] variables, String needle) {
        for (int i = 0; i < variables.length; i++) {
            if (variables[i].equals(needle)) {
                return i;
            }
        }
        return -1;
    }
}
