package de.lmu.ifi.dbs.featureextraction.image;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * TODO: comment
 *
 * @author Elke Achtert (<a href="mailto:achtert@dbs.ifi.lmu.de">achtert@dbs.ifi.lmu.de</a>)
 */
class ImageDescriptor {

  boolean useClassID = true;
  boolean useClassLabel = true;
  private static final double logTable[] = new double[10000];

  private static double step;

  static {
    int size = logTable.length;
    step = 1.0 / size;
    double v = step;
    for (int i = 0; i < size; i++) {
      logTable[i] = Math.log(v);
      v += step;
    }
  }

  /**
   * The default weight for red samples in the conversion, 0.3f.
   */
  public static final double DEFAULT_RED_WEIGHT = 0.3f;

  /**
   * The default weight for green samples in the conversion, 0.59f.
   */
  public static final double DEFAULT_GREEN_WEIGHT = 0.59f;

  /**
   * The default weight for blue samples in the conversion, 0.11f.
   */
  public static final double DEFAULT_BLUE_WEIGHT = 0.11f;

  private static final int RED_SHIFT = 16;

  private static final int GREEN_SHIFT = 8;

  private static final int BLUE_SHIFT = 0;

  private static final int VAL_NUM = 16;

  /**
   * todo pete
   */
  private static final int H_RANGES = 8;

  /**
   * todo pete
   */
  private static final int S_RANGES = 4;

  /**
   * Contains the distances of the neighboring pixels for one pixel.
   */
  private static final int[] DISTANCES = {1, 3, 5};

  public static final double GRAY_SCALE = 256 / VAL_NUM;

  private static final boolean TEXTURE = true;

  /**
   * The width of the underlying image.
   */
  private int width;

  /**
   * The height of the underlying image.
   */
  private int height;

  /**
   * The size of the underlying image.
   */
  private int size;

  private double[][] hsvValues;

  private byte[] grayValue;

  private double colorHist[] = new double[H_RANGES * S_RANGES];

  /**
   * The value for one increment in the color histogram.
   */
  private double hist_val;

  /**
   * The coocurrence matrix for each neighboring distance value.
   */
  private double[][][] coocurrenceMatrix = new double[VAL_NUM][VAL_NUM][DISTANCES.length];

  private double sum[] = new double[VAL_NUM];

  private double p_x[][] = new double[VAL_NUM][ImageDescriptor.DISTANCES.length];

  private double p_y[][] = new double[VAL_NUM][ImageDescriptor.DISTANCES.length];

  private double p_x_Plus_y[][] = new double[VAL_NUM * 2 + 1][ImageDescriptor.DISTANCES.length];

  private double p_x_Minus_y[][] = new double[VAL_NUM][ImageDescriptor.DISTANCES.length];

  /**
   * Contains the mean value of the hsv values of the underlying image.
   */
  private double meanHSV[] = new double[3];


  private double hxy1[] = new double[ImageDescriptor.DISTANCES.length];

  private double hxy2[] = new double[ImageDescriptor.DISTANCES.length];

  /**
   * Contains the standard deviation of the hsv values.
   */
  private double standardDeviation[] = new double[3];

  /**
   * Contains the skewness of the hsv values.
   */
  private double skewness[] = new double[3];


  private double f1[] = new double[ImageDescriptor.DISTANCES.length];

  private double f2[] = new double[ImageDescriptor.DISTANCES.length];

  private double f3[] = new double[ImageDescriptor.DISTANCES.length];

  private double f4[] = new double[ImageDescriptor.DISTANCES.length];

  private double f5[] = new double[ImageDescriptor.DISTANCES.length];

  private double f6[] = new double[ImageDescriptor.DISTANCES.length];

  private double f7[] = new double[ImageDescriptor.DISTANCES.length];

  private double f8[] = new double[ImageDescriptor.DISTANCES.length];

  private double f9[] = new double[ImageDescriptor.DISTANCES.length];

  private double f10[] = new double[ImageDescriptor.DISTANCES.length];

  private double f11[] = new double[ImageDescriptor.DISTANCES.length];

  private double f12[] = new double[ImageDescriptor.DISTANCES.length];

  private double f13[] = new double[ImageDescriptor.DISTANCES.length];


  /**
   * Indicates whether the underlying image is empty.
   */
  private boolean notEmpty;

  /**
   * The name of the underlying image.
   */
  private String imageName;

  /**
   * The class id of the underlying image.
   */
  private Integer classID;

  /**
   * Creates a new image descriptor for the specified image.
   *
   * @param image the image for which the description should be created
   */
  public ImageDescriptor(BufferedImage image) {
    // init width, heigt and size
    this.width = image.getWidth(null);
    this.height = image.getHeight(null);
    this.size = height * width;
    // set the value for one increment in the color histogram
    this.hist_val = 1.0f / size;
    // init the arrays for the gray values and the hsv values
    this.grayValue = new byte[size];
    this.hsvValues = new double[size][3];
    // image is not empty per default
    this.notEmpty = false;

    // calculate hsv and gray values
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int pos = width * y + x;
        calculateValues(pos, image.getRGB(x, y), x, y);

        // update mean
        for (int i = 0; i < 3; i++) {
          meanHSV[i] += hsvValues[pos][i];
        }
      }
    }

    // update mean
    for (int i = 0; i < 3; i++) {
      meanHSV[i] /= hsvValues.length;
    }

    // if image is not empty: calculate moments and statistics
    if (notEmpty) {
      calculateMoments();
      calculateStatistics();
    }
  }

  public boolean isEmpty() {
    return !notEmpty;
  }

  /**
   * Calculates the second and third moment of the hsv values
   * (standard deviation and skewness).
   */
  private void calculateMoments() {
    System.out.println("calc moments");
    double[] sum_2 = new double[3];
    double[] sum_3 = new double[3];

    // sum^2 and sum^3
    for (int i = 0; i < hsvValues.length; i++) {
      for (int j = 0; j < 3; j++) {
        double delta = hsvValues[i][j] - meanHSV[j];
        double square_delta = delta * delta;
        sum_2[j] += square_delta;
        sum_3[j] += square_delta * delta;
      }
    }

    // standard deviation and skewness
    for (int i = 0; i < standardDeviation.length; i++) {
      standardDeviation[i] = Math.sqrt(sum_2[i] / hsvValues.length);
      if (Double.isNaN(standardDeviation[i])) {
        standardDeviation[i] = 0;
      }

      // todo skewness richtig?
      double s_3 = standardDeviation[i] * standardDeviation[i] * standardDeviation[i];
      skewness[i] = sum_3[i] / ((hsvValues.length - 1) * s_3);
      if (Double.isNaN(skewness[i])) {
        skewness[i] = 0;
      }
    }
  }

  /**
   *
   */
  private void calculateStatistics() {
    double temp, temp2;
    float mu[] = new float[DISTANCES.length];


    float mu_x[] = new float[DISTANCES.length];
    float mu_y[] = new float[DISTANCES.length];
    float mu_x_Minus_y[] = new float[DISTANCES.length];

    double stdDev_x[] = new double[DISTANCES.length];
    double stdDev_y[] = new double[DISTANCES.length];
    double stdDev_x_Minus_y[] = new double[DISTANCES.length];

    for (int i = 0; i < VAL_NUM; i++) {
      for (int j = 0; j < VAL_NUM; j++) {
        for (int k = 0; k < DISTANCES.length; k++) {
          //				p(i,j)
          coocurrenceMatrix[i][j][k] /= sum[k];
          temp = coocurrenceMatrix[i][j][k];
          mu[k] += temp;
          p_x[i][k] += temp;
          p_y[j][k] += temp;
          if (i + j > 1) p_x_Plus_y[i + j][k] += temp;
          p_x_Minus_y[Math.abs(i - j)][k] += temp;

          f1[k] += temp * temp;
          temp2 = (i - j);
          f5[k] += (1 / (1 + temp2 * temp2)) * temp;
          if (temp != 0) f9[k] += temp * log(temp);

        }
      }
    }
    for (int k = 0; k < DISTANCES.length; k++) {
      mu[k] /= VAL_NUM * VAL_NUM;
      if (f9[k] != 0) f9[k] = -f9[k];
      mu_x[k] /= VAL_NUM;
      mu_y[k] /= VAL_NUM;
    }

    for (int i = 0; i < VAL_NUM; i++) {
      for (int k = 0; k < DISTANCES.length; k++) {

        temp = p_x[i][k] - mu_x[k];
        stdDev_x[k] += temp * temp;

        temp = p_y[i][k] - mu_y[k];
        stdDev_y[k] += temp * temp;

        f2[k] += i * i * p_x_Minus_y[i][k];
        if (p_x_Minus_y[i][k] != 0) f11[k] += p_x_Minus_y[i][k] * log(p_x_Minus_y[i][k]);

        mu_x_Minus_y[k] += p_x_Minus_y[i][k];
      }
    }

    for (int k = 0; k < DISTANCES.length; k++) {
      stdDev_x[k] = Math.sqrt(stdDev_x[k] / VAL_NUM);
      stdDev_y[k] = Math.sqrt(stdDev_y[k] / VAL_NUM);
      mu_x_Minus_y[k] /= VAL_NUM;
    }

    double times;
    for (int i = 0; i < VAL_NUM; i++) {
      for (int j = 0; j < VAL_NUM; j++) {
        for (int k = 0; k < DISTANCES.length; k++) {
          temp = coocurrenceMatrix[i][j][k];
          times = p_x[i][k] * p_y[j][k];
          if (times != 0) {
            hxy1[k] += temp * log(times);
            hxy2[k] += times * log(times);
          }
          f3[k] += (i * j * temp - mu_x[k] * mu_y[k]) / (stdDev_x[k] * stdDev_y[k]);
          f4[k] += (i - mu[k]) * (i - mu[k]) * temp;

          if (i == 0) {
            temp = p_x_Minus_y[j][k] - mu_x_Minus_y[k];
            f10[k] += temp * temp;
          }
        }
      }

    }


    for (int i = VAL_NUM * 2; i >= 2; i--) {
      for (int k = 0; k < DISTANCES.length; k++) {
        temp = p_x_Plus_y[i][k];
        f6[k] += i * temp;
        if (temp != 0) f8[k] += temp * log(temp);
      }
    }

    for (int k = 0; k < DISTANCES.length; k++) {
      if (hxy1[k] != 0) hxy1[k] = -hxy1[k];
      if (hxy2[k] != 0) hxy2[k] = -hxy2[k];
      if (f8[k] != 0) f8[k] = -f8[k];
      f10[k] /= VAL_NUM;
      if (f11[k] != 0) f11[k] = -f11[k];
      f12[k] = (f9[k] - hxy1[k]) / Math.max(hxy1[k], hxy2[k]);
      f13[k] = Math.sqrt(1 - Math.exp(-2 * (hxy2[k] - f9[k])));
    }

    for (int i = VAL_NUM * 2; i >= 2; i--) {
      for (int k = 0; k < DISTANCES.length; k++) {
        temp = p_x_Plus_y[i][k];
        temp2 = (i - f8[k]);
        f7[k] += temp2 * temp2 * temp;
      }
    }
  }

  private double log(double temp) {

    if (temp <= 0 || temp > 1) {
      throw new IllegalArgumentException(String.valueOf(temp));
    }
    int pos = (int) ((temp - step) / step);

    return logTable[pos];
    //return Math.log(temp);
  }

  /**
   * Calculates the hsv values, gray values, color histogram entry and
   * the entry in the coocurrence matrix for the specified pixel
   *
   * @param pos the flatened osition of the pixel
   * @param rgb the rgb value of the pixel
   * @param x the coordinate of the pixel
   * @param y the y coordinate of the pixel
   */
  private void calculateValues(int pos, int rgb, int x, int y) {
    int r = ((rgb >> RED_SHIFT) & 0xff);
    int g = (rgb >> GREEN_SHIFT) & 0xff;
    int b = (rgb >> BLUE_SHIFT) & 0xff;

    // check if image is empty
    if (!notEmpty && (r > 0 || g > 0 || b > 0)) {
      notEmpty = true;
    }

    // determine hsv value
    hsvValues[pos] = RGBtoHSV(r / 255.0, g / 255.0, b / 255.0);

    // determine gray value
    grayValue[pos] = (byte) ((r * DEFAULT_RED_WEIGHT +
                              g * DEFAULT_GREEN_WEIGHT +
                              b * DEFAULT_BLUE_WEIGHT) / GRAY_SCALE);
    //check gray value
    if (grayValue[pos] >= VAL_NUM) {
      throw new RuntimeException("Should never happen!");
    }

    // color histogram entry
    // todo pete: wat is h_ranges und s_ranges?
    double h = hsvValues[pos][0];
    double s = hsvValues[pos][1];
    int index = ((int) (((H_RANGES - 1) * h / 360f)) + ((H_RANGES - 1) * (int) ((S_RANGES) * s)));
    if (index > (colorHist.length - 1)) {
      index = (colorHist.length - 1);
    }
    colorHist[index] += hist_val;

    // todo pete: wat is sum?
    for (int k = 0; k < DISTANCES.length; k++) {
      int d = DISTANCES[k];
      //horizontal neighbor
      int i = x - d;
      int j = y;
      if (!(i < 0)) {
        increment(grayValue[pos], grayValue[pos - d], k);
        sum[k] += 2;
      }

      //vertical neighbor
      i = x;
      j = y - d;
      if (!(j < 0)) {
        increment(grayValue[pos], grayValue[pos - d * width], k);
        sum[k] += 2;
      }

      //45 diagonal neigbor
      i = x + d;
      j = y - d;
      if (i < width && !(j < 0)) {
        increment(grayValue[pos], grayValue[pos + d - d * width], k);
        sum[k] += 2;
      }

      //135 vertical neighbor
      i = x - d;
      j = y - d;
      if (!(i < 0) && !(j < 0)) {
        increment(grayValue[pos], grayValue[pos - d - d * width], k);
        sum[k] += 2;
      }
    }
  }

  /**
   * Incremets the coocurrence matrix of the specified distance value d
   * at the specified positions (g1,g2) and (g2,g1).
   * @param g1 the gray value of the first pixel
   * @param g2 the gray value of the second pixel
   * @param d the distance value specifiying the coocurrence matrix
   */
  private void increment(int g1, int g2, int d) {
    coocurrenceMatrix[g1][g2][d]++;
    coocurrenceMatrix[g2][g1][d]++;
  }

  /**
   * Transforms the specified rgb value to the corresponding hsv value.
   *
   * @param r the r value
   * @param g the g value
   * @param b the b value
   * @return the hsv values for the specified rgb value
   */
  private double[] RGBtoHSV(double r, double g, double b) {
    double[] hsv = new double[3];

    double min = min(r, g, b);
    double max = max(r, g, b);
    double delta = max - min;

    // h value
    if (max == min) {
      hsv[0] = 0;
    }
    else if (r == max) {
      hsv[0] = ((g - b) / (max - min)) * 60;
    }
    else if (g == max) {
      hsv[0] = (2 + (b - r) / (max - min)) * 60;
    }
    else if (b == max) {
      hsv[0] = (4 + (r - g) / (max - min)) * 60;
    }
    if (hsv[0] < 0) hsv[0] = hsv[0] + 360;

    // s value
    if (max == 0) {
      hsv[1] = 0;
    }
    else {
      hsv[1] = (max - min) / max;
    }

    // v value
    hsv[2] = max;

    return hsv;
  }

  private double[] RGBtoHSV_old(double r, double g, double b) {
    double[] hsv = new double[3];

    double min = min(r, g, b);
    double max = max(r, g, b);
    double delta = max - min;

    // v value
    hsv[2] = max;

    if (max != 0) {
      hsv[1] = delta / max; // s
    }
    else {
      // r = g = b = 0 // s = 0, v is undefined
      hsv[1] = 0;
      hsv[0] = 0;
      return hsv;
    }

    if (delta == 0) {
      hsv[0] = 0;
    }
    else if (r == max) {
      hsv[0] = (g - b) / delta; // between yellow & magenta
    }
    else if (g == max) {
      hsv[0] = 2 + (b - r) / delta; // between cyan & yellow
    }
    else {
      hsv[0] = 4 + (r - g) / delta; // between magenta & cyan
    }

    hsv[0] *= 60; // degrees
    if (hsv[0] < 0)
      hsv[0] += 360;

    return hsv;
  }

  private double max(double r, double g, double b) {
    return Math.max(r, Math.max(g, b));
  }

  private double min(double r, double g, double b) {

    return Math.min(r, Math.min(g, b));
  }

  /**
   * @param writer
   */
  public void writeColorHistogramms(BufferedWriter writer) {
    try {
      if (useClassID) {
        writer.write(imageName);
      }
      for (int i = 0; i < colorHist.length; i++) {
        if (useClassID || i > 0) writer.write(", ");
        writer.write(String.valueOf(colorHist[i]));
      }
      if (useClassLabel) {
        writer.write(", " + classID);
      }

      writer.newLine();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param writer
   */
  public void writeColorMoments(BufferedWriter writer) {
    try {
      if (useClassID) {
        writer.write(imageName);
      }
      for (int i = 0; i < meanHSV.length; i++) {
        if (useClassID || i > 0) writer.write(", ");
        writer.write(String.valueOf(meanHSV[i]));
      }
      for (int i = 0; i < standardDeviation.length; i++) {
        writer.write(", ");
        writer.write(String.valueOf(standardDeviation[i]));
      }
      for (int i = 0; i < skewness.length; i++) {
        writer.write(", ");
        writer.write(String.valueOf(skewness[i]));
      }
      if (useClassLabel) {
        writer.write(", " + classID);
      }

      writer.newLine();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * @param writer
   */
  public void writeF1(BufferedWriter writer) {
    print(f1, writer);
  }

  private void print(double[] f, BufferedWriter writer) {
    if (TEXTURE) {
      try {
        if (useClassID) {
          writer.write(imageName);
        }
        for (int i = 0; i < f.length; i++) {
          if (useClassID || i > 0) writer.write(", ");
          writer.write(String.valueOf(f[i]));
        }
        if (useClassLabel) {
          writer.write(", " + classID);
        }

        writer.newLine();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @param writer
   */
  public void writeF2(BufferedWriter writer) {
    print(f2, writer);
  }

  /**
   * @param writer
   */
  public void writeF3(BufferedWriter writer) {
    print(f3, writer);
  }

  /**
   * @param writer
   */
  public void writeF4(BufferedWriter writer) {
    print(f4, writer);
  }

  /**
   * @param writer
   */
  public void writeF5(BufferedWriter writer) {
    print(f5, writer);
  }

  /**
   * @param writer
   */
  public void writeF6(BufferedWriter writer) {
    print(f6, writer);
  }

  /**
   * @param writer
   */
  public void writeF7(BufferedWriter writer) {
    print(f7, writer);
  }

  /**
   * @param writer
   */
  public void writeF8(BufferedWriter writer) {
    print(f8, writer);
  }

  /**
   * @param writer
   */
  public void writeF9(BufferedWriter writer) {
    print(f9, writer);
  }

  /**
   * @param writer
   */
  public void writeF10(BufferedWriter writer) {
    print(f10, writer);
  }

  /**
   * @param writer
   */
  public void writeF11(BufferedWriter writer) {
    print(f11, writer);
  }

  /**
   * @param writer
   */
  public void writeF12(BufferedWriter writer) {
    print(f12, writer);
  }

  /**
   * @param writer
   */
  public void writeF13(BufferedWriter writer) {
    print(f13, writer);
  }

  /**
   * Sets the name of the underlying image.
   *
   * @param imageName the name of the image to be set
   */
  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  /**
   * Sets the class id for the underlying image.
   *
   * @param classID the id of the class to be set
   */
  public void setClassID(Integer classID) {
    this.classID = classID;
  }


}
