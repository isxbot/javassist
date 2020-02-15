package ex0211;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";
	static String _L_ = System.lineSeparator();
	private ClassPool pool;
	static String className;
	static String methodName;
	static String methodParameter;
	static String parameterValue;

	/*
	 * Constructor
	 */
	public SubstituteMethodBody() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(INPUT_PATH);
		System.out.println("[DBG] Class Paths: " + pool.toString());
	}

	/*
	 * Finds a specified class, modify parameters.
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			if (cc.isModified()) {
				System.err.println("[WRN] The method " + methodName + " has been modified previously!");
			}
			System.err.println(cc.isFrozen());
			cc.instrument(new ExprEditor() {
				public void edit(MethodCall call) throws CannotCompileException {
					String className = call.getClassName();
					String mName = call.getMethodName();

					// Filter out the class we want
					if (className.equals(name)) {
						if (mName.equals(methodName)) {
							System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: "
									+ call.getLineNumber());

							String block1 = "{" + _L_ //
									+ "System.out.println(\"Set parameter to new value.\"); " + _L_ //
									+ "$" + methodParameter + " = " + parameterValue + "; " + _L_ //
									+ "$proceed($$); " + _L_ //
									+ "}";
							System.out.println("[DBG] BLOCK1: " + block1);

							System.out.println("------------------------");
							call.replace(block1);
						}
					}
				}
			});
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IOException e) {
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			e.printStackTrace();
			throw new ClassNotFoundException();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Get user input
	 */
	public static void getInput() {
		UtilMenu.showMenuOptions();
		String[] arguments = new String[4];
		// Get input from user
		if (UtilMenu.getOption() == 1) {
			System.out.println("Enter an application class, name of a method, index of"
					+ " the method parameter, and the value to be assigned to the parameter.");

			System.out.println("\n For example: (ComponentApp, move, 1, 0) or (ServiceApp, fill, 2, 10)");
			arguments = UtilMenu.getArguments();
			if (arguments.length != 4) {
				System.err.println("[WRN] Invalid input size!!");
				System.exit(0);
			}
		}

		className = arguments[0];
		methodName = arguments[1];
		methodParameter = arguments[2].toString();
		parameterValue = arguments[3].toString();
	}

	/*
	 * Main method
	 */
	public static void main(String[] args) throws Throwable {
		getInput();
		SubstituteMethodBody s = new SubstituteMethodBody();
		Class<?> c = s.loadClass(className);
		Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
		mainMethod.invoke(null, new Object[] { args });

	}

}
