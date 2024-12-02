package edu.northeastern.csye6200;

import edu.northeastern.csye6200.entity.Recipe;
import edu.northeastern.csye6200.utility.DataManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainPageView extends VBox {
    private DataManager dataManager;
    
    public MainPageView() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #f0f0f0;");
        
        dataManager = new DataManager();
        
        List<Recipe> allRecipes = dataManager.getRecipes();
        System.out.println("Total recipes loaded: " + allRecipes.size());
        
        Label welcomeLabel = new Label("Welcome to Recipe Management System");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        ScrollPane scrollPane = createRecommendedSection();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        getChildren().addAll(welcomeLabel, scrollPane);
    }
    
    private ScrollPane createRecommendedSection() {
        VBox recommendedBox = new VBox(10);
        recommendedBox.setPadding(new Insets(0, 10, 0, 10));
        recommendedBox.setStyle("-fx-background-color: #f0f0f0;");
        
        Label titleLabel = new Label("Recommended For You");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        FlowPane recipeFlow = new FlowPane();
        recipeFlow.setHgap(20);
        recipeFlow.setVgap(20);
        recipeFlow.setPrefWrapLength(800);
        recipeFlow.setStyle("-fx-background-color: #f0f0f0;");
        
        List<Recipe> randomRecipes = getRandomRecipes(10);
        System.out.println("Random recipes selected: " + randomRecipes.size());
        for (Recipe recipe : randomRecipes) {
            System.out.println("Recipe: " + recipe.getTitle() + ", Image: " + recipe.getImage_url());
        }
        
        for (Recipe recipe : randomRecipes) {
            VBox recipeCard = createRecipeCard(recipe);
            recipeFlow.getChildren().add(recipeCard);
        }
        
        recommendedBox.getChildren().addAll(titleLabel, recipeFlow);
        
        ScrollPane scrollPane = new ScrollPane(recommendedBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f0f0f0; -fx-background-color: #f0f0f0;");
        
        return scrollPane;
    }
    
    private VBox createRecipeCard(Recipe recipe) {
        VBox card = new VBox(8);
        card.setPrefWidth(200);
        card.setMinHeight(300);
        card.setStyle("-fx-background-color: white; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 0); " +
                     "-fx-background-radius: 8; " +
                     "-fx-border-radius: 8; " +
                     "-fx-border-color: #e0e0e0; " +
                     "-fx-border-width: 1;");
        card.setPadding(new Insets(8));
        
        VBox imageContainer = new VBox();
        imageContainer.setStyle("-fx-background-color: #f5f5f5; " +
                              "-fx-background-radius: 6; " +
                              "-fx-border-radius: 6;");
        imageContainer.setPadding(new Insets(4));

        ImageView imageView = new ImageView();
        try {
            String imageUrl = recipe.getImage_url();
            System.out.println("Loading image: " + imageUrl);
            Image image = new Image(new File(imageUrl).toURI().toString());
            imageView.setImage(image);
        } catch (Exception e) {
            System.out.println("Failed to load image for: " + recipe.getTitle());
            e.printStackTrace();
            try {
                imageView.setImage(new Image("file:./data/default-recipe.jpg"));
            } catch (Exception ex) {
                System.out.println("Failed to load default image");
                ex.printStackTrace();
            }
        }
        imageView.setFitWidth(180);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        
        imageContainer.getChildren().add(imageView);
        
        Label titleLabel = new Label(recipe.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-padding: 4 0 2 0;");
        
        HBox timeBox = new HBox(4);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        Label timeIcon = new Label("⏱");
        Label timeLabel = new Label(recipe.getCooking_time());
        timeLabel.setTextFill(Color.GRAY);
        timeBox.getChildren().addAll(timeIcon, timeLabel);
        
        card.getChildren().addAll(imageContainer, titleLabel, timeBox);
        
        card.setOnMouseClicked(e -> {
            RecipeDetailView detailView = new RecipeDetailView();
            detailView.showRecipe(recipe);
        });
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: white; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 0); " +
                         "-fx-background-radius: 8; " +
                         "-fx-border-radius: 8; " +
                         "-fx-border-color: #d0d0d0; " +
                         "-fx-border-width: 1; " +
                         "-fx-cursor: hand;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 0); " +
                         "-fx-background-radius: 8; " +
                         "-fx-border-radius: 8; " +
                         "-fx-border-color: #e0e0e0; " +
                         "-fx-border-width: 1;");
        });
        
        return card;
    }
    
    private List<Recipe> getRandomRecipes(int count) {
        List<Recipe> allRecipes = dataManager.getRecipes();
        if (allRecipes.isEmpty()) {
            System.out.println("No recipes loaded from DataManager");
            return new ArrayList<>();
        }
        
        List<Recipe> randomRecipes = new ArrayList<>(allRecipes);
        Collections.shuffle(randomRecipes);
        
        int actualCount = Math.min(count, randomRecipes.size());
        return randomRecipes.subList(0, actualCount);
    }
}
