package traitement;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;

import java.util.ArrayList;
import java.util.Collections;

public class Traitement {

    /**
     * Normaliser l'histo d'une image
     * @param histo
     * @param pixels le nombre de pixel de l'image
     * @return l'histo normalisé
     */
    public static double[][] normaliser(double[][] histo, int pixels){
        double[][] normal = new double[3][histo[0].length];

        for(int i = 0; i < 3; ++i) {
            for (int j = 0; j < histo[0].length; ++j) {
                normal[i][j] = histo[i][j] / pixels;
            }
        }

        return normal;
    }

    /**
     * Appliquer un filtre médian sur une image
     * @param img
     * @return l'image débruitée
     */
    public static Image filtreMedian(Image img){
        int largeur = img.getXDim();
        int hauteur = img.getYDim();
        int nbCanaux = img.getBDim();
        ByteImage newImg = new ByteImage(largeur, hauteur, 1, 1, nbCanaux);

        for(int i = 0; i < nbCanaux; ++i) {
            for (int x = 0; x < newImg.getXDim(); x++) {
                for (int y = 0; y < newImg.getYDim(); y++) {
                    ArrayList<Integer> valeur = new ArrayList();
                    for (int col = x - 1; col <= x + 1 && col < largeur; ++col) {
                        for (int ligne = y - 1; ligne <= y + 1 && ligne < hauteur; ++ligne) {
                            if (ligne < 0)
                                ++ligne;
                            if (col < 0)
                                ++col;
                            valeur.add(img.getPixelXYBByte(col, ligne, i));
                        }
                    }
                    Collections.sort(valeur);
                    int median;
                    if(valeur.size() % 2 == 0)
                        median = valeur.get((valeur.size()) / 2);
                    else
                        median = valeur.get((valeur.size() + 1) / 2);
                    newImg.setPixelXYBByte(x, y, i, median);
                }
            }
        }
        return newImg;
    }

    /**
     * Calcul la distance de similartié de deux histo
     * @param histo1
     * @param histo2
     * @return d
     */
    public static double distanceSimilarite(double[] histo1, double[] histo2) {
        double d = 0.;
        for(int i = 0; i < histo1.length; ++i){
            d += Math.pow((histo1[i] - histo2[i]), 2);
        }
        d = Math.sqrt(d);
        return d;
    }

}
