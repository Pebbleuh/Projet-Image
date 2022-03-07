import database.Repertoire;
import javax.swing.*;
import java.awt.*;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Appli {
    public static void main(String[] args) {
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

        // Création GUI
        JFrame frame = new JFrame("Comparaison d'images");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.add(new JLabel(new ImageIcon(".\\images\\motos\\" + s)));

        // Créer des boutons radio
        ButtonGroup group = new ButtonGroup();
        JRadioButton radio1 = new JRadioButton("RGB", true);
        JRadioButton radio2 = new JRadioButton("HSV", false);
        group.add(radio1); // Ajouter les boutons radio au groupe
        group.add(radio2);
        JLabel nomImg = new JLabel("Nom de l'image : " + s + "\n");
        JLabel choixMethode = new JLabel("Choisissez la méthode de comparaison : \n", JLabel.CENTER);
        JButton btnCalcul = new JButton("Calculer");

        // Création de panels + ajout des composants aux panels
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(nomImg);
        panel2.add(choixMethode);
        panel2.add(radio1);
        panel2.add(radio2);
        panel2.add(btnCalcul);
        
        // Ajouter panels au frame
        frame.setLayout(new GridLayout(2,2));
        frame.add(panel1);
        frame.add(panel2);
        frame.pack();

        String finalS = s; // nom de l'image à utiliser dans l'action
        String methode = ""; // choisir la méthode de comparaison
        
        // Action déclenchée par btnCalcul
        btnCalcul.addActionListener(e -> { // équivalent à new ActionListener()
            Object source = e.getSource(); // verifier la source de l'action
            if (source == btnCalcul) { // action a effectuer
                if (radio1.isSelected()) {
                    methode.equals("RGB");
                } else if (radio2.isSelected()) {
                    methode.equals("HSV");
                }
                TreeMap<Double, String> imgSimilaires = Repertoire.getImgSimilaires(finalS, images, methode);

                // affichage images similaires
                Set<Double> keys = imgSimilaires.keySet();
                int i = 0;
                for(Double key: keys){
                    frame.add(new JLabel(new ImageIcon(".\\images\\motos\\"
                            + imgSimilaires.get(key))));
                    System.out.println("La distance de similarité " + imgSimilaires.get(key) + " est: " + key);
                    if(++i >= 10)
                        break;
                }
            }
        });

    }
}
