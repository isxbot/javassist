public class ServiceApp {
    public static void main(String[] args) throws Exception {
    	ServiceApp sApp = new ServiceApp();
    	sApp.runService();
    	System.out.println(Class.forName(args[0]).getField(args[1]).getName());
    }
    
    void runService() {
    	System.out.println("Called runService.");
    }
}
