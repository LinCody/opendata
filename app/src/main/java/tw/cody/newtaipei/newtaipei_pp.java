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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class newtaipei_pp extends AppCompatActivity {
    private ListView listView;
    private MyAdapter myAdapter;
    private RequestQueue queue;
    private LinkedList<HashMap<String,String>> data;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtaipei_pp);


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
                "https://data.ntpc.gov.tw/api/datasets/B1464EF0-9C7C-4A6F-ABF7-6BDF32847E68/json?page=0&size=1114",
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
            LayoutInflater inflater = LayoutInflater.from(newtaipei_pp.this);
            View view = inflater.inflate(R.layout.newtaipei_pp_item, null);

            TextView area = view.findViewById(R.id.area);
            area.setText("行政區:" + data.get(position).get("area"));

            TextView name = view.findViewById(R.id.name);
            name.setText("停車場名稱:" + data.get(position).get("name"));

            TextView type = view.findViewById(R.id.type);
            type.setText("1：剩餘車位數 2：靜態停車場資料 =>" + data.get(position).get("type"));

            TextView summary = view.findViewById(R.id.summary);
            summary.setText("停車場概況:" + data.get(position).get("summary"));

            TextView address = view.findViewById(R.id.address);
            address.setText("停車場地址:" + data.get(position).get("address"));

            TextView tel = view.findViewById(R.id.tel);
            tel.setText("停車場電話:" + data.get(position).get("tel"));

            TextView payEx = view.findViewById(R.id.payEx);
            payEx.setText("停車場收費資訊:" + data.get(position).get("payEx"));

            TextView serviceTime = view.findViewById(R.id.serviceTime);
            serviceTime.setText("開放時間:" + data.get(position).get("serviceTime"));

            TextView totalCar = view.findViewById(R.id.totalCar);
            totalCar.setText("汽車總車位數:" + data.get(position).get("totalCar"));

            TextView totalMotor = view.findViewById(R.id.totalMotor);
            totalMotor.setText("機車總格位數:" + data.get(position).get("totalMotor"));

            TextView totalBike = view.findViewById(R.id.totalBike);
            totalBike.setText("腳踏車總車架數:" + data.get(position).get("totalBike"));
            return view;
        }
    }

    private void parseJSON(String json){
        try{
            JSONArray root = new JSONArray(json);
            for (int i=0; i<root.length(); i++){
                HashMap<String,String> dd = new HashMap<>();
                JSONObject row = root.getJSONObject(i);
                dd.put("area", row.getString("area"));
                dd.put("name", row.getString("name"));
                dd.put("type", row.getString("type"));
                dd.put("summary", row.getString("summary"));
                dd.put("address", row.getString("address"));
                dd.put("tel", row.getString("tel"));
                dd.put("payEx", row.getString("payEx"));
                dd.put("serviceTime", row.getString("serviceTime"));
                dd.put("totalCar", row.getString("totalCar"));
                dd.put("totalMotor", row.getString("totalMotor"));
                dd.put("totalBike", row.getString("totalBike"));
                data.add(dd);
            }
            myAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
        progressDialog.dismiss();
    }
}
