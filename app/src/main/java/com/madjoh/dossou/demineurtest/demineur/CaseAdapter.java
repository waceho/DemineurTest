package com.madjoh.dossou.demineurtest.demineur;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.madjoh.dossou.demineurtest.R;
import com.madjoh.dossou.demineurtest.dialog.MesDialog;
import com.madjoh.dossou.demineurtest.ensemble.Global;
import com.madjoh.dossou.demineurtest.ensemble.NiveauGenerateur;

import java.util.ArrayList;

/**
 * Created by AmancioPCMAC on 13/02/2017.
 *
 * Cette class gère le gridlayout
 * contient également les methodes d'évenement après clique sur une case
 */

class CaseAdapter extends BaseAdapter implements NiveauGenerateur {

    private Context context;
    private final ArrayList gridValues;
    private Global global;
    private Drawable drawable;

    // Constructeur et initialisation des valeurs
    CaseAdapter(Context context, ArrayList gridValues) {
        this.context        = context;
        this.gridValues     = gridValues;
        global = (Global)this.context.getApplicationContext();
    }

    @Override
    public int getCount() {

        return gridValues.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        // LayoutInflator pour le payout card_view_list.xml
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (view == null) {
            // définition du layout de notre case
            gridView = inflater.inflate( R.layout.card_view_list , null);
            suiviJeu(i, gridView);

        } else {

            gridView = view;
        }

        return gridView;

    }

    /*
     * Methode de suivi du jeu *******************************************************************************************
     * @param i : position de la case
     * @param gridView : le layout contenant les cases
     */
    private void suiviJeu(final int i, View gridView){
    // initialisattion du bouton de la case
    final Button caseB = (Button) gridView.findViewById(R.id.CaseButton);

    // On gère l'évenement après click et découverte de la case
    caseB.setOnClickListener(new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            // si on tomde sur une mine, on affiche le resultat
            if (global.getNumeroGenere().get(i).toString().equals(MINE)){
                drawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
                caseB.setBackground(drawable);
                // envoie true quand on tombe sur une mine
                ((MainActivity)context).ChangeEmoti(true,PERDU);
                // affichage de la boite de dialogue pour reprendre ou quitter
                ShowAlert();
            }

            // si on tombe sur un autre numéro
            if(!global.getNumeroGenere().get(i).toString().equals(MINE)){
                // cas d'une case vide
                if (global.getNumeroGenere().get(i).toString().equals(VIDE))
                {
                    // on change juste la couleur de fond de la case
                    caseB.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));

                }else {
                    // cas d'un numéro autre numéro  .............................
                    caseB.setText(global.getNumeroGenere().get(i).toString());
                    caseB.setTextSize(18);
                    caseB.setBackgroundColor(context.getResources().getColor(R.color.black_overlay));
                }
                // comptage du nombre de case découvert et
                compteurCase();

            }
        }
    });

    // long click pour affichage de drapeau
    caseB.setOnLongClickListener(new View.OnLongClickListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onLongClick(View view) {
            drawable = context.getResources().getDrawable(R.drawable.drapeau);
            caseB.setBackground(drawable);
            compteurCase();
            return true;
        }
    });
}
    // Methode d'affichage de l'alerte ***********************************************************************************************************
    private void ShowAlert(){
        MesDialog dialogeur = new MesDialog(context);
        // récupération du message de proposition de relance ou quitter
        String message = context.getResources().getString(R.string.FinJeuMessage);
        dialogeur.showAlert(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    // cette methode compte le nombre de case découvert et affiche le message gagne *************************************************************
    private void compteurCase(){
        if (global.getNombreCaseDecouvert() != 0){
            // on déduit le nombre de case découvert du restant, si le nombre de case
           global.setNombreCaseDecouvert(global.getNombreCaseDecouvert()-1);
            if ((global.getNombreCaseDecouvert() - 8) == 0){

                ((MainActivity)context).ChangeEmoti(false,GAGNE);
                ShowAlert();
            }
        }


    }

}
