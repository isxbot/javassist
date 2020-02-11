package ex0206;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;

public class InsertMethodBody extends ClassLoader {
	static String WORK_DIR = System.getProperty("user.dir");
	static String SEP = File.separator;
	static String OUTPUT_DIR = WORK_DIR + SEP + "output";
	static final String INPUT_DIR = WORK_DIR + SEP + "classfiles";
	static String _L_ = System.lineSeparator();
	private ClassPool pool;

	public InsertMethodBody() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(OUTPUT_DIR); // TARGET must be there.
	}

	public static void main(String[] args) {
		String[] arguments = new String[3];
		UtilMenu menu = new UtilMenu();
		menu.showMenuOptions();
		if (menu.getOption() == 1) {
			System.out.println(
					"Enter an application class name, a method name, and a method parameter index (e.g, ComponentApp, food, 1, or ServiceApp, bar, 2: ");
			arguments = menu.getArguments();
		}

		String className = arguments[0];
		String methodName = arguments[1];
		String pIndex = arguments[2];

		try {
			ClassPool defaultPool = ClassPool.getDefault();
			defaultPool.insertClassPath(INPUT_DIR);
			CtClass cc = defaultPool.get(className);
			CtMethod m = cc.getDeclaredMethod(methodName);
			String block = "{ " + _L_ + "System.out.println(\"[DBG] param" + pIndex + ": \" + $" + pIndex + "); " + _L_
					+ "}";
			m.insertBefore(block);
			cc.writeFile(OUTPUT_DIR);
			InsertMethodBody imb = new InsertMethodBody();
			Class<?> c = imb.loadClass(className);
			Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
			mainMethod.invoke(null, new Object[] { args });

		} catch (NotFoundException | CannotCompileException | IOException | ClassNotFoundException
				| IllegalAccessException | NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IOException e) {
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			throw new ClassNotFoundException();
		}
	}
}
