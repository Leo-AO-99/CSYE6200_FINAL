package test.gui;

import edu.northeastern.csye6200.utility.DataManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.util.Random;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;

public class IngredientSearch extends Application {
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

        System.out.println("Warning: Could not find non-overlapping position after " + maxAttempts + " attempts");
        return new double[]{
            random.nextDouble() * (PANE_WIDTH - BUTTON_WIDTH),
            random.nextDouble() * (PANE_HEIGHT - BUTTON_HEIGHT)
        };
    }

    @Override
    public void start(Stage primaryStage) {
        dataManager = new DataManager();
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        
        HBox topArea = new HBox(10);
        topArea.setAlignment(Pos.CENTER_LEFT);
        
        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("search-button");
        
        selectedIngredientsLabel = new Label("Choose ingredients:");
        selectedIngredientsLabel.setWrapText(true);
        
        HBox.setHgrow(selectedIngredientsLabel, javafx.scene.layout.Priority.ALWAYS);
        
        topArea.getChildren().addAll(searchButton, selectedIngredientsLabel);
        
        AnchorPane ingredientsPane = new AnchorPane();
        ingredientsPane.setMinSize(PANE_WIDTH, PANE_HEIGHT);
        ingredientsPane.setPrefSize(PANE_WIDTH, PANE_HEIGHT);
        
        Set<String> ingredients = dataManager.getIngredientsName();
        for (String ingredient : ingredients) {
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
            
            ingredientButton.setOnAction(e -> {
                updateSelectedIngredients(ingredientButton);
            });

            double[] position = findValidPosition(ingredientsPane);
            AnchorPane.setLeftAnchor(ingredientButton, position[0]);
            AnchorPane.setTopAnchor(ingredientButton, position[1]);
            
            ingredientsPane.getChildren().add(ingredientButton);
        }
        
        ScrollPane scrollPane = new ScrollPane(ingredientsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        
        root.getChildren().addAll(topArea, scrollPane);
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("search.css").toExternalForm());
        
        primaryStage.setTitle("Ingredient Search");
        primaryStage.setScene(scene);
        primaryStage.show();
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

    public static void main(String[] args) {
        launch(args);
    }
}
