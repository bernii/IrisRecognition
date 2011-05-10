
public class HistThreshold {

		static int[] input;
		static int[] output;
		int progress;
		int width;
		int height;
		static int lower;
		static int upper;

		public void HistThreshold() {
			progress=0;
		}

		public void init(int[] inputIn, int widthIn, int heightIn, int lowerIn, int upperIn) {
			width=widthIn;
			height=heightIn;
			input = new int[width*height];
			output = new int[width*height];
			input=inputIn;
			lower=lowerIn;
			upper=upperIn;
		}
		public int[] process() {
			progress=0;
			for(int x=0;x<width;x++) {
				progress++;
				for(int y=0;y<height;y++) {
					int value = (input[y*width+x]) & 0xff; 
					if (value >= upper) {
						input[y*width+x] = 0xffffffff;
						hystConnect(x, y);
					}
				}
			}
		
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {
					if (input[y*width+x] == 0xffffffff)
						output[y*width+x] = 0xffffffff;
					else
						output[y*width+x] = 0xff000000;
				}
			}
			return output;
		}
		private void hystConnect(int x, int y) {
			int value = 0;
			for (int x1=x-1;x1<=x+1;x1++) {
				for (int y1=y-1;y1<=y+1;y1++) {
					if ((x1 < width) & (y1 < height) & (x1 >= 0) & (y1 >= 0) & (x1 != x) & (y1 != y)) {
						value = (input[y1*width+x1])  & 0xff;
						if (value != 255) {
							if (value >= lower) {
								input[y1*width+x1] = 0xffffffff;
								hystConnect(x1, y1);
							} 
							else {
								input[y1*width+x1] = 0xff000000;
							}
						}
					}
				}
			}

		}	

		public int getProgress() {
			return progress;
		}

	}
