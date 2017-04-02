package io.gitcafe.jackyshan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jackyshan on 2017/3/29.
 */

public class WebViewActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        final WebView webview = (WebView) findViewById(R.id.wbview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new HelloWebViewClient ());

        final EditText editText = (EditText) findViewById(R.id.edittext);
        webview.loadUrl(editText.getText().toString());

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
        }
    }
}
