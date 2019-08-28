package thread;

public class UppercaseAlphabet {
	public void print() {
		for (char c = 'c'; c <= 'z'; c++) {
			System.out.println(c);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}
}
