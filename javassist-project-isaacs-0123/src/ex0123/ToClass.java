package ex0123;

import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.NotFoundException;

public class ToClass {
	static Scanner scanner = new Scanner(System.in);
	static final String[] validInput = { "CommonServiceA", "CommonComponentB" };

	public static void main(String[] args) {
		String input = "";

		// Keep asking for a value until a valid one is given.
		while (input.isEmpty()) {
			boolean isValid = false;
			input = getInput();
			System.out.println("You entered: " + input);

			for (String arg : validInput) {
				if (arg.contentEquals(input)) {
					isValid = true;
				}
			}

			if (!isValid) {
				System.err.println("[WRN] Invalid Input");
				input = "";
			}
		}

		process(input);
	}

	static String getInput() {
		System.out.println("Enter an input - CommonServiceA or CommonComponentB");
		return scanner.next().trim();
	}

	static void process(String clazz) {
		try {
			ClassPool cp = ClassPool.getDefault();
			CtClass cc = cp.get("target." + clazz);
			CtField[] fields = cc.getFields();
			CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
			
			for(CtField field : fields) {
			String block = "{ System.out.println(\"" + field.getName() + ": \" + " + field.getName() + "); }";
			declaredConstructor.insertAfter(block);
			}
			Class<?> c = cc.toClass();
			c.newInstance();
			
		} catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {

		}
	}

}
