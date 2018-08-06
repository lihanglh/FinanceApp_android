package cn.hangkli.financeapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private WebView webView;

    public WebView getWebView(){
        return webView;
    }

    public static HomeFragment newInstance(int index){
        Bundle bundle = new Bundle();
        bundle.putInt("index", 'A' + index);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);
        webView=(WebView)view.findViewById(R.id.home_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        return view;
    }

    @Override
    public void onResume() {

        final String HOME_URL = "https://finance.sina.cn";


        super.onResume();

        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String urlHome = myPref.getString("SETTING_URL_HOME", HOME_URL);

        webView.loadUrl(urlHome);


    }
}
