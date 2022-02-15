import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import fr.unistra.pelican.ByteImage;
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
    public static void plotHistogram(double[] histogram) throws IOException {

        XYSeries myseries = new XYSeries("Nombre de pixels");
        for (int i = 0; i < histogram.length; i++) {
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

        // create and display a frame...
        ChartFrame frame = new ChartFrame("Projet Image", jfreechart);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * @param histogram, pathToSave
     * @throws IOException
     */
    public static void saveHistogram(double[] histogram, String pathToSave) throws IOException {

        XYSeries myseries = new XYSeries("Nombre de pixels");
        for (int i = 0; i < histogram.length; i++) {
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

        if (pathToSave != null)
            ChartUtilities.saveChartAsPNG(new File(pathToSave), jfreechart, 900, 600);
    }

    /*
     * Retourne l'histogramme d'une image couleur
     * @param img, canal
     * @return histo
     */
    public static double[] histogramme(Image img, int canal) {
        int largeur = img.getXDim();
        int hauteur = img.getYDim();
        double[] histo = new double[256];

        // initialiser l'histo
        for (double cases : histo)
            cases = 0;

        // ajouter à l'histo la valeur de chaque pixel
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                int val = img.getPixelXYBByte(x, y, canal);
                histo[val] += 1;
            }
        }

        /*try {
            plotHistogram(histo);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'affichage : " + e);
        }*/

        double discHisto[] = discretiser(histo);

        return discHisto;
    }

    public static double[] discretiser(double[] histo) {
        double[] histoResult = new double[histo.length / 2];

        // initialiser l'histo
        for (double cases : histoResult)
            cases = 0;

        for (int i = 0; i < histo.length; i++)
            histoResult[i/2] = histo[i] + histo[++i];

        return histoResult;
    }
}