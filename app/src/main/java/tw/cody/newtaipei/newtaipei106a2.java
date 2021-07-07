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

public class newtaipei106a2 extends AppCompatActivity {
    private ListView listView;
    private MyAdapter myAdapter;
    private RequestQueue queue;
    private LinkedList<HashMap<String,String>> data;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtaipei106a2);

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
                "https://data.ntpc.gov.tw/api/datasets/87B66CBF-3EDD-488C-9525-AC8F39979E24/json?page=0&size=32212",
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
            LayoutInflater inflater = LayoutInflater.from(newtaipei106a2.this);
            View view = inflater.inflate(R.layout.newtaipei106a2_item, null);

            TextView date= view.findViewById(R.id.date);
            date.setText("發生時間:" + data.get(position).get("date"));

            TextView location= view.findViewById(R.id.location);
            location.setText("發生地點:" + data.get(position).get("location"));

            TextView field1= view.findViewById(R.id.field1);
            field1.setText("受傷:" + data.get(position).get("field1"));

            TextView field2= view.findViewById(R.id.field2);
            field2.setText("肇事車種:" + data.get(position).get("field2"));
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
                dd.put("location", row.getString("location"));
                dd.put("field1", row.getString("field1"));
                dd.put("field2", row.getString("field2"));

                data.add(dd);
            }
            myAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
        progressDialog.dismiss();
    }
}
