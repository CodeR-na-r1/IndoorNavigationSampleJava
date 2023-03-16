package com.mrx.indoornavigationsamplejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mrx.indoorservice.api.IndoorService;
import com.mrx.indoorservice.domain.model.BeaconsEnvironmentInfo;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    BeaconManager beaconManager;
    Region region;

    IndoorService indoorService;

    Button buttonStart;
    TextView textViewBeacons;
    TextView textViewAzimuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем переменные

        beaconManager = BeaconManager.getInstanceForApplication(this);
        indoorService = IndoorService.INSTANCE.getInstance(this);

        buttonStart = findViewById(R.id.btn_control);
        textViewBeacons = findViewById(R.id.textView_beacons);
        textViewAzimuth = findViewById(R.id.textView_azimuth);

        // Настройка для поиска маячков iBeacon

//        beaconManager.getBeaconParsers().clear();
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        /*
         * После первого запуска необходимо дать разрешение приложению на местоположение в настройках, иначе маяки не найдет
         */

        //
//        region = new Region("all-beacons-region", null, null, null);
//        beaconManager.getRegionViewModel(region).getRangedBeacons().observe(this, observer);
//
//        buttonStart.setOnClickListener(it -> {
//            beaconManager.startRangingBeacons(region);
//        });

        indoorService.getBeaconsEnvironment().getRangingViewModel().observe(this, observerForIndoorServiceBeacons);
        indoorService.getAzimuthManager().getAzimuthViewModel().observe(this, observerForIndoorServiceAzimuth);

        buttonStart.setOnClickListener(it -> {
            indoorService.getBeaconsEnvironment().startRanging();
            indoorService.getAzimuthManager().startListen();
        });
    }

    Observer<Collection<Beacon>> observer = beacons -> {
        textViewBeacons.setText("Ranged: " + Integer.toString(beacons.size()) + " beacons");
    };

    Observer<Collection<BeaconsEnvironmentInfo>> observerForIndoorServiceBeacons = beacons -> {
        textViewBeacons.setText("Ranged: " + Integer.toString(beacons.size()) + " beacons");
    };

    Observer<Float> observerForIndoorServiceAzimuth = azimuth -> {
        textViewBeacons.setText(Float.toString(azimuth));
    };

    @Override
    protected void onPause() {
        super.onPause();

//        beaconManager.startMonitoring(region);
        indoorService.getBeaconsEnvironment().stopRanging();
        indoorService.getAzimuthManager().stopListen();
    }
}