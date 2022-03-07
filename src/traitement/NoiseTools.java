package traitement;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.noise.Speckle;

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