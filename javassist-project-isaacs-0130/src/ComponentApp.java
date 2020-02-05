import java.lang.reflect.Field;

public class ComponentApp {
    public static void main(String[] args) throws Exception {
    	ComponentApp cApp = new ComponentApp();
        cApp.runComponent();
        System.out.println(cApp.getClass().getField("f1").getName());
    }
    
    public static void displayFieldVal(Class<?> var0) throws Exception {
    	Field var1 = var0.getField("f1");
    	String var2 = var1.getName();
    	Object var3 = var1.get(var0.newInstance());
    	System.out.println("Field name: " + var2 + " value: " + var3);
    }
    void runComponent() {
    	System.out.println("Called runComponent.");
    }
}
