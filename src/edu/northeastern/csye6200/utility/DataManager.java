package edu.northeastern.csye6200.utility;

import edu.northeastern.csye6200.entity.Recipe;
import edu.northeastern.csye6200.entity.Ingredient;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class DataManager {
    private static String dataPath = "./data/";
    private Map<Integer, Recipe> recipes;
    private Map<Integer, Ingredient> ingredients;

    public DataManager() {
        recipes = new HashMap<>();
        ingredients = new HashMap<>();
        loadRecipes();
        loadIngredients();
    }

    private void loadRecipes() {
        String filePath = dataPath + "recipe.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {    
                String[] parts = line.split(",");
                if(parts.length < 5) continue;
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String imageUrl = parts[2];
                String instructions = parts[3];
                String cookingTime = parts[4];
                recipes.put(id, new Recipe(id, title, imageUrl, instructions, cookingTime));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadIngredients() {
        String filePath = dataPath + "ingredient.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length < 5) continue;
                int id = Integer.parseInt(parts[0]);
                int recipeId = Integer.parseInt(parts[1]);
                String ingredientName = parts[2];
                String quantity = parts[3];
                String unit = parts[4];
                ingredients.put(id, new Ingredient(id, recipeId, ingredientName, quantity, unit));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getRecipes() {
        return new ArrayList<>(recipes.values());
    }

    public List<Ingredient> getIngredients() {
        return new ArrayList<>(ingredients.values());
    }

    public Set<String> getIngredientsName() {
        Set<String> names = new HashSet<>();
        for (Ingredient ingredient : ingredients.values()) {
            names.add(ingredient.getIngredient_name());
        }
        return names;
    }

    public Recipe getRecipeById(int id) {
        return recipes.get(id);
    }

    public List<Recipe> getRecipeByName(String name) {
        List<Recipe> matchedRecipes = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            return matchedRecipes;
        }
        
        String searchTerm = name.toLowerCase().trim();
        
        for (Recipe recipe : recipes.values()) {
            String recipeTitle = recipe.getTitle().toLowerCase();

            if (recipeTitle.contains(searchTerm) ||
                recipeTitle.equals(searchTerm) ||
                recipeTitle.startsWith(searchTerm)) {
                matchedRecipes.add(recipe);
            }
        }
        
        return matchedRecipes;
    }

    public List<Recipe> ingredientSearchStrict(List<String> ingredients) {
        List<Recipe> result = new ArrayList<>();
        
        // Iterate through all recipes
        for (Recipe recipe : recipes.values()) {
            boolean hasAllIngredients = true;
            List<String> recipeIngredients = new ArrayList<>();
            
            // Get all ingredients for current recipe
            for (Ingredient ingredient : this.ingredients.values()) {
                if (ingredient.getRecipe_id() == recipe.getId()) {
                    recipeIngredients.add(ingredient.getIngredient_name().toLowerCase());
                }
            }
            
            // Check if recipe contains all searched ingredients
            for (String searchIngredient : ingredients) {
                if (!recipeIngredients.contains(searchIngredient.toLowerCase())) {
                    hasAllIngredients = false;
                    break;
                }
            }
            
            if (hasAllIngredients) {
                result.add(recipe);
            }
        }
        
        return result;
    }

    public List<Recipe> ingredientSearchLoose(List<String> ingredients) {
        List<Recipe> result = new ArrayList<>();
        
        // Iterate through all recipes
        for (Recipe recipe : recipes.values()) {
            List<String> recipeIngredients = new ArrayList<>();
            
            // Get all ingredients for current recipe
            for (Ingredient ingredient : this.ingredients.values()) {
                if (ingredient.getRecipe_id() == recipe.getId()) {
                    recipeIngredients.add(ingredient.getIngredient_name().toLowerCase());
                }
            }
            
            // Check if recipe contains any of the searched ingredients
            for (String searchIngredient : ingredients) {
                if (recipeIngredients.contains(searchIngredient.toLowerCase())) {
                    result.add(recipe);
                    break;
                }
            }
        }
        
        return result;
    }

    public void close() {
        saveRecipes();
        saveIngredients();
    }

    private void saveRecipes() {
        String filePath = dataPath + "recipe.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header line
            bw.write("id,title,image_url,instructions\n");
            // Sort by id before writing
            List<Recipe> sortedRecipes = new ArrayList<>(recipes.values());
            sortedRecipes.sort((r1, r2) -> Integer.compare(r1.getId(), r2.getId()));
            for (Recipe recipe : sortedRecipes) {
                String line = String.format("%d,%s,%s,%s\n",
                        recipe.getId(),
                        recipe.getTitle(),
                        recipe.getImage_url(),
                        recipe.getProcess());
                bw.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveIngredients() {
        String filePath = dataPath + "ingredient.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header line
            bw.write("id,recipe_id,ingredient_name,quantity,unit\n");
            // Sort by id before writing
            List<Ingredient> sortedIngredients = new ArrayList<>(ingredients.values());
            sortedIngredients.sort((i1, i2) -> Integer.compare(i1.getId(), i2.getId()));
            for (Ingredient ingredient : sortedIngredients) {
                String line = String.format("%d,%d,%s,%s,%s\n",
                        ingredient.getId(),
                        ingredient.getRecipe_id(),
                        ingredient.getIngredient_name(),
                        ingredient.getQuantity(),
                        ingredient.getUnit());
                bw.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
