package ex0130;

import java.util.Scanner;

public class CustomClassLoader extends java.lang.ClassLoader {
	Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		String[] classAndField = new String[2];
		
		// Find class
		Class<?> foundClass = new CustomClassLoader().findClass(classAndField[0]);
		
		// Add field

	}

	/*
	 * Find class matching given String class name.
	 */
	@Override
	public Class findClass(String name) {
		return null;
		
	}
	
	/*
	 * Add a field to given class.
	 */
	Class addField(String field, Class<?> clazz) {
		return clazz;
		
	}
	
	void getInput(String[] classAndField) {
		System.out.println("Enter a class name: ");
		classAndField[0] = input.nextLine().trim();
		
		System.out.println("Enter a field name: ");
		classAndField[1] = input.nextLine().trim();
		
		for(String arg : classAndField) {
			if (arg.isEmpty()) {
				getInput(classAndField);
			}
		}
	}

}
