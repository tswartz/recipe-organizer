import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
	
	public static void main(String[] args) {
		
		
		JFrame frame = new JFrame("Recipe Organizer");
		frame.getContentPane().add(new RecipeOrganizer(), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}