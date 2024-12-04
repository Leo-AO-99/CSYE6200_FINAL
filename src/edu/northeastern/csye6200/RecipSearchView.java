package edu.northeastern.csye6200;

import edu.northeastern.csye6200.entity.Recipe;
import edu.northeastern.csye6200.entity.DMHandler;
import edu.northeastern.csye6200.entity.Ingredient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecipSearchView extends VBox {
    private static final String IMAGE_PATH = "";
    private Label resultLabel;

    public RecipSearchView() {
    
        setSpacing(10);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Recipe Search");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter recipe name...");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchRecipe(searchField.getText().trim()));

        searchBox.getChildren().addAll(searchField, searchButton);

        resultLabel = new Label("Search for a recipe by its name.");
        resultLabel.setWrapText(true);
        resultLabel.setStyle("-fx-font-size: 14px;");

        getChildren().addAll(titleLabel, searchBox, resultLabel);
    }

    private void searchRecipe(String recipeName) {
        if (recipeName.isEmpty()) {
            resultLabel.setText("Please enter a recipe name.");
            return;
        }

        List<Recipe> recipes = DMHandler.dataManager.getRecipeByName(recipeName);
        if (!recipes.isEmpty()) {
            // 创建结果显示区域
            VBox resultsBox = new VBox(15);
            resultsBox.setPadding(new Insets(20));
            resultsBox.setStyle("-fx-background-color: white;");

            Label titleLabel = new Label("Found Recipes:");
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            resultsBox.getChildren().add(titleLabel);

            for (Recipe recipe : recipes) {
                HBox recipeRow = createRecipeRow(recipe);
                resultsBox.getChildren().add(recipeRow);
            }

            ScrollPane scrollPane = new ScrollPane(resultsBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefViewportHeight(500);
            scrollPane.setStyle("-fx-background-color: white;");
            
            resultLabel.setGraphic(scrollPane);
            resultLabel.setText("");
        } else {
            resultLabel.setGraphic(null);
            resultLabel.setText("No recipes found matching: " + recipeName);
        }
    }

    private HBox createRecipeRow(Recipe recipe) {
        HBox recipeRow = new HBox(15);
        recipeRow.setPadding(new Insets(10));
        recipeRow.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5;");

        ImageView recipeImage = loadRecipeImage(recipe.getImage_url());
        recipeImage.setFitWidth(100);
        recipeImage.setFitHeight(100);

        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, javafx.scene.layout.Priority.ALWAYS);

        Label recipeTitleLabel = new Label(recipe.getTitle());
        recipeTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        recipeTitleLabel.setWrapText(true);

        List<String> recipeIngredients = new ArrayList<>();
        for (Ingredient ingredient : DMHandler.dataManager.getIngredients()) {
            if (ingredient.getRecipe_id() == recipe.getId()) {
                recipeIngredients.add(ingredient.getIngredient_name() + 
                    " " + ingredient.getQuantity() + 
                    " " + ingredient.getUnit());
            }
        }

        Label ingredientsLabel = new Label("Ingredients: " + String.join(", ", recipeIngredients));
        ingredientsLabel.setWrapText(true);
        ingredientsLabel.setStyle("-fx-font-size: 14px;");

        infoBox.getChildren().addAll(recipeTitleLabel, ingredientsLabel);
        recipeRow.getChildren().addAll(recipeImage, infoBox);

        recipeRow.setOnMouseClicked(e -> {
            System.out.println("Clicked recipe: " + recipe.getTitle());
            RecipeDetailView detailView = new RecipeDetailView();
            detailView.showRecipe(recipe);
        });

        recipeRow.setOnMouseEntered(e -> 
            recipeRow.setStyle("-fx-background-color: #e8e8e8; -fx-background-radius: 5; -fx-cursor: hand;"));
        recipeRow.setOnMouseExited(e -> 
            recipeRow.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5;"));

        return recipeRow;
    }

    private ImageView loadRecipeImage(String imagePath) {
        File imageFile = new File(IMAGE_PATH + imagePath);
        if (!imageFile.exists()) {
            imageFile = new File(IMAGE_PATH + "default.png");
        }
        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }
}
