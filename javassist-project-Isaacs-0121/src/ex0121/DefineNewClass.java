package ex0121;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

public class DefineNewClass {
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + File.separator + "output";
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		ClassPool pool = ClassPool.getDefault();
		String[] classes = new String[3];
		String line;
		int i = 0;
		
		do {
			System.out.print("Enter a class name: ");
		} while(!(line = input.nextLine()).isEmpty()); {
			System.out.print("Enter a class name: ");
			classes[i] = line.trim();
			i++;
		}
		
		if(classes.length < 3) {
			System.exit(0);
		}
		
		i = 0;
		for(String arg : classes) {
			System.out.println("Argument " + i + " " + classes[i]);
			i++;
		}
		
//        System.out.println("Enter a superclass: ");
//        createClass(pool, input.next().trim());
//        
//        System.out.println("Enter the first subclass: ");
//        createClass(pool, input.next().trim());
//        
//        System.out.println("Enter the second subclass: ");
//        createClass(pool, input.next().trim());

        
        
        

	}

	/*
	 * Create new class from String.
	 */
	static CtClass createClass(ClassPool pool, String newClass) {
		CtClass cc = pool.makeClass(newClass);
		return cc;
	}

}
