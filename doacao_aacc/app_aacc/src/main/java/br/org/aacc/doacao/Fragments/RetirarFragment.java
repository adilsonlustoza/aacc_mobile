package br.org.aacc.doacao.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.PrefHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.R;
import br.org.aacc.doacao.TabsCacccActivity;
import br.org.aacc.doacao.Utils.MaskWatcher;
import br.org.aacc.doacao.Utils.UtilApplication;
import cn.carbs.android.library.MDDialog;

import static br.org.aacc.doacao.Helper.ConstantHelper.CEL_MASK;
import static br.org.aacc.doacao.Helper.ConstantHelper.TEL_MASK;


public class RetirarFragment extends _SuperFragment {

    private static final String TAG = "RetirarFragment";
    private View view;
    private StringBuilder _mensagemValidation;

    private StringBuilder stringBuilder;
    private GenericParcelable<Caccc> cacccGenericParcelable;

    private EditText editTextNome;
    private EditText editTexTelefone;
    private EditText editTextCelular;


    public RetirarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retirar, container, false);

    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.Init(bundle);

    }

    private void Init(Bundle bundle) {

        try {

            (getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            editTexTelefone = getView().findViewById(R.id.txtTelefone);
            editTexTelefone.addTextChangedListener(new MaskWatcher(TEL_MASK, '-'));

            editTextCelular = getView().findViewById(R.id.txtCelular);
            editTextCelular.addTextChangedListener(new MaskWatcher(CEL_MASK, '-'));
            editTextCelular.setText(getCellPhoneNumber());

            this.editTextNome = getView().findViewById(R.id.txtResponsavel);
            this.editTextNome.setText(PrefHelper.getString(getContext(), PrefHelper.PreferenciaNome));

            bundleArguments = this.getArguments();


            bundleArguments = this.getArguments();

            if (bundleArguments != null)
                cacccGenericParcelable = bundleArguments.getParcelable(ConstantHelper.objCaccc);
            else
            {
                cacccUtilApplication= (UtilApplication<String, GenericParcelable<Caccc>>) getActivity().getApplicationContext();
                cacccGenericParcelable =  cacccUtilApplication.getElementElementDictionary(ConstantHelper.objCaccc);
            }


            if (cacccGenericParcelable != null) {

                idCentro = cacccGenericParcelable.getValue().getId();
                eMailCentro = cacccGenericParcelable.getValue().getEmail();
                nomeCentro = cacccGenericParcelable.getValue().getName();
                isAutorizado = cacccGenericParcelable.getValue().isAutorizado();
                if (!isAutorizado)
                    eMailCentro = ConstantHelper.emailCacccTest;
            }

            this.ConfigFab();
            this.ConfigStateRemove();
        } catch (Exception e) {
            TrackHelper.WriteError(this, "Init", e.getMessage());
        }
    }

    private String FillValidateForm() {
        try {

            String spinnerTipoRetirada = ((Spinner) getView().findViewById(R.id.spinnerTipoRetirada)).getSelectedItem().toString();
            String txtResponsavel = ((TextView) getView().findViewById(R.id.txtResponsavel)).getText().toString();
            String txtTelefone = ((TextView) getView().findViewById(R.id.txtTelefone)).getText().toString();
            String txtCelular = ((TextView) getView().findViewById(R.id.txtCelular)).getText().toString();
            String txtObservacao = ((TextView) getView().findViewById(R.id.txtObservacao)).getText().toString();

            view = getLayoutInflater().inflate(R.layout.custom_dialog_fragment_retiradal, null);

            ((TextView) view.findViewById(R.id.lblTipoRetirada)).setText(spinnerTipoRetirada);
            ((TextView) view.findViewById(R.id.lblNome)).setText(txtResponsavel);
            ((TextView) view.findViewById(R.id.lblTelefone)).setText(txtTelefone);
            ((TextView) view.findViewById(R.id.lblCelular)).setText(txtCelular);
            ((TextView) view.findViewById(R.id.lblObservacao)).setText(txtObservacao);


            stringBuilder = new StringBuilder();

            stringBuilder.append("Olá, " + nomeCentro + ", observe a mensagem a seguir para fornecer mais informações.").append("\n\n");

            stringBuilder.append("Dados do usuário").append("\n\n");

            stringBuilder.append("Tipo mensagem :  " + spinnerTipoRetirada).append('\n');
            stringBuilder.append("Nome          :  " + txtResponsavel).append('\n');
            stringBuilder.append("Telefone      :  " + txtTelefone).append('\n');
            stringBuilder.append("Celular       :  " + txtCelular).append('\n');
            stringBuilder.append("Mensagem      :  " + txtObservacao).append("\n\n");

            return stringBuilder.toString();

        } catch (Exception e) {
            TrackHelper.WriteError(this, "GetDataFromForm", e.getMessage());
        }

        return null;
    }


    private void ConfigStateRemove() {
        try {
            /*
            spinnerEstadoRetirar = getView().findViewById(R.id.spinnerEstado);
            ArrayAdapter simpleAdapter = (ArrayAdapter) spinnerEstadoRetirar.getAdapter();
            for (int i = 0; i < simpleAdapter.getCount(); i++) {
                String value = simpleAdapter.getItem(i).toString();
                if (value.contains(EstadoPadrao))
                    spinnerEstadoRetirar.setSelection(i);
                    */


        } catch (Exception e) {
            throw e;
        }
    }


    private void ConfigFab() {
        try {
            FloatingActionButton floatingActionButton = getView().findViewById(R.id.fab_agenda);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ExibiDadosRetirada();
                }


            });
        } catch (Exception e) {
            throw e;
        }

    }

    private void ExibiDadosRetirada() {

        try {

            String _mensagemLenght= ((TextView) getView().findViewById(R.id.txtObservacao)).getText().toString();

            _mensagemValidation = new StringBuilder();
            int _count = 0;
            final String _dataForm = this.FillValidateForm();
            String[] _validateDataForm = _dataForm.split("\n");

            _mensagemValidation.append("Campo(s) requerido(s) !").append("\n");

            for (String fields : _validateDataForm) {

                String[] _mensagemCheck = fields.split(":");

                if (_mensagemCheck.length > 0) {
                    if (_mensagemCheck[0].length() < 3)
                        continue;

                    if (_count > 4)
                        break;

                    if (
                            _mensagemCheck[0].toLowerCase().contains("cep") ||
                                    _mensagemCheck[0].toLowerCase().contains("tipo") ||
                                    _mensagemCheck[0].toLowerCase().contains("dados") ||
                                    _mensagemCheck[0].toLowerCase().contains("data") ||
                                    _mensagemCheck[0].toLowerCase().contains("fone") ||
                                    _mensagemCheck[0].toLowerCase().contains("per")


                            )
                        continue;

                    if (_mensagemCheck.length > 1) {
                        if (_mensagemCheck[0].equals(""))
                            continue;

                        if (_mensagemCheck[1].length() < 4) {
                            _count += 1;
                            _mensagemValidation.append(_mensagemCheck[0]).append(", ");
                        }
                    }
                }

            }

            if (_count > 0 || _mensagemLenght.length() <25 ) {
                String _mensagem = _mensagemValidation.toString()
                        .trim()
                        //  .replaceFirst(",", "")
                        .replace(",,", ",")
                        .replace("  ", " ");


                _mensagem = _mensagem.substring(0, _mensagem.length() - 1);

               if(_mensagemLenght.length()<25)
                   _mensagem = "A mensagem deve possuir no mínimo 25 caracteres.";

                Snackbar snackbar = Snackbar
                        .make(getView(), _mensagem, Snackbar.LENGTH_LONG);
                snackbar.show();
                return;
            }


            new MDDialog.Builder(getActivity())
                    .setTitle("Confira os dados de envio")
                    .setContentView(view)
                    .setBackgroundCornerRadius(15)
                    .setIcon(R.drawable.smartphone_mail_24)
                    .setContentTextColor(R.color.color_grey)
                    .setPrimaryTextColor(R.color.color_grey)
                    .setNegativeButton("Cancelar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setPositiveButton("Enviar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TabsCacccActivity) getActivity())
                                    .BackgroundSendMailNoAtach(
                                            getActivity(),
                                            eMailCentro,
                                            _dataForm
                                    );
                        }

                    })
                    .create()
                    .show();


        } catch (Exception e) {

            TrackHelper.WriteError(this, "ExibiDadosRetirada", e.getMessage());
        }
    }


    private String getCellPhoneNumber() {
        String mPhoneNumber = null;
        int tam;

        try {
            TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                mPhoneNumber = "99999999999";

            mPhoneNumber = tMgr.getLine1Number();

            tam = mPhoneNumber.length();

            try {
                if (!TextUtils.isEmpty(mPhoneNumber) && tam >= 9)
                    mPhoneNumber = "(11)" + mPhoneNumber.substring(tam - 10, tam - 5) + "-" + mPhoneNumber.substring(tam - 4, tam);
            }
            catch(Exception e)
            {
                TrackHelper.WriteError(this, "Error to get Number of Cell", e.getMessage());

            }

        } catch (Exception e) {
            TrackHelper.WriteError(this, "getCellPhoneNumber", e.getMessage());
        }


        return mPhoneNumber;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


}


