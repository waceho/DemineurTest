package com.madjoh.dossou.demineurtest.demineur;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.madjoh.dossou.demineurtest.R;
import com.madjoh.dossou.demineurtest.ensemble.Generateur;
import com.madjoh.dossou.demineurtest.ensemble.Global;
import com.madjoh.dossou.demineurtest.ensemble.Mode;
import com.madjoh.dossou.demineurtest.ensemble.NiveauGenerateur;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NiveauGenerateur {

    // emoticon de statut du joueur
    private ImageView statutIcon;
    // class global application
    private Global global;
    // gridview contenant mes cases
    private GridView gridView;
    // tableau contenant les nombres généré aléatoirement avec les mines
    private static final ArrayList NombreGenere = new ArrayList() ;
    // tableau des cases affiché
    private static final ArrayList MirroirNombreGenere = new ArrayList() ;
    // Minuterie du jeu
    private TextView Minuterie;

    private TextView messageFinal;

    private long startTime = 0L;

    private Handler MonHandler = new Handler();

    private long TimerEnMilliseconde = 0L;

    private long timeSwapBuff = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Minuterie = (TextView)findViewById(R.id.Minuterie);
        messageFinal = (TextView)findViewById(R.id.resultat);
        statutIcon = (ImageView)findViewById(R.id.ImageEtat);

//        Global = new MethodeGlobal(getApplicationContext());
        global = (Global)getApplicationContext();
    // récupération du nombre de difficulté depuis @String -> Array
        int NombreDifficulte = getResources().getStringArray(R.array.difficulte).length;

    // Ajout des difficultés dans un tableau temporaire***************************************************************
        ArrayList TDifficulte = new ArrayList();
        TDifficulte.addAll(Arrays.asList(getResources().getStringArray(R.array.difficulte)).subList(0, NombreDifficulte));

        // Ajout du tableau dans le controleur **********************************************************
        global.setListMode(TDifficulte);

        gridView = (GridView) findViewById(R.id.grilleCase);

    }

    @Override
    public void onStart(){
        super.onStart();
        Initialisation();
    }

    @Override
    public void onResume(){
        super.onResume();
       LanceMinuterie();
        }
    @Override
    public void onRestart(){
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();
        StopMinuterie();
    }

    @Override
    public void onStop(){
        super.onStop();
        StopMinuterie();
    }

    public void CreationGrille(int NbreColonne, String mode){

        gridView.setNumColumns(NbreColonne);
        // on vide la liste précédente de numéro et de mise généré.
        NombreGenere.clear();
        MirroirNombreGenere.clear();

        Generateur D = new Generateur(getApplicationContext(),mode);

        int[][] genere = D.generateur();
        for (int[] aGenere : genere) {
            for (int j = 0; j < genere[0].length; j++) {
                if (aGenere[j] == 9) {
                    // ajout d'une mine
                    NombreGenere.add(MINE);
                    MirroirNombreGenere.add("");

                } else {
                    // ajout des numéros
                    NombreGenere.add(aGenere[j]);
                    MirroirNombreGenere.add("");
                }
            }
        }
        // set ramdom number table NombreGenere into
        global.setNumeroGenere(NombreGenere);
        // définition de la taille du nombre de case à surveiller
        global.setNombreCaseDecouvert(NombreGenere.size());
        gridView.setAdapter(  new CaseAdapter(this, MirroirNombreGenere) );
    }

    /*
     * Methode de création de la boite de dialoque avec les niveaux de difficulté *********************************************************
     * Il serait préférable de créer un modele de dialog à partir de plus de deux boites de dialogue
     */
    public void ModeDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
        View contenu = View.inflate(getApplicationContext(), R.layout.dialogmode, null);
        LinearLayout Lm = (LinearLayout)contenu.findViewById(R.id.linearL);

        // Récupération de la liste des modes de difficulté disponible
        ArrayList listmode = global.getListMode();

        // Création des boutons avec texte automatique......................................................................
        for (int i = 0 ; i < listmode.size(); i++){
            final Button btn = new Button(getApplicationContext());
            btn.setId(i+1);
            btn.setText(listmode.get(i).toString());
            Lm.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lanceFirstGame(dialog,btn.getText().toString());
                }
            });
        }

        // ajout de la vue à la boite de dialogue
        dialog.setContentView(contenu);
        dialog.setTitle(getResources().getString(R.string.titreChoixMode));
        // forcer l'utilisateur à choisir une difficulté avant le lancement d'une partie.............
        dialog.setCancelable(false);
        dialog.show();
    }

    /*
     * Methode de lancement de l'activité principal avec les cases *********************************
     * @param: dialog : a boite à afficher
     * @param : Mode : le mode
     */
    public void lanceFirstGame(Dialog dialog, String Mode){
        // récupération du mode de difficulté choisi
        global.setModeEncours(Mode);
        // congédier la boite de dialogue
        dialog.dismiss();
        // Recrée l'activité
        recreate();
    }
    // Minuterie du jeu ****************************************************************************
    private Runnable updateTimer = new Runnable() {
        public void run() {

            TimerEnMilliseconde = SystemClock.uptimeMillis() - startTime;

            long HeureMAJ = timeSwapBuff + TimerEnMilliseconde;

            int seconds = (int) (HeureMAJ / 1000);

            int minutes = seconds / 60;

            seconds = seconds % 60;
            int milliseconds = (int) (HeureMAJ % 1000);
     // formatage du code pour utilisatin dans affichage en minute et seconde*
            Minuterie.setText("" + minutes + ":"

                            + String.format("%02d", seconds) + ":"

                            + String.format("%03d", milliseconds));

            MonHandler.postDelayed(this, 0);

        }

    };
   // Methode de lancement de la minuterie *********************************************************
    public void LanceMinuterie(){
        if (global.getModeEncours()!=null) {
            startTime = SystemClock.uptimeMillis();
            MonHandler.postDelayed(updateTimer, 0);
        }
    }

    // Methode d'arrêt de la minuterie *************************************************************
    public void StopMinuterie(){
        timeSwapBuff += TimerEnMilliseconde;
        MonHandler.removeCallbacks(updateTimer);
    }

    /*
      * Methode de changement emoticon
      * @resultat : true si on tombe sur une mine***************************************************
      */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void ChangeEmoti(boolean resultat, String message){
        if (resultat){
            statutIcon.setBackground(getResources().getDrawable(R.drawable.lost));
            messageFinal.setText(message);
            // arrêt de la minuterie
            StopMinuterie();
        }
        else {
            statutIcon.setBackground(getResources().getDrawable(R.drawable.win));
            messageFinal.setText(message);
            messageFinal.setTextColor(getResources().getColor(R.color.colorGreen));
            // arrêt de la minuterie
            StopMinuterie();
        }
    }

    // Methode initial de notre applcatin
    public void Initialisation(){
        // Verification d'un mode en cours et paramétrage de la grille de case
        if (global.getModeEncours()!=null){

            if (global.getModeEncours().equals(Mode.FACILE.toString())){
                CreationGrille(FACILE,Mode.FACILE.toString());
            }

            if (global.getModeEncours().equals(Mode.NORMAL.toString())){
                CreationGrille(NORMAL,Mode.NORMAL.toString());
            }

            if (global.getModeEncours().equals(Mode.AVANCE.toString())){
                CreationGrille(AVANCE,Mode.AVANCE.toString());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                statutIcon.setBackground(getResources().getDrawable(R.drawable.neutre));
            }

        }else {
            // on force l'utilisateur à choisir une difficulté
            ModeDialog();
        }

    }

}
