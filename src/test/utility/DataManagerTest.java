package test.utility;

import edu.northeastern.csye6200.utility.DataManager;
import edu.northeastern.csye6200.entity.Recipe;
import edu.northeastern.csye6200.entity.Ingredient;
import java.util.List;
import java.util.Arrays;
public class DataManagerTest {
    private static DataManager dm;

    public static void main(String[] args) {
        dm = new DataManager();
        testGetAllRecipes();
        testGetAllIngredients();
        testIngredientSearchStrict();
        testIngredientSearchLoose();
        dm.close();
    }

    private static void testGetAllRecipes() {
        System.out.println("测试获取所有食谱:");
        for(Recipe recipe : dm.getRecipes()) {
            System.out.println("ID: " + recipe.getId());
            System.out.println("标题: " + recipe.getTitle());
            System.out.println("图片URL: " + recipe.getImage_url());
            System.out.println("制作步骤: " + recipe.getProcess());
            System.out.println("--------------------");
        }
    }

    private static void testGetAllIngredients() {
        System.out.println("\n测试获取所有配料:");
        for(Ingredient ingredient : dm.getIngredients()) {
            System.out.println("ID: " + ingredient.getId());
            System.out.println("食谱ID: " + ingredient.getRecipe_id());
            System.out.println("配料名称: " + ingredient.getIngredient_name());
            System.out.println("数量: " + ingredient.getQuantity());
            System.out.println("单位: " + ingredient.getUnit());
            System.out.println("--------------------");
        }
    }

    private static void testIngredientSearchStrict() {
        System.out.println("\n测试严格配料搜索:");
        List<Recipe> strictResults = dm.ingredientSearchStrict(Arrays.asList("Chicken", "Alfredo Sauce"));
        for(Recipe recipe : strictResults) {
            System.out.println("找到食谱: " + recipe.getTitle());
        }
    }

    private static void testIngredientSearchLoose() {
        System.out.println("\n测试模糊配料搜索:");
        List<Recipe> looseResults = dm.ingredientSearchLoose(Arrays.asList("Chicken", "Beef"));
        for(Recipe recipe : looseResults) {
            System.out.println("找到食谱: " + recipe.getTitle());
        }
    }
}
