package com.example.user.mymangalist.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.mymangalist.R;
import com.example.user.mymangalist.adapters.ChaptersAdapter;
import com.example.user.mymangalist.persistence.DatabaseClass;
import com.example.user.mymangalist.services.DownloadService;

import java.io.File;
import java.util.ArrayList;



public class Chapters extends ActionBarActivity
{
    ProgressDialog pd;
    ProgressBar[] pb;
    public String TAG="TAG";
    ListView listView;
    File path;
    Bitmap bitmap;
    Integer recievechapterno=-1;
    String chapternos="";
    DatabaseClass helper;
    View globalview;
    int progress_progressbar=0,totalpages=0;
    Toolbar toolbar;
    ArrayList<String> chapterslinks = new ArrayList<>();
    public BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = getIntent().getExtras();
            if(intent.hasExtra("progress"))
            {
                pd.dismiss();
                progress_progressbar= intent.getIntExtra("progress", 1);
                totalpages = intent.getIntExtra("totalimages", 1);
                Integer chapterno = intent.getIntExtra("chapterno",1)-1;
                Log.d(TAG,"chapterno"+chapterno);
                pb[chapterno].setProgress((int)(((float)progress_progressbar/totalpages)*100));
                if(progress_progressbar==totalpages)
                    pb[chapterno].setVisibility(View.INVISIBLE);

            }
            if(intent.hasExtra("manganame"))
            {
                String manganame = bundle.getString("manganame");
                recievechapterno = bundle.getInt("chapterno");
                Toast.makeText(getApplicationContext(), "Saved download to history", Toast.LENGTH_SHORT).show();

            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        toolbar = (Toolbar)findViewById(R.id.app_bar_chapters);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        helper = new DatabaseClass(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Chapters.this);
        Bundle bundle = getIntent().getExtras();
        listView = (ListView)findViewById(R.id.chapter);
        chapterslinks=bundle.getStringArrayList("chapterslinks");
        pb = new ProgressBar[chapterslinks.size()];
        String manganame = bundle.getString("manganame");
        String chapternoslocal = sharedPreferences.getString(manganame,"");
        if(!chapternoslocal.equals(""))
            chapternos = chapternoslocal;
        manganame = manganame.substring(0,1).toUpperCase()+manganame.substring(1);
        ArrayList<String> chapterslist = new ArrayList<>();
        for(int i=0;i<chapterslinks.size();i++)
            chapterslist.add(manganame+" - "+(i+1));
        final ChaptersAdapter chaptersAdapter = new ChaptersAdapter(getApplicationContext(),chapterslist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,chapterslist);
        listView.setAdapter(chaptersAdapter);
        final String finalManganame = manganame;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                pb[i]=(ProgressBar)view.findViewById(R.id.chaptersrow_progressbar);
                pb[i].getProgressDrawable().setColorFilter(Color.parseColor("#FF4081"), PorterDuff.Mode.MULTIPLY);
                pb[i].setVisibility(View.VISIBLE);
                pb[i].setProgress(0);
                Intent intent = new Intent(Chapters.this,DownloadService.class);
                intent.putExtra("page1",chapterslinks.get(i));
                intent.putExtra("manganame", finalManganame);
                intent.putExtra("chapterno",i+1);
                Toast.makeText(Chapters.this,"Download Starting...\n Please Wait...",Toast.LENGTH_LONG).show();
                pd = ProgressDialog.show(Chapters.this,"","Loading",true);
                pd.setCanceledOnTouchOutside(true);
                Log.d(TAG,chapterslinks.toString());
                Log.d(TAG,i+"- chapterno");
                startService(intent);
            }
        });
        int i=listView.getFirstVisiblePosition();
        Log.d(TAG,i+"i");
        int l=listView.getLastVisiblePosition();
        for(;i<l;i++)
        {
            Log.d(TAG,chaptersAdapter.getItem(i).equals("Hunter x hunter - 8")+" that");
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION_SERVICE));
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(receiver);
        if(pd!=null)
            pd.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_chapters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
