package tw.cody.newtaipei;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class newtaipeirp extends AppCompatActivity {
    private ListView listView;
    private MyAdapter myAdapter;
    private RequestQueue queue;
    private LinkedList<HashMap<String,String>> data;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtaipeirp);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("loading...");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        data = new LinkedList<>();
        queue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.listView);
        initListView();
        fetchRemoteData();
    }

    private void fetchRemoteData(){
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.GET,
                "https://data.ntpc.gov.tw/api/datasets/54A507C4-C038-41B5-BF60-BBECB9D052C6/json?page=0&size=31377",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("cody", response);
                        parseJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("cody", error.toString());
                        progressDialog.dismiss();
                    }
                });
        queue.add(request);
    }

    private void initListView(){
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
//            return 0;
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            return null;
            LayoutInflater inflater = LayoutInflater.from(newtaipeirp.this);
            View view = inflater.inflate(R.layout.newtaipeirp_item, null);

            TextView ID = view.findViewById(R.id.ID);
            ID.setText("車格序號:" + data.get(position).get("ID"));

            TextView CELLID = view.findViewById(R.id.CELLID);
            CELLID.setText("車格編號:" + data.get(position).get("CELLID"));

            TextView NAME= view.findViewById(R.id.NAME);
            NAME.setText("車格類型:" + data.get(position).get("NAME"));

            TextView DAY= view.findViewById(R.id.DAY);
            DAY.setText("收費天:" + data.get(position).get("DAY"));

            TextView HOUR= view.findViewById(R.id.HOUR);
            HOUR.setText("收費時段:" + data.get(position).get("HOUR"));

            TextView PAY= view.findViewById(R.id.PAY);
            PAY.setText("收費形式:" + data.get(position).get("PAY"));

            TextView PAYCASH= view.findViewById(R.id.PAYCASH);
            PAYCASH.setText("費率:" + data.get(position).get("PAYCASH"));

            TextView ROADNAME= view.findViewById(R.id.ROADNAME);
            ROADNAME.setText("路段名稱:" + data.get(position).get("ROADNAME"));

            TextView CELLSTATUS= view.findViewById(R.id.CELLSTATUS);
            CELLSTATUS.setText("車格狀態判斷:" + data.get(position).get("CELLSTATUS"));

            TextView ISNOWCASH= view.findViewById(R.id.ISNOWCASH);
            ISNOWCASH.setText("收費時段判斷:" + data.get(position).get("ISNOWCASH"));
            return view;
        }
    }

    private void parseJSON(String json){
        try{
            JSONArray root = new JSONArray(json);
            for (int i=0; i<root.length(); i++){
                HashMap<String,String> dd = new HashMap<>();
                JSONObject row = root.getJSONObject(i);
                dd.put("ID", row.getString("ID"));
                dd.put("CELLID", row.getString("CELLID"));
                dd.put("NAME", row.getString("NAME"));
                dd.put("DAY", row.getString("DAY"));
                dd.put("HOUR", row.getString("HOUR"));
                dd.put("PAY", row.getString("PAY"));
                dd.put("PAYCASH", row.getString("PAYCASH"));
                dd.put("ROADNAME", row.getString("ROADNAME"));
                dd.put("CELLSTATUS", row.getString("CELLSTATUS"));
                dd.put("ISNOWCASH", row.getString("ISNOWCASH"));
                data.add(dd);
            }
            myAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
        progressDialog.dismiss();
    }
}
