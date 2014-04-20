import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class RecipeParser {
    
    private String fileName; //name of file you're parsing
    
    public RecipeParser(String fileName) {
        this.fileName = fileName;
    }
    
	// returns a list of recipes from the file
    public ArrayList<Recipe> readRecipes() {
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        try {
            File file = new File(fileName);  
            Scanner fs = new Scanner(file); 
            while (fs.hasNext()) {
                String title = getTagContents(fs, "recipe-title");
                String content = getTagContents(fs, "recipe-content");
                recipeList.add(new Recipe(title, content));
            }
			fs.close();
            return recipeList;
        } catch (Exception e) {
            return recipeList;
        }
    }
	
	// extracts the contents within given tag using the given scanner
	private String getTagContents(Scanner fs, String tag) {
		String content = "";
		fs.findWithinHorizon("<" + tag + ">", 0);
		boolean keepScanning = true;
		while (keepScanning) {
			String nextString = fs.next();
			if (!nextString.equals("</"+ tag + ">")) {
				if (!content.equals("")) {
					content+= " ";
				}
				content += nextString;
			} else {
				keepScanning = false;
			}
		}
		return content;
	}
    
	// writes given recipe to end of file
    public void addRecipe(Recipe recipe) {
        String source = "\n<recipe-title> " + recipe.getTitle() + " </recipe-title> " +
        "<recipe-content> " + recipe.getContent() + " </recipe-content>";
        writeToFile(source, true);
    }
    
	// deletes given recipe from file
	// extracts entire contents of file and saves contents to string
	// removes given recipe from string
	// writes content string back to file
    public void deleteRecipe(Recipe recipe) {
        String title = recipe.getTitle();
        String content = recipe.getContent();
		String lineToBeDeleted = "<recipe-title>(\\s*)" + title + 
		"(\\s*)</recipe-title>(\\s*)<recipe-content>(\\s*)" + content + "(\\s*)</recipe-content>";
		String source = scanWholeFile();
		source = source.replaceFirst(lineToBeDeleted, "");
		writeToFile(source, false);

    }
	
	// Replaces old version of recipe with new version
	public void editRecipe(Recipe newRecipe, Recipe oldRecipe) {
		String oldLine = "<recipe-title>(\\s*)" + oldRecipe.getTitle() + 
			"(\\s*)</recipe-title>(\\s*)<recipe-content>(\\s*)" + oldRecipe.getContent() + "(\\s*)</recipe-content>";
		String newLine = "\n<recipe-title> " + newRecipe.getTitle() + " </recipe-title> " +
			"<recipe-content> " + newRecipe.getContent() + " </recipe-content>";
		String source = scanWholeFile();
		source = source.replaceFirst(oldLine, newLine);
		writeToFile(source, false);
    }
	
	// scans the whole source file into a string and returns that string
	private String scanWholeFile() {
		try {
			File file = new File(fileName);
			Scanner fs = new Scanner(file);
			String source = "";
			while (fs.hasNext()) {
				source += fs.next() + " ";
			}
			fs.close();
			return source;
		}
		catch (Exception e) {
			return "";
		}
	}
	
	// writes the given source string to the file
	// if append is true, it will append the source to the end of the file
	// if false, it replaces the whole file
	private void writeToFile(String source, boolean append) {
		try {
			FileWriter f = new FileWriter(fileName, append);
			char buffer[] = new char[source.length()];
			source.getChars(0, source.length(), buffer, 0);
			for (int i=0; i<buffer.length; i++) {
				f.write(buffer[i]);
			}
			f.close();
		} catch (Exception e) {
		}
	}
    
}