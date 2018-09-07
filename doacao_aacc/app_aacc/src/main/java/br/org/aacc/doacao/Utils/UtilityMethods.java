package br.org.aacc.doacao.Utils;

import android.text.TextUtils;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.org.aacc.doacao.Domain.ObjectValue.TipoCampanha;
import br.org.aacc.doacao.Domain.ObjectValue.TipoDoacao;
import br.org.aacc.doacao.Helper.TrackHelper;

/**
 * Created by Adilson on 30/12/2017.
 */

public class UtilityMethods {

    private static SimpleDateFormat _formatter;
    private static Date _date;
    private static String _dateString;
    private static TipoDoacao _tipoDoacao;
    private static String _tipoCampanha;

    public static Date ParseStringToDate(String date) throws ParseException {
        _date = null;
        _formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            _date = _formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return _date;
    }


    public static String ParseDateToString(Date date) throws ParseException {


        _formatter = new SimpleDateFormat("dd/MM/yyyy");
        _dateString=null;

        try {
            _dateString = _formatter.format(date);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return _dateString;
    }

    public static TipoDoacao EnumTipoDoacao(String tipoDoacao) {

        switch (tipoDoacao) {

            case "0":
                _tipoDoacao = TipoDoacao.Nenhum;
                break;
            case "1":
                _tipoDoacao = TipoDoacao.CupomFiscal;
                break;
            case "2":
                _tipoDoacao = TipoDoacao.PagSeguro;
                break;
            case "3":
                _tipoDoacao = TipoDoacao.PayPal;
                break;
            case "4":
                _tipoDoacao = TipoDoacao.PagSeguro_PayPal;
                break;
            default:
                _tipoDoacao = TipoDoacao.ContaBancaria;
                break;
        }
        return _tipoDoacao;

    }


    public static String getTipoCampanha(int tipoCampanha) {

        switch (tipoCampanha) {

            case 1:
                _tipoCampanha = TipoCampanha.Campanha.name();
                break;
            case 2:
                _tipoCampanha = TipoCampanha.Evento.name();
                break;
            case 3:
                _tipoCampanha = TipoCampanha.Projeto.name();
                break;
            case 4:
                _tipoCampanha = TipoCampanha.Noticia.name();
                break;
            case 5:
                _tipoCampanha = TipoCampanha.Depoimento.name();
                break;
            case 6:
                _tipoCampanha = TipoCampanha.Parceiro.name();
                break;
            case 7:
                _tipoCampanha = TipoCampanha.Voluntario.name();
                break;
            default:
                _tipoCampanha = "Desconhecida";
                break;
        }
        return _tipoCampanha;

    }


    public static boolean IsAtiva(String ativa) {
        if (ativa.indexOf("true") != -1)
            return true;
        return false;
    }

    public static boolean IsBool(String ativa) {
        if (ativa.indexOf("true") != -1)
            return true;
        return false;
    }

    public static String[] ParseStringToStringArray(String phase, int tam) {
        try {
            String[] parseWords = TextUtils.split(phase, " ");
            String[] result;
            String parseText = "";
            int k = 0;
            int j = 1;

            int fraseInteira = parseWords.length / 6;
            int fraseIncompleta = parseWords.length % 6;

            if (fraseIncompleta > 0)
                fraseInteira += 1;

            result = new String[fraseInteira];

            for (int i = 0; i < parseWords.length; i++) {
                parseText += " " + parseWords[i];
                if (j % 6 == 0) {
                    result[k] = parseText;
                    k++;
                    parseText = "";
                }
                j++;
            }

            result[k] = parseText;
            k++;
            return result;

        } catch (Exception e) {
            throw e;
        }

    }


    public static String RemoveAccent(String str) {
        try {
            if (str != null && !TextUtils.isEmpty(str))
                return Normalizer.normalize(str, Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "")
                        .replaceAll(" ", "");
        } catch (Exception e) {
            TrackHelper.WriteError(UtilityMethods.class, "RemoveCaracteresEspeciais ", e.getMessage());
        }
        return str;
    }


    public static long DiferenceMinutes(long dataStart, long dataEnd) {
        try {
            long diff = dataStart - dataEnd;
            if (diff > 0)
                return diff / (60 * 1000) % 60;

        } catch (Exception e) {
            TrackHelper.WriteError(UtilityMethods.class, "DiferenceMinutes", e.getMessage());
        }

        return 0;
    }

}
