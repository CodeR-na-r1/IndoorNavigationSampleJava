package com.mrx.indoornavigationsamplejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;

import com.mrx.indoorservice.api.IndoorService;
import com.mrx.indoorservice.domain.model.BeaconsEnvironmentInfo;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    IndoorService indoorService;
    BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indoorService = IndoorService.INSTANCE.getInstance(this);
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // Настройка для поиска маячков iBeacon

        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        /*
         * После первого запуска необходимо дать разрешение приложению на местоположение в настройках, иначе маяки не найдет
         */

        // todo здесь далее ваш код

        indoorService.getBeaconsEnvironment().getRangingViewModel().observe(this, x);
        indoorService.getBeaconsEnvironment().startRanging();
    }

    Observer<Collection<BeaconsEnvironmentInfo>> x = beacons -> {
        Log.d("myTag", Integer.toString(beacons.size()));
    };

    @Override
    protected void onPause() {
        super.onPause();

        indoorService.getBeaconsEnvironment().stopRanging();
    }
}