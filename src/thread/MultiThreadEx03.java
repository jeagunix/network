package thread;

public class MultiThreadEx03 {
	public static void main(String[] args) {
		Thread thread1 = new DigitThread();
		Thread thread2 = new AlphanetThread();
		Thread thread3 = new Thread(new UppercaseAlphabetRunnablelmpl());
		
		
		thread1.start();
		thread2.start();
		thread3.start();
	}
}
