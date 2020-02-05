public class ServiceApp {
    public static void main(String[] args) throws Exception {
    	ServiceApp sApp = new ServiceApp();
    	sApp.runService();
    	System.out.println(sApp.getClass().getField("f2").getName());
    }
    
    void runService() {
    	System.out.println("Called runService.");
    }
}
