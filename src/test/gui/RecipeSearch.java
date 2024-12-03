package test.gui;

import edu.northeastern.csye6200.entity.Recipe;
import edu.northeastern.csye6200.utility.DataManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class RecipeSearch extends Application {
    private DataManager dataManager;
    private static final String IMAGE_PATH = "data/image/recipe/";
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        dataManager = new DataManager();
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);

        // Title
        Label titleLabel = new Label("Recipe Search");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Search Area
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter recipe name...");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchRecipe(searchField.getText().trim()));

        searchBox.getChildren().addAll(searchField, searchButton);

        // Result Area
        resultLabel = new Label("Search for a recipe by its name.");
        resultLabel.setWrapText(true);
        resultLabel.setStyle("-fx-font-size: 14px;");

        root.getChildren().addAll(titleLabel, searchBox, resultLabel);

        // Scene Setup
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("search.css").toExternalForm());
        primaryStage.setTitle("Recipe Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void searchRecipe(String recipeName) {
        if (recipeName.isEmpty()) {
            resultLabel.setText("Please enter a recipe name.");
            return;
        }

        // Fetch recipe data from DataManager
        List<Recipe> recipes = dataManager.getRecipeByName(recipeName);
        if (!recipes.isEmpty()) {
            Recipe recipe = recipes.get(0);
            // Create the result view
            VBox recipeBox = new VBox(10);
            recipeBox.setAlignment(Pos.CENTER_LEFT);

            // Recipe Image
            ImageView recipeImage = loadRecipeImage(recipe.getImage_url());
            recipeBox.getChildren().add(recipeImage);

            // Recipe Title
            Label recipeTitle = new Label(recipe.getTitle());
            recipeTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            recipeBox.getChildren().add(recipeTitle);

            // Recipe Instructions
            Text recipeInstructions = new Text("Instructions: " + recipe.getProcess());
            recipeInstructions.setWrappingWidth(400);
            recipeBox.getChildren().add(recipeInstructions);

            resultLabel.setGraphic(recipeBox);
            resultLabel.setText("");
        } else {
            resultLabel.setGraphic(null);
            resultLabel.setText("No recipe found with the name: " + recipeName);
        }
    }


    private ImageView loadRecipeImage(String imagePath) {
        File imageFile = new File(IMAGE_PATH + imagePath);
        if (!imageFile.exists()) {
            imageFile = new File(IMAGE_PATH + "default.png"); // Use a default image if not found
        }
        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
