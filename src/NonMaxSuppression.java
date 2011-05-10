import java.awt.*;
import java.awt.image.*;
import java.applet.*;
import java.net.*;
import java.io.*;
import java.lang.Math;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JApplet;
import javax.imageio.*;
import javax.swing.event.*;

public class NonMaxSuppression {

		int[] magnitude;
		double[] direction;
		int[] output;
		float[] template={-1,0,1,-2,0,2,-1,0,1};;
		int progress;
		int templateSize=3;
		int width;
		int height;

		public NonMaxSuppression() {
			progress=0;
		}

		public void init(int[] magnitudeIn, double[] directionIn, int widthIn, int heightIn) {
			width=widthIn;
			height=heightIn;
			magnitude = new int[width*height];
			direction = new double[width*height];
			output = new int[width*height];
			magnitude=magnitudeIn;
			direction=directionIn;
		}
		public int[] process() {
	
			progress=0;
		
			for(int x=0;x<width;x++) {
				progress++;
				for(int y=0;y<height;y++) {
					if ((magnitude[y*width+x]&0xff) > 0) {
						double angle = direction[y*width+x];
						int Mint = magnitude[y*width+x]&0xff;

						// angle wants to be the normal so add pi/2
						angle = angle + (Math.PI / 2);

						double roottwo = Math.sqrt(2);
						int x1 = (int)Math.ceil((Math.cos(angle + Math.PI/8) * roottwo) - 0.5);
						int y1 = (int)Math.ceil((-Math.sin(angle + Math.PI/8) * roottwo) - 0.5);
						int x2 = (int)Math.ceil((Math.cos(angle - Math.PI/8) * roottwo) - 0.5);
						int y2 = (int)Math.ceil((-Math.sin(angle - Math.PI/8) * roottwo) - 0.5);

						double M1 = (magnitude[(y+y1)*width+(x+x1)]&0xff + magnitude[(y+y2)*width+(x+x2)]&0xff)/2;

						angle = angle + Math.PI;

						x1 = (int)Math.ceil((Math.cos(angle + Math.PI/8) * roottwo) - 0.5);
						y1 = (int)Math.ceil((-Math.sin(angle + Math.PI/8) * roottwo) - 0.5);
						x2 = (int)Math.ceil((Math.cos(angle - Math.PI/8) * roottwo) - 0.5);
						y2 = (int)Math.ceil((-Math.sin(angle - Math.PI/8) * roottwo) - 0.5);

						double M2 = (magnitude[(y+y1)*width+(x+x1)]&0xff + magnitude[(y+y2)*width+(x+x2)]&0xff)/2;

						if ((Mint > M1) && (Mint >= M2)) {
							output[y*width+x] = 0xff000000 | (Mint << 16 | Mint << 8 | Mint);
						} else {
							output[y*width+x] = 0xff000000;
						}

					} 
					else 
						output[y*width+x] = 0xff000000;
				}
			}
			return output;
		}

		public int getProgress() {
			return progress;
		}

	}
