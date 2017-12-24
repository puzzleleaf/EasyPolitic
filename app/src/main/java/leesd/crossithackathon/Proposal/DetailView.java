package leesd.crossithackathon.Proposal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import leesd.crossithackathon.R;

/**
 * Created by user on 2017-12-24.
 */

public class DetailView extends AppCompatActivity {
    WebView webView;
    String getdata = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civil_proposal_detail);

        webView = (WebView)findViewById(R.id.webView);

        getdata = getIntent().getStringExtra("html");

        WebSettings setting= webView.getSettings();//web 설정할 수 있다.
        setting.setJavaScriptEnabled(true);///중용함!!!!!
        webView.setWebViewClient(new WebViewClient());
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);

        webView.loadDataWithBaseURL(null, creHtml(), "text/html", "utf-8", null);

    }
    public String creHtml() // 구성 이렇게해서 보여주면됨.
    {
        StringBuffer sb= new StringBuffer("<HTML>");
        sb.append("<HEAD>");
        //sb.append("<meta name=viewport"+"content=initial-scale=1.0; minimum-scale=1.0; user-scalable=yes;/>");
        sb.append("</HEAD>");
        sb.append("<BODY style='margin:0;'>");
        sb.append(getdata); // 여기에다가 위에서 얻어온 html 값을 넣어주면 된다.
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }
}
