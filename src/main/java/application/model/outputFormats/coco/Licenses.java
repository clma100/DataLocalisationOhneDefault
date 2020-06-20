package application.model.outputFormats.coco;

import lombok.Data;

/**
 * Represents the Licenses Field in Coco. The names of each attribute will be printed via JSON Objectmapper
 * in Coco.class. So dont change names if the format doesnt change.
 */
@Data
public class Licenses {

    private int id;
    private String name;
    private String url;

    // --Commented out: Constructor never used, createt for completeness
    //	public Licenses() {
    //		this.id = -1;
    //		this.name = "unknown";
    //		this.url = "unknown";
    //	}
}
