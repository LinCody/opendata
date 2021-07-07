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

public class newtaipei107a2 extends AppCompatActivity {
    private ListView listView;
    private MyAdapter myAdapter;
    private RequestQueue queue;
    private LinkedList<HashMap<String,String>> data;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtaipei107a2);

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
                "https://data.ntpc.gov.tw/api/datasets/BF3F4F49-5885-429E-83FA-A165654FF4BE/json?page=0&size=34639",
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
            LayoutInflater inflater = LayoutInflater.from(newtaipei107a2.this);
            View view = inflater.inflate(R.layout.newtaipei107a2_item, null);

            TextView date= view.findViewById(R.id.date);
            date.setText("發生日期:" + data.get(position).get("date"));

            TextView time= view.findViewById(R.id.time);
            time.setText("發生時間:" + data.get(position).get("time"));

            TextView district= view.findViewById(R.id.district);
            district.setText("行政區:" + data.get(position).get("district"));

            TextView road= view.findViewById(R.id.road);
            road.setText("路名:" + data.get(position).get("road"));

            TextView hurt= view.findViewById(R.id.hurt);
            hurt.setText("受傷人數:" + data.get(position).get("hurt"));

            TextView GPS_longitude= view.findViewById(R.id.GPS_longitude);
            GPS_longitude.setText("GPS經度:" + data.get(position).get("GPS_longitude"));

            TextView GPS_latitude= view.findViewById(R.id.GPS_latitude);
            GPS_latitude.setText("GPS緯度:" + data.get(position).get("GPS_latitude"));
            return view;
        }
    }

    private void parseJSON(String json){
        try{
            JSONArray root = new JSONArray(json);
            for (int i=0; i<root.length(); i++){
                HashMap<String,String> dd = new HashMap<>();
                JSONObject row = root.getJSONObject(i);
                dd.put("date", row.getString("date"));
                dd.put("time", row.getString("time"));
                dd.put("district", row.getString("district"));
                dd.put("road", row.getString("road"));
                dd.put("hurt", row.getString("hurt"));
                dd.put("GPS_longitude", row.getString("GPS_longitude"));
                dd.put("GPS_latitude", row.getString("GPS_latitude"));
                data.add(dd);
            }
            myAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
        progressDialog.dismiss();
    }
}
