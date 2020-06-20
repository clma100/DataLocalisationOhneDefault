package application.model.outputFormats.coco;

import application.model.Annotations;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;

/**
 * Represents the Coco- Format. The names of each attribute will be printed via JSON Objectmapper. So dont change
 * names if the format doesnt change.
 */
@Data
public class Coco {

    private Info info;
    private ArrayList <Licenses> licenses;
    private ArrayList <CategoriesCoco> categories;
    private ArrayList <ImagesCoco> images;
    private ArrayList <AnnotationCoco> annotations;

    public Coco(ArrayList <Images> imagesList, ArrayList <Categories> categoriesList, ArrayList <Annotations> annotationsList) {
        this.info = new Info();
        this.licenses = new ArrayList <>();
        this.categories = setCocoCategories(categoriesList);
        this.images = setCocoImages(imagesList);
        this.annotations = setCocoAnnotations(annotationsList);
    }

    /**
     * Transforms the saved data regarding categories into the required structure for coco.
     *
     * @param categoriesList, common list of categories
     * @return list of categories in coco
     */
    private ArrayList <CategoriesCoco> setCocoCategories(ArrayList <Categories> categoriesList) {
        ArrayList <CategoriesCoco> CategoriesCocos = new ArrayList <>();
        for (Categories categories : categoriesList) {
            CategoriesCoco categoriesCoco = new CategoriesCoco(categories);
            CategoriesCocos.add(categoriesCoco);
        }
        return CategoriesCocos;
    }

    /**
     * Transforms the saved data regarding images into the required structure for coco.
     *
     * @param imagesList common list of images
     * @return list of images in coco
     */
    private ArrayList <ImagesCoco> setCocoImages(ArrayList <Images> imagesList) {
        ArrayList <ImagesCoco> ImagesCocos = new ArrayList <>();
        for (Images images : imagesList) {
            ImagesCoco imagesCoco = new ImagesCoco(images);
            ImagesCocos.add(imagesCoco);
        }
        return ImagesCocos;
    }

    /**
     * Transforms the saved data regarding annotations into the required structure for coco
     *
     * @param annotationsList common list of annotations
     * @return list of annotations in coco
     */
    private ArrayList <AnnotationCoco> setCocoAnnotations(ArrayList <Annotations> annotationsList) {
        ArrayList <AnnotationCoco> annotationCocoList = new ArrayList <>();
        for (Annotations annotations : annotationsList) {
            AnnotationCoco annotationCoco = new AnnotationCoco(annotations);
            annotationCocoList.add(annotationCoco);
        }
        return annotationCocoList;
    }

    /**
     * Prints an Coco- Object as JSON- String
     *
     * @return JSON- formatted String of object
     * @throws JsonProcessingException writeValueAsString doesnt posses
     */
    public String printJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
