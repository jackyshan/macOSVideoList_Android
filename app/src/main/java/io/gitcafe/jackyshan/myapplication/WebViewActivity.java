package io.gitcafe.jackyshan.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jackyshan on 2017/3/29.
 */

public class WebViewActivity extends Activity {
    private List<String> list = new ArrayList<String>();

    private HelloArrayAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        final WebView webview = (WebView) findViewById(R.id.wbview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new HelloWebViewClient ());

        Intent intent = getIntent();
        final String url = intent.getStringExtra("url");

        final EditText editText = (EditText) findViewById(R.id.edittext);
        if (url != null) {
            webview.loadUrl(url);
        }
        else {

            webview.loadUrl(editText.getText().toString());
        }

        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.loadUrl(editText.getText().toString());
            }
        });

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.evaluateJavascript("getUrls()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        String[] list = s.split("\\+");
                        Log.i("tag", list.toString());
                        Intent intent = new Intent();
                        intent.putExtra("list", list);
                        intent.setClass(WebViewActivity.this, VideoListActivity.class);
                        startActivity(intent);

                    }
                });
            }
        });

        //列表
        ListView listView = (ListView) findViewById(R.id.listview);

        mAdapter = new HelloArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                webview.evaluateJavascript("getUrls()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        String[] list = s.split("\\+");

                        ArrayList<String> resultList = new ArrayList<String>();
                        for (String element: list) {

                            if (element.endsWith("/")) {
                                resultList.add(element);
                            }
                            else if (element.endsWith(".mp4")) {
                                resultList.add(element);
                            }
                            else if (element.endsWith(".avi")) {
                                resultList.add(element);
                            }
                            else if (element.endsWith(".mkv")) {
                                resultList.add(element);
                            }
                            else if (element.endsWith(".3gp")) {
                                resultList.add(element);
                            }
                            else if (element.endsWith(".rmvb")) {
                                resultList.add(element);
                            }
                            else if (element.endsWith(".wmv")) {
                                resultList.add(element);
                            }
                            else if (element.endsWith(".mpg")) {
                                resultList.add(element);
                            }
                            else if (element.endsWith(".flv")) {
                                resultList.add(element);
                            }
                            else if (element.endsWith(".swf")) {
                                resultList.add(element);
                            }
                        }

                        String url = resultList.get(i);
                        if (url.endsWith("/")) {
                            Intent intent = new Intent();
                            intent.putExtra("url", url);
                            intent.setClass(WebViewActivity.this, WebViewActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            String type = getFileType(url);
                            Log.i("Type------", type);
                            intent.setDataAndType(Uri.parse(url), "video/"+type);
                            startActivity(intent);
                        }
                    }
                });
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

        public HelloArrayAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Nullable
        @Override
        public String getItem(int position) {
            String url = (String) super.getItem(position);
            try {
                return java.net.URLDecoder.decode(url, "utf-8");
//                if (url.endsWith("/")) {
//                    return java.net.URLDecoder.decode(url, "utf-8");
//                }
//
//                String decodeUrl = url;
//                String pattern = "\\w+.\\.mp4$";
//                Pattern r = Pattern.compile(pattern);
//                Matcher m = r.matcher(decodeUrl);
//                String resultUrl = decodeUrl;
//                if (m.find()) {
//                    resultUrl = m.group(0);
//                }
//
//                return java.net.URLDecoder.decode(resultUrl, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "url编码错误";
            }
        }
    }

    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            String js = "function getUrls(){"+
                    "var objs = document.getElementsByTagName(\"a\");"+
                    "var imgScr = '';"+
                    "for(var i=0;i<objs.length;i++){"+
                    "imgScr = imgScr + objs[i].href + '+';"+
                    "};"+
                    "return imgScr;"+
                    "};";
            view.loadUrl("javascript:" + js);

            Log.i("tag","pagefinish-------");

            view.evaluateJavascript("getUrls()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    String[] list = s.split("\\+");

                    ArrayList<String> resultList = new ArrayList<String>();
                    for (String element: list) {
                        if (element.endsWith("/")) {
                            resultList.add(element);
                        }
                        else if (element.endsWith(".mp4")) {
                            resultList.add(element);
                        }
                        else if (element.endsWith(".avi")) {
                            resultList.add(element);
                        }
                        else if (element.endsWith(".mkv")) {
                            resultList.add(element);
                        }
                        else if (element.endsWith(".3gp")) {
                            resultList.add(element);
                        }
                        else if (element.endsWith(".rmvb")) {
                            resultList.add(element);
                        }
                        else if (element.endsWith(".wmv")) {
                            resultList.add(element);
                        }
                        else if (element.endsWith(".mpg")) {
                            resultList.add(element);
                        }
                        else if (element.endsWith(".flv")) {
                            resultList.add(element);
                        }
                        else if (element.endsWith(".swf")) {
                            resultList.add(element);
                        }
                    }
                    mAdapter.clear();
                    mAdapter.addAll(resultList);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
