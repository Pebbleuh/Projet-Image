import database.Repertoire;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Appli {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String[] images = Repertoire.getListImg();

        boolean exist = false;
        String s;
        do { // choix de l'image ref
            System.out.print("Veuillez saisir le nom d'une image (ex. 000.jpg) : ");
            s = in.nextLine(); // 000.jpg
            // on vérifie que l'image saisie existe
            for(String img : images)
                if(s.equals(img))
                    exist = true;
        } while(!exist);

        // choisir la méthode de comparaison
        String methode;
        do {
            System.out.print("Veuillez choisir la méthode de comparaison (RGB ou HSV) : ");
            methode = in.nextLine().toUpperCase(); // RGB || HSV
        } while(!(methode.equals("RGB") || methode.equals("HSV")));

        TreeMap<Double, String> imgSimilaires = Repertoire.getImgSimilaires(s, images, methode);

        Image imgRef = ImageLoader.exec("..\\images\\motos\\" + s);
        imgRef.setColor(true);
        Viewer2D.exec(imgRef); // affichage de l'image demandée

        // affichage images similaires
        Set<Double> keys = imgSimilaires.keySet();

        int i = 0;
        for(Double key: keys){
            Image img = ImageLoader.exec("..\\images\\motos\\" + imgSimilaires.get(key));
            Viewer2D.exec(img);
            System.out.println("La distance de similarité " + imgSimilaires.get(key) + " est: " + key);
            if(++i >= 10)
                break;
        }

    }
}
