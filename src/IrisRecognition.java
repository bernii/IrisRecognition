
import ij.ImagePlus;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.jhlabs.image.PolarFilter;

public class IrisRecognition {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Text textFileUrl = null;
	private Button buttonFileUrl = null;
	private Button buttonStart = null;
	private Canvas canvasImage = null;
	private Canvas canvasImage1 = null;
	private Canvas canvasImage2 = null;
	
	private Feature[] gabor; //gabor coords of currently processed iris
	private Vector <Feature[]> irisDb = new Vector(); // iris database
	private Vector <String> fileNames = new Vector(); // iris photos file names
	
	/**
	 * 
	 */

	Image image ;
	
	/**
	 * This method initializes canvasImage	
	 *
	 */
	private void createCanvasImage() {
		canvasImage = new Canvas(sShell, SWT.NONE);
		canvasImage.setBounds(new Rectangle(40, 61, 199, 124));
	}

	/**
	 * This method initializes canvasImage1	
	 *
	 */
	private void createCanvasImage1() {
		canvasImage1 = new Canvas(sShell, SWT.NONE);
		canvasImage1.setBounds(new Rectangle(373, 62, 199, 124));
	}

	/**
	 * This method initializes canvasImage2	
	 *
	 */
	private void createCanvasImage2() {
		canvasImage2 = new Canvas(groupIris, SWT.NONE);
		canvasImage2.setLocation(new Point(9, 82));
		canvasImage2.setSize(new Point(199, 124));
	}

	/**
	 * This method initializes canvasImage3	
	 *
	 */
	private void createCanvasImage3() {
		canvasImage3 = new Canvas(groupIris, SWT.NONE);
		canvasImage3.setLocation(new Point(217, 82));
		canvasImage3.setSize(new Point(199, 124));
	}

	/**
	 * This method initializes canvasImage4	
	 *
	 */
	private void createCanvasImage4() {
		canvasImage4 = new Canvas(groupIris, SWT.NONE);
		canvasImage4.setLocation(new Point(425, 82));
		canvasImage4.setSize(new Point(199, 124));
	}

	/**
	 * This method initializes canvasImage5	
	 *
	 */
	private void createCanvasImage5() {
		canvasImage5 = new Canvas(groupNoise, SWT.NONE);
		canvasImage5.setLocation(new Point(7, 43));
		canvasImage5.setSize(new Point(98, 98));
	}

	/**
	 * This method initializes groupIris	
	 *
	 */
	private void createGroupIris() {
		groupIris = new Group(compositeAnalyze, SWT.NONE);
		groupIris.setText("Iris");
		groupIris.setBounds(new Rectangle(5, 5, 630, 217));
		createCanvasImage2();
		groupIris.setLayout(null);
		createCanvasImage3();
		createCanvasImage4();
		labelIrisThreshold = new Label(groupIris, SWT.NONE);
		labelIrisThreshold.setBounds(new Rectangle(11, 64, 67, 15));
		labelIrisThreshold.setText("Threshold");
		labelIrisAcumulator = new Label(groupIris, SWT.NONE);
		labelIrisAcumulator.setBounds(new Rectangle(220, 63, 156, 15));
		labelIrisAcumulator.setText("Hough transform");
		labelIrisCircle = new Label(groupIris, SWT.NONE);
		labelIrisCircle.setBounds(new Rectangle(425, 63, 142, 15));
		labelIrisCircle.setText("Iris");
		textThresholdIrisDown = new Text(groupIris, SWT.BORDER);
		textThresholdIrisDown.setBounds(new Rectangle(84, 59, 45, 21));
		textThresholdIrisDown.setText("5");
		textThresholdIrisUp = new Text(groupIris, SWT.BORDER);
		textThresholdIrisUp.setBounds(new Rectangle(133, 59, 44, 21));
		textThresholdIrisUp.setText("10");
		labelPupilDiameter = new Label(groupIris, SWT.NONE);
		labelPupilDiameter.setBounds(new Rectangle(347, 16, 95, 15));
		labelPupilDiameter.setText("Pupil radius");
		textPupilDiameter = new Text(groupIris, SWT.BORDER);
		textPupilDiameter.setBounds(new Rectangle(450, 10, 46, 21));
		textPupilDiameter.setText("34");
		textIrisDiameter = new Text(groupIris, SWT.BORDER);
		textIrisDiameter.setBounds(new Rectangle(285, 10, 45, 21));
		textIrisDiameter.setText("98");
		textPupilDiameterFound = new Text(groupIris, SWT.BORDER);
		textPupilDiameterFound.setBounds(new Rectangle(500, 10, 42, 21));
		textPupilDiameterFound.setEditable(false);
		textPupilDiameterFound.setText("34");
		labelIrisDiameter = new Label(groupIris, SWT.NONE);
		labelIrisDiameter.setBounds(new Rectangle(172, 16, 106, 15));
		labelIrisDiameter.setText("Iris radius");
	}

	/**
	 * This method initializes groupNoise	
	 *
	 */
	private void createGroupNoise() {
		groupNoise = new Group(compositeAnalyze, SWT.NONE);
		groupNoise.setLayout(null);
		groupNoise.setText("Noise");
		groupNoise.setBounds(new Rectangle(5, 226, 627, 154));
		createCanvasImage5();
		labelNoiseThreshold = new Label(groupNoise, SWT.NONE);
		labelNoiseThreshold.setBounds(new Rectangle(7, 25, 63, 15));
		labelNoiseThreshold.setText("Threshold");
		createCanvasImage6();
		createCanvasImage7();
		labelNoiseAccumulator = new Label(groupNoise, SWT.NONE);
		labelNoiseAccumulator.setBounds(new Rectangle(216, 27, 144, 15));
		labelNoiseAccumulator.setText("Hough transform");
		labelNoise = new Label(groupNoise, SWT.NONE);
		labelNoise.setBounds(new Rectangle(425, 24, 99, 15));
		labelNoise.setText("Lines and noise");
		textThresholdLinesDown = new Text(groupNoise, SWT.BORDER);
		textThresholdLinesDown.setBounds(new Rectangle(74, 20, 42, 21));
		textThresholdLinesDown.setText("10");
		textThresholdLinesUp = new Text(groupNoise, SWT.BORDER);
		textThresholdLinesUp.setBounds(new Rectangle(120, 20, 49, 21));
		textThresholdLinesUp.setText("20");
	}

	/**
	 * This method initializes canvasImage6	
	 *
	 */
	private void createCanvasImage6() {
		canvasImage6 = new Canvas(groupNoise, SWT.NONE);
		canvasImage6.setLocation(new Point(215, 43));
		canvasImage6.setSize(new Point(98, 98));
	}

	/**
	 * This method initializes canvasImage7	
	 *
	 */
	private void createCanvasImage7() {
		canvasImage7 = new Canvas(groupNoise, SWT.NONE);
		canvasImage7.setLocation(new Point(422, 43));
		canvasImage7.setSize(new Point(98, 98));
	}

	/**
	 * This method initializes tabFolder	
	 *
	 */
	private void createTabFolder() {
		tabFolder = new TabFolder(sShell, SWT.NONE);
		createCompositeAnalyze();
		createCompositeNormalisation();
		createCompositeGabor();
		tabFolder.setBounds(new Rectangle(14, 190, 654, 413));
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Analyze");
		tabItem.setControl(compositeAnalyze);
		TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
		tabItem1.setText("Normalization");
		tabItem1.setControl(compositeNormalisation);
		TabItem tabItem2 = new TabItem(tabFolder, SWT.NONE);
		tabItem2.setText("Gabor filter");
		tabItem2.setControl(compositeGabor);		
	}

	/**
	 * This method initializes compositeAnalyze	
	 *
	 */
	private void createCompositeAnalyze() {
		compositeAnalyze = new Composite(tabFolder, SWT.NONE);
		compositeAnalyze.setLayout(null);
		createGroupIris();
		createGroupNoise();
	}

	/**
	 * This method initializes compositeNormalisation	
	 *
	 */
	private void createCompositeNormalisation() {
		compositeNormalisation = new Composite(tabFolder, SWT.NONE);
		compositeNormalisation.setLayout(null);
		createCanvasNormalization1();
		createCanvasNormalization2();
		createCanvasNormalization3();
		createCanvasNormalization4();
	}
	
	private void createCompositeGabor() {
		compositeGabor = new Composite(tabFolder, SWT.NONE);
		compositeGabor.setLayout(null);

		createGabCanv1();
		createGabCanv2();
		createGabCanv3();
		createGabCanv4();
		createGabCanv5();
		createGabCanv6();
		createGabCanv7();
		createGabCanv8();
		createGabCanv9();
		createGabCanv10();
		createGabCanv11();
		createGabCanv12();
		gabLabel1 = new Label(compositeGabor, SWT.NONE);
		gabLabel1.setBounds(new Rectangle(30, 10, 145, 20));
		gabLabel1.setText("");
		gabLabel11 = new Label(compositeGabor, SWT.NONE);
		gabLabel11.setBounds(new Rectangle(30, 73, 148, 18));
		gabLabel11.setText("");
		gabLabel12 = new Label(compositeGabor, SWT.NONE);
		gabLabel12.setBounds(new Rectangle(31, 133, 145, 18));
		gabLabel12.setText("");
		gabLabel13 = new Label(compositeGabor, SWT.NONE);
		gabLabel13.setBounds(new Rectangle(31, 192, 142, 22));
		gabLabel13.setText("");
		gabLabel14 = new Label(compositeGabor, SWT.NONE);
		gabLabel14.setBounds(new Rectangle(32, 252, 137, 20));
		gabLabel14.setText("");
		gabLabel15 = new Label(compositeGabor, SWT.NONE);
		gabLabel15.setBounds(new Rectangle(30, 313, 137, 19));
		gabLabel15.setText("");
		gabLabel16 = new Label(compositeGabor, SWT.NONE);
		gabLabel16.setBounds(new Rectangle(360, 12, 157, 24));
		gabLabel16.setText("");
		gabLabel17 = new Label(compositeGabor, SWT.NONE);
		gabLabel17.setBounds(new Rectangle(360, 73, 155, 20));
		gabLabel17.setText("");
		gabLabel18 = new Label(compositeGabor, SWT.NONE);
		gabLabel18.setBounds(new Rectangle(361, 135, 152, 23));
		gabLabel18.setText("");
		gabLabel19 = new Label(compositeGabor, SWT.NONE);
		gabLabel19.setBounds(new Rectangle(361, 192, 150, 22));
		gabLabel19.setText("");
		gabLabel110 = new Label(compositeGabor, SWT.NONE);
		gabLabel110.setBounds(new Rectangle(361, 253, 149, 23));
		gabLabel110.setText("");
		gabLabel111 = new Label(compositeGabor, SWT.NONE);
		gabLabel111.setBounds(new Rectangle(361, 313, 150, 23));
		gabLabel111.setText("");
		gabCanvas.add(gabCanv1);
		gabCanvas.add(gabCanv2);
		gabCanvas.add(gabCanv3);
		gabCanvas.add(gabCanv4);
		gabCanvas.add(gabCanv5);
		gabCanvas.add(gabCanv6);
		gabCanvas.add(gabCanv7);
		gabCanvas.add(gabCanv8);
		gabCanvas.add(gabCanv9);
		gabCanvas.add(gabCanv10);
		gabCanvas.add(gabCanv11);
		gabCanvas.add(gabCanv12);
		
		gabLabels.add(gabLabel1);
		gabLabels.add(gabLabel11);
		gabLabels.add(gabLabel12);
		gabLabels.add(gabLabel13);
		gabLabels.add(gabLabel14);
		gabLabels.add(gabLabel15);
		gabLabels.add(gabLabel16);
		gabLabels.add(gabLabel17);
		gabLabels.add(gabLabel18);
		gabLabels.add(gabLabel19);
		gabLabels.add(gabLabel110);
		gabLabels.add(gabLabel111);
	}
	

	Vector <Canvas>gabCanvas = new Vector();  //  @jve:decl-index=0:
	Vector <Label>gabLabels = new Vector();  //  @jve:decl-index=0:
	

	/**
	 * This method initializes canvasNormalization1	
	 *
	 */
	private void createCanvasNormalization1() {
		canvasNormalization1 = new Canvas(compositeNormalisation, SWT.NONE);
		canvasNormalization1.setLocation(new Point(14, 29));
		canvasNormalization1.setSize(new Point(199, 124));
	}

	/**
	 * This method initializes canvasNormalization2	
	 *
	 */
	private void createCanvasNormalization2() {
		canvasNormalization2 = new Canvas(compositeNormalisation, SWT.NONE);
		canvasNormalization2.setLocation(new Point(270, 31));
		canvasNormalization2.setSize(new Point(199, 124));
	}

	/**
	 * This method initializes canvasNormalization3	
	 *
	 */
	private void createCanvasNormalization3() {
		canvasNormalization3 = new Canvas(compositeNormalisation, SWT.NONE);
		canvasNormalization3.setLocation(new Point(19, 194));
		canvasNormalization3.setSize(new Point(199, 124));
	}

	/**
	 * This method initializes canvasNormalization4	
	 *
	 */
	private void createCanvasNormalization4() {
		canvasNormalization4 = new Canvas(compositeNormalisation, SWT.NONE);
		canvasNormalization4.setLocation(new Point(270, 195));
		canvasNormalization4.setSize(new Point(199, 124));
	}

	/**
	 * This method initializes gabCanv1	
	 *
	 */
	private void createGabCanv1() {
		gabCanv1 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv1.setBounds(new Rectangle(30, 30, 256, 32));
	}

	/**
	 * This method initializes gabCanv2	
	 *
	 */
	private void createGabCanv2() {
		gabCanv2 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv2.setBounds(new Rectangle(30, 90, 256, 32));
	}

	/**
	 * This method initializes gabCanv3	
	 *
	 */
	private void createGabCanv3() {
		gabCanv3 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv3.setBounds(new Rectangle(30, 150, 256, 32));
	}

	/**
	 * This method initializes gabCanv4	
	 *
	 */
	private void createGabCanv4() {
		gabCanv4 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv4.setBounds(new Rectangle(30, 210, 256, 32));
	}

	/**
	 * This method initializes gabCanv5	
	 *
	 */
	private void createGabCanv5() {
		gabCanv5 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv5.setBounds(new Rectangle(30, 270, 256, 32));
	}

	/**
	 * This method initializes gabCanv6	
	 *
	 */
	private void createGabCanv6() {
		gabCanv6 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv6.setBounds(new Rectangle(30, 330, 256, 32));
	}

	/**
	 * This method initializes gabCanv7	
	 *
	 */
	private void createGabCanv7() {
		gabCanv7 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv7.setBounds(new Rectangle(360, 30, 256, 32));
	}

	/**
	 * This method initializes gabCanv8	
	 *
	 */
	private void createGabCanv8() {
		gabCanv8 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv8.setBounds(new Rectangle(360, 90, 256, 32));
	}

	/**
	 * This method initializes gabCanv9	
	 *
	 */
	private void createGabCanv9() {
		gabCanv9 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv9.setBounds(new Rectangle(360, 150, 256, 32));
	}

	/**
	 * This method initializes gabCanv10	
	 *
	 */
	private void createGabCanv10() {
		gabCanv10 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv10.setBounds(new Rectangle(360, 210, 256, 32));
	}

	/**
	 * This method initializes gabCanv11	
	 *
	 */
	private void createGabCanv11() {
		gabCanv11 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv11.setBounds(new Rectangle(360, 270, 256, 32));
	}

	/**
	 * This method initializes gabCanv12	
	 *
	 */
	private void createGabCanv12() {
		gabCanv12 = new Canvas(compositeGabor, SWT.NONE);
		gabCanv12.setBounds(new Rectangle(360, 330, 256, 32));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* Before this is run, be sure to set up the launch configuration (Arguments->VM Arguments)
		 * for the correct SWT library path in order to run with the SWT dlls. 
		 * The dlls are located in the SWT plugin jar.  
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 *       installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		Display display = Display.getDefault();
		IrisRecognition thisClass = new IrisRecognition();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Iris Recognition 0.1 - 2007, Bernard Kobos, Piotr Zaborowski");
		sShell.setSize(new Point(689, 684));
		sShell.setLayout(null);
		textFileUrl = new Text(sShell, SWT.BORDER);
		textFileUrl.setBounds(new Rectangle(15, 18, 215, 19));
		textFileUrl.setText("res/osoba7a.jpg");
		buttonFileUrl = new Button(sShell, SWT.NONE);
		buttonFileUrl.setBounds(new Rectangle(240, 15, 101, 25));
		buttonFileUrl.setText("Select file...");
		buttonFileUrl.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog dialog = new FileDialog(sShell, SWT.NULL);
		        String path = dialog.open();
		        if (path != null) {
		
		          File file = new File(path);
		          if (file.isFile())
		            textFileUrl.setText(file.toString());
		        }
			}
		});
		buttonStart = new Button(sShell, SWT.NONE);
		buttonStart.setBounds(new Rectangle(569, 618, 99, 25));
		buttonStart.setText("START");
		buttonStart.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// vars initialization
				endedThreads = 0 ;
				threadsToEnd = 0 ;
				threadRadius = 0 ;
				threadVariation = 0 ;
				
				// load and display image
				image = new Image(Display.getDefault(),textFileUrl.getText());
				Image imgScaled = new Image(Display.getDefault(), image.getImageData().scaledTo((int)(canvasImage.getBounds().width),(int)(canvasImage.getBounds().height)));
				canvasImage.setBackgroundImage(imgScaled);

				buttonStart.setEnabled(false);
				processImage();
			}
		});
		createCanvasImage();
		createCanvasImage1();
		labelImage = new Label(sShell, SWT.NONE);
		labelImage.setBounds(new Rectangle(42, 42, 116, 15));
		labelImage.setText("Eye image");
		labelEdges = new Label(sShell, SWT.NONE);
		labelEdges.setBounds(new Rectangle(372, 43, 128, 15));
		labelEdges.setText("Found edges");
		createTabFolder();
		
		databaseAddButton = new Button(sShell, SWT.NONE);
		databaseAddButton.setBounds(new Rectangle(28, 619, 92, 23));
		databaseAddButton.setText("Add to DB");
		databaseAddButton.setVisible(false);
		databaseAddButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				System.out.println("Adding person to DB");
				irisDb.add(gabor);
				String filename = (textFileUrl.getText().substring(textFileUrl.getText().lastIndexOf("\\")+1));
				fileNames.add(filename);
				bazaLength.setVisible(true);
				bazaLength.setText(fileNames.size() + " persons in DB.");
				databaseAddButton.setVisible(false);
				compareButton.setVisible(false);
			}
		});
		bazaLength = new Label(sShell, SWT.NONE);
		bazaLength.setBounds(new Rectangle(138, 626, 128, 13));
		bazaLength.setText("x persons in DB");
		bazaLength.setVisible(false);
		compareButton = new Button(sShell, SWT.NONE);
		compareButton.setBounds(new Rectangle(268, 620, 106, 23));
		compareButton.setText("Compare irises");
		compareButton.setVisible(false);
		compareButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						System.out.println("Comparing iris with db"); 
						compareResult.setText("Iris won:\n" + Database.compare(gabor, irisDb, fileNames));
						compareResult.setVisible(true);
					}
				});
		compareResult = new Label(sShell, SWT.NONE);
		compareResult.setBounds(new Rectangle(380, 618, 157, 31));
		compareResult.setText("Iris won:\n");
		compareResult.setVisible(false);
		loadBase = new Button(sShell, SWT.NONE);
		loadBase.setBounds(new Rectangle(463, 13, 94, 29));
		loadBase.setText("Open db...");
		loadBase.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog dialog = new FileDialog(sShell, SWT.NULL);
				String ext[] = {"*.iris"};
				dialog.setFilterExtensions(ext);
		        String path = dialog.open();
		        if (path != null) {
		        	FileInputStream fis = null;
		        	ObjectInputStream in = null;
		        	try
		        	{
		        	  fis = new FileInputStream(path);
		        	  in = new ObjectInputStream(fis);
		        	  Vector database = (Vector)in.readObject();
		        	  in.close();
		        	  irisDb = (Vector)database.get(0);
		        	  fileNames = (Vector)database.get(1);
					  bazaLength.setVisible(true);
					  bazaLength.setText(fileNames.size() + " persons in DB.");
					  System.out.println("Opened database from " + path);
					  if(gabor!=null) {
						  compareButton.setVisible(true);
					  }
		        	}
		        	catch(IOException ex)
		        	  {
		        	   ex.printStackTrace();
		        	  }
		        	 catch(ClassNotFoundException ex)
		        	 {
		        	    ex.printStackTrace();
		        	 }
		        	}
		        }
		});
		saveBase = new Button(sShell, SWT.NONE);
		saveBase.setBounds(new Rectangle(573, 13, 89, 29));
		saveBase.setText("Save DB...");
		saveBase.setVisible(false);
		saveBase.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog dialog = new FileDialog(sShell, SWT.NULL);
				String ext[] = {"*.iris"};
				dialog.setFilterExtensions(ext);
		        String path = dialog.open();
		        if(!path.endsWith("iris")) path = path.concat(".iris");
		        if (path != null) {
			        	  FileOutputStream fos = null;
			        	  ObjectOutputStream out = null;
			        	  Vector database = new Vector();
			        	  database.add(irisDb);
			        	  database.add(fileNames);
			        	  try
				       	     {
				       	        fos = new FileOutputStream(path);
				       	        out = new ObjectOutputStream(fos);
				       	        out.writeObject(database);
				       	        System.out.println("Database saved at " + path);
				       	     }
				       	  catch(IOException ex)
				       	     {
				       	           ex.printStackTrace();
				             }
				          
		        }
			}
		});
		
		
	}
	
	public int orig[] = null;
	private Sobel sobelObject;  //  @jve:decl-index=0:
	private NonMaxSuppression nonMaxSuppressionObject;
	private HistThreshold histThresholdObject;
	private HoughLine lineHoughObject;  //  @jve:decl-index=0:
	private int width ;
	private int height ;
	private Image SobelImage;  //  @jve:decl-index=0:
	private Image MaxSuppImage;
	private Image HystImage;
	private Canvas canvasImage3 = null;
	private Canvas canvasImage4 = null;
	private Image OverlayImage;
	private Image LinesImage;
	private Image HoughAccImage;
	private HoughCircle houghCircle;
	private int[] origFiltered;
	private Image CircleImage;
	private Canvas canvasImage5 = null;
	private Group groupIris = null;
	private Label labelImage = null;
	private Label labelEdges = null;
	private Label labelIrisThreshold = null;
	private Label labelIrisAcumulator = null;
	private Label labelIrisCircle = null;
	private Text textThresholdIrisDown = null;
	private Text textThresholdIrisUp = null;
	private Group groupNoise = null;
	private Label labelNoiseThreshold = null;
	private Canvas canvasImage6 = null;
	private Canvas canvasImage7 = null;
	private Label labelNoiseAccumulator = null;
	private Label labelNoise = null;
	private Text textThresholdLinesDown = null;
	private Text textThresholdLinesUp = null;
	private TabFolder tabFolder = null;
	private Composite compositeAnalyze = null;
	private Composite compositeNormalisation = null;
	private Composite compositeGabor = null;
	private Image HystImageLines;
	private HoughCircle houghCircle2;
	private Canvas canvasNormalization1 = null;
	private Canvas canvasNormalization2 = null;
	private Canvas canvasNormalization3 = null;
	private Canvas canvasNormalization4 = null;

	private int[] origcp;
	/**
	 * Normalized image in rectangular coordinates
	 */
	private Image normalizedImage;
	/**
	 * mask of the normalized image
	 */
	private Image normalizedMask;
	private Label labelPupilDiameter = null;
	private Text textPupilDiameter = null;
	private Text textIrisDiameter = null;
	private Button databaseAddButton = null;
	private Label bazaLength = null;
	private Button compareButton = null;
	private Label compareResult = null;
	private Image imgScaled4;
	private int movedX;
	private int movedY;
	private int size;
	private float scaleFactorY;
	private float scaleFactorX;
	private Image imgScaled2;
	private int rmax;
	private int[] acc;
	private Image IrisAndPupilFullSize;
	private int iris_x;
	private int iris_y;
	private int iris_r;
	
	private float scalingFactor = (float) 0.5 ;
	
	private void processImage(){
		// Load image and scale it down to boost computing 
		image = new Image(Display.getDefault(), image.getImageData().scaledTo((int)(image.getBounds().width*scalingFactor),(int)(image.getBounds().height*scalingFactor)));
		width = image.getBounds().width ;
		height = image.getBounds().height ;
		orig=new int[width*height];

		BufferedImage bufImg = AWTBufferedImageSWTImage.convertToAWT(image.getImageData());
		
		PixelGrabber grabber = new PixelGrabber(bufImg, 0, 0, width, height, orig, 0, width);
		try {
			grabber.grabPixels();
			origcp = orig.clone() ;
		}
		catch(InterruptedException e2) {
			System.out.println("error: " + e2);
		}

		sobelObject = new Sobel();
		nonMaxSuppressionObject = new NonMaxSuppression();
		histThresholdObject = new HistThreshold();
		lineHoughObject = new HoughLine();
		houghCircle = new HoughCircle();
		houghCircle2 = new HoughCircle();

		sobelObject.init(orig,width,height);
		orig = sobelObject.process();
				
		// Sobel transform - edge detection
		sobel();

		/**
		 * Hough circle transform
		 * Detect iris with Hough Transform
		 */
		hough();

		/**
		 * Extraction of rectangle with iris (smaller area of pupil search)
		 */
		iris();
		/** 
		 * Search for pupil - multithreaded brute force !
		 */
		pupil();
	}
	private void iris() {
		orig = Utils.extractRectangleFromArray(houghCircle.r, houghCircle.centerCords.x, 
				houghCircle.centerCords.y, orig,width);
		origcp = Utils.extractRectangleFromArray(houghCircle.r, houghCircle.centerCords.x, 
				houghCircle.centerCords.y, origcp,width);

		// Move rectangle according to original image
		movedX  = iris_x-iris_r ;
		movedY = iris_y-iris_r ;
		size = 2*houghCircle.r ;
		System.out.println("Moved by "+movedX+","+movedY+" sred "+2* houghCircle.r);
		width = height = 2* houghCircle.r ;

		IrisAndPupilFullSize = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(origcp,size,size));
	}

	private void pupil() {
		histThresholdObject.init(orig,width,height,10,20);
		origFiltered = histThresholdObject.process();

		int pupil_r = (int)(Integer.parseInt(textPupilDiameter.getText())*scalingFactor) ;
		findCircleBruteForce(houghCircle2,pupil_r,origFiltered,width,height);
	}

	private void hough() {
		System.out.println("IRIS - Hough");

		iris_r = (int)(Integer.parseInt(textIrisDiameter.getText())*scalingFactor) ;
		houghCircle.init(origFiltered,width,height,iris_r);
		houghCircle.setLines(1);
		origFiltered = houghCircle.process();

		iris_x = houghCircle.centerCords.x ;
		iris_y = houghCircle.centerCords.y ;
		iris_r = houghCircle.r ;


		CircleImage = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(origFiltered,width,height));
		Image imgScaled = new Image(Display.getDefault(), CircleImage.getImageData().scaledTo((int)(canvasImage4.getBounds().width),(int)(canvasImage4.getBounds().height)));
		imgScaled2 = new Image(Display.getDefault(), image.getImageData().scaledTo((int)(canvasImage4.getBounds().width),(int)(canvasImage4.getBounds().height)));

		GC gc = new GC(imgScaled);
		scaleFactorY = (float)image.getBounds().height/imgScaled2.getBounds().height ;
		scaleFactorX = (float)image.getBounds().width/imgScaled2.getBounds().width ;

		System.out.println(image.getBounds().width+"/"+imgScaled2.getBounds().width+" ScaleX = "+scaleFactorX+" "+image.getBounds().height+"/"+imgScaled2.getBounds().height+" ScaleY = "+scaleFactorY);
		gc.drawImage(imgScaled2, 0,0);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		gc.drawOval((int)((iris_x-iris_r)/scaleFactorX), (int)((iris_y-iris_r)/scaleFactorY), (int)((2*iris_r)/scaleFactorX), (int)((2*iris_r)/scaleFactorY));
		gc.dispose();

		imgScaled4 = imgScaled;
		canvasImage4.setBackgroundImage(imgScaled);

		rmax = (int)Math.sqrt(width*width + height*height);
		acc = new int[width * height];
		acc=houghCircle.getAcc();
		// Display Hough matrix
		HoughAccImage = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(acc,width,height));
		imgScaled = new Image(Display.getDefault(), HoughAccImage.getImageData().scaledTo(199,124));
		canvasImage3.setBackgroundImage(imgScaled);
	}

	private void sobel() {
		double direction[] = new double[width*height];
		direction=sobelObject.getDirection();

		PaletteData palette = new PaletteData(0xFF , 0xFF00 , 0xFF0000);
		ImageData imgData = new ImageData(width,height,24,palette);
		imgData.setPixels(0, 0, width, orig, 0);

		SobelImage = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(orig,width,height));
		// Scale and display image with edges detected
		Image imgScaled = new Image(Display.getDefault(), SobelImage.getImageData().scaledTo((int)(canvasImage1.getBounds().width),(int)(canvasImage1.getBounds().height)));
		canvasImage1.setBackgroundImage(imgScaled);

		nonMaxSuppressionObject.init(orig,direction,width,height);
		orig = nonMaxSuppressionObject.process();
		int[] orig2 = orig.clone();
		// Threshloding
		histThresholdObject.init(orig2,width,height,Integer.parseInt(textThresholdIrisDown.getText()),Integer.parseInt(textThresholdIrisUp.getText()));
		origFiltered = histThresholdObject.process();
		HystImage = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(origFiltered,width,height));
		imgScaled = new Image(Display.getDefault(), HystImage.getImageData().scaledTo((int)(canvasImage2.getBounds().width),(int)(canvasImage2.getBounds().height)));
		canvasImage2.setBackgroundImage(imgScaled);
	}

	private void threadsCompleted(){
		// Put diameter into textfield
		textPupilDiameterFound.setText((int)(houghCircle2.r*1/scalingFactor)+"");

		int pupil_x = houghCircle2.centerCords.x ;
		int pupil_y = houghCircle2.centerCords.y ;
		int pupil_r = houghCircle2.r ;
		CircleImage = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(origFiltered,width,height));
		Image imgScaled = new Image(Display.getDefault(), CircleImage.getImageData().scaledTo((int)(0.5*CircleImage.getBounds().width),(int)(0.5*CircleImage.getBounds().height)));
		GC gc = new GC(canvasImage4);
		gc.drawImage(imgScaled4, 0, 0);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		System.out.println("movX+pupX-pupR = "+movedX+"+"+pupil_x+"-"+pupil_r+"="+(movedX+pupil_x-pupil_r));
		gc.drawOval((int)((movedX+pupil_x-pupil_r)/scaleFactorX), (int)((movedY+pupil_y-pupil_r)/scaleFactorY), (int)(2*pupil_r/scaleFactorX), (int)(2*pupil_r/scaleFactorY));
		Image imageIrisAndPupil = new Image(Display.getDefault(),canvasImage4.getBounds());
		gc.copyArea(imageIrisAndPupil , 0, 0);
		gc.dispose();
		canvasImage4.setBackgroundImage(imageIrisAndPupil);
		/**
		 * Linear Hough transform - find eyelashes and eyelids
		 */
		histThresholdObject.init(orig,width,height, Integer.parseInt(textThresholdLinesDown.getText()),Integer.parseInt(textThresholdLinesUp.getText()));
		orig = histThresholdObject.process(); 

		HystImageLines = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(orig,width,height));
		imgScaled = new Image(Display.getDefault(), HystImageLines.getImageData().scaledTo((int)(canvasImage5.getBounds().width),(int)(canvasImage5.getBounds().height)));
		canvasImage5.setBackgroundImage(imgScaled);

		int halfHeight = (int)(height/2) ;
		int[] upper = Utils.getHalf(orig, halfHeight, width, true);
		int[] lower = Utils.getHalf(orig, halfHeight, width, false);

		lineHoughObject.init(upper,width,height);
		lineHoughObject.setLines(5);
		upper = lineHoughObject.process();
		int[] accUpper = lineHoughObject.getAcc(); 

		java.awt.Point[] upperLinePoints = lineHoughObject.getLineCoords(true);

		lineHoughObject.init(lower,width,height);
		lineHoughObject.setLines(2);
		lower = lineHoughObject.process();
		int[] accLower = lineHoughObject.getAcc(); 
		java.awt.Point[] lowerLinePoints = lineHoughObject.getLineCoords(false);

		System.out.println("Points in lines:\n up[ ("+upperLinePoints[0].x+","+upperLinePoints[0].y+") , ("+upperLinePoints[1].x+","+upperLinePoints[1].y+") ] down [ ("+lowerLinePoints[0].x+","+lowerLinePoints[0].y+") , ("+lowerLinePoints[1].x+","+lowerLinePoints[1].y+") ]");

		int mergePoint = (int)(orig.length/2) ;
		orig = Utils.mergeArrays(upper, lower,mergePoint);

		OverlayImage = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(orig,width,height));
		imgScaled = new Image(Display.getDefault(), OverlayImage.getImageData().scaledTo((int)(OverlayImage.getBounds().width/scaleFactorX),(int)(OverlayImage.getBounds().height/scaleFactorY)));
		imgScaled2 = new Image(Display.getDefault(), image.getImageData().scaledTo((int)(image.getBounds().width/scaleFactorX),(int)(image.getBounds().height/scaleFactorY)));

		gc = new GC(imgScaled);
		gc.setAlpha(90);
		int startX = (int)((houghCircle.centerCords.x-houghCircle.r)/scaleFactorX) ;
		int startY = (int)((houghCircle.centerCords.y-houghCircle.r)/scaleFactorY) ;
		System.out.println("Start X = " + startX +" startY "+startY+" bok ="+2*houghCircle.r+ " rys  = "+imgScaled2.getBounds().height+" , "+imgScaled2.getBounds().width);
		int hwx = (int)(2*houghCircle.r/scaleFactorX) ;
		int hwy = (int)(2*houghCircle.r/scaleFactorY) ;
		gc.drawImage(imgScaled2,startX ,startY,hwx,hwy,0,0,hwx,hwy);

		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		gc.drawLine((int)(upperLinePoints[0].x/scaleFactorX), (int)(upperLinePoints[0].y/scaleFactorY), (int)(upperLinePoints[1].x/scaleFactorX), (int)(upperLinePoints[1].y/scaleFactorY));
		gc.drawLine((int)(upperLinePoints[0].x/scaleFactorX), (int)(lowerLinePoints[0].y/scaleFactorY), (int)(lowerLinePoints[1].x/scaleFactorX), (int)(lowerLinePoints[1].y/scaleFactorY));
		gc.dispose();
		imgScaled = new Image(Display.getDefault(), imgScaled.getImageData().scaledTo((int)(canvasImage7.getBounds().width),(int)(canvasImage7.getBounds().height)));
		canvasImage7.setBackgroundImage(imgScaled);
		rmax = (int)Math.sqrt(width*width + height*height);
		HoughAccImage = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(accUpper,180,rmax));
		imgScaled = new Image(Display.getDefault(), HoughAccImage.getImageData().scaledTo(199,62));

		HoughAccImage = new Image(Display.getDefault(),AWTBufferedImageSWTImage.createSWTimage(accLower,180,rmax));
		imgScaled2 = new Image(Display.getDefault(), HoughAccImage.getImageData().scaledTo(199,62));
		gc = new GC(canvasImage6);
		gc.drawImage(imgScaled,0 ,0);
		gc.drawImage(imgScaled2,0 ,62);
		imgScaled = new Image(Display.getDefault(),HoughAccImage.getBounds()) ;
		gc.copyArea(imgScaled, 0, 0);
		gc.dispose();
		canvasImage6.setBackgroundImage(imgScaled);

		/**
		 * Transform from polar coordintaes to Cartesian coordintaes
		 */

		gc = new GC(imageIrisAndPupil);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		gc.drawLine((int)((movedX+upperLinePoints[0].x)/scaleFactorX), (int)((movedY+upperLinePoints[0].y)/scaleFactorY), (int)((movedX+upperLinePoints[1].x)/scaleFactorX), (int)((movedY+upperLinePoints[1].y)/scaleFactorY));
		gc.drawLine((int)((movedX+upperLinePoints[0].x)/scaleFactorX), (int)((movedY+lowerLinePoints[0].y)/scaleFactorY), (int)((movedX+lowerLinePoints[1].x)/scaleFactorX), (int)((movedY+lowerLinePoints[1].y)/scaleFactorY));
		gc.dispose();
		canvasNormalization2.setBackgroundImage(imageIrisAndPupil);

		PolarFilter polFilter = new PolarFilter();
		polFilter.setType(PolarFilter.POLAR_TO_RECT);
		BufferedImage imageNormalized1temp = new BufferedImage(IrisAndPupilFullSize.getBounds().width,IrisAndPupilFullSize.getBounds().height,BufferedImage.TYPE_INT_RGB);
		polFilter.filter(AWTBufferedImageSWTImage.convertToAWT(IrisAndPupilFullSize.getImageData()), imageNormalized1temp);
		normalizedImage = new Image(Display.getDefault(),AWTBufferedImageSWTImage.convertToSWT(imageNormalized1temp)) ;
		imgScaled = new Image(Display.getDefault(), normalizedImage.getImageData().scaledTo(canvasNormalization1.getBounds().width,canvasNormalization1.getBounds().height));
		canvasNormalization1.setBackgroundImage(imgScaled);
		int imgwidth = IrisAndPupilFullSize.getBounds().width ;
		int imgheight = IrisAndPupilFullSize.getBounds().height ;

		normalizedMask = new Image(Display.getDefault(),imgwidth,imgheight) ;
		gc = new GC(normalizedMask);
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(0, 0, imgwidth, imgheight);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		gc.fillOval(0,0, 2*iris_r, 2*iris_r);

		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		gc.fillOval((int)((pupil_x-pupil_r)), (int)((pupil_y-pupil_r)), 2*pupil_r, 2*pupil_r);
		gc.fillPolygon(new int[]{0,upperLinePoints[0].y,imgwidth,upperLinePoints[1].y,imgwidth,0,0,0});
		gc.fillPolygon(new int[]{0,lowerLinePoints[0].y,imgwidth,lowerLinePoints[1].y,imgwidth,imgheight,0,imgheight});
		gc.dispose();
		imageNormalized1temp = new BufferedImage(IrisAndPupilFullSize.getBounds().width,IrisAndPupilFullSize.getBounds().height,BufferedImage.TYPE_INT_RGB);
		polFilter.filter(AWTBufferedImageSWTImage.convertToAWT(normalizedMask.getImageData()), imageNormalized1temp);
		normalizedMask = new Image(Display.getDefault(),AWTBufferedImageSWTImage.convertToSWT(imageNormalized1temp));
		imgScaled = new Image(Display.getDefault(), normalizedMask.getImageData().scaledTo(canvasNormalization1.getBounds().width,canvasNormalization1.getBounds().height));
		canvasNormalization2.setBackgroundImage(imgScaled);

		buttonStart.setEnabled(true);				
		makeFiltering() ;

	}
	private ThreadCircle[] thc ;
	int[] values ;
	/**
	 * Search for circle with brute-force - multithreaded
	 * Computing many Hough transforms - for different radius and the maximum is selected
	 * @param houghCircle
	 * @param radius
	 * @param orig
	 * @param width
	 * @param height
	 */
	private void findCircleBruteForce(HoughCircle circleHoughObject3,
			int radius,int[] orig,int width, int height) {
		int variation = 7 ;
		values = new int[2*variation];
		thc = new ThreadCircle[variation*2];
		int a = 0 ;
		for(int i = radius -variation; i< radius+variation ; i++,a++){
			System.out.println("Starting thread "+a+" radius "+i);
			thc[a] = new ThreadCircle(orig,width,height,i,a,this);
			thc[a].start();
		}
		threadsToEnd = a ;
		threadRadius = radius ;
		threadVariation = variation ;
		threadCircleHough = circleHoughObject3 ;
	}
	int endedThreads = 0 ;
	int threadsToEnd = 0 ;
	int threadRadius = 0 ;
	int threadVariation = 0 ;
	HoughCircle threadCircleHough ;
	
	public synchronized void threadEnded(){
		endedThreads ++ ;
		if(endedThreads>=threadsToEnd){
			// If all threads have finished, let's find the maximum
			threadsOutput = findCircleBruteForceContinue() ;
			origFiltered = threadsOutput ;
			System.out.println("All threads ENDED - phase2");
			threadsCompleted();
		}
		
	}
	int[] threadsOutput ;
	private Text textPupilDiameterFound = null;
	private Label labelIrisDiameter = null;
	private Button loadBase = null;
	private Button saveBase = null;
	private Canvas gabCanv1 = null;
	private Canvas gabCanv2 = null;
	private Canvas gabCanv3 = null;
	private Canvas gabCanv4 = null;
	private Canvas gabCanv5 = null;
	private Canvas gabCanv6 = null;
	private Canvas gabCanv7 = null;
	private Canvas gabCanv8 = null;
	private Canvas gabCanv9 = null;
	private Canvas gabCanv10 = null;
	private Canvas gabCanv11 = null;
	private Canvas gabCanv12 = null;
	private Label gabLabel1 = null;
	private Label gabLabel11 = null;
	private Label gabLabel12 = null;
	private Label gabLabel13 = null;
	private Label gabLabel14 = null;
	private Label gabLabel15 = null;
	private Label gabLabel16 = null;
	private Label gabLabel17 = null;
	private Label gabLabel18 = null;
	private Label gabLabel19 = null;
	private Label gabLabel110 = null;
	private Label gabLabel111 = null;
	/**
	 * Search of maximum in transforms
	 * @return
	 */
	private int[] findCircleBruteForceContinue(){
		for(int i =0 ; i<thc.length;i++){
			values[i] = (thc[i]).val ;
		}
		int maxval = -10 ;
		int maxindex = -1; 
		for (int i = 0 ;i<values.length;i++) {
			if(values[i]>maxval){
				maxval = values[i] ;
				maxindex = i ;
			}
		}
		
		int minval = 10000 ;
		int minidex = -1; 
		for (int i = 0 ;i<values.length;i++) {
			if(values[i]<=minval){
				minval = values[i] ;
				minidex = i ;
			}
		}
		
		System.out.println("maxval = "+maxval+" dla i= "+maxindex);
		System.out.println("minval = "+minval+" dla i= "+minidex);
		threadCircleHough.init(orig,width,height,threadRadius-threadVariation+maxindex);
		threadCircleHough.setLines(1);
		return threadCircleHough.process();
	}

    private void makeFiltering() {
    	// Gabor filter
        System.out.println("Gabor filter: ");
        Image imgScaled = new Image(Display.getDefault(), normalizedImage.getImageData().scaledTo(256,32));
        ij.ImagePlus imga = new ImagePlus("img", AWTBufferedImageSWTImage.convertToAWT(imgScaled.getImageData()));
        ij.process.ImageProcessor ip = imga.getProcessor();
        ij.measure.Calibration cal = imga.getCalibration();
        ip.setCalibrationTable(cal.getCTable());
        ip = ip.convertToFloat();
        imga = new ImagePlus("img", ip);
        float[] img = bijnum.BIJutil.vectorFromImageStack(imga, 0); //image to float[]

        Image maskScaled = new Image(Display.getDefault(), normalizedMask.getImageData().scaledTo(256,32));
        ij.ImagePlus maska = new ImagePlus("mask", AWTBufferedImageSWTImage.convertToAWT(maskScaled.getImageData()));
        ip = maska.getProcessor();
        cal = maska.getCalibration();
        ip.setCalibrationTable(cal.getCTable());
        ip = ip.convertToFloat();
        maska = new ImagePlus("mask", ip);
        float[] mask = bijnum.BIJutil.vectorFromImageStack(maska, 0); //mask to float[]
        
        
        float[] scales = {2, 16}; //parametr scales
        
        gabor = MyGabor.filter(img, mask, imga.getWidth(), scales); //obliczamy gabora
        System.out.println("Factors number: " + gabor.length + " x " + gabor[0].vector.length);
        
        int i=0;
        for(Feature ficzer: gabor) {
        	gabLabels.get(i).setText(ficzer.toString());
        	ImagePlus gab = bijnum.BIJutil.showVectorAsImage(ficzer.vector, imga.getWidth());
        	gab.getWindow().close();
        	gabCanvas.get(i).setBackgroundImage(new Image(compositeGabor.getDisplay(), 
        			AWTBufferedImageSWTImage.convertToSWT(Utils.toBufferedImage(gab.getImage()))));
        	i++;
        }       
        	
        databaseAddButton.setVisible(true);
        if(fileNames.size() > 1) {
                compareButton.setVisible(true);
                saveBase.setVisible(true);
        }
}

}
