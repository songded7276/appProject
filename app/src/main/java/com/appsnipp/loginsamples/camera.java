package com.appsnipp.loginsamples;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class camera extends AppCompatActivity implements PhotoFragment.OnFragmentInteractionListener{
    private String upload_URL = URLS.URL_CAMERA;
    private RequestQueue rQueue;
    EditText value ;
    ProgressBar simpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //simpleProgressBar=(ProgressBar)findViewById(R.id.progressBar2);


        setContentView(R.layout.activity_camera);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.res_photo_layout, new PhotoFragment())
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onFragmentInteraction(Bitmap bitmap) {
        if (bitmap != null) {
            simpleProgressBar = findViewById(R.id.progressBar2);
            simpleProgressBar.setVisibility(View.VISIBLE);
            ImageFragment imageFragment = new ImageFragment();
            imageFragment.imageSetupFragment(bitmap);
            uploadImage(bitmap);

        }
    }


    private void uploadImage(final Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("success", new String(response.data));
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            //jsonObject.toString();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("reading"), Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("test", jsonObject.getString("reading"));
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("tags", "ccccc");  add string parameters
                return params;
            }

            /*
             *pass files using below method
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };


        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(camera.this);
        rQueue.add(volleyMultipartRequest);
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap background = Bitmap.createBitmap(433, 106, Bitmap.Config.ARGB_8888);

        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();

        Canvas canvas = new Canvas(background);

        float scale = 433 / originalWidth;

        float xTranslation = 0.0f;
        float yTranslation = (106 - originalHeight * scale) / 2.0f;

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(bitmap, transformation, paint);
        background.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
