import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

import java.io.IOException;
import java.util.Scanner;

public class Appli {

    public static void main(String[] args) throws IOException {
        Image test1 = ImageLoader.exec("Z:\\projetImage\\motos\\motos\\000.jpg");
        Image bruitee = lecture();
        bruitee.setColor(true);
        Viewer2D.exec(test1);
        Viewer2D.exec(bruitee);
        int nbPixels = test1.getXDim() * test1.getYDim();
        for (int i = 0; i <= 2; i++) {
            double[] histo = HistogramTools.histogramme(test1, i);
            histo = HistogramTools.discretiser(histo);
            histo = Traitement.normaliser(histo, nbPixels);
            //HistogramTools.plotHistogram(histo);
        }
    }

    /*
     * Lit une image et lui applique le filtre médian
     */
    private static Image lecture() {
        //Charger une image en mémoire
        Scanner in = new Scanner(System.in);
        System.out.print("Entrez le chemin de l'image : ");
        String s = in.nextLine();
        Image img = ImageLoader.exec(s);
        img = Traitement.addNoise(img, 0.2);
        img = Traitement.filtreMedian(img);
        return img;
    }
}