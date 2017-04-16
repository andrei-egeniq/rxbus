package com.egeniq.rxbus;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.egeniq.rx.BusSignal;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_COLOR = "color";
    private static final String KEY_PROGRESS_VISIBILITY = "visibility";

    private Disposable mDisposable;
    private ProgressBar mProgressBar;

    private Integer mColor;
    private DataService mDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // dependency injection (via Dagger) in a real project
        mDataService = ((BusApplication)getApplicationContext()).getDataService();

        // setup UI action
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataService.fetch();
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        // restore state
        if(savedInstanceState == null) {
            mColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mColor = savedInstanceState.getInt(KEY_COLOR);
            //noinspection WrongConstant
            mProgressBar.setVisibility(savedInstanceState.getInt(KEY_PROGRESS_VISIBILITY));
        }

        updateUI();
    }

    private void updateUI() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(mColor));
        getWindow().setBackgroundDrawable(new ColorDrawable(mColor));
    }

    // connect to bus
    @Override
    protected void onResume() {
        super.onResume();

        mDisposable = mDataService.getBus().subscribe(new Consumer<BusSignal<Integer>>() {
            @Override
            public void accept(@NonNull BusSignal<Integer> signal) throws Exception {
                if(signal.isError()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage(signal.getError().getMessage())
                            .show();
                } else {
                    mColor = signal.getData();
                    mProgressBar.setVisibility(View.GONE);
                    updateUI();
                }
            }
        });
    }

    // disconnect from bus
    @Override
    protected void onPause() {
        super.onPause();
        mDisposable.dispose();
    }

    // save state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_PROGRESS_VISIBILITY, mProgressBar.getVisibility());
        outState.putInt(KEY_COLOR, mColor);
    }
}
