
package com.example.user.mymangalist.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mymangalist.R;
import com.example.user.mymangalist.adapters.NavBarAdapter;
import com.example.user.mymangalist.fragments.HomeFragment;
import com.example.user.mymangalist.fragments.ItemFragment;
import com.example.user.mymangalist.fragments.ItemFragment_downloaded;
import com.example.user.mymangalist.fragments.NavBarFragment;
import com.example.user.mymangalist.persistence.DatabaseClass;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



public class MainActivity extends ActionBarActivity implements ItemFragment.OnFragmentInteractionListener,ItemFragment_downloaded.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener
{
    public DatabaseClass helper;
    public String TAG="TAG";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public NavBarFragment navbarfragment;
    private ActionBarDrawerToggle drawerlistener;
    private ArrayList<String> navbarimagelist;
    private ArrayList<String> curfrag_manganames = new ArrayList<>();
    private ArrayList<String> curfrag_mangalinks = new ArrayList<>();
    private ArrayList<ArrayList<String>> dataarray=new ArrayList<>();
    private ArrayList<String> manganames=new ArrayList<>(),chapternosarraylist = new ArrayList<>();
    private int[] chapternos;
    EditText editText;
    Boolean mostpopular=true;
    ImageView luffy;
    ImageView naruto;
    ImageView gon;
    ImageView mashiro;
    ImageView mikasa;
    ImageView hunter;
    Animation anim;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        luffy = (ImageView) findViewById(R.id.imageView);
        naruto = (ImageView) findViewById(R.id.imageView2);
        gon = (ImageView) findViewById(R.id.imageView3);
        mashiro = (ImageView) findViewById(R.id.imageView4);
        mikasa = (ImageView) findViewById(R.id.imageView5);
        hunter = (ImageView) findViewById(R.id.imageView6);
        anim = AnimationUtils.loadAnimation(this, R.anim.image_swaper);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        preferences = getSharedPreferences("fullmangalistnames", Context.MODE_PRIVATE);
        helper = new DatabaseClass(this);
        dataarray = helper.getAllData();
        manganames = dataarray.get(0);
        Collections.reverse(manganames);
        editText = (EditText)findViewById(R.id.edittext);
        editText.getLayoutParams().height=0;
        editText.setInputType(0);
        chapternos = new int[manganames.size()];
        chapternosarraylist = dataarray.get(1);
        Collections.reverse(chapternosarraylist);
        for(int i=0;i<manganames.size();i++)
            chapternos[i]=Integer.parseInt(chapternosarraylist.get(i));
        navbarfragment=(NavBarFragment)getSupportFragmentManager().findFragmentById(R.id.navbarfragment);
        navbarfragment.setUp((DrawerLayout)findViewById(R.id.drawerlayout),toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        drawerlistener = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.ic_drawer,R.string.drawertoggleopen,R.string.drawertoggleclose)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                toolbar.setNavigationIcon(R.drawable.ic_back);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                toolbar.setNavigationIcon(R.drawable.ic_drawer);
                super.onDrawerClosed(drawerView);
            }


        };
        drawerLayout.setDrawerListener(drawerlistener);
        drawerlistener.setDrawerIndicatorEnabled(true);
        drawerLayout.openDrawer(Gravity.LEFT);
        drawerLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                drawerlistener.syncState();
            }
        });
        final ListView listView = (ListView)findViewById(R.id.list_fragment);
        final ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.NavbarlistArray)));
        final int[] imagesarray = {R.drawable.onepiece,R.drawable.buggy,R.drawable.dodo,R.drawable.shanks,R.drawable.ace};
        NavBarAdapter adapter = new NavBarAdapter(getApplicationContext(),arrayList,imagesarray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                for (int z = 0; z < adapterView.getChildCount(); z++) {
                    View listItem = adapterView.getChildAt(z);
                    listItem.setBackgroundColor(Color.parseColor("#373737"));

                    TextView textView=(TextView)listItem.findViewById(R.id.listtext);
                    textView.setTextColor(Color.WHITE);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                if (i == 0) {
                    luffy.setVisibility(View.INVISIBLE);
                    naruto.setVisibility(View.INVISIBLE);
                    gon.setVisibility(View.INVISIBLE);
                    mikasa.setVisibility(View.INVISIBLE);
                    mashiro.setVisibility(View.VISIBLE);
                    hunter.setVisibility(View.INVISIBLE);
                    view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    HomeFragment fragment = HomeFragment.newInstance(manganames);
                    ft.replace(R.id.mainfragment,fragment,"Main Fragment");
                    ft.commit();
                }
                if(i==1)
                {
                    if(mostpopular)
                    {
                        getpopmangalistTask task = new getpopmangalistTask();
                        task.execute();
                        mostpopular = false;
                        view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                        luffy.setVisibility(View.INVISIBLE);
                        naruto.setVisibility(View.VISIBLE);
                        mashiro.setVisibility(View.INVISIBLE);
                        mikasa.setVisibility(View.INVISIBLE);
                        gon.setVisibility(View.INVISIBLE);
                        hunter.setVisibility(View.INVISIBLE);

                    }
                }
                if(i==2)
                {
                    naruto.setVisibility(View.INVISIBLE);
                    gon.setVisibility(View.INVISIBLE);
                    mashiro.setVisibility(View.INVISIBLE);
                    mikasa.setVisibility(View.INVISIBLE);
                    luffy.setVisibility(View.VISIBLE);
                    hunter.setVisibility(View.INVISIBLE);
                    view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                    editText.getLayoutParams().height=30;
                    editText.setHint("ENNAMA");
                    editText.addTextChangedListener(watcher);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

                    if(preferences!=null)
                    {
                        Set<String> set = preferences.getStringSet("sharedString_manganames", new HashSet<String>());
                        ArrayList<String> fulllist_manganames = new ArrayList<String>(set);
                        Set<String> set2 = preferences.getStringSet("sharedString_mangalinks", new HashSet<String>());
                        ArrayList<String> fulllist_mangalinks = new ArrayList<String>(set2);
                        ItemFragment fragment = ItemFragment.newInstance(fulllist_manganames, fulllist_mangalinks);
                        curfrag_manganames = fulllist_manganames;
                        curfrag_mangalinks = fulllist_mangalinks;
                        ft.replace(R.id.mainfragment, fragment, "Main Fragment");
                        ft.commit();

                    }
                    getfullmangalistTask task = new getfullmangalistTask();
                    task.execute();
                }
                if(i==3)
                {
                    luffy.setVisibility(View.INVISIBLE);
                    naruto.setVisibility(View.INVISIBLE);
                    gon.setVisibility(View.INVISIBLE);
                    mashiro.setVisibility(View.INVISIBLE);
                    mikasa.setVisibility(View.VISIBLE);
                    hunter.setVisibility(View.INVISIBLE);
                    dataarray = helper.getAllData();
                    manganames = dataarray.get(0);
                    Collections.reverse(manganames);
                    chapternos = new int[manganames.size()];
                    chapternosarraylist = dataarray.get(1);
                    Collections.reverse(chapternosarraylist);
                    for(int t=0;t<manganames.size();t++)
                        chapternos[t]=Integer.parseInt(chapternosarraylist.get(t));
                    view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                    editText.getLayoutParams().height=30;
                    editText.addTextChangedListener(new TextWatcher()
                    {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
                        {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
                        {
                        }

                        @Override
                        public void afterTextChanged(Editable editable)
                        {

                        }
                    });
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    ItemFragment_downloaded ifd = ItemFragment_downloaded.newInstance(manganames,chapternos);
                    ft.replace(R.id.mainfragment,ifd,"Main Fragment");
                    ft.commit();
                }

                if(i==4)
                {
                    view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                    finish();
                    System.exit(0);
                }
            }
        });

    }
    private final TextWatcher watcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {

        }
    };

    public class getfullmangalistTask extends AsyncTask<Void,Void,ArrayList<ArrayList<String>>>
    {
        @Override
        protected void onPreExecute()
        {
            Toast.makeText(MainActivity.this,"Refreshing...",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }
        @Override
        protected ArrayList<ArrayList<String>> doInBackground(Void... voids)
        {
            ArrayList<ArrayList<String>> newarraylist = new ArrayList<>();
            String mangalisturl = "http://www.mangareader.net/alphabetical";
            try
            {
                URL url = new URL(mangalisturl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                InputStream inputStream = httpURLConnection.getInputStream();
                newarraylist = parseforfulllist(inputStream);
            } catch (Exception e)
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return newarraylist;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists)
        {
            try
            {
                SharedPreferences.Editor editor=preferences.edit();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                ItemFragment fragment = ItemFragment.newInstance(arrayLists.get(0), arrayLists.get(1));
                curfrag_manganames=arrayLists.get(0);
                curfrag_mangalinks=arrayLists.get(1);
                Set<String> sharedString_manganames = new HashSet<String>();
                for(int i=0;i<arrayLists.get(0).size();i++)
                {
                    sharedString_manganames.add(arrayLists.get(0).get(i));
                }
                Set<String> sharedString_mangalinks = new HashSet<String>();
                for(int i=0;i<arrayLists.get(0).size();i++)
                {
                    sharedString_mangalinks.add(arrayLists.get(1).get(i));
                }
                editor.putStringSet("sharedString_manganames",sharedString_manganames);
                editor.putStringSet("sharedString_mangalinks", sharedString_mangalinks);
                editor.apply();
                ft.replace(R.id.mainfragment, fragment, "Main Fragment");
                ft.commit();
            }catch (Exception e)
            {
            Toast.makeText(MainActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(arrayLists);
        }
    }
    public class getpopmangalistTask extends AsyncTask<Void,Void,ArrayList<ArrayList<String>>>
    {
        ProgressDialog pd;
        @Override
        protected void onPreExecute()
        {
            pd = ProgressDialog.show(MainActivity.this,"","Loading...",true);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(Void... voids)
        {
            ArrayList<ArrayList<String>> newarraylist=new ArrayList<>();
            String mangalisturl = "http://www.mangareader.net";
            try
            {
                URL url = new URL(mangalisturl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                newarraylist = parseforpopular(inputStream);
            } catch (Exception e)
            {
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return newarraylist;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists)
        {
            pd.dismiss();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            try
            {
                ItemFragment fragment = ItemFragment.newInstance(arrayLists.get(0), arrayLists.get(1));
                ft.replace(R.id.mainfragment, fragment, "Main Fragment");
                ft.commit();
                editText = (EditText)findViewById(R.id.edittext);
                editText.getLayoutParams().height=30;
                editText.setHint("ENNAMA");
            }catch (Exception e)
        {
            Toast.makeText(MainActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();
        }
            super.onPostExecute(arrayLists);
        }
    }
    public ArrayList<ArrayList<String>> parseforpopular(InputStream inputStream)
    {
        ArrayList<ArrayList<String>> finalresult = new ArrayList<>();
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
            results.add(node.getAttributes().item(1).getNodeValue() + "");
        }

        finalresult.add(manganames);
        finalresult.add(results);
        return finalresult;
    }
    public ArrayList<ArrayList<String>> parseforfulllist(InputStream inputStream)
    {
        ArrayList<ArrayList<String>> finalresult = new ArrayList<>();
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
        NodeList nodeList = root.getElementsByTagName("ul");
        NodeList itemchildren;
        Integer count=0;
        for(int i=0;i<nodeList.getLength();i++)
        {
            if(nodeList.item(i).hasAttributes())
            if(nodeList.item(i).getAttributes().item(0).getNodeName().equals("class"))
            {
                itemchildren = nodeList.item(i).getChildNodes();
                for(int j=0;j<itemchildren.getLength();j++)
                {
                    count++;
                    results.add(itemchildren.item(j).getFirstChild().getAttributes().item(0).getNodeValue());
                    manganames.add(itemchildren.item(j).getFirstChild().getFirstChild().getNodeValue());
                }
            }
        }
        finalresult.add(manganames);
        finalresult.add(results);
        return finalresult;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onFragmentInteraction(String id)
    {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }
}

