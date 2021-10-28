package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity implements Fragment1.OnButtonClickListner {


    private int[] frames;
    private boolean hiden;
    private int [] sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            frames = new int[]{R.id.frame1,R.id.frame2,R.id.frame3, R.id.frame4};
            hiden = false;

            sequence = new int[]{0,1,2,3};

            Fragment[] fragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (int i = 0; i < 4; i++){
                transaction.add(frames[i],fragments[i]);
            }

            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            frames = savedInstanceState.getIntArray("FRAMES");
            hiden = savedInstanceState.getBoolean("HIDEN");
            sequence = savedInstanceState.getIntArray("SEQUENCE");
        }


    }

    protected void onInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putIntArray("FRAMES", frames);
        outState.putBoolean("HIDEN",hiden);
        outState.putIntArray("SEQUENCE", sequence);
    }

    @Override
    public void onButtonClickShuffle() {
        Toast.makeText(getApplicationContext(), "Shuffle", Toast.LENGTH_SHORT).show();

        List<Integer> s = new ArrayList<>(Arrays.asList(sequence[0], sequence[1], sequence[2], sequence[3]));
        Collections.shuffle(s);
        for(int i = 0; i < 4; i++) sequence[i] = s.get(i);

        newFragments();


    }

    @Override
    public void onButtonClickClockwise() {
        Toast.makeText(getApplicationContext(),"Clockwise",Toast.LENGTH_SHORT).show();

        List<Integer> list = new ArrayList<Integer>(Arrays.asList(frames[0], frames[1], frames[2], frames[3]));
        Collections.shuffle(list);
        for (int i = 0; i < 4; i++) frames[i] = list.get(i).intValue();

        newFragments();

    }

    @Override
    public void onButtonClickHide() {
        Toast.makeText(getApplicationContext(),"Hide",Toast.LENGTH_SHORT).show();

            if(hiden) return;

            FragmentManager fragmentManager = getSupportFragmentManager();

            for (Fragment f : fragmentManager.getFragments()) {

                if (f instanceof Fragment1 ) continue;

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(f);


                transaction.addToBackStack(null);
                transaction.commit();
            }

            hiden = true;
    }

    @Override
    public void onButtonClickRestore() {
        Toast.makeText(getApplicationContext(),"Restore",Toast.LENGTH_SHORT).show();

        if (!hiden) return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment f : fragmentManager.getFragments()) {
            if (f instanceof Fragment1) continue;
            transaction.show(f);
        }

        transaction.addToBackStack(null);
        transaction.commit();

        hiden = false;

    }
    @Override
    public void onAttachFragment(@NonNull Fragment fragment){
        super.onAttachFragment(fragment);

        if (fragment instanceof Fragment1) {
            ((Fragment1) fragment).setOnButtonClickListner(this);
        }
    }

    private void newFragments() {

        Fragment[] newFragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};

        Fragment[] inSequence = new Fragment[] {newFragments[sequence[0]], newFragments[sequence[1]], newFragments[sequence[2]], newFragments[sequence[3]] };
        newFragments = inSequence;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (int i = 0; i < 4; i++) {
            transaction.replace(frames[i], newFragments[i]);
            if (hiden && !(newFragments[i] instanceof Fragment1)) transaction.hide(newFragments[i]);
        }

        transaction.addToBackStack(null);
        transaction.commit();


        /*Fragment fragment = new Fragment1();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.button_hide, fragment)
                .addToBackStack(null)
                .commit();*/

    }
}