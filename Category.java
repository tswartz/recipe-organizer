import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class Category {

	public ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
	
	public ArrayList<Recipe> getRecipeList() {
		return recipeList;
	}
	
	public void setRecipeList(ArrayList<Recipe> value) {
		recipeList = value;
	}
	
	// returns true if recipe title is already in recipe list
	public boolean hasRecipeTitle(String title) {
		for (int i=0; i<recipeList.size(); i++) {
			if (recipeList.get(i).getTitle().equals(title)) {
				return true;
			} 
		}
		return false;
	}
	
	/*
	public void addRecipe(Recipe r) {
		recipeList.add(r);
	}
	
	public void deleteRecipe(Recipe r) {
		recipeList.remove(r);
	}
	
	public void editRecipe(Recipe newRecipe, Recipe oldRecipe) {
		int index = recipeList.indexOf(oldRecipe);
		ArrayList<Recipe> newRecipeList = new ArrayList<Recipe>();
		for (int i=0; i<recipeList.size(); i++) {
			Recipe recipe = recipeList.get(i);
			if (recipe.equals(oldRecipe)) {
				recipe = newRecipe;
			}
			newRecipeList.add(recipe);
		}
		recipeList = newRecipeList;
	}
	 */
	
	public String lookUpRecipe(String title) {
		if (recipeList.isEmpty()) {
			return "";
		} else {
			for (int i=0; i<recipeList.size(); i++) {
				Recipe recipe = recipeList.get(i);
				if (recipe.getTitle().equals(title)) {
					return recipe.getContent();
				}
			}
			return "No recipe found";
		}
	}
}