import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RecipeOrganizer extends JPanel {
	
	static ArrayList<String> categories;
	static JFrame frame = new JFrame("Recipes");
	
	public RecipeOrganizer() {
		
		try {
			categories = new ArrayList<String>();
			File file = new File("categories.txt");  
			Scanner fs = new Scanner(file);
			while (fs.hasNext()) {
				categories.add(fs.next());
			}
		} catch (Exception e) {
		}
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		
		JTabbedPane tabbedPane = showCategories();
		
		JButton addButton = new JButton("Add Category");
		addButton.addActionListener(new AddListener());
		buttonPane.add(addButton);
		
		JButton deleteButton = new JButton("Delete Category");
		deleteButton.addActionListener(new DeleteListener());
		buttonPane.add(deleteButton);
		
		mainPane.add(buttonPane);
		mainPane.add(tabbedPane);
		
		add(mainPane, BorderLayout.CENTER);
		
	}
	
	public static String capitalize(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	class AddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame();
			JOptionPane optionPane = new JOptionPane();
			String categoryToAdd = (String)optionPane.showInputDialog(frame, "Type the name of the category", "Add Category", JOptionPane.PLAIN_MESSAGE, null, null, "");
			if (categoryToAdd != null) {
				categoryToAdd = categoryToAdd.toLowerCase();
				String fileName = categoryToAdd + ".txt";
				try {
					FileOutputStream fos = new FileOutputStream(fileName);
					PrintStream ps = new PrintStream(fos);
					ps.close();
					FileWriter f = new FileWriter("categories.txt", true);
					categoryToAdd = " " + categoryToAdd;
					char buffer[] = new char[categoryToAdd.length()];
					categoryToAdd.getChars(0, categoryToAdd.length(), buffer, 0);
					for (int i=0; i<buffer.length; i++) {
						f.write(buffer[i]);
					}
					f.close();
					update();
					System.out.println("category was added successfully");
				} catch (Exception ex) {
					System.out.println("category could not be created");
				}
			}
		}
	}
	
	class DeleteListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame();
			Object[] categoriesArray = categories.toArray();
			JOptionPane optionPane = new JOptionPane();
			String categoryToDelete = (String)optionPane.showInputDialog(frame, "Choose a category to delete", "Delete Category", JOptionPane.PLAIN_MESSAGE, null, categoriesArray, categoriesArray[0]);
			if (categoryToDelete != null) {
				try {
					File file = new File("categories.txt");  
					Scanner scan = new Scanner(file);  
					scan.useDelimiter("\\Z");  
					String source = scan.next();
					scan.close();
					source = source.replaceFirst(categoryToDelete, "");				
					FileWriter f = new FileWriter("categories.txt", false);
					char buffer[] = new char[source.length()];
					source.getChars(0, source.length(), buffer, 0);
					for (int i=0; i<buffer.length; i++) {
						f.write(buffer[i]);
					}
					f.close();
					update();
					System.out.println("category was deleted successfully");
				} catch (Exception ex) {
					System.out.println(categoryToDelete + " could not be deleted");
				}
				File fileToDelete = new File(categoryToDelete + ".txt");
				fileToDelete.delete();
			}
		}
	}
	
	public static JTabbedPane showCategories() {
		JTabbedPane tabbedPane = new JTabbedPane();
		for (String category : categories) {
			String tabTitle = capitalize(category) + " Recipes";
			JComponent panel = new RecipeList(category);
			tabbedPane.addTab(tabTitle, null, panel, "");
		}
		return tabbedPane;
	}
	
	public static void update() {
		JComponent newContentPane = new RecipeOrganizer();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
		frame.pack();
	}
	
	public static void createAndShowGUI() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JComponent newContentPane = new RecipeOrganizer();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}

}