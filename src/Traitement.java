import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.noise.Speckle;

import java.util.ArrayList;
import java.util.Collections;

public class Traitement {

    public static Image addNoise(Image img, double noiseLevel) {
        //Déclarer une nouvelle image pour stocker resultat
        Image result = Speckle.exec(img, noiseLevel, 2);
        return result;
    }

    public static Image filtreMedian(Image img) {
        int largeur = img.getXDim();
        int hauteur = img.getYDim();
        int nbCanaux = img.getBDim(); // pour images couleur à plusieurs canaux
        ByteImage result = new ByteImage(largeur, hauteur, 1, 1, nbCanaux);

        // parcourir l'image
        for (int i = 0; i < nbCanaux; ++i) {
            for (int x = 0; x < result.getXDim(); x++) {
                for (int y = 0; y < result.getYDim(); y++) {
                    ArrayList<Integer> t = new ArrayList();
                    int taille = 0; // taille de la matrice
                    // visiter les voisins du pixel : ne dois pas dépasser la taille de l'image
                    for (int ligne = x - 1; ligne <= x + 1 && ligne < largeur; ligne++) {
                        for (int colonne = y - 1; colonne <= y + 1 && colonne < hauteur; colonne++) {
                            if (ligne < 0) // dépa
                                ligne++;
                            if (colonne < 0)
                                colonne++;
                            taille++;
                            t.add(img.getPixelXYBByte(ligne, colonne, i));
                        }
                    }
                    Collections.sort(t);
                    int median = t.get((t.size() + 1) / 2);
                    result.setPixelXYBByte(x, y, i, median);
                }
            }
        }
        return result;
    }

    public static double[] normaliser(double[] histo, int nbPixels) {
        for (int i = 0; i < histo.length; i++)
            histo[i] = histo[i]/nbPixels;
        return histo;
    }
}
