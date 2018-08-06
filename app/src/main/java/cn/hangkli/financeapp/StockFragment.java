package cn.hangkli.financeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v4.app.Fragment;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.hangkli.financeapp.data.Stock;

public class StockFragment extends Fragment {

    ArrayList<String> stockStrList = new ArrayList<String>();
    ArrayAdapter<String> aa;

   public static StockFragment newInstance(int index) {

       Bundle bundle = new Bundle();
       bundle.putInt("index", 'A' + index);

       StockFragment fragment = new StockFragment();
       fragment.setArguments(bundle);
       return fragment;

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stock_fragment, container, false);

        ListView listView = (ListView)view.findViewById(R.id.stock_list);

        aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stockStrList);

        listView.setAdapter(aa);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {

                Toast.makeText(getContext(), Long.toString(id), Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(getActivity(), StockActivity.class);
//                intent.putExtra("code", id);
//
//                startActivity(intent);

            }

        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load stock data:
        //new LoadThread().start();
        Thread t = new Thread(new Runnable() {
            public void run() {
                refreshStocks();
            }
        });
        t.start();

    }

    @Override
    public void onResume() {
        super.onResume();

        refreshStocks();


    }

    private void refreshStocks() {

            final String SO_URL= "http://192.168.1.12:8080/stock";
            ArrayList<Stock> stocks;


            try {

                SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String ipStockApi = myPref.getString("SETTING_URL_STOCK_API", SO_URL);
                String url = ipStockApi;

                HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(3000);

                if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    try {
                        InputStream in = con.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        stocks = new Gson().fromJson(reader, new TypeToken<List<Stock>>(){}.getType());

                        stockStrList.clear();
                        for(Stock stock: stocks) {

                            stockStrList.add(stock.toString());
                        }

                        reader.close();

                        aa.notifyDataSetChanged();

                    }
                    catch (IOException e) {
                        Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
                    }
                    finally {
                        con.disconnect();
                    }
                }

            }
            catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
            }

        }


}
