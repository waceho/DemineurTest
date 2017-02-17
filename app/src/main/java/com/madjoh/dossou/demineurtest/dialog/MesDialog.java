package com.madjoh.dossou.demineurtest.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madjoh.dossou.demineurtest.R;
import com.madjoh.dossou.demineurtest.demineur.MainActivity;

/**
 * Created by AmancioPCMAC on 16/02/2017.
 */

public class MesDialog  {

    private Context mContext;
    public MesDialog(Context context){
        this.mContext = context;
    }

    public void showAlert(String resultat) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // titre de la boite de dialogue
        alertDialog.setTitle(R.string.app_name);

        // Setting Dialog Message
        alertDialog
                .setMessage(resultat);
        // click button OUI
        alertDialog.setPositiveButton(R.string.oui,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)mContext).recreate();
                    }
                });

        // click button non
        alertDialog.setNegativeButton(R.string.non,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ((MainActivity)mContext).finish();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
