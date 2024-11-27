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
            
            Scene scene = new Scene(root, 1000, 800);
            primaryStage.setTitle("CSYE6200 - Recipe Management System");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        
        // 创建Search菜单及其子菜单项
        Menu searchMenu = new Menu("Search");
        MenuItem titleSearchItem = new MenuItem("Title Search");
        MenuItem ingredientSearchItem = new MenuItem("Ingredient Search");
        searchMenu.getItems().addAll(titleSearchItem, ingredientSearchItem);
        
        // 创建Import菜单
        Menu importMenu = new Menu("Import");
        MenuItem importMenuItem = new MenuItem("Import");
        importMenu.getItems().add(importMenuItem);
        
        // 添加事件处理
        titleSearchItem.setOnAction(e -> handleTitleSearch());
        ingredientSearchItem.setOnAction(e -> handleIngredientSearch());
        importMenuItem.setOnAction(e -> handleImport());
        
        // 将菜单添加到菜单栏
        menuBar.getMenus().addAll(searchMenu, importMenu);
        
        return menuBar;
    }
    
    private void handleTitleSearch() {
        System.out.println("Title search clicked");
    }
    
    private void handleIngredientSearch() {
        // 创建配料搜索视图
        IngredientSearchView searchView = new IngredientSearchView();
        // 将搜索视图设置到主界面的中心
        root.setCenter(searchView);
    }
    
    private void handleImport() {
        System.out.println("Import clicked");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}