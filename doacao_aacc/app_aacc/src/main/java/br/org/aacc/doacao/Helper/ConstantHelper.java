package br.org.aacc.doacao.Helper;

/**
 * Created by ubuntu on 12/3/16.
 */
public class ConstantHelper {

    public static boolean isLogado;
    public static boolean isLogadoRedesSociais;
    public static boolean foiTrocadaImagemPerfil;
    public static int login=1;
    public static boolean isStartedAplication=true;
    public static final String _urlPagSeguro="https://pagseguro.uol.com.br/checkout/v2/donation.html";
    public static final String _urlPayPal="https://www.paypal.com/cgi-bin/webscr ";
    public static final String _urlPolitica="http://www.adilsonlustoza.com/Analista/Programador/Politica";

    public static String pref_atualizar="pref_atualizar";
    public static String AppName="Doação AACC";

    public static int SplashScreenTime=4000;
    public static int OneSecond = 1000;
    public static int OneMinute = 1000 * 60;

    public static  String ToolbarSubTitleNoticias="Notícias";
    public static  String ToolbarSubTitleVisaoDeRua="Visão de rua";
    public static  String ToolbarSubTitleBazares="Bazares";
    public static  String ToolbarSubTitleCaccc="Entidades";
    public static  String ToolbarSubTitleDoacao="Faça sua doação";
    public static  String ToolbarSubTitleSuper="Painel de bordo";
    public static  String ToolbarSubTitlePreferencia="Preferências";
    public static  String ToolbarSubTitlePagSeguro="Integração PagSeguro";
    public static  String ToolbarSubTitleLogin="Identificação básica";
    public static  String ToolbarSubTitleBemVindo="Acessar aplicativo";
    public static  String ToolbarSubTitlePerfil="Perfil";
    public static  String ToolbarSubTitleAjuda="Ajuda";

    public static  String TabSobre="Sobre";
    public static  String TabBoletim="Panfleto";
    public static  String TabRetirar="Correio";

    public static String IntentStartAllServices="doacao.mais.start.services";

    public  static String emailCacccTest="adilsonlustoza@gmail.com";
    public  static String emailApp="doacaomais@gmail.com";
    public  static String emailPasswordApp="doacaomais@451";
    public  static String emailToTest="adilson1979@hotmail.com";
    public  static String objBundle="objBundle";

    public  static String emailSubjectRetirada="Você recebeu uma mensagem através do App AACC";

    public  static String objCaccc="objCaccc";
    public  static String objBazar="objBazar";
    public  static String objNoticia="objNoticia";

    public  static String objActivity="objActivity";

    public static String urlWebApiListAllNotificacoes="http://doacaomais.adilsonlustoza.com/Analista/Programador/Notificacao/ListarNotificacoesAtivas";

    public  static String urlWebApiListAllCaccc="http://doacaomais.adilsonlustoza.com/Analista/Programador/Caccc/ListarCaccc";
    public static  String urlWebApiConteudoContasPorCaccc ="http://10.0.2.2/Appointment.WebApi/Analista/Programador/Caccc/ConteudoContasPorCaccc/{0}";

    public  static String fileConteudoContasPorCaccc="fileConteudoContasPorCaccc.json";
    public  static String fileListAllCaccc="fileListAllCaccc.json";

    public  static String urlWebApiListAllCacccBazar="http://doacaomais.adilsonlustoza.com/Analista/Programador/Caccc/ListarCacccBazares";
    public static String urlWebApiListAllBazaresPorCaccc="http://10.0.2.2/Appointment.WebApi/Analista/Programador/Bazar/ListarBazarPorCacccId/{0}";

    public  static String fileListAllCacccBazar="fileListAllCacccBazar.json";
    public  static String fileListAllBazaresPorCaccc="fileListAllBazaresPorCaccc.json";

    public  static String urlWebApiListAllBazar="http://doacaomais.adilsonlustoza.com/Analista/Programador/Bazar/ListarBazar";
    public  static String fileListAllBazar="fileListAllBazar.json";

    public  static String urlWebApiListAllNoticia="http://doacaomais.adilsonlustoza.com/Analista/Programador/Noticia/ListarNoticia";
    public  static String fileListAllNoticia="fileListAllNoticia.json";

    public  static String urlWebApiListarNoticiasGeraisPorCacccId="http://10.0.2.2/Appointment.WebApi/Analista/Programador/Noticia/ListarNoticiasGeraisPorCacccId/{0}";
    public  static String fileListListarNoticiasGeraisPorCacccId="fileListNoticiasGeraisPorCacccId.json";


    public  static final String  urlWebApiListAllCampanhas="http://doacaomais.adilsonlustoza.com/Analista/Programador/Boletim/ListarBoletim/";
    public  static final String  fileListAllCampanhas="fileListAllBoletim.json";

    public  static final String urlWebApiListarConteudoContasPorCaccc="http://doacaomais.adilsonlustoza.com/Analista/Programador/Caccc/ListarConteudoContasPorCaccc/";
    public  static final String fileListarConteudoContasPorCaccc="fileListarConteudoContasPorCaccc.json";


    public static final String CEP_MASK="#####-###";
    public static final String CEL_MASK="(##)#####-####";
    public static final String TEL_MASK="(##)####-####";


}
