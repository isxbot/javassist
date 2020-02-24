import java.lang.reflect.Proxy;
import java.util.Iterator;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.MemberValue;
import util.UtilMenu;

public class AnnotatedFieldExample {
	public static void main(String[] args) {

		while (true) {
			// Get user input.
			UtilMenu.showMenuOptions();
			UtilMenu.getOption();
			String[] arguments = UtilMenu.getArguments();

			if (arguments.length != 3) {
				System.err.println("[WRN] Invalid input size!!");
			}

			System.out.print("\n");

			String className = arguments[0];
			String[] annotations = { arguments[1], arguments[2] };

			// Create class pool, get target class.
			try {
				ClassPool pool = ClassPool.getDefault();
				CtClass ct = pool.get("target." + className);
				// Process target class and user entered annotations
				process(ct, annotations);
				System.out.println();

			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	static void process(CtClass ct, String[] annotations) throws ClassNotFoundException {
		String targetField = "target." + annotations[0];
		String targetAnnotation = "target." + annotations[1];

		CtField[] fields = ct.getFields();

		for (CtField field : fields) {
			if (field.hasAnnotation(targetField)) {
				Annotation annotation = getAnnotation(field.getAnnotation(Class.forName(targetAnnotation)));
				showAnnotation(annotation);
			}
		}
	}

	static Annotation getAnnotation(Object obj) {
		// Get the underlying type of a proxy object in java
		AnnotationImpl annotationImpl = //
				(AnnotationImpl) Proxy.getInvocationHandler(obj);
		return annotationImpl.getAnnotation();
	}

	static void showAnnotation(Annotation annotation) {
		Iterator<?> iterator = annotation.getMemberNames().iterator();
		while (iterator.hasNext()) {
			Object keyObj = (Object) iterator.next();
			MemberValue value = annotation.getMemberValue(keyObj.toString());
			System.out.println("[DBG] " + keyObj + ": " + value);
		}
		System.out.print("\n");

	}

}
