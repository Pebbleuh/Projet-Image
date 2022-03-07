package database;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import histogramme.HistogramTools;
import traitement.Traitement;

import java.io.*;
import java.util.Scanner;
import java.util.TreeMap;

public class Repertoire {

    /**
     * Récupère la liste d'images d'un répertoire
     * @return la liste d'img
     */
    public static String[] getListImg() {
        File repertoire = new File("..\\images\\motos");
        return repertoire.list();
    }

    /**
     * Recherche dans un répertoire
     * les images similaires à une image référence
     * @param s nom de l'image de référence
     * @param listeImg listes d'images
     * @param methode RGB ou HSV
     * @return imgSimilaires
     */
    public static TreeMap<Double, String> getImgSimilaires(String s, String[] listeImg, String methode){
        TreeMap<Double, String> imgSimilaires = new TreeMap<>();

        Image imgRef = ImageLoader.exec("..\\images\\motos\\" + s);
        int nbPixels = imgRef.getXDim() * imgRef.getYDim();
        imgRef = Traitement.filtreMedian(imgRef);
        double seuil = methode.equals("RGB")?0.8:200000;

        // créer histo de l'image ref
        double[][] histoRef = getHistoDB(s, methode);
        if(histoRef == null) {
            histoRef = HistogramTools.getHisto(imgRef, methode);
            stockHisto(s, histoRef, methode);
        }

        // on parcours le repertoire
        for (int i = 0; i < listeImg.length; i++) {
            if(!s.equals(listeImg[i])) { // ne pas calculer l'image demandée
                Image img = ImageLoader.exec("..\\images\\motos\\" + listeImg[i]);
                img = Traitement.filtreMedian(img);

                // calcul histo des images du rep
                double similarite = 0.;

                double[][] histo = getHistoDB(listeImg[i], methode);
                if(histo == null) {
                    histo = HistogramTools.getHisto(img, methode);
                    stockHisto(listeImg[i], histo, methode);
                }

                // comparaison histoRef et histo
                for (int j = 0; j < 3; j++) {
                    similarite += Traitement.distanceSimilarite(histoRef[j],histo[j]);
                }

                // si dépasse le seuil de similarité on enregistre pas l'image
                if(similarite < seuil)
                    imgSimilaires.put(similarite, listeImg[i]);
            }
        }

        return imgSimilaires;
    }

    /**
     * Converti un histo en string
     * @param img nom de l'image
     * @param histo
     * @return s
     */
    public static String histoToString(String img, double[][] histo){
        String s = img + "\n";
        for(int i = 0; i < 3; ++i) {
            for (int j = 0; j < histo[i].length; ++j) {
                s += histo[i][j];
                if(j != histo[i].length - 1)
                    s += ";";
            }
            if(i < 2)
                s += "\n";
        }
        return s;
    }

    /**
     * Converti un String en histo
     * @param s
     * @return histo
     */
    public static double[] stringToHisto(String s) {
        if(s != null){
            String[] histoString = s.split(";"); // histo de la bd
            double[] histo = new double[histoString.length];
            for (int i = 0; i < histoString.length; ++i) {
                histo[i] = Double.parseDouble(histoString[i]);
            }
            return histo;
        }
        return null;
    }

    /**
     * Stock un histo dans un fichier .txt
     * @param img nom de l'image
     * @param histo
     * @param methode RGB ou HSV
     */
    public static void stockHisto(String img, double[][] histo, String methode) {
        String name = "histo" + methode + ".txt";
        try {
            File file = new File(name);
            String s = histoToString(img, histo);

            // créer le fichier s'il n'existe pas
            if (!file.exists())
                file.createNewFile();
            else
                s = "\n" + s;

            FileWriter fw = new FileWriter(name, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère l'histo d'une image dans un fichier .txt
     * @param img nom de l'image
     * @param methode RBG ou HSV
     * @return histo ou null
     */
    public static double[][] getHistoDB(String img, String methode) {
        String name = "histo" + methode + ".txt";
        int i = 1;
        String line = new String();
        double[][] histo = new double[3][];
        boolean exist = false;
        try {
            File fichier = new File(name);
            if(!fichier.exists())
                return null;
            //lire le fichier
            FileInputStream file = new FileInputStream(fichier);
            Scanner scanner = new Scanner(file);

            // parcourir le fichier
            while(scanner.hasNextLine()) {
                // regarder uniquement le nom des images
                if (i % 5 == 0)
                    line = scanner.nextLine();
                else
                    scanner.nextLine();
                ++i;
                // on s'arrete après avoir trouvé l'image
                if (line.equals(img)) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                histo[0] = stringToHisto(scanner.nextLine());
                histo[1] = stringToHisto(scanner.nextLine());
                histo[2] = stringToHisto(scanner.nextLine());
                return histo;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}