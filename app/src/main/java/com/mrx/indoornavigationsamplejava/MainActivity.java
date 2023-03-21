package com.mrx.indoornavigationsamplejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mrx.indoorservice.api.IndoorService;
import com.mrx.indoorservice.domain.model.BeaconsEnvironmentInfo;
import com.mrx.indoorservice.domain.model.EnvironmentInfo;
import com.mrx.indoorservice.domain.model.Point;
import com.mrx.indoorservice.domain.model.PositionInfo;
import com.mrx.indoorservice.domain.model.StateEnvironment;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    // Обьявим переменные в классе MainActivity

    BeaconManager beaconManager;
    Region region;

    IndoorService indoorService;

    Button buttonStart;
    TextView textViewBeacons;
    TextView textViewPosition;
    TextView textViewAzimuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем переменные

        //beaconManager = BeaconManager.getInstanceForApplication(this);
        indoorService = IndoorService.INSTANCE.getInstance(this);
        indoorService.getPosition().setEnvironment(stateEnvironment);

        buttonStart = findViewById(R.id.btn_control);
        textViewBeacons = findViewById(R.id.textView_beacons);
        textViewPosition = findViewById(R.id.textView_position);
        textViewAzimuth = findViewById(R.id.textView_azimuth);

        // Настройка для поиска маячков iBeacon

//        beaconManager.getBeaconParsers().clear();
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        /*
         * После первого запуска необходимо дать разрешение приложению на местоположение в настройках, иначе маяки не найдет + включить bluetooth
         */

        // Подпишемся на обновление данных с маячков и запустим мониторинг
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

    // Напишем функцию обратного вызова
    Observer<Collection<Beacon>> observer = beacons -> {
        textViewBeacons.setText("Ranged: " + Integer.toString(beacons.size()) + " beacons");
    };

    Observer<Collection<BeaconsEnvironmentInfo>> observerForIndoorServiceBeacons = beacons -> {
        String temp = "Ranged: " + Integer.toString(beacons.size()) + " beacons\n";
        for (BeaconsEnvironmentInfo beacon : beacons) {
            temp += beacon.getBeaconId() + " -> " + beacon.getDistance() + "\n";
        }
        textViewBeacons.setText(temp);

        // Определение позиции далее
        if (beacons.size() > 2) {
            PositionInfo position = indoorService.getPosition().getPosition(indoorService.getMapper().fromBeaconsEnvironmentInfoToEnvironmentInfo(beacons));
            textViewPosition.setText("Position: (" + position.getPosition().getX() + ", " + position.getPosition().getY() + ")");
        }

    };

    Observer<Float> observerForIndoorServiceAzimuth = azimuth -> {
        textViewAzimuth.setText(Float.toString(azimuth));
    };

    ArrayList stateEnvironment = new ArrayList(Arrays.asList(
            new StateEnvironment("DF:6A:59:AE:F9:CC", new Point<>(0.0, 0.0)),
            new StateEnvironment("D3:81:75:66:79:B8", new Point<>(10.0, 0.0)),
            new StateEnvironment("E4:C1:3F:EF:49:D7", new Point<>(10.0, 10.0)),
            new StateEnvironment("E6:96:DA:5C:82:59", new Point<>(0.0, 10.0))
    ));

    @Override
    protected void onPause() {
        super.onPause();

//        beaconManager.stopMonitoring(region);
        indoorService.getBeaconsEnvironment().stopRanging();
        indoorService.getAzimuthManager().stopListen();
    }
}