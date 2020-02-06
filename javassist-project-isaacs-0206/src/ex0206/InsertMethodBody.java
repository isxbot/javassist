package ex0206;

import util.UtilMenu;

public class InsertMethodBody extends ClassLoader {

	public static void main(String[] args) {
		String[] arguments = new String[3];
		UtilMenu menu = new UtilMenu();
		menu.showMenuOptions();
		if (menu.getOption() == 1) {
			System.out.println(
					"Enter an application class name, a method name, and a method parameter index (e.q, ComponentApp, food, 1, or ServiceApp, bar, 2: ");
			arguments = menu.getArguments();

		}
	}

	protected Class<?> findClass(String name) {

		return null;
	}
}
