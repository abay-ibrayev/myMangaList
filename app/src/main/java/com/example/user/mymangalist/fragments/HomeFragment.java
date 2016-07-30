package com.example.user.mymangalist.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.mymangalist.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



public class HomeFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";


    private ArrayList<String> mParam1;

    private OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance(ArrayList<String> param1)
    {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = (TextView) view.findViewById(R.id.hometext);
        textView.setTextColor(Color.GRAY);
        textView.setText("Total Number of Downloads : "+mParam1.size());
        textView.setTextColor(Color.BLACK);
        Set <String> uniquenames = new HashSet<>(mParam1);
        textView.append("\n\nManga Downloaded : "+uniquenames.size());
        return view;
    }



    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        public void onFragmentInteraction(String id);
    }

}
