package edu.northeastern.csye6200;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class RecipeImport extends VBox{

	private String[] answers = new String[5];
    private String[] questions = new String[5];
    
    public RecipeImport() {
    	setSpacing(10);
        setPadding(new Insets(15));
    	
    	questions[0] = "What is the name of the recipe?";
    	questions[1] = "What is the ingredient of the recipe?";
    	questions[2] = "What is the url of the image of the recipe?";
    	questions[3] = "Please describe the process of the recipe.";
    	questions[4] = "What is the cooking time of the recipe?";
    	
        VBox mainVBox = new VBox(10);
        mainVBox.setPadding(new javafx.geometry.Insets(10));
        
        for (int i = 0; i < 5; i++) {
            VBox questionVBox = new VBox(5);
            Label questionLabel = new Label(questions[i]);
            TextField answerField = new TextField();
            answerField.setPromptText("Enter your answer here");
            final int index = i;
            answerField.textProperty().addListener((observable, oldValue, newValue) -> {
                answers[index] = newValue; 
            });
            
            getChildren().addAll(questionLabel, answerField);
            getChildren().add(questionVBox);
        }

        Button submitButton = new Button("Submit Answers");
        submitButton.setOnAction(e -> {
            System.out.println("User's Answers:");
            for (int i = 0; i < answers.length; i++) {
                System.out.println("Answer to Question " + (i + 1) + ": " + answers[i]);
            }
        });
        getChildren().add(submitButton);
    }
    public String[] getAnswers() {
    	return answers;
    }
}
