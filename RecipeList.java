import java.util.*;
import java.io.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class RecipeList extends JPanel {
	
	private JList list;
	private DefaultListModel listModel;
	private JTextArea content = new JTextArea(20, 25);
	private Category c;
	private ListSelectionModel listSelectionModel;
	private JOptionPane optionPane = new JOptionPane();
	private JFrame frame = new JFrame();

	public RecipeList(String category) {
        super(new BorderLayout());
		
		content.setLineWrap(true);
		content.addInputMethodListener(new TextAreaContentChangedHandler());
		// initialize saved list of recipes in given category
		listModel = new DefaultListModel();
		ArrayList<Recipe> recipes;
		RecipeParser parser = new RecipeParser(category + ".txt");
		c = new Category();
		c.setRecipeList(parser.readRecipes());
		recipes = c.getRecipeList();
		for (int i=0; i<recipes.size(); i++) {
			listModel.addElement(recipes.get(i).getTitle());
		}
		
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
		listSelectionModel = list.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        JScrollPane listScrollPane = new JScrollPane(list);
		
		
        //Create a panel that uses BoxLayout.
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new AddListener(parser));
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(new EditListener(parser));
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new DeleteListener(parser));
		JButton moveButton = new JButton("Move");
		moveButton.addActionListener(new MoveListener(parser));
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(addButton);
		buttonPane.add(editButton);
		buttonPane.add(deleteButton);
		buttonPane.add(moveButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel buttonAndListPanel = new JPanel();
		buttonAndListPanel.setLayout(new BoxLayout(buttonAndListPanel, BoxLayout.Y_AXIS));
		buttonAndListPanel.add(listScrollPane);
		buttonAndListPanel.add(buttonPane);
		
		JScrollPane recipeContentPane = new JScrollPane(content);
		
		// main pane for the side scroll bar and the text content pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
								   buttonAndListPanel, recipeContentPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(350);
		
		//Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		buttonAndListPanel.setMinimumSize(minimumSize);
		recipeContentPane.setMinimumSize(minimumSize);
		
        add(splitPane, BorderLayout.CENTER);
    }
	
	class AddListener implements ActionListener {
		
		RecipeParser parser;
		
		public AddListener(RecipeParser parser) {
			this.parser = parser;
		}
		
		public void actionPerformed(ActionEvent e) {
			String title = (String)optionPane.showInputDialog(frame, "Type the title of the recipe", "Add a recipe - Title", JOptionPane.PLAIN_MESSAGE, null, null, "");
			if (title != null) {
				while (title != null && c.hasRecipeTitle(title)) {
					title = (String)optionPane.showInputDialog(frame, "Recipe title is already used. Choose another title", "Add a recipe - Title", JOptionPane.PLAIN_MESSAGE, null, null, "");
				}
				if (title != null) {
					String content = (String)optionPane.showInputDialog(frame, "Type the content of the recipe", "Add a recipe - Content", JOptionPane.PLAIN_MESSAGE, null, null, "");
					if (content != null) {
						Recipe newRecipe = new Recipe(title, content);
						//c.addRecipe(newRecipe);
						parser.addRecipe(newRecipe);
						c.setRecipeList(parser.readRecipes());
						listModel.addElement(title);
					}
				}
			}
		}
	}
	
	class EditListener implements ActionListener {
		
		RecipeParser parser;
		
		public EditListener(RecipeParser parser) {
			this.parser = parser;
		}
		
		public void actionPerformed(ActionEvent e) {
			int index = list.getSelectedIndex();
			String title = listModel.getElementAt(index).toString();
			String content = c.lookUpRecipe(title);
			Recipe oldRecipe = new Recipe(title, content);
			title = (String)optionPane.showInputDialog(frame, "Edit the title of the recipe", "Edit recipe - Title", JOptionPane.PLAIN_MESSAGE, null, null, title);
			if (title != null) {
				content = (String)optionPane.showInputDialog(frame, "Edit the content of the recipe", "Edit recipe - Content", JOptionPane.PLAIN_MESSAGE, null, null, content);
				if (content != null) {
					Recipe newRecipe = new Recipe(title, content);
					//c.editRecipe(newRecipe, oldRecipe);
					parser.editRecipe(newRecipe, oldRecipe);
					c.setRecipeList(parser.readRecipes());
					listModel.set(index, title);
					list.setSelectedIndex(0);
					list.setSelectedIndex(index);
				}
			}
		}
	}
	
	class DeleteListener implements ActionListener {
		
		RecipeParser parser;
		
		public DeleteListener(RecipeParser parser) {
			this.parser = parser;
		}
		
		public void actionPerformed(ActionEvent e) {
			int selectedIndex = list.getSelectedIndex();
			String title = listModel.getElementAt(selectedIndex).toString();
			String content = c.lookUpRecipe(title);
			listModel.removeElementAt(selectedIndex);
			Recipe recipeToDelete = new Recipe(title, content);
			//c.deleteRecipe(recipeToDelete);
			parser.deleteRecipe(recipeToDelete);
			c.setRecipeList(parser.readRecipes());
		}
	}
	
	class MoveListener implements ActionListener {
		
		RecipeParser parser;
		
		public MoveListener(RecipeParser parser) {
			this.parser = parser;
		}
		
		public void actionPerformed(ActionEvent e) {
			int index = list.getSelectedIndex();
			String title = listModel.getElementAt(index).toString();
			String content = c.lookUpRecipe(title);
			Recipe recipe = new Recipe(title, content);
			try {
				ArrayList<String> categories = new ArrayList<String>();
				File file = new File("categories.txt");  
				Scanner fs = new Scanner(file);
				while (fs.hasNext()) {
					categories.add(fs.next());
				}
				JFrame frame = new JFrame();
				Object[] categoriesArray = categories.toArray();
				JOptionPane optionPane = new JOptionPane();
				String newCategory = (String)optionPane.showInputDialog(frame, "Where you would like to move \"" + title + "\"?", "Move Recipe", JOptionPane.PLAIN_MESSAGE, null, categoriesArray, categoriesArray[0]);
				RecipeParser newCategoryParser = new RecipeParser(newCategory + ".txt");
				DeleteListener deleteListener = new DeleteListener(parser);
				deleteListener.actionPerformed(null); //deletes recipe from this category
				newCategoryParser.addRecipe(recipe); //adds it to new category
			} catch (Exception ex) {
			}
		}
	}
	
	class SharedListSelectionHandler implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			int firstIndex = e.getFirstIndex();
			if (list.getSelectedIndex() == -1) {
				list.setSelectedIndex(0);
			}
			String title = listModel.getElementAt(list.getSelectedIndex()).toString();
			content.setText(c.lookUpRecipe(title));
			
		}
	}
	
	class TextAreaContentChangedHandler implements InputMethodListener {
		
		public void caretPositionChanged(InputMethodEvent e) {
		}
		
		public void inputMethodTextChanged(InputMethodEvent e) {
			AttributedCharacterIterator text = e.getText();
			String str = "";
			boolean keepIterating = true;
			while (keepIterating) {
				try {
					str += text.next();
				} catch (Exception ex) {
					keepIterating = false;
				}
			}
			System.out.println(str);
		}
		
	}
	
	
}