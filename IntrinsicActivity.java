package edu.harvard.cs50.intrinsic_value;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.android.volley.Request;
import com.android.volley.RequestQueue;
//import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

public class IntrinsicActivity extends AppCompatActivity {
    private EditText EPSttm;
    private EditText GrowthRate;
    private EditText MinimumRateofReturn;
    private EditText MarginOfSafety;
    private EditText PERatio;
    private TextView FinalValue;
    private TextView FinalValueWithSafety;
    private EditText Stock_Ticker;
    private String stockName;
    private TextView stockinformation;

    private RequestQueue requestQueue;
    private JsonObjectRequest get_stock;
    private JSONObject result;
    private JSONArray results;
    private String descriptionEPS;
    private String descriptionPE;
    private String descriptionGrowth;


    Double input_EPS;
    Double input_GrowthRate;
    Double input_MinimumRateofReturn;
    Double input_MarginOfSafety;
    Double input_PERatio;
    Double tempstore;

    private Button enter;
    private Button button;
    //private Button autope;
    //private Button autogrowth;
    //private Button autoeps;
    //private String stockdescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrinsic);

        EPSttm = findViewById(R.id.epsttm);
        GrowthRate = findViewById(R.id.growthrate);
        MinimumRateofReturn = findViewById(R.id.rateofreturn);
        MarginOfSafety = findViewById(R.id.marginofsafety);
        PERatio = findViewById(R.id.peratio);
        FinalValue = findViewById(R.id.FinalValue);
        enter = findViewById(R.id.entervalues);
        FinalValueWithSafety = findViewById(R.id.valuewithmarginofsafety);
        Stock_Ticker = findViewById(R.id.stock_ticker); //impt.
        Intent intent = getIntent();
        stockName = intent.getStringExtra("Name");
        stockinformation = findViewById(R.id.JSONcode);
        button = findViewById(R.id.button);
        //autope = findViewById(R.id.autope);
        //autogrowth = findViewById(R.id.autogrowth);
        //autoeps = findViewById(R.id.autoeps);

        MinimumRateofReturn.setText("10");
        MarginOfSafety.setText("10");


        /*autoeps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEPS();
                EPSttm.setText(descriptionEPS);

            }
        });

        autogrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGrowth();
                GrowthRate.setText(descriptionGrowth);

            }
        });

        autope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStocks();
                PERatio.setText(descriptionPE);

            }
        });*/



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStocks();
                loadGrowth();
                loadEPS();

                EPSttm.setText(descriptionEPS);
                PERatio.setText(descriptionPE);
                GrowthRate.setText(descriptionGrowth);
            }
        });



        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_EPS = Double.parseDouble(EPSttm.getText().toString());
                input_GrowthRate = Double.parseDouble(GrowthRate.getText().toString());
                input_MinimumRateofReturn = Double.parseDouble(MinimumRateofReturn.getText().toString());
                input_MarginOfSafety = Double.parseDouble(MarginOfSafety.getText().toString());
                input_PERatio = Double.parseDouble(PERatio.getText().toString());
                Double value1 = (double) Math.round(((input_EPS * (Math.pow((1 + input_GrowthRate), 9))) *
                        input_PERatio) / (Math.pow(1 + input_MinimumRateofReturn / 100, 9)));
                String value = Double.toString(value1);
                FinalValue.setText(value);


                String safevalue = Double.toString(value1 - (value1 * ((input_MarginOfSafety / 100))));

                FinalValueWithSafety.setText(safevalue);


                //stockinformation.setText(stockdescription);

            }
        });


    }


    public void loadStocks() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-balance-sheet?symbol=" + Stock_Ticker.getText())
                .get()
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "bc25829867msha60651ae0f5bc14p1e2136jsn1bd7c27f8fd2")
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONObject Object = json.getJSONObject("earnings");
                        JSONObject object = Object.getJSONObject("earningsChart");
                        JSONArray array = object.getJSONArray("quarterly");
                        JSONObject firstquarter = array.getJSONObject(0);
                        JSONObject firstquarterresult = firstquarter.getJSONObject("actual");
                        String firstresult = firstquarterresult.getString("raw");

                        //JSONObject o_bject1 = json.getJSONObject("earningsHistory");
                        //JSONArray object2 = o_bject1.getJSONArray("History");
                        //JSONObject object3 = object2.getJSONObject(0);
                        //JSONObject object4 = object3.getJSONObject("epsDifference");
                        //JSONObject objectlast = object4.getJSONObject("raw");


                        //descriptionEPS = firstresult;
                        //descriptionEPS = objectlast.toString();

                        JSONObject Object1 = json.getJSONObject("summaryDetail");
                        JSONObject object1 = Object1.getJSONObject("trailingPE");

                        descriptionPE = object1.getString("raw");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //response.body().close();

                }
            });
            response.body().close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void loadGrowth() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-analysis?symbol=" + Stock_Ticker.getText())
                .get()
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "bc25829867msha60651ae0f5bc14p1e2136jsn1bd7c27f8fd2")
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONObject Object = json.getJSONObject("earningsTrend");
                        JSONArray array = Object.getJSONArray("trend");
                        JSONObject object = array.getJSONObject(4);
                        JSONObject object1 = object.getJSONObject("growth");

                        descriptionGrowth = object1.getString("raw");


                        JSONObject o_bject1 = json.getJSONObject("earningsHistory");
                        JSONArray object2 = o_bject1.getJSONArray("history");
                        JSONObject object3 = object2.getJSONObject(0);
                        JSONObject object4 = object3.getJSONObject("epsDifference");
                        // Double objectlast = object4.getJSONObject("raw");
                        Double objectlast = object4.getDouble("raw");

                        //descriptionEPS = objectlast.toString();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //response.body().close();

                }
            });
            response.body().close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void loadEPS()

    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-statistics?region=US&symbol=" + Stock_Ticker.getText())
                .get()
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "bc25829867msha60651ae0f5bc14p1e2136jsn1bd7c27f8fd2")
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONObject Object = json.getJSONObject("defaultKeyStatistics");
                        JSONObject object = Object.getJSONObject("trailingEps");
                        Double object1 = object.getDouble("raw");

                        descriptionEPS = object1.toString();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //response.body().close();

                }
            });
            response.body().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


