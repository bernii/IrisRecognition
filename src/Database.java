import java.util.Vector;


public class Database {


	public static String compare(Feature[] currentIris, Vector<Feature[]> irisDb, Vector<String> nameDb) { 
		/*
		 * Comparison through Minkowski metric
		 */
		Vector<Float> distance = new Vector<Float>();
		for(Feature[] iris: irisDb) { // iterate through DB
			float dist = 0f;
			for(int i=0; i<iris.length; i++) { // iterate through Gabor factors
				dist = dist + distance(iris[i].toVector(), currentIris[i].toVector());  // compute distance
			}
			distance.add(dist);
			System.out.println(dist);
		}

		// select shortest distance
		int shortest=0;
		float shortDist=(Float)distance.get(0);
		for(int i=1; i<distance.size(); i++) {
			if((Float)distance.get(i)<shortDist){
				shortest=i;
				shortDist=(Float)distance.get(i);
			}
		}
		return nameDb.get(shortest) + ": " + shortDist;
	}

	/**
	 * Return the distance according to some Minkowski metric.
	 * This implements Euclidean distance. To save time, the POWER(1/k) is not performed.
	 * @param a a float[] vector containing a point
	 * @param b a float[] vector containing another point.
	 * @return the Minkowski distance between a and b.
	 */
	protected static float distance(float [] a, float [] b)
	{
		double d = 0;
		for (int i = 0; i < a.length; i++)
			d += Math.pow(a[i] - b[i], 2);
		return (float) d;
	}
}
