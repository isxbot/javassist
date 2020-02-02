package ex0128;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;

public class JavassistLoader {
	static final String SEP = File.separator;
	static String workDir = System.getProperty("user.dir");
	static Scanner scanner = new Scanner(System.in);
	static String outputDir = workDir + SEP + "output";

	enum USAGE_METHODS {
		add, remove;
	}

	enum INCREMENT_METHODS {
		incX, incY;
	}

	enum GETTER_METHODS {
		getX, getY;
	}

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalArgumentException,
			InvocationTargetException, IOException {
		List<String> classes = new ArrayList<String>();
		List<String> methods = new ArrayList<String>();
		HashSet<String> commonSuperClasses = new HashSet<String>();
		String selectedSuperAsString;

		System.out.println("Available classes are: Point, Circle, Rectangle.");
		getInput("Enter a class: ", classes);

		System.out.println(
				"Enter a usage method, an increment method, and a getter method.\nAvailable methods are: add or remove (usage), incX or incY (increment), getX or getY (getter).");
		getInput("Enter a method: ", methods);

		// Check if 3 classes and methods were given, exit if not.
		if (classes.size() < 3 | methods.size() < 3) {
			System.out.println("[WRN] Invalid input size!!");
			System.exit(0);
		}

		// Check if class names start with "Common", add to set if so.
		for (String clazz : classes) {
			if (clazz.startsWith("Common")) {
				commonSuperClasses.add(clazz);
			}
		}

		// Select Class to be SuperClass
		if (commonSuperClasses.isEmpty()) {
			selectedSuperAsString = classes.get(0);
			classes.remove(0);

		} else {
			String longestSuper = "";
			for (String commonClass : commonSuperClasses) {
				// Check for the longest Super Class, use first if there's a tie.
				if (commonClass.length() > longestSuper.length()) {
					longestSuper = commonClass;
				}
			}
			selectedSuperAsString = longestSuper;
			classes.remove(classes.indexOf(longestSuper));
			System.out.println("[DBG] Removing class " + longestSuper + " from class list.");
		}

		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = null;
			boolean useRuntimeClass = true;

			if (useRuntimeClass) {
				insertClassPathRunTimeClass(pool, classes);
			} else {
				insertClassPath(pool);
			}

			// Set superclass
			for (String arg : classes) {
				cc = pool.get("target." + arg);
				setSuperClass(cc, "target." + selectedSuperAsString, pool);
			}

			// Determine usage, increment, and getter methods.
			String usageMethod = null;
			String incMethod = null;
			String getterMethod = null;

			for (String method : methods) {
				if (method.equals(USAGE_METHODS.add.toString()) || method.equals(USAGE_METHODS.remove.toString())) {
					usageMethod = method;
				}

				if (method.equals(INCREMENT_METHODS.incX.toString())
						|| method.equals(INCREMENT_METHODS.incY.toString())) {
					incMethod = method;
				}

				if (method.equals(GETTER_METHODS.getX.toString()) || method.equals(GETTER_METHODS.getY.toString())) {
					getterMethod = method;
				}
			}

			// Insert method call to increment
			for (String clazz : classes) {
				cc = pool.getCtClass("target." + clazz);
				if (!cc.isFrozen()) {
					addMethodCall(pool, cc = pool.get("target." + clazz), usageMethod, incMethod, getterMethod);
				} else {
					System.out.println("[WRN] The class " + cc.getName() + " has been modified previously.");
				}
			}

		} catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Insert method class
	 */
	static void addMethodCall(ClassPool pool, CtClass cc, String usageMethod, String incMethod, String getterMethod)
			throws NotFoundException, CannotCompileException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException,
			InvocationTargetException, IOException {

		CtMethod m1 = cc.getDeclaredMethod(usageMethod);
		String BLK1 = "\n{\n" //
				+ "    " + incMethod + "();" + "\n" //
				+ "    " + "System.out.println(\"[TR] " + getterMethod + " result : \" + " + getterMethod + "()); "
				+ "\n" + "}";
		System.out.println("[DBG] Block: " + BLK1);
		m1.insertBefore(BLK1);
		cc.writeFile(outputDir);
		executeMethodCall(pool, cc.getName().toString(), usageMethod);
	}

	/*
	 * Execute method call
	 */
	static void executeMethodCall(ClassPool pool, String clazz, String usageMethod)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException {
		Loader cl = new Loader(pool);
		Class<?> c = cl.loadClass(clazz);
		Object obj = c.newInstance();
		System.out.println("[DBG] Created " + clazz + " object.");

		Class<?> objClass = obj.getClass();
		Method m = objClass.getDeclaredMethod(usageMethod, new Class[] {});
		System.out.println("[DBG] Called getDeclaredMethod.");
		Object retVal = m.invoke(c.newInstance(), new Object[] {});
		System.out.println("[DBG] getVal results: " + retVal);
	}

	/*
	 * Insert Class Path with Class loader.
	 */
	static void insertClassPathRunTimeClass(ClassPool pool, List<String> argsAsList)
			throws NotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		for (String arg : argsAsList) {
			Class<?> subclass = Class.forName("target." + arg);
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
		String strClassPath = workDir + SEP + "bin";
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] Insert classpath from insertClassPath: " + strClassPath);
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

	/*
	 * Take input from user, assign to ArrayList. Only accept 3 arguments.
	 */
	static void getInput(String ask, List<String> arrayList) {
		String line;

		for (int i = 0; i < 3; i++) {
			System.out.print(ask);
			line = scanner.nextLine();
			if (!line.isEmpty()) {
				arrayList.add(line);
			}
		}

		if (arrayList.size() != 3) {
			System.out.println("[WRN] Invalid Input");
			arrayList.clear();
			((ArrayList<String>) arrayList).trimToSize();
			getInput(ask, arrayList);
		}
	}
}
