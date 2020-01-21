package ex0116;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class SetSuperClass {
	static final String SEP = File.separator;
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + SEP + "output";

	public static void main(String[] args) {
		List<String> argsAsList = new ArrayList(Arrays.asList(args));
		HashSet<String> commonSuperClasses = new HashSet<String>();
		String selectedSuperAsString;

		// Check if at least 3 arguments are given, exit if not.
		if (argsAsList.size() < 3) {
			System.exit(0);
		}

		// Check if class names start with "Common", add to set if so.
		for (String arg : argsAsList) {
			if (arg.startsWith("Common")) {
				commonSuperClasses.add(arg);
			}
		}

		// Select Class to be SuperClass
		if (commonSuperClasses.isEmpty()) {
			selectedSuperAsString = argsAsList.get(0);
			argsAsList.remove(0);

		} else {
			String longestSuper = "";
			for (String commonClass : commonSuperClasses) {
				// Check for the longest Super Class, use first if there's a tie.
				if (commonClass.length() > longestSuper.length()) {
					longestSuper = commonClass;
				}
			}
			selectedSuperAsString = longestSuper;
			argsAsList.remove(argsAsList.indexOf(longestSuper));
			System.out.println("[DBG] Removing class " + longestSuper + " from class list.");
		}

		try {
			ClassPool pool = ClassPool.getDefault();
			boolean useRuntimeClass = false;

			if (useRuntimeClass) {
				insertClassPathRunTimeClass(pool, argsAsList);
			} else {
				insertClassPath(pool);
			}

			for (String arg : argsAsList) {
				CtClass cc = pool.get("target." + arg);
				setSuperClass(cc, "target." + selectedSuperAsString, pool);
				cc.writeFile(outputDir);
				System.out.println("[DBG] Write output to: " + outputDir);
			}

		} catch (NotFoundException | CannotCompileException | IOException | InstantiationException
				| IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Is this method needed?
	 */
	static void insertClassPathRunTimeClass(ClassPool pool, List<String> argsAsList)
			throws NotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		for (String arg : argsAsList) {
			Class<?> subclass = Class.forName(arg);
			Object subclassInstace = subclass.newInstance();
			ClassClassPath classPath = new ClassClassPath(subclassInstace.getClass());
			pool.insertClassPath(classPath);
			System.out.println("[DBG] Insert classpath: " + classPath.toString());
		}
	}

	/*
	 * Insert ClassPath
	 */
	static void insertClassPath(ClassPool pool) throws NotFoundException {
		String strClassPath = workDir + SEP + "bin";
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] Insert classpath: " + strClassPath);
	}

	/*
	 * Sets given Class as the superclass for other classes.
	 */
	static void setSuperClass(CtClass curClass, String superClass, ClassPool pool)
			throws NotFoundException, CannotCompileException {
		curClass.setSuperclass(pool.get(superClass));
		System.out.println("[DBG] Set superclass: " + curClass.getSuperclass().getName() + //
				", subclass: " + curClass.getName());
	}

}
