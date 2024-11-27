package edu.northeastern.csye6200;

import edu.northeastern.csye6200.utility.DataManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import edu.northeastern.csye6200.entity.Recipe;
import edu.northeastern.csye6200.entity.Ingredient;

public class IngredientSearchView extends VBox {
    private DataManager dataManager;
    private Random random = new Random();
    private Label selectedIngredientsLabel;
    private static final String IMAGE_PATH = "data/image/ingredient/";
    private static final double BUTTON_WIDTH = 120;
    private static final double BUTTON_HEIGHT = 60;
    private static final double PANE_WIDTH = 800;
    private static final double PANE_HEIGHT = 600;
    private static final double MIN_SPACING = 20;

    private Map<ToggleButton, Integer> selectedOrder = new LinkedHashMap<>();
    private int selectionCounter = 0;
    private List<Recipe> searchResults = new ArrayList<>();

    public IngredientSearchView() {
        dataManager = new DataManager();
        
        this.setSpacing(10);
        this.setPadding(new Insets(15));
        
        HBox topArea = createTopArea();
        
        ScrollPane scrollPane = createContentArea();
        
        this.getStylesheets().add(getClass().getResource("search-view.css").toExternalForm());
        
        this.getChildren().addAll(topArea, scrollPane);
    }

    private HBox createTopArea() {
        HBox topArea = new HBox(10);
        topArea.setAlignment(Pos.CENTER_LEFT);
        
        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("search-button");
        
        ToggleButton strictButton = new ToggleButton("Strict");
        strictButton.setSelected(true);
        ToggleButton looseButton = new ToggleButton("Loose");
        
        searchButton.setOnAction(e -> {
            List<String> selectedIngredients = selectedOrder.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> {
                    VBox content = (VBox) entry.getKey().getGraphic();
                    return ((Label) content.getChildren().get(1)).getText();
                })
                .collect(Collectors.toList());

            if (selectedIngredients.isEmpty()) {
                System.out.println("No ingredients selected");
                return;
            }

            boolean isStrictMode = strictButton.isSelected();
            searchResults = searchRecipes(selectedIngredients, isStrictMode);
            
            System.out.println("Search Mode: " + (isStrictMode ? "Strict" : "Loose"));
            
            System.out.println("Selected Ingredients: " + String.join(", ", selectedIngredients));
            
            if (searchResults.isEmpty()) {
                System.out.println("No recipes found with selected ingredients");
            } else {
                System.out.println("\nFound Recipes:");
                for (Recipe recipe : searchResults) {
                    System.out.println("- " + recipe.getTitle());
                }
            }
            
            showSearchResults();
        });
        
        strictButton.setOnAction(e -> {
            if(looseButton.isSelected()) looseButton.setSelected(false);
            if(!strictButton.isSelected() && !looseButton.isSelected()) {
                strictButton.setSelected(true);
            }
        });
        
        looseButton.setOnAction(e -> {
            if(strictButton.isSelected()) strictButton.setSelected(false);
            if(!strictButton.isSelected() && !looseButton.isSelected()) {
                looseButton.setSelected(true);
            }
        });
        
        selectedIngredientsLabel = new Label("Choose ingredients:");
        selectedIngredientsLabel.setWrapText(true);
        
        HBox.setHgrow(selectedIngredientsLabel, javafx.scene.layout.Priority.ALWAYS);
        
        topArea.getChildren().addAll(searchButton, strictButton, looseButton, selectedIngredientsLabel);
        return topArea;
    }

    private ScrollPane createContentArea() {
        AnchorPane ingredientsPane = new AnchorPane();
        ingredientsPane.setMinSize(PANE_WIDTH, PANE_HEIGHT);
        ingredientsPane.setPrefSize(PANE_WIDTH, PANE_HEIGHT);
        
        Set<String> ingredients = dataManager.getIngredientsName();
        for (String ingredient : ingredients) {
            ToggleButton ingredientButton = createIngredientButton(ingredient);
            double[] position = findValidPosition(ingredientsPane);
            AnchorPane.setLeftAnchor(ingredientButton, position[0]);
            AnchorPane.setTopAnchor(ingredientButton, position[1]);
            ingredientsPane.getChildren().add(ingredientButton);
        }
        
        ScrollPane scrollPane = new ScrollPane(ingredientsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        return scrollPane;
    }

    private ToggleButton createIngredientButton(String ingredient) {
        ToggleButton ingredientButton = new ToggleButton();
        ingredientButton.getStyleClass().add("ingredient-bubble");
        ingredientButton.getStyleClass().add("color-" + (random.nextInt(10) + 1));
        
        VBox buttonContent = new VBox(8);
        buttonContent.setAlignment(Pos.CENTER);
        
        ImageView imageView = loadIngredientIcon(ingredient);
        
        Label textLabel = new Label(ingredient);
        textLabel.setStyle("-fx-font-size: 13px;");
        
        buttonContent.getChildren().addAll(imageView, textLabel);
        ingredientButton.setGraphic(buttonContent);
        
        ingredientButton.setOnAction(e -> updateSelectedIngredients(ingredientButton));
        
        return ingredientButton;
    }

    private boolean isOverlapping(double x, double y, AnchorPane pane) {
        double checkWidth = BUTTON_WIDTH + MIN_SPACING;
        double checkHeight = BUTTON_HEIGHT + MIN_SPACING;
        
        for (javafx.scene.Node node : pane.getChildren()) {
            double nodeX = AnchorPane.getLeftAnchor(node);
            double nodeY = AnchorPane.getTopAnchor(node);
            
            boolean xOverlap = Math.abs(x - nodeX) < checkWidth;
            boolean yOverlap = Math.abs(y - nodeY) < checkHeight;
            
            if (xOverlap && yOverlap) {
                return true;
            }
        }
        return false;
    }

    private double[] findValidPosition(AnchorPane pane) {
        int maxAttempts = 100;
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            double x = random.nextDouble() * (PANE_WIDTH - BUTTON_WIDTH);
            double y = random.nextDouble() * (PANE_HEIGHT - BUTTON_HEIGHT);
            
            x = Math.max(MIN_SPACING, Math.min(x, PANE_WIDTH - BUTTON_WIDTH - MIN_SPACING));
            y = Math.max(MIN_SPACING, Math.min(y, PANE_HEIGHT - BUTTON_HEIGHT - MIN_SPACING));
            
            if (!isOverlapping(x, y, pane)) {
                return new double[]{x, y};
            }
            attempts++;
        }

        return new double[]{
            random.nextDouble() * (PANE_WIDTH - BUTTON_WIDTH),
            random.nextDouble() * (PANE_HEIGHT - BUTTON_HEIGHT)
        };
    }

    private ImageView loadIngredientIcon(String ingredient) {
        String iconPath = IMAGE_PATH + ingredient + ".png";
        File iconFile = new File(iconPath);
        
        if (!iconFile.exists()) {
            iconPath = IMAGE_PATH + "Unknown.png";
        }
        
        try {
            Image image = new Image(new File(iconPath).toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(32);
            imageView.setFitHeight(32);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            return imageView;
        } catch (Exception e) {
            System.err.println("Error loading icon for: " + ingredient);
            try {
                ImageView defaultView = new ImageView(new Image(new File(IMAGE_PATH + "Unknown.png").toURI().toString()));
                defaultView.setFitWidth(32);
                defaultView.setFitHeight(32);
                defaultView.setPreserveRatio(true);
                defaultView.setSmooth(true);
                return defaultView;
            } catch (Exception ex) {
                System.err.println("Error loading default icon");
                return new ImageView();
            }
        }
    }

    private void updateSelectedIngredients(ToggleButton button) {
        if (button.isSelected()) {
            selectedOrder.put(button, selectionCounter++);
        } else {
            selectedOrder.remove(button);
        }
        
        HBox selectedItemsBox = new HBox(10);
        selectedItemsBox.setAlignment(Pos.CENTER_LEFT);

        Label prefixLabel = new Label("Choose ingredients:");
        selectedItemsBox.getChildren().add(prefixLabel);

        selectedOrder.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .forEach(entry -> {
                ToggleButton tb = entry.getKey();
                    
                HBox itemBox = new HBox(5);
                itemBox.setAlignment(Pos.CENTER_LEFT);
                
                Label hashLabel = new Label("#");
                
                VBox content = (VBox) tb.getGraphic();
                ImageView originalIcon = (ImageView) content.getChildren().get(0);
                Label originalLabel = (Label) content.getChildren().get(1);
                
                ImageView iconCopy = new ImageView(originalIcon.getImage());
                iconCopy.setFitWidth(16);
                iconCopy.setFitHeight(16);
                iconCopy.setPreserveRatio(true);
                iconCopy.setSmooth(true);
                
                Label nameLabel = new Label(originalLabel.getText());
                
                itemBox.getChildren().addAll(hashLabel, iconCopy, nameLabel);
                
                selectedItemsBox.getChildren().add(itemBox);
            });
        
        selectedIngredientsLabel.setGraphic(selectedItemsBox);
        selectedIngredientsLabel.setText("");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSearchResults() {
        if (searchResults.isEmpty()) {
            showAlert("No recipes found with selected ingredients");
            return;
        }

        Stage resultStage = new Stage();
        resultStage.setTitle("Search Results");

        VBox resultBox = new VBox(15);
        resultBox.setPadding(new Insets(20));
        resultBox.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("Found Recipes:");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        resultBox.getChildren().add(titleLabel);

        for (Recipe recipe : searchResults) {
            HBox recipeRow = new HBox(15);
            recipeRow.setPadding(new Insets(10));
            recipeRow.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5;");

            ImageView recipeImage = new ImageView();
            String imagePath = recipe.getImage_url();
            System.out.println("Image path: " + imagePath);
            try {
                Image image = new Image(new File(imagePath).toURI().toString());
                recipeImage.setImage(image);
            } catch (Exception e) {
                try {
                    Image defaultImage = new Image(new File("data/image/recipe/default.jpg").toURI().toString());
                    recipeImage.setImage(defaultImage);
                } catch (Exception ex) {
                    System.err.println("Error loading default recipe image");
                }
            }
            recipeImage.setFitWidth(100);
            recipeImage.setFitHeight(100);
            recipeImage.setPreserveRatio(true);

            VBox infoBox = new VBox(10);
            infoBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(infoBox, javafx.scene.layout.Priority.ALWAYS);

            Label recipeTitleLabel = new Label(recipe.getTitle());
            recipeTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            recipeTitleLabel.setWrapText(true);

            List<String> recipeIngredients = new ArrayList<>();
            for (Ingredient ingredient : dataManager.getIngredients()) {
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

            resultBox.getChildren().add(recipeRow);
        }

        ScrollPane scrollPane = new ScrollPane(resultBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(600, 500);
        scrollPane.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(scrollPane);
        resultStage.setScene(scene);
        resultStage.show();
    }

    private List<Recipe> searchRecipes(List<String> ingredients, boolean isStrictMode) {
        if (isStrictMode) {
            return dataManager.ingredientSearchStrict(ingredients);
        } else {
            return dataManager.ingredientSearchLoose(ingredients);
        }
    }
}
