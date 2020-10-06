package my.test.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ycbjie.webviewlib.X5WebView;

import androidx.appcompat.app.AppCompatActivity;
import my.test.myapplication.R;
import my.test.myapplication.utils.NetUtil;

public class NewsDetailActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar webProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
       ImageView imageView =findViewById(R.id.toolbar_layout);
        Glide.with(this).load(getIntent().getStringExtra("img")).into(
        imageView);
        webView=findViewById(R.id.webview);
        webProgress=findViewById(R.id.web_progress);
       // webView.setWebViewClient(webViewClient);
       // YcX5WebChromeClient webChromeClient = new YcX5WebChromeClient(webView,this);
        webView.setWebChromeClient(chromeClient);
        webviewSetting();
        webView.loadUrl(getIntent().getStringExtra("url"));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //     Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
           //             .setAction("Action", null).show();
                shareToMore();
            }
        });
    }
    private void shareToMore() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String title = getIntent().getStringExtra("title");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_link_format, title, getIntent().getStringExtra("url")));
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)));
    }
    WebViewClient webViewClient = new WebViewClient() {
        /**
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //view.loadUrl(url);
            //Toast.makeText(WebViewActivity.this, "没有权限的数据", Toast.LENGTH_SHORT).show();
            return false;
        }
    };
    WebChromeClient chromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            webProgress.setProgress(newProgress);
            if (newProgress==100){
                webProgress.setVisibility(View.GONE);
            }
        }
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    };
    private void webviewSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        //设置自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        // webSettings.setBuiltInZoomControls(true); //设置可以缩放
        //webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
        //webSettings.setTextZoom(2);//设置文本的缩放倍数，默认为 100
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        // webSettings.supportMultipleWindows();  //多窗口
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        //webSettings.setAllowFileAccess(true);  //设置可以访问文件
        //webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
        webSettings.setDefaultFontSize(14);//设置 WebView 字体的大小，默认大小为 16
        //webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
        if (NetUtil.getNetWorkState(this)!=-1) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
      //  webSettings.setDatabaseEnabled(true);//开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        String cacheDirPath = getFilesDir().getAbsolutePath() + "news_webviewcache";
        webSettings.setAppCachePath(cacheDirPath);
       // moreWin(webSettings);
    }
}