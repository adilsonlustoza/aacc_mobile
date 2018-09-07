package br.org.aacc.doacao.Helper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import br.org.aacc.doacao.Interfaces.OnCustomDialogClickListener;
import br.org.aacc.doacao.Utils.EnumCommand;

/**
 * Created by ubuntu on 4/21/17.
 */

public class SimpleDialogFragmentHelper extends DialogFragment {

    private static SimpleDialogFragmentHelper simpleDialogFragmentHelper;
    private static Bundle bundle;

    private static AlertDialog.Builder builder;
    private static String title;
    private static String message;

    private static OnCustomDialogClickListener _onCustomDialogClickListener;

    private EnumCommand _enumCommand;
    private boolean _exitApp;

    public SimpleDialogFragmentHelper() {
        // Empty constructor required for DialogFragment
    }

    public static SimpleDialogFragmentHelper newInstance(String titulo, String message, OnCustomDialogClickListener onCustomDialogClickListener) {

        simpleDialogFragmentHelper = new SimpleDialogFragmentHelper();
        bundle = new Bundle();

        bundle.putString("title", titulo);
        bundle.putString("message", message);

        simpleDialogFragmentHelper.setArguments(bundle);
        _onCustomDialogClickListener = onCustomDialogClickListener;
        return simpleDialogFragmentHelper;

        // Empty constructor required for DialogFragment
    }



    public void setCommand(EnumCommand enumCommand,boolean exitApp)
    {
        _enumCommand =enumCommand;
        _exitApp=exitApp;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        title = getArguments().getString("title");

        message = getArguments().getString("message");

        builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);

        builder.setMessage(message);


        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (dialog != null) {
                    if(_exitApp)
                        _onCustomDialogClickListener.onItemClick(EnumCommand.ExitApp);
                     else
                        _onCustomDialogClickListener.onItemClick(EnumCommand.CloseWindow);
                    dialog.dismiss();
                }
            }

        });

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    _onCustomDialogClickListener.onItemClick(_enumCommand);
                } catch (Exception e) {
                    TrackHelper.WriteError(this, "onCreateDialog", e.getMessage());
                }
            }
        });


        return builder.create();


    }



}

