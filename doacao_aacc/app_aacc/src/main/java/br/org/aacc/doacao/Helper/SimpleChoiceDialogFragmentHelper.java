package br.org.aacc.doacao.Helper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import br.org.aacc.doacao.Interfaces.OnCustomDialogClickListener;

/**
 * Created by ubuntu on 4/21/17.
 */

public class SimpleChoiceDialogFragmentHelper extends DialogFragment {

    private static SimpleChoiceDialogFragmentHelper simpleDialogFragmentHelper;
    private static Bundle bundle;

    private static AlertDialog.Builder builder;
    private static String title;
    private static String message;
    private static String[] itens;

    private static OnCustomDialogClickListener _onCustomDialogClickListener;

    public SimpleChoiceDialogFragmentHelper() {
        // Empty constructor required for DialogFragment
    }

    public static SimpleChoiceDialogFragmentHelper newInstance(String titulo,String message,String itens[]) {

        simpleDialogFragmentHelper = new SimpleChoiceDialogFragmentHelper();
        bundle = new Bundle();

        bundle.putString("title", titulo);
        bundle.putString("message",message);
        bundle.putCharSequenceArray("itens", itens);

        simpleDialogFragmentHelper.setArguments(bundle);

        return simpleDialogFragmentHelper;

        // Empty constructor required for DialogFragment
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        title = getArguments().getString("title");
        message =  getArguments().getString("message");
        itens = getArguments().getStringArray("itens");

        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setSingleChoiceItems(itens, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

}

