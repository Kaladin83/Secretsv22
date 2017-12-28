package com.example.maratbe.secretsv2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

/**
 * Created by MARATBE on 12/23/2017.
 */

public class Thoughts extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View readSecrets = inflater.inflate(R.layout.activity_thoughts, container, false);

        return readSecrets;
    }
}
