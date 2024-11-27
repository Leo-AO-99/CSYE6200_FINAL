package edu.northeastern.csye6200;

import edu.northeastern.csye6200.entity.Ingredient;
import edu.northeastern.csye6200.entity.Recipe;
import edu.northeastern.csye6200.utility.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailView {
    private Recipe recipe;
    private List<Ingredient> ingredients = new ArrayList<>();
    private DataManager dataManager;

    @FXML
    private Label LabelRecipe;
    
    @FXML
    private ImageView ImageRecipe;
    
    @FXML
    private GridPane GridIngredients;
    
    @FXML
    private ScrollPane PaneSteps;
    
    @FXML
    private TextField TextTime;

    public void showRecipe(Recipe recipe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RecipeDetailView.fxml"));
            Parent root = loader.load();
            
            RecipeDetailView controller = loader.getController();
            controller.setData(recipe.getId());
            
            Stage stage = new Stage();
            stage.setTitle(recipe.getTitle());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setData(int recipeID) {
        dataManager = new DataManager();
        this.recipe = dataManager.getRecipeById(recipeID);
        for (Ingredient i : dataManager.getIngredients()) {
            if (i.getRecipe_id() == recipeID)
                this.ingredients.add(i);
        }
        
        LabelRecipe.setText(recipe.getTitle());
        
        try {
            Image image = new Image(new File(recipe.getImage_url()).toURI().toString());
            ImageRecipe.setImage(image);
            ImageRecipe.setFitHeight(400);
            ImageRecipe.setFitWidth(400);
        } catch (Exception e) {
            System.err.println("Error loading recipe image: " + e.getMessage());
        }
        
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPrefWidth(30);  
        col0.setHalignment(HPos.CENTER);  
        GridIngredients.getColumnConstraints().add(col0);
        
        for (int num = 0; num < ingredients.size(); num++) {
            Ingredient i = ingredients.get(num);
            try {
                ImageView ingredientImage = new ImageView(
                    new Image(new File("data/image/ingredient/" + i.getIngredient_name() + ".png").toURI().toString())
                );
                ingredientImage.setFitWidth(30);
                ingredientImage.setFitHeight(30);
                ingredientImage.setPreserveRatio(true);  
                GridIngredients.add(ingredientImage, 0, num);
                GridIngredients.add(new Label(i.getIngredient_name()), 1, num);
                GridIngredients.add(new Label(i.getQuantity()), 2, num);
                GridIngredients.add(new Label(i.getUnit()), 3, num);
            } catch (Exception e) {
                System.err.println("Error loading ingredient image: " + e.getMessage());
            }
        }
        
        VBox steps = new VBox(5);
        steps.getChildren().add(new Label(recipe.getProcess()));
        PaneSteps.setContent(steps);
        
        TextTime.setText(recipe.getCooking_time());
    }
}
