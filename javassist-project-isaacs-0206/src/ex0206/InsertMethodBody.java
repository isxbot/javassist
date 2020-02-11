package ex0206;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import util.UtilMenu;

public class InsertMethodBody {
	static String WORK_DIR = System.getProperty("user.dir");
	static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
	static String _L_ = System.lineSeparator();

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
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = pool.get("target." + className);
			CtMethod m = cc.getDeclaredMethod(methodName);
			String block = "{ " + _L_ + "System.out.println(\"[DBG] param1: \" + $" + pIndex + "); " + _L_ + "}";
			m.insertBefore(block);
			cc.writeFile(OUTPUT_DIR);
		} catch (NotFoundException | CannotCompileException | IOException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
