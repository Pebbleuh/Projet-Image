package histogramme;

import traitement.Traitement;
import java.awt.Color;
import java.io.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import fr.unistra.pelican.Image;

public class HistogramTools {


    /**
     * @param histogram
     * @throws IOException
     */
    public static void plotHistogram(double [] histogram) throws IOException{

        XYSeries myseries = new XYSeries("Nombre de pixels");
        for(int i=0;i<histogram.length;i++){
            myseries.add(new Double(i), new Double(histogram[i]));
        }
        XYSeriesCollection myseriescollection = new XYSeriesCollection(myseries);

        JFreeChart jfreechart = ChartFactory.createXYBarChart("Histogramme de l'image", "Valeur pixel", false, "Nombre de pixels", myseriescollection, PlotOrientation.VERTICAL, true, false, false);
        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = jfreechart.getXYPlot();

        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setRangeGridlinePaint(Color.white);
        NumberAxis axis = (NumberAxis) xyplot.getDomainAxis();

        axis.setLowerMargin(0);
        axis.setUpperMargin(0);

        // create and display a frame...
        ChartFrame frame = new ChartFrame("DUT 2 Informatique - Image", jfreechart);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * @param histogram
     * @param pathToSave
     * @throws IOException
     */
    public static void saveHistogram(double [] histogram, String pathToSave) throws IOException{

        XYSeries myseries = new XYSeries("Nombre de pixels");
        for(int i=0;i<histogram.length;i++){
            myseries.add(new Double(i), new Double(histogram[i]));
        }
        XYSeriesCollection myseriescollection = new XYSeriesCollection(myseries);

        JFreeChart jfreechart = ChartFactory.createXYBarChart("Histogramme de l'image", "Niveaux de gris", false, "Nombre de pixels", myseriescollection, PlotOrientation.VERTICAL, true, false, false);
        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = jfreechart.getXYPlot();

        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setRangeGridlinePaint(Color.white);
        NumberAxis axis = (NumberAxis) xyplot.getDomainAxis();

        axis.setLowerMargin(0);
        axis.setUpperMargin(0);

        if(pathToSave!=null)
            ChartUtilities.saveChartAsPNG(new File(pathToSave), jfreechart, 900, 600);
    }

    /**
     * Calcul l'histo d'une image selon la méthode voulu
     * @param img l'image
     * @param methode RGB ou HSV
     * @return histo
     */
    public static double[][] getHisto(Image img, String methode){
        if(methode.equals("RGB")) {
            double[][] histo = creerHistoRGB(img);
            histo = HistogramTools.discretiser(histo);
            return Traitement.normaliser(histo, img.getXDim() * img.getYDim());
        }
        else
            return creerHistoHSV(img);
    }

    /**
     * Calcule l'histo RGB d'une image
     * @param img l'image
     * @return histoRGB
     */
    public static double[][] creerHistoRGB(Image img){
        int largeur = img.getXDim();
        int hauteur = img.getYDim();
        double[][] histo = new double[3][256];

        // init histo à 0
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < histo[0].length; ++j)
                histo[i][j] = 0;
        }

        for(int i = 0; i < 3; ++i) {
            for (int x = 0; x < largeur; x++) {
                for (int y = 0; y < hauteur; y++) {
                    int val = img.getPixelXYBByte(x, y, i);
                    histo[i][val] += 1;
                }
            }
        }

        return histo;
    }

    /**
     * Calcul histo HSV d'une image
     * @param img l'image
     * @return histoHSV
     */
    public static double[][] creerHistoHSV(Image img){
        int largeur = img.getXDim();
        int hauteur = img.getYDim();

        double[][] histo = new double[3][];
        histo[0] = new double[361];
        histo[1] = new double[101];
        histo[2] = new double[101];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                int r = img.getPixelXYBByte(x, y, 0);
                int g = img.getPixelXYBByte(x, y, 1);
                int b = img.getPixelXYBByte(x, y, 2);

                double max = Math.max(Math.max(r, g), b);
                double min = Math.min(Math.min(r, g), b);

                int v = (int) ((max / 255) * 100);
                int s = 0;
                if(max > 0)
                    s = (int) ((1 - min / max) * 100);
                int h = (int) (Math.acos((r - (1/2) * g - (1/2) * b) / Math.sqrt(r*r + g*g + b*b + r*g + r*b + g*b)) * 100);
                if(b > g)
                    h = 360 - h;

                ++histo[0][h];
                ++histo[1][s];
                ++histo[2][v];
            }
        }
        return histo;
    }

    /**
     * Discrétiser un histo
     * @param histo
     * @return l'histo discrétisé
     */
    public static double[][] discretiser(double[][] histo){
        double[][] result = new double[3][histo[0].length/2];

        for(int i = 0; i < 3 ; ++i) {
            for (int j = 0; j < histo[0].length; ++j) {
                result[i][j / 2] = histo[i][j] + histo[i][++j];
            }
        }

        return result;
    }

}