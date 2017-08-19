import java.util.ArrayList;


 
public class SemaphoreBTree {
	
	
	 
	private ArrayList semaphoreBTree = null;

 
	public SemaphoreBTree() {
		semaphoreBTree = new ArrayList();
	}

	/**
	 * Creates a Logical Binary Semaphore Tree
	 * @param numberOfSemaphores The number of semaphores required to achieve mutual exclusion
	 */
 
	public void createSemaphoreBinaryTree(int numberOfSemaphores) {

		for(int i = 0; i < numberOfSemaphores; i++) {
			Semaphore s = new Semaphore("s"+i);
			semaphoreBTree.add(s);
		}
	}

 
	public ArrayList getSemaphoreTree() {
		return semaphoreBTree;
	}

 
	public void setSemaphoreTree(ArrayList semaphoreTree) {
		this.semaphoreBTree = semaphoreTree;
	}

}
