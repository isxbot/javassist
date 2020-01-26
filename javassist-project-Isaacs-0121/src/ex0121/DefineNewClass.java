package ex0121;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

public class DefineNewClass {
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + File.separator + "output";
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		ClassPool pool = ClassPool.getDefault();
		ArrayList<String> classes = new ArrayList<String>();
		String selectedSuper = "";

		getInput(classes);

		// TODO Check for common classes
		for (String clazz : classes) {
			if (clazz.startsWith("Common") && (clazz.length() > selectedSuper.length())) {
				selectedSuper = clazz;
			}
		}
		// TODO Find longest common class, or select first argument.

		// TODO Set superclass

		for (String arg : classes) {
			makeClass(pool, arg);
		}

	}

	/*
	 * Create new class from String.
	 */
	static CtClass makeClass(ClassPool pool, String newClass) {
		CtClass cc = pool.makeClass(newClass);
		try {
			cc.writeFile(outputDir);
		} catch (CannotCompileException | IOException e) {
			System.out.println("Could not create output dir: " + e);
		}
		return cc;
	}

	/*
	 * Take input from user, assign to ArrayList. Only accept 3 arguments.
	 */
	static void getInput(ArrayList<String> classes) {
		String line;

		for (int i = 0; i < 3; i++) {
			System.out.print("Enter a class name: ");
			line = input.nextLine();
			if (!line.isEmpty()) {
				classes.add(line);
			}
		}

		if (classes.size() != 3) {
			System.out.println("[WRN] Invalid Input");
			classes.clear();
			classes.trimToSize();
			getInput(classes);
		}
	}

}
