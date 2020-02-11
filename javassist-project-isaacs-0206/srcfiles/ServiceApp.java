public class ServiceApp {

	public static void main(String[] args) {
		ServiceApp sApp = new ServiceApp();
        sApp.bar(10, 20);
	}

	public void bar(int n, int m) {
	    System.out.println("[DBG] bar called");
	}
}
