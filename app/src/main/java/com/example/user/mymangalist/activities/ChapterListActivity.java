package com.example.user.mymangalist.activities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.mymangalist.R;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class ChapterListActivity extends ActionBarActivity
{
    getpopmangalistTask task;
    public static String TAG="TAG";
    ArrayList<String> results = new ArrayList<>();
    String manganame;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapterlistactivity);
        Bundle bundle = getIntent().getExtras();
        manganame=bundle.getString("manganame");
        task = new getpopmangalistTask();
        try
        {
            results = task.execute().get();
        } catch (Exception e)
        {
        }
        Toast.makeText(getApplicationContext(),results.toString(),Toast.LENGTH_SHORT).show();
        ListView listView = (ListView)findViewById(R.id.chapterslist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,results);
        listView.setAdapter(adapter);
    }

    public class getpopmangalistTask extends AsyncTask<Void,Void,ArrayList<String>>
    {
        @Override
        protected ArrayList<String> doInBackground(Void... voids)
        {
            ArrayList<String> newarraylist=new ArrayList<>();
            String mangalisturl = "http://www.mangareader.net";
            try
            {
                URL url = new URL(mangalisturl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                InputStream inputStream = httpURLConnection.getInputStream();
                newarraylist = parseforpopular(inputStream);
            } catch (Exception e)
            {

            }
            return newarraylist;
        }
    }
    public ArrayList<String> parseforpopular(InputStream inputStream)
    {
        ArrayList<String> results=new ArrayList<>(),manganames=new ArrayList<>();
        OutputStream stream= new OutputStream()
        {
            @Override
            public void write(int i) throws IOException
            {

            }
        };
            Document document = null;
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);
            document=tidy.parseDOM(inputStream,stream);
            Document document1 = (org.w3c.dom.Document)document;
            org.w3c.dom.Element root = (org.w3c.dom.Element) document1.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("b");
            Node node;
            for(int i=0;i<40;i++)
            {
                if(!nodeList.item(i).hasChildNodes())
                    break;
                node = nodeList.item(i).getFirstChild();
                manganames.add(node.getFirstChild().getNodeValue());
                results.add(node.getAttributes().item(1).getNodeValue()+"");
            }
            return manganames;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_chapterlistactivity, menu);
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
