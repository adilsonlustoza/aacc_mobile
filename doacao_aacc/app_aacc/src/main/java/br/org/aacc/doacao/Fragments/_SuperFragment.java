package br.org.aacc.doacao.Fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import br.org.aacc.doacao.Domain.Noticia;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.NoticiasActivity;
import br.org.aacc.doacao.R;
import br.org.aacc.doacao.Utils.HandleFile;



public class _SuperFragment extends Fragment {

    protected Bundle bundleArguments;
    protected int idCentro;
    protected String nomeCentro;
    protected String eMailCentro;
    protected boolean isAutorizado;
    protected Intent intent;
    protected JSONArray _jsonArrayResponse;
    protected JSONObject _jsonObject;
    protected HandleFile handleFile;
    private PieChart _pieChart;
    private List<PieEntry> _yValues;
    private PieDataSet _pieDataSet;
    private Description _description;
    private GenericParcelable<Noticia> noticiaGenericParcelable;
    private Bundle bundle;
    private Noticia noticia;
    private PieEntry pieEntry;
    private PieData pieData;

    public _SuperFragment() {
        // Required empty public constructor
    }

    //region ***Ciclo de Vida***


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setRetainInstance(true);
        this.InitPieChart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

  //endregion

    //region ***Metodos***
    private void InitPieChart() {

        try {

            _pieChart = getView().findViewById(R.id.pieChart);

            _pieChart.setExtraOffsets(5, 10, 5, 15);
            _pieChart.setDragDecelerationFrictionCoef(0.85f);
            _pieChart.setCenterText("Epidemologia");
            _pieChart.setDrawHoleEnabled(true);
            _pieChart.setHoleColor(Color.TRANSPARENT);
            _pieChart.setTransparentCircleRadius(15f);
            _pieChart.getLegend().setEnabled(false);

            _yValues = new ArrayList<>();
            _yValues.add(new PieEntry(25.9f, "Leucemias"));
            _yValues.add(new PieEntry(18.9f, "Cerébro - SNC"));
            _yValues.add(new PieEntry(19f, "Linfoma"));
            _yValues.add(new PieEntry(7f, "T. Ósseos"));
            _yValues.add(new PieEntry(8f, "Sarcomas P.M."));
            _yValues.add(new PieEntry(5f, "T. Wilms"));
            _yValues.add(new PieEntry(16.2f, "Outros"));

            _pieChart.animateY(4000, Easing.EasingOption.EaseInOutCubic);

            _pieDataSet = new PieDataSet(_yValues, "");

            _pieDataSet.setSliceSpace(3f);
            _pieDataSet.setSelectionShift(10f);
            _pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            _description = new Description();
            _description.setText("Principais tipos de câncer infanto juvenil.");
            _description.setTextSize(12.5f);
            _pieChart.setDescription(_description);

             pieData = new PieData(_pieDataSet);
             pieData.setValueTextSize(15f);
             pieData.setValueTextColor(Color.WHITE);
            _pieChart.setData(pieData);
            _pieChart.setUsePercentValues(true);
            _pieChart.setOnChartValueSelectedListener(new InteractionChart());

        }
        catch (Exception e)
        {
            TrackHelper.WriteError(this,"",e.getMessage());
        }

    }

    private class InteractionChart implements  OnChartValueSelectedListener{

        @Override
        public void onValueSelected(Entry e, Highlight h) {

            if(e instanceof  PieEntry)
            {
                try {
                    pieEntry = (PieEntry) e;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent = new Intent(getContext(), NoticiasActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            noticia = new Noticia();
                            noticia.setName(pieEntry.getLabel());
                            noticiaGenericParcelable = new GenericParcelable<>(noticia);
                            bundle = new Bundle();
                            bundle.putParcelable(ConstantHelper.objNoticia, noticiaGenericParcelable);
                            intent.putExtra(ConstantHelper.objBundle, bundle);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                            else
                                startActivity(intent);

                        }
                    }, ConstantHelper.OneSecond);
                }
                catch (Exception ex)
                {
                    TrackHelper.WriteError(this,"InteractionChart",ex.getMessage());
                }

            }

        }

        @Override
        public void onNothingSelected() {

        }
    }
  //endregion
}
