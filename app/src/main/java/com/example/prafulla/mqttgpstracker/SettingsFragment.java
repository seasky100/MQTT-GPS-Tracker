package com.example.prafulla.mqttgpstracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import android.content.SharedPreferences;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{

    public static final String PREFS_NAME = "MGTPrefsFile";

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        try
        {
            SharedPreferences sharedPrefs = this.getActivity().getSharedPreferences("MGTPrefsFile",0);;

            String server = sharedPrefs.getString("server", "");
            String port = sharedPrefs.getString("port", "");
            String uname = sharedPrefs.getString("uname", "");
            String passw = sharedPrefs.getString("passw", "");
            String topic = sharedPrefs.getString("topic", "");

            ((AutoCompleteTextView) view.findViewById(R.id.serverURI)).setText(server);
            ((EditText) view.findViewById(R.id.port)).setText(port);
            ((EditText) view.findViewById(R.id.uname)).setText(uname);
            ((EditText) view.findViewById(R.id.password)).setText(passw);
            ((EditText) view.findViewById(R.id.topic)).setText(topic);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Button btn = (Button) view.findViewById(R.id.saveButton);
        btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        // Do something in response to button click
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context

        try
        {
            String server = ((AutoCompleteTextView) this.getView().findViewById(R.id.serverURI)).getText().toString();
            String port = ((EditText) this.getView().findViewById(R.id.port)).getText().toString();
            String uname = ((EditText) this.getView().findViewById(R.id.uname)).getText().toString();
            String passw = ((EditText) this.getView().findViewById(R.id.password)).getText().toString();
            String topic = ((EditText) this.getView().findViewById(R.id.topic)).getText().toString();

            if (server.isEmpty() || port.isEmpty() || uname.isEmpty() || passw.isEmpty() || topic.isEmpty())
            {
                Toast.makeText(getActivity().getApplicationContext(), R.string.missingDetails, Toast.LENGTH_LONG).show();
            }
            else
            {
                SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("server", server);
                editor.putString("port", port);
                editor.putString("uname", uname);
                editor.putString("passw", passw);
                editor.putString("topic", topic);

                // Commit the edits!
                editor.commit();

                Toast.makeText(getActivity().getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
