package application.model.outputFormats.coco;

import lombok.Data;

/**
 * Represents the Info Field in Coco. The names of each attribute will be printed via JSON Objectmapper
 * in Coco.class. So dont change names if the format doesnt change.
 */
@Data
public class Info {

    private int year;
    private String version;
    private String description;
    private String contributor;
    private String url;
    private String date_created;

    public Info() {
        this.year = -1;
        this.version = "unknown";
        this.description = "unknown";
        this.contributor = "unknown";
        this.url = "unknown";
        this.date_created = "unknown";
    }
}
