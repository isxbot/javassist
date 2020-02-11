public class ComponentApp {

	public static void main(String[] args) {
		ComponentApp cApp = new ComponentApp();
		cApp.foo(1, 2);

	}
	
	public void foo(int n, int m) {
	    System.out.println("[DBG] foo called");
	}

}
