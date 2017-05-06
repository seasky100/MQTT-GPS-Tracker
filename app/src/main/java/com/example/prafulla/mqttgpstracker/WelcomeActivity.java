package com.example.prafulla.mqttgpstracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class WelcomeActivity extends AppCompatActivity {

    public static MqttAndroidClient client;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private String topic;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            ((TextView)findViewById(R.id.welcomeMsg)).setText("");

            fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_connect:
                    fragment = new ConnectFragment();
                    break;
                case R.id.navigation_settings:
                    fragment = new SettingsFragment();
                    break;
                case R.id.navigation_messages:
                    fragment = new MessagesFragment();
                    break;
            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment).commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        try
        {
            SharedPreferences sharedPrefs = this.getSharedPreferences("MGTPrefsFile",0);;

            String server = sharedPrefs.getString("server", "");
            String port = sharedPrefs.getString("port", "");
            String uname = sharedPrefs.getString("uname", "");
            String passw = sharedPrefs.getString("passw", "");
            topic = sharedPrefs.getString("topic", "") + "/#";

            if (server.isEmpty() || port.isEmpty() || uname.isEmpty() || passw.isEmpty() || topic.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Please Enter MQTT Server Details", Toast.LENGTH_LONG).show();
            }
            else
            {
                String uri = "tcp://" + server + ":" + port;

                //String clientId = MqttClient.generateClientId();
                String clientId = "MGT" + System.nanoTime();

                MqttConnectOptions conOpt = new MqttConnectOptions();
                conOpt.setCleanSession(false);
                conOpt.setConnectionTimeout(10);
                //conOpt.setKeepAliveInterval(30);
                conOpt.setUserName(uname);
                conOpt.setPassword(passw.toCharArray());

                client = new MqttAndroidClient(this, uri, clientId);

                client.setTraceEnabled(true);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Toast.makeText(getApplicationContext(), "The Connection is lost..", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        if(message != null) {
                            Toast.makeText(getApplicationContext(), "Message Received", Toast.LENGTH_SHORT).show();
                            ((TextView) findViewById(R.id.welcomeMsg)).setText(message.toString());
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });

                try {
                    client.connect(conOpt, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            //DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                            //disconnectedBufferOptions.setBufferEnabled(true);
                            //disconnectedBufferOptions.setBufferSize(100);
                            //disconnectedBufferOptions.setPersistBuffer(false);
                           // disconnectedBufferOptions.setDeleteOldestMessages(false);
                           // mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                            subscribeToTopic();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Toast.makeText(getApplicationContext(), "Failed to connect to server..", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (MqttException e) {
                    Log.e(this.getClass().getCanonicalName(),
                            "MqttException Occured", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        try {
            if(client != null && client.isConnected()) {
                client.disconnect();
            }
        }
        catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(),
                    "MqttException Occured", e);
        }
        super.onBackPressed();
    }

    public void subscribeToTopic(){
        try {
            String[] topics = new String[1];

            client.subscribe(topic, 1, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getApplicationContext(), "Subscribed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getApplicationContext(), "Failed to subscribe..", Toast.LENGTH_LONG).show();
                }
            });


        } catch (MqttException ex){
            System.err.println("Exception while subscribing");
            ex.printStackTrace();
        }
    }

}
