package edu.northeastern.csye6200;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private BorderPane root;
    private MenuBar menuBar;

    @Override
    public void start(Stage primaryStage) {
        try {
            root = new BorderPane();
            menuBar = createMenuBar();
            root.setTop(menuBar);

            MainPageView mainPageView = new MainPageView();
            root.setCenter(mainPageView);

            Scene scene = new Scene(root, 1000, 800);
            primaryStage.setTitle("CSYE6200 - Recipe Management System");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Home menu
        Menu homeMenu = new Menu("Home");
        MenuItem homeItem = new MenuItem("Go to Home");
        homeItem.setOnAction(e -> handleHome());
        homeMenu.getItems().add(homeItem);

        // Search menu
        Menu searchMenu = new Menu("Search");
        MenuItem titleSearchItem = new MenuItem("Title Search");
        MenuItem ingredientSearchItem = new MenuItem("Ingredient Search");
        titleSearchItem.setOnAction(e -> handleTitleSearch());
        ingredientSearchItem.setOnAction(e -> handleIngredientSearch());
        searchMenu.getItems().addAll(titleSearchItem, ingredientSearchItem);

        // Import menu
        Menu importMenu = new Menu("Import");
        MenuItem importMenuItem = new MenuItem("Import");
        importMenuItem.setOnAction(e -> handleImport());
        importMenu.getItems().add(importMenuItem);

        // Add menus to the menu bar
        menuBar.getMenus().addAll(homeMenu, searchMenu, importMenu);

        return menuBar;
    }

    private void handleHome() {
        MainPageView mainPageView = new MainPageView();
        root.setCenter(mainPageView);
    }

    private void handleTitleSearch() {
        RecipSearchView searchView = new RecipSearchView();
        root.setCenter(searchView);
    }

    private void handleIngredientSearch() {
        IngredientSearchView searchView = new IngredientSearchView();
        root.setCenter(searchView);
    }

    private void handleImport() {
        RecipeImport recipeImport = new RecipeImport();
        root.setCenter(recipeImport);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
