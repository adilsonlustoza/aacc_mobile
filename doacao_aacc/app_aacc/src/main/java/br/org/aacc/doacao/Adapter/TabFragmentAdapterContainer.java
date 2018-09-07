package br.org.aacc.doacao.Adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import br.org.aacc.doacao.Fragments.CacccFragment;
import br.org.aacc.doacao.Fragments.CampanhaFragment;
import br.org.aacc.doacao.Fragments.RetirarFragment;

public class TabFragmentAdapterContainer extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private Fragment fragment;
    Bundle bundle;


    public TabFragmentAdapterContainer(FragmentManager fm, int NumOfTabs, @Nullable Bundle bundle)
    {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                fragment  = new CacccFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new CampanhaFragment();
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment = new RetirarFragment();
                fragment.setArguments(bundle);
                break;

            case 3:
                fragment = new RetirarFragment();
                fragment.setArguments(bundle);
                break;
            default:
               fragment=null;
            break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}