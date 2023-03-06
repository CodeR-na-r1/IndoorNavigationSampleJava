package com.mrx.indoornavigationsamplejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;

import com.mrx.indoorservice.api.IndoorService;
import com.mrx.indoorservice.domain.model.BeaconsEnvironmentInfo;

import org.altbeacon.beacon.BeaconManager;

import java.util.Collection;

NOT_IMPLEMENT

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observer<Collection<BeaconsEnvironmentInfo>> x = null;  // toDo init x (add listener and test him)

        IndoorService indoorService = IndoorService.INSTANCE.getInstance(this);

        /*
         * После первого запуска необходимо дать разрешение приложению на местоположение в настройках, иначе маяки не найдет
         */

        // todo здесь ваш далее код

        indoorService.getBeaconsEnvironment().getRangingViewModel().observe(this, x); // toDo x no null !!!

    }
}