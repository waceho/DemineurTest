package com.madjoh.dossou.demineurtest.ensemble;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by AmancioPCMAC on 13/02/2017.
 */

public class Global extends Application {

    public String getModeEncours() {
        return ModeEncours;
    }

    public void setModeEncours(String modeEncours) {
        ModeEncours = modeEncours;
    }

    private String ModeEncours ;

    public ArrayList getListMode() {
        return ListMode;
    }

    public void setListMode(ArrayList listMode) {
        ListMode = listMode;
    }

    // tableau des modes de difficulte
    private ArrayList ListMode;

    public ArrayList getNumeroGenere() {
        return NumeroGenere;
    }

    public void setNumeroGenere(ArrayList numeroGenere) {
        NumeroGenere = numeroGenere;
    }
    // tableau des numéros obtenu aléatoirement
    private ArrayList NumeroGenere;

    public int getNombreCaseDecouvert() {
        return NombreCaseDecouvert;
    }

    public void setNombreCaseDecouvert(int nombreCaseDecouvert) {
        NombreCaseDecouvert = nombreCaseDecouvert;
    }

    private int NombreCaseDecouvert;
}
