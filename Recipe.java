import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class Recipe {

	private String title;
	private String content;
	
	public Recipe(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String value) {
		title = value;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String value) {
		content = value;
	}
	
	public String toString() {
		return "Recipe | title: " + title + " content: " + content;
	}
	
	public boolean equals(Recipe recipe) {
		return recipe.getTitle().equals(title) && recipe.getContent().equals(content);
	}
}