package com.example.prafulla.mqttgpstracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectFragment extends Fragment {

    public ConnectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


/*        Bundle arguments = new Bundle();
        arguments.putBoolean("IsConnected", true);
        MessagesFragment fragment = new MessagesFragment();
        fragment.setArguments(arguments);*/

        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

}
