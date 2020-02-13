package target;

public class ComponentApp {
	int x = 10, y = 10;
	
	void draw() {
		move(1,1);
	}
	
	void move(int dx, int dy) {
		x = dx;
		y = dy;
		System.out.println("\t[ComponentApp] x: " + x + ", y: " + y);
		System.out.println("\t[ComponentApp] dx: " + dx + ", dy: " + dy);
	}

	public static void main(String[] args) {
		System.out.println("[ComponentApp] Run...");
		ComponentApp cApp = new ComponentApp();
		cApp.draw();
		System.out.println("[ComponentApp] Done.");
	}
}
