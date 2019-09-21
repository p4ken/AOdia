package com.kamelong2.aodia;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.kamelong.aodia.R;
import com.kamelong2.OuDia.DiaFile;

public class AOdiaFragment extends Fragment {
    protected View fragmentContainer= null;

    public DiaFile diaFile=null;
    public  View findViewById(int id){
        return fragmentContainer.findViewById(id);
    }
    public AOdiaActivity getAOdiaActivity(){
        return (AOdiaActivity)getActivity();
    }
    public String fragmentName(){
        return "";
    }
    public String fragmentHash() {
        return "help";
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ((TextView) getActivity().findViewById(R.id.titleView)).setText(fragmentName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
