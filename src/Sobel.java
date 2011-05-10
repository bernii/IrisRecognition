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

public class Sobel {

		int[] input;
		int[] output;
		float[] template={-1,0,1,-2,0,2,-1,0,1};;
		int progress;
		int templateSize=3;
		int width;
		int height;
		double[] direction;

		public void Sobel() {
			progress=0;
		}

		public void init(int[] original, int widthIn, int heightIn) {
			width=widthIn;
			height=heightIn;
			input = new int[width*height];
			output = new int[width*height];
			direction = new double[width*height];
			input=original;
		}
		public int[] process() {
			float[] GY = new float[width*height];
			float[] GX = new float[width*height];
			int[] total = new int[width*height];
			progress=0;
			int sum=0;
			int max=0;

			for(int x=(templateSize-1)/2; x<width-(templateSize+1)/2;x++) {
				progress++;
				for(int y=(templateSize-1)/2; y<height-(templateSize+1)/2;y++) {
					sum=0;

					for(int x1=0;x1<templateSize;x1++) {
						for(int y1=0;y1<templateSize;y1++) {
							int x2 = (x-(templateSize-1)/2+x1);
							int y2 = (y-(templateSize-1)/2+y1);
							float value = (input[y2*width+x2] & 0xff) * (template[y1*templateSize+x1]);
							sum += value;
						}
					}
					GY[y*width+x] = sum;
					for(int x1=0;x1<templateSize;x1++) {
						for(int y1=0;y1<templateSize;y1++) {
							int x2 = (x-(templateSize-1)/2+x1);
							int y2 = (y-(templateSize-1)/2+y1);
							float value = (input[y2*width+x2] & 0xff) * (template[x1*templateSize+y1]);
							sum += value;
						}
					}
					GX[y*width+x] = sum;

				}
			}
			for(int x=0; x<width;x++) {
				for(int y=0; y<height;y++) {
					total[y*width+x]=(int)Math.sqrt(GX[y*width+x]*GX[y*width+x]+GY[y*width+x]*GY[y*width+x]);
					direction[y*width+x] = Math.atan2(GX[y*width+x],GY[y*width+x]);
					if(max<total[y*width+x])
						max=total[y*width+x];
				}
			}
			float ratio=(float)max/255;
			for(int x=0; x<width;x++) {
				for(int y=0; y<height;y++) {
					sum=(int)(total[y*width+x]/ratio);
					output[y*width+x] = 0xff000000 | ((int)sum << 16 | (int)sum << 8 | (int)sum);
				}
			}
			progress=width;
			return output;
		}

		public double[] getDirection() {
			return direction;
		}
		public int getProgress() {
			return progress;
		}

	}
