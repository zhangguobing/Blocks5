package com.zjonline.blocks5.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.zjonline.blocks5.R;

import butterknife.Bind;
import butterknife.OnClick;
import cn.campusapp.router.Router;
import cn.campusapp.router.router.ActivityRouter;

/**
 * author：zhangguobing on 2017/6/27 14:16
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_web)
public class WebActivity extends BaseActivity implements ChromeClientCallbackManager.ReceivedTitleCallback {

    private static final String KEY_PREFIX = WebActivity.class.getName();
    private static final String EXTRA_URL = KEY_PREFIX + ".url";

    @Bind(R.id.container)
    LinearLayout mContainer;
    @Bind(R.id.iv_back)
    ImageView mBackImg;
    @Bind(R.id.iv_finish)
    ImageView mFinishImg;
    @Bind(R.id.tv_title)
    TextView mTitleTv;
    @Bind(R.id.view_line)
    View mLine;

    protected AgentWeb mAgentWeb;

    public static void create(Context context, String url){
        Intent intent = new Intent(context,WebActivity.class);
        intent.putExtra(EXTRA_URL,url);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        String url = getIntent().getStringExtra(EXTRA_URL);
        mTitleTv.setText(url);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mContainer,new LinearLayout.LayoutParams(-1,-1) )
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .setReceivedTitleCallback(this)
                .setWebViewClient(mWebViewClient)
//                .setWebChromeClient(mWebChromeClient)
//                .setWebViewClient(mWebViewClient)
//                .setSecutityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()//
                .ready()
                .go(url);

        mAgentWeb.getLoader().loadUrl(url);
    }


    private WebViewClient mWebViewClient = new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(mFinishImg != null && mAgentWeb != null ){
                if(mAgentWeb.getWebCreator().get().canGoBack()){
                    mLine.setVisibility(View.VISIBLE);
                    mFinishImg.setVisibility(View.VISIBLE);
                }else{
                    mLine.setVisibility(View.GONE);
                    mFinishImg.setVisibility(View.GONE);
                }
            }
        }
    };

    @OnClick({R.id.iv_back,R.id.iv_finish})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                if (!mAgentWeb.back()) finish();
                break;
            case R.id.iv_finish:
                finish();
                break;
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (mTitleTv != null && !TextUtils.isEmpty(title)){
            if (title.length() > 10)
                title = title.substring(0, 10) + "...";
            mTitleTv.setText(title);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
    }
}
