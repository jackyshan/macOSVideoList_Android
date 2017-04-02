package io.gitcafe.jackyshan.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;

/**
 * Created by jackyshan on 2017/4/2.
 */

public class VideoListActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_videolist);

        Intent intent = getIntent();
        final String[] list = intent.getStringArrayExtra("list");
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new HelloArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = list[i];
                Log.i("tag", url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String type = getFileType(url);
                Log.i("Type------", type);
                intent.setDataAndType(Uri.parse(url), "video/"+type);
                startActivity(intent);
            }
        });

    }

    /**
     * 获取文件后缀名
     * @param filename
     * @return
     */
    public String getFileType(String filename){
        int pos = filename.lastIndexOf(".");

        if(pos == -1){
            return "mp4";
        }

        return filename.substring(pos+1);
    }

    private class HelloArrayAdapter<T> extends ArrayAdapter {

        public HelloArrayAdapter(Context context, int resource, T[] objects) {
            super(context, resource, objects);
        }

        @Nullable
        @Override
        public String getItem(int position) {
            String url = (String) super.getItem(position);
            try {
                return java.net.URLDecoder.decode(url, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "url编码错误";
            }
        }
    }
}
