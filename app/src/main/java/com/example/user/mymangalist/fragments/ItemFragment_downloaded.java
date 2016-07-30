package com.example.user.mymangalist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.mymangalist.activities.ImageActivity;
import com.example.user.mymangalist.R;
import com.example.user.mymangalist.persistence.DatabaseClass;

import java.io.File;
import java.util.ArrayList;

public class ItemFragment_downloaded extends Fragment implements AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseClass helper;
    private ArrayList<String> mParam1=new ArrayList<>();
    private int[] mParam2;
    private ArrayList<String> arrayList=new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    public static ItemFragment_downloaded newInstance(ArrayList<String> param1, int[] param2)
    {
        ItemFragment_downloaded fragment = new ItemFragment_downloaded();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        args.putIntArray(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public ItemFragment_downloaded()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        helper = new DatabaseClass(getActivity());
        if (getArguments() != null)
        {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getIntArray(ARG_PARAM2);
            for(int i=0;i<mParam1.size();i++)
                arrayList.add(mParam1.get(i)+" - "+mParam2[i]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_itemdownloaded, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayList);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (null != mListener)
        {
            Intent intent = new Intent(getActivity(),ImageActivity.class);
            intent.putExtra("manganame",mParam1.get(position));
            intent.putExtra("chapterno",mParam2[position]);
            startActivity(intent);
        }
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Chapter?").setMessage("Do you want to delete all images in this chapter?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int t)
            {
                File path;
                path = new File(Environment.getExternalStorageDirectory().toString()+"/MangaDownloader/"+mParam1.get(i)+"/");
                arrayList.remove(i);
                long id = helper.delete(mParam1.get(i),mParam2[i]);
                Boolean res = true;
                for(int j=0;res!=false;j++)
                {
                    File file = new File(path, (mParam2[i]) + " - " + (j + 1) + ".jpg");
                    try
                    {
                        res = file.delete();
                    } catch (Exception e)
                    {
                    }
                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
                    ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
                    Toast.makeText(getActivity(),"Deleted Chapter",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }


    public void setEmptyText(CharSequence emptyText)
    {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView)
        {
            ((TextView) emptyView).setText(emptyText);
        }
    }


    public interface OnFragmentInteractionListener
    {
        public void onFragmentInteraction(String id);
    }

}
