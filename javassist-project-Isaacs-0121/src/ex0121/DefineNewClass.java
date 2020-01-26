package ex0121;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class DefineNewClass {
	static final String SEP = File.separator;
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + SEP + "output";
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		ArrayList<String> classes = new ArrayList<String>();
		String selectedSuper = "";

		getInput(classes);

		for (String clazz : classes) {
			if (clazz.startsWith("Common") && (clazz.length() > selectedSuper.length())) {
				selectedSuper = clazz;
			}
		}

		// If a Common class wasn't found, use first argument.
		if (selectedSuper.isEmpty()) {
			System.out.println("A Common class wasn't found; using first argument.");
			selectedSuper = classes.get(0);
		}
		System.out.println("[DBG] " + selectedSuper + " has been determined to be a suitable SuperClass.");	

		try {
			ClassPool pool = ClassPool.getDefault();
			boolean useRuntimeClass = false;

			// Create the classes.
			for (String arg : classes) {
				makeClass(pool, arg);
			}
			
			// Remove the selectedSuper from the list of classes.
			classes.remove(selectedSuper);

			if (useRuntimeClass) {
				insertClassPathRunTimeClass(pool, classes);
			} else {
				insertClassPath(pool);
			}

			for (String arg : classes) {
				CtClass cc = pool.get(arg);
				setSuperClass(cc, selectedSuper, pool);
				cc.writeFile(outputDir);
				System.out.println("[DBG] Write output to: " + outputDir);
			}

		} catch (NotFoundException | CannotCompileException | IOException | InstantiationException
				| IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Insert Class Path with Class loader.
	 */
	static void insertClassPathRunTimeClass(ClassPool pool, List<String> classes)
			throws NotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		for (String arg : classes) {
			Class<?> subclass = Class.forName(arg);
			Object subclassInstace = subclass.newInstance();
			ClassClassPath classPath = new ClassClassPath(subclassInstace.getClass());
			pool.insertClassPath(classPath);
			System.out.println("[DBG] Insert classpath from insertClassPathRunTimeClass: " + classPath.toString());
		}
	}

	/*
	 * Insert ClassPath as String
	 */
	static void insertClassPath(ClassPool pool) throws NotFoundException {
		String strClassPath = workDir + SEP + "output";
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] Insert classpath from insertClassPath: " + strClassPath);
	}

	/*
	 * Initialize SuperClass relationships.
	 */
	static void setSuperClass(CtClass cc, String clazz, ClassPool pool) throws NotFoundException, CannotCompileException {
		cc.defrost();
		cc.setSuperclass(pool.get(clazz));
		System.out.println("[DBG] Set superclass: " + cc.getSuperclass().getName() + ", subclass: " + cc.getName());
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
