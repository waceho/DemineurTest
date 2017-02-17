package com.madjoh.dossou.demineurtest.ensemble;

import android.content.Context;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by AmancioPCMAC on 13/02/2017.
 */

public class Generateur implements NiveauGenerateur{


    private String dificulte;
    private int[][] genere;

    // constructeur *****************************************
    public Generateur(Context context, String Modedifficulte){
        Global application = (Global) context.getApplicationContext();
       this.dificulte = Modedifficulte;

    }

    /*
     * Cette methode genere le tableau selon le niveau de difficulté
     *  return un tableau à double entrée
     */
    public int[][] generateur(){
        int[][] Finalgenerer = null;


            if(this.dificulte.equals(Mode.FACILE.toString())){
                this.genere = new int[FACILE][FACILE];
            }

            if(this.dificulte.equals(Mode.NORMAL.toString())){
                this.genere = new int[NORMAL][NORMAL];
            }

            if(this.dificulte.equals(Mode.AVANCE.toString())){
                this.genere = new int[AVANCE][AVANCE];
            }


        String str;
        try {
            str = generateString(this.genere.length*this.genere[0].length,NbreMine);
            Finalgenerer = NbreMinesParSquar(diviseEnTab(this.genere.length,str));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Generateur.class.getName()).log(Level.SEVERE, null, ex);
        }


        return Finalgenerer;
    }
    // cette methode genere les indices aléatoirement ***************************************************************************
    private static String generateString(int taille,int Nombre) throws NoSuchAlgorithmException{
        String texte = "";
        // les indices
        Vector<Integer> v = new Vector<>();
        //Contient les indices aléatoire
        Vector<Integer> w = new Vector<>();
        int compt = Nombre;
        int randInt;

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(random.generateSeed(compt));

        for(int i = 0; i < taille; i++){
            v.add(i);
            w.add(0);
        }

        for(int i = 0; i < taille; i++){
            if(compt>0){
                taille = v.size();
                randInt = random.nextInt(taille);
                w.set(v.get(randInt),1);
                v.removeElementAt(randInt);
                v.trimToSize();
                --compt;

            }else{
                taille = v.size();
                randInt = random.nextInt(taille);
                w.set(v.get(randInt),0);
                v.removeElementAt(randInt);
                v.trimToSize();
            }
        }
        for(int i = 0; i< w.size();i++){
            texte+=w.get(i);
        }
        return texte;
    }
    // division en tableau *************************************************************************
    private static int[][] diviseEnTab(int length, String str){
        int[][] mines = new int[length][length];
        String s;
        for(int i = 0;i<length;i++){
            s = str.substring(i*length, (i+1)*length);
            for(int j = 0; j<s.length();j++){
                if((s.charAt(j)+"").compareTo("1")==0 ){
                    mines[i][j] = 1;
                }else{
                    mines[i][j] = 0;
                }
            }
        }
        return mines;
    }
  // return le nombre de mines par squar*************************************************************
    private static int[][] NbreMinesParSquar(int[][] mines){
        int[][] num = new int[mines.length][mines[0].length];
        int[][] approx = new int[mines.length+2][mines[0].length+2];
        int[][] NbreMines = new int[mines.length+2][mines[0].length+2];
        for(int i = 1;i<NbreMines.length-1;i++){
            System.arraycopy(mines[i - 1], 0, NbreMines[i], 1, NbreMines[0].length - 1 - 1);
        }

        for(int i = 1 ; i < NbreMines.length-1 ; i++ ){
            for(int j = 1 ; j < NbreMines.length-1 ; j++ ){
                approx[i][j] = 0;
                for(int k = i-1 ; k <= i+1 ; k++ ){
                    for(int l = j-1 ; l <= j+1 ; l++ ){
                        if(k==i && l==j){
                            // ne rien faire //
                        }else{
                            approx[i][j] +=((NbreMines[k][l] != 0 )?1:0);
                        }

                    }
                }
                if(NbreMines[i][j]==1){
                    num[i-1][j-1] = 9;
                }else{
                    num[i-1][j-1] = approx[i][j];
                }

                approx[i][j] = 0;
            }
        }
        return num;
    }


}
