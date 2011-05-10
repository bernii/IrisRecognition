import volume.GaussianDerivative;
import volume.Kernel1D;
import bijnum.BIJmatrix;


public class MyGabor {

    public static Feature[] filter(float image[], float mask[], int width, float scales[])
    {
        int nrOrders = 3;
        Feature ls[][][] = new Feature[scales.length][nrOrders][];
        float thetas[][] = new float[nrOrders][];
        int length = 0;
        for(int j = 0; j < scales.length; j++)
        {
            for(int order = 0; order < nrOrders; order++)
            {
                thetas[order] = thetaSet(order);
                ls[j][order] = L(image, mask, width, order, scales[j], thetas[order]);
                length += ls[j][order].length;
            }

        }

        Feature Ln[] = new Feature[length];
        int index = 0;
        for(int j = 0; j < scales.length; j++)
        {
            for(int order = 0; order < nrOrders; order++)
            {
                for(int i = 0; i < ls[j][order].length; i++)
                    Ln[index++] = ls[j][order][i];

            }

        }

        ls = (Feature[][][])null;
        return Ln;
    }


    public static float[] thetaSet(int order)
    {
        float theta[] = new float[order + 1];
        theta[0] = 0.0F;
        if(order == 1)
            theta[1] = 90F;
        else
        if(order == 2)
        {
            theta[1] = 60F;
            theta[2] = 120F;
        } else
        if(order != 0)
            throw new IllegalArgumentException("order > 2");
        return theta;
    }
    
    protected static String name(int order, double scale, double theta, String extraText)
    {
        if(order == 0)
            return extraText + "L" + order + " scale=" + scale + "p";
        else
            return extraText + "L" + order + "(" + theta + " dgrs) scale=" + scale + "p";
    }

    public static Feature[] L(float image[], float mask[], int width, int n, double scale, float theta[])
    {
        Feature f[] = new Feature[theta.length];
        float L[][] = new float[theta.length][];
        if(n == 0)
        {
            volume.Kernel1D k0 = new GaussianDerivative(scale, 0);
            L[0] = convolvex(image, mask, width, image.length / width, k0);
            L[0] = convolvey(L[0], width, image.length / width, k0);
            f[0] = new Feature(name(n, scale, 0.0D, ""), L[0]);
        } else
        if(n == 1)
        {
            volume.Kernel1D k0 = new GaussianDerivative(scale, 0);
            volume.Kernel1D k1 = new GaussianDerivative(scale, 1);
            float Lx[] = convolvex(image, mask, width, image.length / width, k1);
            Lx = convolvey(Lx, width, image.length / width, k0);
            float Ly[] = convolvex(image, mask, width, image.length / width, k0);
            Ly = convolvey(Ly, width, image.length / width, k1);
            for(int i = 0; i < theta.length; i++)
            {
                double cth = Math.cos((double)(theta[i] / 180F) * 3.1415926535897931D);
                double sth = Math.sin((double)(theta[i] / 180F) * 3.1415926535897931D);
                float px[] = new float[Lx.length];
                BIJmatrix.mulElements(px, Lx, cth);
                float py[] = new float[Lx.length];
                BIJmatrix.mulElements(py, Ly, sth);
                L[i] = BIJmatrix.addElements(px, py);
                f[i] = new Feature(name(n, scale, theta[i], ""), L[i]);
            }

        } else
        if(n == 2)
        {
            volume.Kernel1D k0 = new GaussianDerivative(scale, 0);
            volume.Kernel1D k1 = new GaussianDerivative(scale, 1);
            volume.Kernel1D k2 = new GaussianDerivative(scale, 2);
            float Lxx[] = convolvex(image, mask, width, image.length / width, k2);
            Lxx = convolvey(Lxx, width, image.length / width, k0);
            float Lxy[] = convolvex(image, mask, width, image.length / width, k1);
            Lxy = convolvey(Lxy, width, image.length / width, k1);
            float Lyy[] = convolvex(image, mask, width, image.length / width, k0);
            Lyy = convolvey(Lyy, width, image.length / width, k2);
            for(int i = 0; i < theta.length; i++)
            {
                double cth = Math.cos((double)(theta[i] / 180F) * 3.1415926535897931D);
                double sth = Math.sin((double)(theta[i] / 180F) * 3.1415926535897931D);
                double c2th = cth * cth;
                double csth = cth * sth;
                double s2th = sth * sth;
                float pxx2[] = new float[Lxx.length];
                BIJmatrix.mulElements(pxx2, Lxx, c2th);
                float pxy2[] = new float[Lxy.length];
                BIJmatrix.mulElements(pxy2, Lxy, 2D * csth);
                float pyy2[] = new float[Lyy.length];
                BIJmatrix.mulElements(pyy2, Lyy, s2th);
                L[i] = BIJmatrix.addElements(pxx2, pxy2);
                BIJmatrix.addElements(L[i], L[i], pyy2);
                f[i] = new Feature(name(n, scale, theta[i], ""), L[i]);
            }

        }
        return f;
    }
    
    
        /**
         * Convolution of plane with 1D separated kernel along the x-axis.
         * The image plane is organized as one 1D vector of width*height.
         * Return the result as a float array. plane is not touched.
         * @param plane the image.
         * @param width the width in pixels of the image.
         * @param height the height of the image in pixels.
         * @param kernel a Kernel1D kernel object.
         * @see Kernel1D
         * @return a float[] with the resulting convolution.
         */
        public static float [] convolvex(float [] plane, float [] mask, int width, int height, Kernel1D kernel)
        {
                float [] result = new float[plane.length];
                for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                {
                        float d = 0;
                        // Around x, convolve over -kernel.halfwidth ..  x .. +kernel.halfwidth.
                        for (int k = -kernel.halfwidth; k <= kernel.halfwidth; k++)
                        {
                                // Mirror edges if needed.
                                int xi = x+k;
                                int yi = y;
                                if (xi < 0) xi = -xi;
                                else if (xi >= width) xi = 2 * width - xi - 1;
                                if (yi < 0) yi = -yi;
                                else if (yi >= height) yi = 2 * height - yi - 1;
                                if(mask[yi*width+xi]!=0) //sprawdzamy czy maska nie jest zerowa
                                d += plane[yi*width+xi] * kernel.k[k + kernel.halfwidth];
                        }
                        result[y*width+x] = d;
                }
                return result;
        }
        /**
         * Convolution of plane with 1D separated kernel along the y-axis.
         * The image plane is organized as one 1D vector of width*height.
         * Return the result as a float array. plane is not touched.
         * @param plane the image.
         * @param width the width in pixels of the image.
         * @param height the height of the image in pixels.
         * @param kernel a Kernel1D kernel object.
         * @see Kernel1D
         * @return a float[] with the resulting convolution.
         */
        public static float [] convolvey(float [] plane, int width, int height, Kernel1D kernel)
        {
                float [] result = new float[plane.length];
                // Convolve in y direction.
                for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                {
                        float d = 0;
                        // Around y, convolve over -kernel.halfwidth ..  y .. +kernel.halfwidth.
                        for (int k = -kernel.halfwidth; k <= kernel.halfwidth; k++)
                        {
                                // Mirror edges if needed.
                                int xi = x;
                                int yi = y+k;
                                if (xi < 0) xi = -xi;
                                else if (xi >= width) xi = 2 * width - xi - 1;
                                if (yi < 0) yi = -yi;
                                else if (yi >= height) yi = 2 * height - yi - 1;
                                d += plane[yi*width+xi] * kernel.k[k + kernel.halfwidth];
                        }
                        result[y*width+x] = d;
                }
                return result;
        }

        
}
