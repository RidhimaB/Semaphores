import java.io.File;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main implements Runnable {
	
	/**
	 * Variables Declaration 
	 * numberOfThreads - Total Inputed Thread Count
	 * semaphoreToThreadMappingList - Mapping all threads to particular resource
	 * randomNumber - To be generated Random numbers for sleep 
	 * semaphoreBTree - It is Binary Tree
	 */
	private int numberOfThreads; 
	private ArrayList semaphoreToThreadMappingList;
	private static Random randomNumber;
	private SemaphoreBTree semaphoreBTree;
	private long processStartTime ;
	private long processEndTime ;
	
	private static PrintStream outStream;
	
	public Main(int threadNumber) {
		if(threadNumber < 1) {
			log("Program needs at least One thread to execute.Please input minimum Two threds as you can see accurate results.");
			return;
		}
		numberOfThreads = threadNumber;
		randomNumber = new Random();
		if(numberOfThreads > 1) {
		semaphoreToThreadMappingList = new ArrayList();
		semaphoreBTree = new SemaphoreBTree();
		semaphoreBTree.createSemaphoreBinaryTree(threadNumber - 1);
		getCurrectLeafNode(semaphoreBTree.getSemaphoreTree(), 0);
		}
		processStartTime = 0 ;
		processStartTime = System.currentTimeMillis();
		processEndTime = processStartTime + 15*1000;
    //	System.out.println("Process Starting .."+ processStartTime);
		log("Process Starting .."+ processStartTime);
		createAndStartMultiThreads();
	}
	
	private void createAndStartMultiThreads() {
		for (int threadId = 0; threadId < numberOfThreads; threadId++) {
			Thread t= new Thread(this, "p" + threadId);
			//System.out.println("Starting thread id: " + t.getName());
			log("Starting thread id: " + t.getName());
			t.start();
		}
	}
	
 
	private void getCurrectLeafNode(ArrayList semaphoreTree, int currentNode) {

		int sizeOfTree = semaphoreTree.size();
		int leftNode = currentNode*2 + 1;
		int rightNode = currentNode*2 + 2;
		if(leftNode < sizeOfTree) {getCurrectLeafNode(semaphoreTree, leftNode);}
		else semaphoreToThreadMappingList.add(semaphoreTree.get(currentNode));
		if(rightNode < sizeOfTree) {getCurrectLeafNode(semaphoreTree, rightNode);}
		else semaphoreToThreadMappingList.add(semaphoreTree.get(currentNode));
		return;
	}
	
	
	
	
	public void run() {
	  	boolean terminateFlag=true;
  	
		Thread t = Thread.currentThread();
		sleep(t, 100, 1000);
		List semaphoreList = new ArrayList();
		while(terminateFlag) {
			getSharedResource(semaphoreList);
			sleep(t, 250, 500);
		  if(System.currentTimeMillis()>=processEndTime){
		     terminateFlag=false;
		 	long proceedTime = (System.currentTimeMillis()-processStartTime)/1000;
        //	System.out.println("Total Processed Time :" + proceedTime + " Sec");
			log("Total Processed Time :" + proceedTime + " Sec");
        	System.exit(0);
		  }
		}
	}

	
	private void sleep(Thread t, int min, int max) {
		int sleepTime = randomNumber.nextInt(max-min) + min;
		try {
			t.sleep(sleepTime);
		} catch(InterruptedException e) {
			log("Thread Id: " + t.getName() + " Interrupted");
		}
	}
	
	
	
	
	
	private void getSharedResource(List semaphoreList) {

 
		if(numberOfThreads == 1) {
			sharedSemaphoreResource();
			return;
		}
		
		Semaphore currentSemaphore = (Semaphore)getFirstElement(semaphoreList);
		while(currentSemaphore == null || !currentSemaphore.getName().equals("s0") ) {
			Semaphore semaObj = getSemaphoreResourceToBeAcquired(currentSemaphore);
			acquireSemaphoreResource(semaObj, semaphoreList);
			currentSemaphore = semaObj;
		}
		sharedSemaphoreResource();
		releaseSemaphoreResource(semaphoreList);
	}
	
	synchronized private void sharedSemaphoreResource() {

		Thread t = Thread.currentThread();
		int sleepTime = randomNumber.nextInt(500) + 500;
	//	System.out.println("Thread " + t.getName() + " in critical section now. Will be in critical section for  " + sleepTime + " milliseconds ..");
		log("Thread " + t.getName() + " in critical section now. Will be in critical section for  " + sleepTime + " milliseconds ..");
		try {
			t.sleep(sleepTime);
		} catch(InterruptedException e) {
			log("Thread Id: " + t.getName() + " Interrupted");
		}
	}
	
	
	private Semaphore getSemaphoreResourceToBeAcquired(Semaphore currentSemaphore) {
		
		ArrayList semaphoreTree = semaphoreBTree.getSemaphoreTree();
		if(currentSemaphore == null) {
			String tname = Thread.currentThread().getName();
			int tid = Integer.valueOf(tname.substring(1)).intValue();
			return (Semaphore)semaphoreToThreadMappingList.get(tid);
		}
		else {
			int index = semaphoreTree.indexOf(currentSemaphore);
			if(index%2 == 0) {
				return (Semaphore)semaphoreTree.get(index/2 - 1);
			}
			else {
				return (Semaphore)semaphoreTree.get(index/2);
			}
		}
	}
	
	private void acquireSemaphoreResource(Semaphore s, List semaphoreList) {
		s.acquireLock(outStream);
		semaphoreList.add(s);
	}
	
	private void releaseSemaphoreResource(List semaphoreList) {
		while(semaphoreList.size() != 0) {
			Semaphore s = (Semaphore)removeELement(semaphoreList);
			s.releaseLock(outStream);
		}
	}
	
	public Object removeELement(List list ){		 
			Object e = list.get(list.size()-1);
			list.remove(list.size()-1);
			return e;
		
	}
	public Object getFirstElement(List list){
		
		if(list.size() == 0) {
			return null;
		}
		else {
			return list.get(list.size()-1);
		}
	}
	
	
	public void log(String msg){
		
		if(outStream!=null){
			outStream.println(msg);
		}
	}
	
	
	public static void main(String[] args) {
		try {
			if(powerOfTwo(Integer.parseInt(args[0]))){
			File outFile  = new File("D:/log.log");
			 outStream = new PrintStream(new FileOutputStream(outFile));
			 
	//		System.setOut(outStream);	

			new Main(Integer.parseInt(args[0]));
			}else{
				System.out.println("Invalid parameter.Please give power of 2");
				
			}
		}catch(FileNotFoundException e) {
			System.out.println("Invalid input file.Loggers will not be captured");
			new Main(Integer.parseInt(args[0]));
		} 
		catch(Exception e) {
			System.out.println("Invalid parameter. Please type in: java start <number of threads>");
		} 
		finally{
			if(outStream!=null)
				outStream.flush();
		}
	}
	
	 private static boolean powerOfTwo(int number){
	        int square = 1;
	        if(number==1 || number==0)
	        	return false;
	        while(number >= square){
	            if(number == square){
	                return true;
	            }
	            square = square*2;
	        }
	        return false;
	    }

 


}
