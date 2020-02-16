
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
	static String _L_ = System.lineSeparator();
	static String TARGET_APP;
	static int numberOfFields;

	public static void main(String[] args) throws Throwable {
		String[] input = getInput();
		TARGET_APP = input[0];
		numberOfFields = Integer.parseInt(input[1]);
		NewExprAccess s = new NewExprAccess();
		Class<?> c = s.loadClass(TARGET_APP);
		Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
		mainMethod.invoke(null, new Object[] { args });
	}

	private ClassPool pool;

	/*
	 * Constructor
	 */
	public NewExprAccess() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(CLASS_PATH); // TARGET must be there.
	}

	/*
	 * Finds a specified class. The bytecode for that class can be modified.
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			cc.instrument(new ExprEditor() {
				public void edit(NewExpr newExpr) throws CannotCompileException {
					try {
						String longName = newExpr.getConstructor().getLongName();
						if (longName.startsWith("java.")) {
							return;
						}
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
					CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();
					String fieldType = null;
					String log = String.format("[Edited by ClassLoader] new expr: %s, " //
							+ "line: %d, signature: %s", newExpr.getEnclosingClass().getName(), //
							newExpr.getLineNumber(), newExpr.getSignature());
					System.out.println(log);

					StringBuilder str = new StringBuilder();
					str.append("{" + _L_);
					str.append("\t$_ = $proceed($$);" + _L_);
					str.append("\tString cName = $_.getClass().getName();" + _L_);

					if (fields.length < numberOfFields) {
						numberOfFields = fields.length;
					}

					for (int i = 0; i < numberOfFields; i++) {
						try {
							fieldType = fields[i].getType().getName();
						} catch (NotFoundException e) {
							e.printStackTrace();
						}
						str.append("\tString fName" + i + " = $_.getClass().getDeclaredFields()[" + i + "].getName();"
								+ _L_);
						str.append("\tString fieldFullName" + i + " = cName + \".\" + fName" + i + ";" + _L_);
						str.append("\t" + fieldType + " field" + i + "Value = $_." + fields[i].getName() + ";" + _L_);
						str.append("\tSystem.out.println(\"[Instrument] \" + fieldFullName" + i + " + \" : \" +  field"
								+ i + "Value" + ");" + _L_);
					}
					str.append("}");
					System.out.println(str.toString());
					newExpr.replace(str.toString());
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
		}
	}

	/*
	 * Get user input
	 */
	public static String[] getInput() {
		UtilMenu.showMenuOptions();
		String[] arguments = new String[2];

		if (UtilMenu.getOption() == 1) {
			System.out.println("Enter an application class and the number of fields to be analyzed and displayed.");

			System.out.println("\n For example: (ComponentApp, 1) or (ServiceApp, 100)");
			arguments = UtilMenu.getArguments();
			if (arguments.length != 2) {
				System.err.println("[WRN] Invalid input size!!");
				System.exit(0);
			}
		}
		return arguments;
	}
}