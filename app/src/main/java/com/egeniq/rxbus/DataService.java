package com.egeniq.rxbus;

import android.graphics.Color;

import com.egeniq.rx.BusRelay;
import com.egeniq.rx.BusSignal;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by andrei on 15/04/2017.
 */

public class DataService {

    private BusRelay<BusSignal<Integer>> mBusRelay;

    public DataService() {
        mBusRelay = BusRelay.create();
    }

    public BusRelay<BusSignal<Integer>> getBus() {
        return mBusRelay;
    }

    public void fetch() {
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Random random = new Random();

                int r = random.nextInt(255);
                int g = random.nextInt(255);
                int b = random.nextInt(255);

                return Color.argb(255, r, g, b);
            }
        }).delay(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer c) throws Exception {
                        mBusRelay.accept(BusSignal.create(c));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mBusRelay.accept(BusSignal.<Integer>error(throwable));
                    }
                });
    }
}
