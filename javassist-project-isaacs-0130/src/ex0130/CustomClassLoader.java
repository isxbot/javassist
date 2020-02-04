package ex0130;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

public class CustomClassLoader extends java.lang.ClassLoader {
	static String WORK_DIR = System.getProperty("user.dir");
	static String INPUT_DIR = WORK_DIR + File.separator + "bin";
	Scanner input = new Scanner(System.in);

	public static void main(String[] args) throws Throwable {
		String[] classAndField = new String[2];
		CustomClassLoader loader = new CustomClassLoader();
		
		// Get class and field for modification.
		loader.getInput(classAndField);
		
		// Find class
		Class<?> foundClass = loader.findClass(classAndField);
		foundClass.getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });
		
		// Add field

	}

	/*
	 * Find class matching given String class name.
	 */
	public Class<?> findClass(String[] classAndField) throws ClassNotFoundException {
		try {
			ClassPool pool = new ClassPool();
			CtClass cc = pool.get("target." + classAndField[0]);
			if(classAndField[0].contentEquals("ComponentApp") | classAndField[0].contentEquals("ServiceApp")) {
				CtField field = new CtField(CtClass.doubleType, classAndField[1], cc);
				field.setModifiers(Modifier.PUBLIC);
				cc.addField(field, CtField.Initializer.constant(0));
			}
			byte[] bArr = cc.toBytecode();
			return defineClass(classAndField[0], bArr, 0, bArr.length);
		} catch (NotFoundException e) {
	         throw new ClassNotFoundException();
	      } catch (IOException e) {
	         throw new ClassNotFoundException();
	      } catch (CannotCompileException e) {
	         throw new ClassNotFoundException();
	      }
	}
	
	/*
	 * Add a field to given class.
	 */
//	Class addField(String field, Class<?> clazz) {
//		return clazz;
//		
//	}
	
	void getInput(String[] classAndField) {
		System.out.println("Enter a class name: ");
		classAndField[0] = input.nextLine().trim();
		
		System.out.println("Enter a field name: ");
		classAndField[1] = input.nextLine().trim();
		
		for(String arg : classAndField) {
			if (arg.isEmpty()) {
				getInput(classAndField);
			}
		}
	}

}
