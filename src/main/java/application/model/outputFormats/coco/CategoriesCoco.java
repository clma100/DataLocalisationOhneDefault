package application.model.outputFormats.coco;

import application.model.subsets.Categories;
import lombok.Data;

/**
 * Represents the Categories Field in Coco. The names of each attribute will be printed via JSON Objectmapper
 * in Coco.class. So dont change names if the format doesnt change.
 */
@Data
public class CategoriesCoco {
    private long id;
    private String name;
    private String supercategory;

    public CategoriesCoco(Categories categories) {
        this.id = categories.getId();
        this.name = categories.getName();
        this.supercategory = "unknown";
    }
}
