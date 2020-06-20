package application.services.parsingServices;

import application.model.Annotations;
import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.model.subsets.Polygon;
import application.services.parentClass.PrepService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads all files in Coco- Format and stores the passed data in common lists.
 * Current Format:
 * {
 * "info": info,
 * "licenses": [license],
 * "categories": [category],
 * "images": [image],
 * "annotations": [annotation]
 * }
 * "categories": [{"id": 1, "name": "name", "supercategory": "supercategory"},
 * {"id": 2, "name": "name2", "supercategory": "supercategory2"}]
 * "images": [{"id": 1, "width": 111, "height": 222, "file_name": "hallo.jpg", "license": 2 ...}]
 * "annotations": [{"id": 2, "image_id": 3, "category_id": 2, "segmentation": [[2.0,23.0]], "area": 2.0, "bbox": [xmin,ymax,width,height], "iscrowd": 0 or 1}]
 */
public class CocoService extends PrepService {

    public CocoService(ArrayList <Images> imagesList, ArrayList <Categories> categoriesList, ArrayList <Annotations> annotationsList, ArrayList <ArrayList <String>> infoLog) {
        super(imagesList, categoriesList, annotationsList, infoLog);
    }

    /**
     * Extracts the contained information in the file and stores it by using JSON Objectmapper and going through the
     * tree. !As coco assigns the images and categories to each annotation per own Id, images and categories are
     * temporarily saved in other ArrayList until all annotations are read and the belonging image and category stored.
     *
     * @param filePath, file path of passed Multipartfile
     */
    public void readCocoJson(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File(filePath));
            ArrayList <Images> imagesArrayList = new ArrayList <>();
            ArrayList <Categories> categoriesArrayList = new ArrayList <>();
            if (jsonNode.has("images")) {
                for (int i = 0; i < jsonNode.get("images").size(); i++) {
                    JsonNode node = jsonNode.get("images").get(i);
                    int imageId = node.get("id").asInt();
                    String imageName = node.get("file_name").asText();
                    int width = node.get("width").asInt();
                    int height = node.get("height").asInt();
                    Images image = new Images(width, height, imageName, imageId);
                    if (image.getFileName().contains(".")) {
                        image.setFileName(image.getFileName().substring(0, image.getFileName().lastIndexOf(".")));
                    }
                    imagesArrayList.add(image);
                }
            }
            if (jsonNode.has("categories")) {
                for (int i = 0; i < jsonNode.get("categories").size(); i++) {
                    JsonNode node = jsonNode.get("categories").get(i);
                    int categorieId = node.get("id").asInt();
                    String categoryName = node.get("name").asText();
                    Categories categorie = new Categories(categoryName, categorieId);
                    categoriesArrayList.add(categorie);
                }
            }
            for (int i = 0; i < jsonNode.get("annotations").size(); i++) {
                JsonNode node = jsonNode.get("annotations").get(i);
                Annotations annotation = new Annotations();
                if (node.has("image_id")) {
                    annotation.setImage(getImageById(node.get("image_id").asInt(), imagesArrayList));
                }
                if (node.has("segmentation")) {                  //checks if key exists, value can be NULL
                    JsonNode segmentationNode = node.get("segmentation");
                    ArrayList <ArrayList <Double>> polygonList = new ArrayList <>();
                    if (!segmentationNode.has("counts")) {               //then POLYGON
                        for (int x = 0; x < segmentationNode.size(); x++) {
                            ArrayList <Double> polygonInner = new ArrayList <>();
                            for (int j = 0; j < segmentationNode.get(x).size(); j++) {
                                polygonInner.add(segmentationNode.get(x).get(j).asDouble());
                            }
                            polygonList.add(polygonInner);
                        }
                        Polygon polygon = new Polygon(polygonList);
                        annotation.setPolygon(polygon);
                    } else if (segmentationNode.has("counts")) {              //else RLE
                        this.infoLog.get(infoLog.size() - 1).add("RLE bei Image_Id:" + node.get("image_id").asInt() + " wird ignoriert (set to = [0])");
                        ArrayList <Double> polygonInner = new ArrayList <>();
                        polygonList.add(polygonInner);
                        annotation.setPolygon(new Polygon(polygonList));
                    }
                }
                if (node.has("id")) {
                    annotation.setId(annotationsList.size() + 1);
                }
                if (node.has("category_id")) {
                    annotation.setCategory(getCategoryById(node.get("category_id").asInt(), categoriesArrayList));
                }
                if (node.has("bbox")) {
                    JsonNode bboxNode = node.get("bbox");
                    Bbox bbox = new Bbox();
                    bbox.setXMin(bboxNode.get(0).asDouble());
                    bbox.setYMax(bboxNode.get(1).asDouble());
                    bbox.setWidth(bboxNode.get(2).asDouble());
                    bbox.setHeight(bboxNode.get(3).asDouble());
                    annotation.setBbox(bbox);
                    annotation.setAbsoluteValueBbox();
                }
                annotationsList.add(annotation);
            }
            for (Images images : imagesArrayList) {
                images.setId(imagesList.size() + 1);
                imagesList.add(images);
            }
            imagesArrayList.clear();
            for (Categories categories : categoriesArrayList) {
                categories.setId(categoriesList.size() + 1);
                categoriesList.add(categories);
            }
            categoriesArrayList.clear();
            this.infoLog.get(infoLog.size() - 1).add("eingelesen. ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Images getImageById(long id, ArrayList <Images> imagesList) {
        for (Images image : imagesList) {
            if (image.getId() == id) {
                return image;
            }
        }
        Images image = new Images(id);
        imagesList.add(image);
        return image;
    }

    private Categories getCategoryById(long id, ArrayList <Categories> categoriesList) {
        for (Categories category : categoriesList) {
            if (category.getId() == id) {
                return category;
            }
        }
        Categories category = new Categories(id);
        categoriesList.add(category);
        return category;
    }
}
