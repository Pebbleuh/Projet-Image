package traitement;

import java.io.IOException;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.noise.Speckle;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;


public class NoiseTools {

    /**
     * Ajouter du bruit sur une image
     * @param img
     * @param noiseLevel
     * @return result
     */
    public static Image addNoise(Image img, double noiseLevel){

        //Déclarer une nouvelle image pour stocker resultat
        Image result = Speckle.exec(img, noiseLevel, 2);

        return result;
    }

}