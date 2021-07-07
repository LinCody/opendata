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

public class newtaipeirtp extends AppCompatActivity {
    private ListView listView;
    private MyAdapter myAdapter;
    private RequestQueue queue;
    private LinkedList<HashMap<String,String>> data;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtaipeirtp);

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
                "https://data.ntpc.gov.tw/api/datasets/D9F18DB5-41C7-41D4-B7F0-82A335255B08/json?page=0&size=564",
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
            LayoutInflater inflater = LayoutInflater.from(newtaipeirtp.this);
            View view = inflater.inflate(R.layout.newtaipeirtp_item, null);

            TextView AREA= view.findViewById(R.id.AREA);
            AREA.setText("車格序號:" + data.get(position).get("AREA"));

            TextView ROAD_NAME= view.findViewById(R.id.ROAD_NAME);
            ROAD_NAME.setText("路段名稱:" + data.get(position).get("ROAD_NAME"));

            TextView WEEKDAYS_TIME= view.findViewById(R.id.WEEKDAYS_TIME);
            WEEKDAYS_TIME.setText("平日收費時間:" + data.get(position).get("WEEKDAYS_TIME"));

            TextView HOLIDAYS_AND_NATIONAL_HOLIDAYS_CHARGING_TIME= view.findViewById(R.id.HOLIDAYS_AND_NATIONAL_HOLIDAYS_CHARGING_TIME);
            HOLIDAYS_AND_NATIONAL_HOLIDAYS_CHARGING_TIME.setText("例假日及國定假日收費時間:" + data.get(position).get("HOLIDAYS_AND_NATIONAL_HOLIDAYS_CHARGING_TIME"));

            TextView RATES= view.findViewById(R.id.RATES);
            RATES.setText("費率:" + data.get(position).get("RATES"));
            return view;
        }
    }

    private void parseJSON(String json){
        try{
            JSONArray root = new JSONArray(json);
            for (int i=0; i<root.length(); i++){
                HashMap<String,String> dd = new HashMap<>();
                JSONObject row = root.getJSONObject(i);
                dd.put("AREA", row.getString("AREA"));
                dd.put("ROAD_NAME", row.getString("ROAD_NAME"));
                dd.put("WEEKDAYS_TIME", row.getString("WEEKDAYS_TIME"));
                dd.put("HOLIDAYS_AND_NATIONAL_HOLIDAYS_CHARGING_TIME", row.getString("HOLIDAYS_AND_NATIONAL_HOLIDAYS_CHARGING_TIME"));
                dd.put("RATES", row.getString("RATES"));

                data.add(dd);
            }
            myAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
        progressDialog.dismiss();
    }
}
