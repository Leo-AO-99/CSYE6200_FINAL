package edu.northeastern.csye6200.entity;

public class Ingredient extends Base {
    private static int serial_id = 1;
    private int recipe_id;
    private String ingredient_name;
    private String quantity;
    private String unit;

    public Ingredient(int recipe_id, String ingredient_name, String quantity, String unit) {
        this.id = serial_id++;
        this.recipe_id = recipe_id;
        this.ingredient_name = ingredient_name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Ingredient(int id, int recipe_id, String ingredient_name, String quantity, String unit) {
        serial_id = Math.max(id + 1, serial_id);
        this.id = id;
        this.recipe_id = recipe_id;
        this.ingredient_name = ingredient_name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
