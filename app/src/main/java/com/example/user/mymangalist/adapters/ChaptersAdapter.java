package com.example.user.mymangalist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.mymangalist.R;

import java.util.ArrayList;

/**
 * Created by user on 14-04-2016.
 */
public class ChaptersAdapter extends ArrayAdapter<String>
{
    public static String TAG="TAG";
    public ArrayList<String> newarray = new ArrayList<>();
    ProgressBar pb;
    public ChaptersAdapter(Context context,ArrayList<String> arrayList)
    {
        super(context, R.layout.chaptersrow,arrayList);
        this.newarray=arrayList;
    }
    static class ViewHolder {
        TextView text;
        ProgressBar progressBar;
    }
    @Override

    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater;
        ViewHolder holder = null;
        if(convertView==null)
        {
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.chaptersrow,parent,false);
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(R.id.chaptersrow_text);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();

        }
        holder.text.setText(newarray.get(position));
        return convertView;

    }
    public void setProgress(int i)
    {
        pb.setProgress(i);
    }
    public void setProgressBarvisibility(Boolean b)
    {
        if(b)
        pb.setVisibility(View.VISIBLE);
    }
}
