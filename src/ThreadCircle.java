import org.eclipse.swt.widgets.Display;


public class ThreadCircle extends Thread {

	
	private int width;
	private int height;
	private int radius;
	private int[] orig;
	private int threadnum ;
	private IrisRecognition irisRecognition ;
	public int val = -1 ;

	public ThreadCircle(int[] orig,int width,int height,int radius, int threadnum, IrisRecognition irisRecognition){
		this.orig = orig ;
		this.width = width ;
		this.height = height ;
		this.radius = radius ;
		this.threadnum = threadnum ;
		this.irisRecognition = irisRecognition ;
	}
	
	public void run() {
		HoughCircle circHobj = new HoughCircle();
		circHobj.init(orig,width,height,radius);
		circHobj.setLines(1);
		circHobj.processBruteForce();
		val = circHobj.value ;
		System.out.println("Thread num "+threadnum+" val ="+val+" radius = "+circHobj.r);
		// public void threadEnded(){
		Display.getDefault().asyncExec(new Runnable() {
		 public void run() {
			 irisRecognition.threadEnded();
		 }
		});
	}

}
