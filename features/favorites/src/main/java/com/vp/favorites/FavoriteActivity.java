package com.vp.favorites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.vp.favorites.ListFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class FavoriteActivity extends AppCompatActivity{
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingActivityInjector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {

            //ArrayList<> customer = (Customer)extras.getSerializable(getString(R.string.favorite));
            // do something with the customer
            getSupportFragmentManager()
                        .beginTransaction()
                        .replace(com.vp.list.R.id.fragmentContainer, new ListFragment(), ListFragment.TAG)
                        .commit();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
