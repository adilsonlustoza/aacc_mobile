package br.org.aacc.doacao.Helper;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * Created by ubuntu on 2/5/17.
 */
public class HttpHelper {

    private static final String TAG = HttpHelper.class.getSimpleName();
    private static Context _context;
    private static JSONArray _jsonArray;

    public HttpHelper() {}

    public HttpHelper(Context context) {
        _context = context;
    }

    public JSONArray makeVolleyServiceGet(String reqUrl, Map<String, String> objParams) {

        StringRequest stringRequest;
        RequestQueue requestQueue;
        final Map<String, String> params = objParams;
        _jsonArray=null;

        try {


            stringRequest = new StringRequest(Request.Method.GET, reqUrl,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                _jsonArray = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            TrackHelper.WriteError(TAG, "makeVolleyServiceCall", error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };


            requestQueue = Volley.newRequestQueue(_context);
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            throw e;
        }

        return _jsonArray;
    }

    public JSONArray makeVolleyServicePost(String reqUrl, Map<String, String> objParams) {

        StringRequest stringRequest;
        RequestQueue requestQueue;
        final Map<String, String> params = objParams;
        _jsonArray=null;

        try {


            stringRequest = new StringRequest(Request.Method.POST, reqUrl,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                _jsonArray = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            TrackHelper.WriteError(TAG, "makeVolleyServiceCall onErrorResponse", error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };


            requestQueue = Volley.newRequestQueue(_context);
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            TrackHelper.WriteError(TAG, "makeVolleyServicePost", e.getMessage());

        }

        return _jsonArray;
    }

    public static String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);


        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }


        return response;
    }

    public static int makeServiceSend(String reqUrl, JSONObject jsonObject) {
        int HttpResult = -3;

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestMethod("POST");
            // read the response

            String json = jsonObject.toString();
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            outputStreamWriter.write(json);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            HttpResult = conn.getResponseCode();

            if (HttpResult > -1)
                return HttpResult;
            return -1;

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }


        return HttpResult;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}

