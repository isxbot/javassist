package target;

public class ServiceApp {
	int x = 1000, y = 1000;
	
	void draw() {
		move(2,2);
	}
	
	void move(int dx, int dy) {
		x = dx;
		y = dy;
		System.out.println("\t[ServiceApp] x: " + x + ", y: " + y);
		System.out.println("\t[ServiceApp] dx: " + dx + ", dy: " + dy);
	}

	public static void main(String[] args) {
		System.out.println("[ServiceApp] Run...");
		ServiceApp sApp = new ServiceApp();
		sApp.draw();
		System.out.println("[ServiceApp] Done.");
	}
}
}
