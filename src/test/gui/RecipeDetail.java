package test.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.csye6200.entity.Ingredient;
import edu.northeastern.csye6200.entity.Recipe;
import edu.northeastern.csye6200.utility.DataManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecipeDetail extends Application{
	
	private Recipe recipe;
	private List<Ingredient>ingredients=new ArrayList<>();
	
	@FXML
	private Label LabelRecipe;
	
	@FXML
	private ImageView ImageRecipe;
	
	@FXML
	private GridPane GridIngredients;
	
	@FXML
	private ScrollPane PaneSteps;
	
	public void setData(int recipeID) {
		DataManager dm=new DataManager();
		this.recipe=dm.getRecipeById(recipeID);
		for (Ingredient i:dm.getIngredients()) {
			if (i.getRecipe_id()==recipeID)
				this.ingredients.add(i);
		}
		
		LabelRecipe.setText(recipe.getTitle());
		ImageRecipe.setImage(new Image(new File(recipe.getImage_url()).toURI().toString()));
		ImageRecipe.setFitHeight(400);
		ImageRecipe.setFitWidth(400);
		
		ColumnConstraints col0 = new ColumnConstraints();
		col0.setPrefWidth(30);  
		col0.setHalignment(HPos.CENTER);  

		GridIngredients.getColumnConstraints().add(col0);
		
		for (int num=0;num<ingredients.size();num++) {
			Ingredient i=ingredients.get(num);
			ImageView ingredientImage=new ImageView(new Image(new File("data/image/ingredient/"+i.getIngredient_name()+".png").toURI().toString()));
			ingredientImage.setFitWidth(30);
		    ingredientImage.setFitHeight(30);
		    ingredientImage.setPreserveRatio(true);  
			GridIngredients.add(ingredientImage,0,num);
			GridIngredients.add(new Label(i.getIngredient_name()),1,num);
			GridIngredients.add(new Label(i.getQuantity()),2,num);
			GridIngredients.add(new Label(i.getUnit()),3,num);
		}
		
		VBox steps=new VBox(5);
		steps.getChildren().add(new Label(recipe.getProcess()));
		PaneSteps.setContent(steps);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader= new FXMLLoader(getClass().getResource("RecipeDetail.fxml"));
		Parent root=loader.load();
		RecipeDetail controller=loader.getController();
		controller.setData(1);

 
		Scene scene = new Scene(root);
		primaryStage.setTitle("MySceneBuilderApp");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args){
		launch(args);
	}
}

