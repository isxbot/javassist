public class ComponentApp {
    public static void main(String[] args) throws Exception {
    	ComponentApp cApp = new ComponentApp();
        cApp.runComponent();
        System.out.println(Class.forName(args[0]).getField(args[1]).getName());
    }
    
    void runComponent() {
    	System.out.println("Called runComponent.");
    }
}
