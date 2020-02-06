package ex04;

import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class ToClassIsaacs {
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		String[] inputList = new String[3];
		inputList = getInput(inputList);
		process(inputList[0], inputList[1], inputList[2]);
	}

	static void process(String clazz, String field1, String field2) {
		try {
			ClassPool cp = ClassPool.getDefault();
			CtClass cc = cp.get("target." + clazz);

			if (cc.isFrozen()) {
				cc.defrost();
			}

			CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);

			String block = "{ System.out.println(\"" + field1 + ": \" + " + field1 + "); }";
			declaredConstructor.insertAfter(block);

			block = "{ System.out.println(\"" + field2 + ": \" + " + field2 + "); }";
			declaredConstructor.insertAfter(block);

			Class<?> c = cc.toClass();
			c.newInstance();

		} catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {

		}
	}

	/*
	 * Take input from user, assign to ArrayList. Only accept 3 arguments.
	 */
	static String[] getInput(String[] inputList) {
		String line;
		System.out.println(
				"Available classes are CommonServiceA (fields: idA, nameA) and CommonComponentB (fields: idB, nameB).");
		System.out.print("Enter a class name and two fields (separated by commas): ");
		line = input.nextLine();

		inputList = line.split(",");
		for (int i = 0; i < inputList.length; i++) {
			inputList[i] = inputList[i].trim();
		}

		if (inputList.length < 3) {
			System.err.println("[WRN] Invalid Input");
			inputList = getInput(inputList);
		}
		return inputList;
	}
}
