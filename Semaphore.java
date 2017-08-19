import java.io.PrintStream;

 

public class Semaphore {

 
	private String treeName;
	 
	private boolean lock = false;
	
 
	public Semaphore(String treeName) {
		this.treeName = treeName;
	}
 
	public String getName() {
		return treeName;
	}
 
	public void setName(String treeName) {
		this.treeName = treeName;
	}
 
	public boolean isLock() {
		return lock;
	}
	/**
	 * The caller tries to lock the semaphore for further access
	 * If success, lock is set to true
	 * If failure, the thread goes to wait
	 * The method is synchronized as it uses mutual exclusion property of Semaphore
	 */
	synchronized public void acquireLock(PrintStream outStream) {
		String threadName = Thread.currentThread().getName();
		while(isLock()) {
			try{
				System.out.println("Thread " + threadName + " trying to lock semaphore " + treeName + " : failure [waiting]");
				log("Thread " + threadName + " trying to lock semaphore " + treeName + " : failure [waiting]",outStream);
				wait();
			} catch(InterruptedException e){
				System.out.println("Thread was interrupted while waiting");
			}
		}
		System.out.println("Thread " + threadName + " trying to lock semaphore " + treeName + " : success");
		log("Thread " + threadName + " trying to lock semaphore " + treeName + " : success",outStream);
		lock = true;
	}
	/**
	 * The caller releases the semaphore
	 * The method is synchronized as it uses mutual exclusion property of Semaphore
	 */
	synchronized public void releaseLock(PrintStream outStream) {
		String threadName = Thread.currentThread().getName();
		System.out.println("Thread " + threadName + " releasing lock on semaphore " + treeName);
		log("Thread " + threadName + " releasing lock on semaphore "+ treeName ,outStream);
		lock = false;
		notifyAll();
	}
	
	public void log(String msg,PrintStream outStream){
		
		if(outStream!=null){
			outStream.println(msg);
		}
	}
}
