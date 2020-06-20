package application.model.subsets;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Categories {

    private long id;
    private String name;

    public Categories() {
        this.id = -1;
        this.name = "unknown";
    }

    public Categories(String name, long id) {
        this.id = id;
        this.name = name;
    }

    public Categories(long id) {
        this.id = id;
        this.name = "unknown";
    }

    /**
     * @param categoryName,   name of category without extension
     * @param categoriesList, currently stored list of categories
     * @return Creates either a new category, when it not existing yet, else returns the existing
     */
    public static Categories createCategory(String categoryName, ArrayList <Categories> categoriesList) {
        int containsCategorie = containsCategoryAtIndex(categoryName, categoriesList);
        if (containsCategorie == -1) {
            Categories category = new Categories(categoryName, categoriesList.size() + 1);
            categoriesList.add(category);
            return category;
        }
        return categoriesList.get(containsCategorie);
    }

    /**
     * Checks if category already exists, and if, at which index.
     *
     * @param categoryName,   name of categorie without file extension
     * @param categoriesList, currently stored list of categories
     * @return -1 if not existing, index else
     */
    private static int containsCategoryAtIndex(String categoryName, ArrayList <Categories> categoriesList) {
        for (int i = 0; i < categoriesList.size(); i++) {
            if (categoriesList.get(i).getName() != null && categoriesList.get(i).getName().equals(categoryName)) {
                return i;
            }
        }
        return -1;
    }
}
