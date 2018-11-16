package br.org.aacc.doacao;


import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import br.org.aacc.doacao.Adapter.TabFragmentAdapterContainer;
import br.org.aacc.doacao.Api.CircleTransformPicasso;
import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Utils.UtilApplication;


public class TabsCacccActivity extends _SuperActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private TabFragmentAdapterContainer tabCacccAdapter;
    private ViewPager viewPager;
    private String titulo;
    private ImageView imageViewCentro;
    private String urlThumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        try {

            _cacccUtilApplication= (UtilApplication<String, GenericParcelable<Caccc>>) getApplicationContext();
            _cacccGenericParcelable =  _cacccUtilApplication.getElementElementDictionary(ConstantHelper.objCaccc);

            if (_cacccGenericParcelable != null) {

                titulo = _cacccGenericParcelable.getValue().getName();
                urlThumbnail = _cacccGenericParcelable.getValue().getUrlImage();

                this.ConfigureToolbarSuporte();
                this.ConfigureReturnToolbar();
                this.ConfigureAppBarLayout(titulo);
                this.ConfigTabs();
            }


        } catch (Exception e) {
            TrackHelper.WriteError(this, "onCreate", e.getMessage());
        }

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private void ConfigTabs() {
        try {
            tabLayout = findViewById(R.id.tab_layout);

            tabLayout.removeAllTabs();

            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.sobre).setText(ConstantHelper.TabSobre));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.anotacao).setText(ConstantHelper.TabBoletim));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.chat).setText(ConstantHelper.TabRetirar));


            tabLayout.setTabTextColors(getResources().getColor(R.color.colorFontWhite), getResources().getColor(R.color.colorFontWhite));

            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));

            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            viewPager = this.findViewById(R.id.pager);

            tabCacccAdapter = new TabFragmentAdapterContainer(getSupportFragmentManager(), tabLayout.getTabCount(), null);

            viewPager.setAdapter(tabCacccAdapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    viewPager.setCurrentItem(tab.getPosition());


                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        } catch (Exception e) {
            TrackHelper.WriteError(this, "ConfigTabs", e.getMessage());

        }

    }


    private void ConfigureAppBarLayout(final String title) {
        try {


            final Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {


                    appBarLayout = findViewById(R.id.appBar);

                    // appBarLayout.setExpanded(false, true);

                    collapsingToolbarLayout = appBarLayout.findViewById(R.id.collapsing);
                    collapsingToolbarLayout.setTitle(title);

                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorFontWhite));
                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorFontWhite));

                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.LEFT);

                    collapsingToolbarLayout.setScrimAnimationDuration(ConstantHelper.OneSecond);

                    collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
                    collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


                    imageViewCentro = collapsingToolbarLayout.findViewById(R.id.imgCentro);
                    if (imageViewCentro != null) {
                        imageViewCentro.setVisibility(View.VISIBLE);
                        imageViewCentro.setAlpha(0f);

                        Picasso.with(_context).load(urlThumbnail)
                                .fit().transform(new CircleTransformPicasso())
                                .into(imageViewCentro, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        //    imageViewCentro.animate().setDuration(ConstantHelper.OneSecond).alpha(1.0f).start();
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });

                    }


                    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                        @Override
                        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                            if (verticalOffset == 0) {
                                toolbar.setLogo(null);
                            } else {
                                toolbar.setLogo(R.mipmap.ic_launcher);
                            }
                        }
                    });

                }
            };

            handler.postDelayed(run, 1500);
        } catch (Exception e) {
            TrackHelper.WriteError(this, "CollapsingToolbarLayoutClose", e.getMessage());
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            TrackHelper.WriteInfo(this, "onConfigurationChanged", "ORIENTATION_PORTRAIT");
            this.ConfigTabs();

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TrackHelper.WriteInfo(this, "onConfigurationChanged", "ORIENTATION_LANDSCAPE");
            this.ConfigTabs();

        }

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }




}






