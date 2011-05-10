import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;


public class Utils {

	public static BufferedImage toBufferedImage(java.awt.Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage)image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent Pixels
		boolean hasAlpha = false;

		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(
					image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	public static int[] extractRectangleFromArray(int radius, int x, int y, int[] arr,int width){
		int [] output = new int[radius*radius*4];
		int b = 0 ;
		for(int a = y - radius ; a < y + radius ; a++){
			for(int i = x-radius ; i<x+radius ; i++){
				output[b] = arr[a*width +i];
				b++ ;
			}
		}
		return output;

	}

	public static int[] getHalf2(int[] arr, int row,int width, boolean upper){	
		int rowsNum = (int)(arr.length / width) ;
		int widthHalf = (int)(width/2) ;
		int actualRow = 1 ;
		int [] output ;
		System.out.println("Upper = "+upper+" arr len "+arr.length+" width " +width+ "rows"+rowsNum);
		if(upper){
			System.out.println(" Size = "+(width*(rowsNum-row))+" start = " + 0);
			output = new int[width*(rowsNum-row)];
			for(int i = 0 ; i<output.length ; i++){
				if(i>actualRow*width-widthHalf){
					actualRow++ ;
					System.out.print(" "+actualRow);
				}
				else
					output[i] = arr[i+(actualRow-1)*width] ;
			}
		}else{
			System.out.println(" Size = "+(width*(rowsNum-(rowsNum-row)))+" start = " + (width*(rowsNum-row)));
			output = new int[width*(rowsNum-(rowsNum-row))];
			for(int i = 0 ; i<output.length ; i++){
				if(i>actualRow*width){
					actualRow++ ;
					System.out.print(" "+actualRow);
				}
				else
					output[i] = arr[i+(actualRow-1)*width+widthHalf] ;
			}
		}
		return output;
	}

	public static int[] getHalf(int[] arr, int row,int width, boolean upper){	
		int rowsNum = (int)(arr.length / width) ;
		int [] output = new int[arr.length];  

		if(upper){
			for(int i = 0 ; i<(int)(output.length/2) ; i++)
				output[i] = arr[i] ;
		}else{
			for(int i = 0 ; i<(int)(output.length/2) ; i++)
				output[i+(int)(output.length/2)] = arr[(width*(rowsNum-row))+i] ;
		}
		return output;
	}

	public static int[] getHalf1(int[] arr, int row,int width, boolean upper){	
		int rowsNum = (int)(arr.length / width) ;
		int [] output ;
		System.out.println("Upper = "+upper);
		if(upper){
			System.out.println(" Size = "+(width*(rowsNum-row))+" start = " + 0);
			output = new int[width*(rowsNum-row)];
			for(int i = 0 ; i<output.length ; i++)
				output[i] = arr[i] ;
		}else{
			System.out.println(" Size = "+(width*(rowsNum-(rowsNum-row)))+" start = " + (width*(rowsNum-row)));
			output = new int[width*(rowsNum-(rowsNum-row))];
			for(int i = 0 ; i<output.length ; i++)
				output[i] = arr[(width*(rowsNum-row))+i] ;
		}
		return output;
	}

	public static int[] mergeArrays(int arr1[], int arr2[],int arrlength){
		int [] output = new int[arrlength*2];
		for(int i=0 ; i < arrlength; i++){
			output[i] = arr1[i];
		}
		for(int i=0 ; i < arrlength; i++){
			output[i+arrlength] = arr2[arrlength+i];
		}
		return output ;
	}

	public static int[] mergeArrays2(int arr1[], int arr2[]){
		int [] output = new int[arr1.length+arr2.length];
		for(int i=0 ; i < arr1.length; i++){
			output[i] = arr1[i];
		}
		for(int i=0 ; i < arr2.length; i++){
			output[i+arr1.length] = arr2[i];
		}
		return output ;
	}
}
