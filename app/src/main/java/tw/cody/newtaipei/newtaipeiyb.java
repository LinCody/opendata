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

public class newtaipeiyb extends AppCompatActivity {
    private ListView listView;
    private MyAdapter myAdapter;
    private RequestQueue queue;
    private LinkedList<HashMap<String,String>> data;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtaipeiyb);

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
                "https://data.ntpc.gov.tw/api/datasets/71CD1490-A2DF-4198-BEF1-318479775E8A/json?page=0&size=654",
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
            LayoutInflater inflater = LayoutInflater.from(newtaipeiyb.this);
            View view = inflater.inflate(R.layout.newtaipeiyb_item, null);

            TextView sarea = view.findViewById(R.id.sarea);
            sarea.setText("區域:" + data.get(position).get("sarea"));

            TextView sna = view.findViewById(R.id.sna);
            sna.setText("名稱:" + data.get(position).get("sna"));

            TextView tot = view.findViewById(R.id.tot);
            tot.setText("總停車格" + data.get(position).get("tot"));

            TextView ar = view.findViewById(R.id.ar);
            ar.setText("地址:" + data.get(position).get("ar"));

            TextView sbi = view.findViewById(R.id.sbi);
            sbi.setText("可借車位數:" + data.get(position).get("sbi"));

            TextView bemp = view.findViewById(R.id.bemp);
            bemp.setText("可還空位數:" + data.get(position).get("bemp"));

            TextView lat = view.findViewById(R.id.lat);
            lat.setText("緯度:" + data.get(position).get("lat"));

            TextView lng = view.findViewById(R.id.lng);
            lng.setText("經度:" + data.get(position).get("lng"));

            TextView mday = view.findViewById(R.id.mday);
            mday.setText("資料更新時間:" + data.get(position).get("mday"));
            return view;
        }
    }

    private void parseJSON(String json){
        try{
            JSONArray root = new JSONArray(json);
            for (int i=0; i<root.length(); i++){
                HashMap<String,String> dd = new HashMap<>();
                JSONObject row = root.getJSONObject(i);
                dd.put("sarea", row.getString("sarea"));
                dd.put("sna", row.getString("sna"));
                dd.put("tot", row.getString("tot"));
                dd.put("ar", row.getString("ar"));
                dd.put("sbi", row.getString("sbi"));
                dd.put("bemp", row.getString("bemp"));
                dd.put("lat", row.getString("lat"));
                dd.put("lng", row.getString("lng"));
                dd.put("mday", row.getString("mday"));
                data.add(dd);
            }
            myAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
        progressDialog.dismiss();
    }
}
