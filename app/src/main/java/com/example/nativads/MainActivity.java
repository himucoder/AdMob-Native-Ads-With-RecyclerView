package com.example.nativads;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.NativeAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<HashMap<String,String>> arrayList;
    ArrayList<HashMap<String,String>> finalarrayList;

    HashMap<String, String> hashMap;

    MyRecyclerView myRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);

        arrayList = new ArrayList<>();
        finalarrayList = new ArrayList<>();

        myRecyclerView = new MyRecyclerView();
        recyclerView.setAdapter(myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        LoadData();


    }

    private void LoadData() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.102:8080/class%20286/check.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("serverRes", response.toString());


                try {

                    JSONArray jsonArray = response.getJSONArray("adscheck");

                    for (int x=0; x<jsonArray.length(); x++){

                        JSONObject jsonObject = jsonArray.getJSONObject(x);

                        if (jsonObject.has("imageitems")){
                           JSONArray imageArray = jsonObject.getJSONArray("imageitems");

                           for (int i=0; i<imageArray.length(); i++){
                               JSONObject imageObject = imageArray.getJSONObject(i);

                               String imageIdName = imageObject.getString("imageidName");
                               String imageImage = imageObject.getString("imageImage");

                               hashMap = new HashMap<>();
                               hashMap.put("itemType", "image");
                               hashMap.put("imageidname", imageIdName);
                               hashMap.put("loadimage", imageImage);
                               arrayList.add(hashMap);

                           }

                        }else if (jsonObject.has("videoitems")){
                            JSONArray videoArray = jsonObject.getJSONArray("videoitems");

                            for (int v=0; v<videoArray.length(); v++){
                                JSONObject videoObject = videoArray.getJSONObject(v);

                                String videoCaption = videoObject.getString("videoidName");
                                String videoLoad = videoObject.getString("videoImage");

                                hashMap = new HashMap<>();
                                hashMap.put("itemType", "video");
                                hashMap.put("videocaption",videoCaption);
                                hashMap.put("loadvideo",videoLoad);
                                arrayList.add(hashMap);


                            }

                        }

                    }


                } catch ( JSONException e) {
                    throw new RuntimeException(e);
                }

                finalarraylistview();
                myRecyclerView.notifyDataSetChanged();


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error", toString());

            }
        });



        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);


    }

    public class MyRecyclerView extends RecyclerView.Adapter{

        int IMAGE_VIEW = 0;
        int VIDEO_VIEW = 1;
        int NATIVE_AD_VIEW = 2;

        private class ImageViewHolder extends RecyclerView.ViewHolder{

            TextView imageidName;
            ImageView loadImage;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);

                imageidName = itemView.findViewById(R.id.imageidName);
                loadImage = itemView.findViewById(R.id.loadImage);


            }
        }

        private class VideoViewHolder extends RecyclerView.ViewHolder{

            TextView videocaption;
            ImageView loadVideo;

            public VideoViewHolder(@NonNull View itemView) {
                super(itemView);

                videocaption = itemView.findViewById(R.id.videoCaption);
                loadVideo = itemView.findViewById(R.id.loadVideo);
            }
        }

        private class NativeAdViewHolder extends RecyclerView.ViewHolder{

            TemplateView templateView;

            public NativeAdViewHolder(@NonNull View itemView) {
                super(itemView);

                templateView = itemView.findViewById(R.id.my_template);


            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();

            if (viewType == IMAGE_VIEW){

                View view = inflater.inflate(R.layout.image_layout, parent, false);
                return new ImageViewHolder(view);

            } else if (viewType == VIDEO_VIEW) {
                View view = inflater.inflate(R.layout.video_layout, parent, false);
                return new VideoViewHolder(view);


            }  View view = inflater.inflate(R.layout.native_ad_layout, parent, false);
                return new NativeAdViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if (getItemViewType(position) == IMAGE_VIEW){

                ImageViewHolder IVHolder = (ImageViewHolder) holder;

                hashMap = finalarrayList.get(position);
                String idName = hashMap.get("imageidname");
                String loadimage = hashMap.get("loadimage");

                IVHolder.imageidName.setText(idName);

                Glide
                        .with(MainActivity.this)
                        .load(loadimage)
                        .centerCrop()
                  //    .placeholder(R.drawable.loading_spinner)
                        .into(IVHolder.loadImage);



            } else if (getItemViewType(position) == VIDEO_VIEW) {

                VideoViewHolder VVHolder = (VideoViewHolder) holder;

                hashMap = finalarrayList.get(position);
                String caption = hashMap.get("videocaption");
                String videoId = hashMap.get("loadvideo");

                String loadvideo = "https://img.youtube.com/vi/"+ videoId+ "/hqdefault.jpg";

                VVHolder.videocaption.setText(caption);

                Glide
                        .with(MainActivity.this)
                        .load(loadvideo)
                        .centerCrop()
                        //.placeholder(R.drawable.loading_spinner)
                        .into(VVHolder.loadVideo);

            }else if (getItemViewType(position) == NATIVE_AD_VIEW){

                NativeAdViewHolder NVHolder = (NativeAdViewHolder) holder;

                AdLoader adLoader = new AdLoader.Builder(MainActivity.this, getString(R.string.native_add_unit_id))
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {

                                NVHolder.templateView.setNativeAd(nativeAd);
                            }
                        })
                        .build();

                adLoader.loadAd(new AdRequest.Builder().build());



            }

        }

        @Override
        public int getItemCount() {
            return finalarrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            hashMap = finalarrayList.get(position);
            String items = hashMap.get("itemType");

            if (items != null && items.contains("image")){
                return IMAGE_VIEW;

            }else if(items != null && items.contains("video")){

                return VIDEO_VIEW;

            }else {

                return NATIVE_AD_VIEW;
            }

        }

    }

    private void finalarraylistview(){

        finalarrayList = new ArrayList<>();

        for (int x=0; x<arrayList.size(); x++){

            if(x>1 && x%5==0){
                hashMap = new HashMap<>();
                hashMap.put("itemType", "NATIVE_AD_VIEW");
                finalarrayList.add(hashMap);

            }


            hashMap = arrayList.get(x);
            finalarrayList.add(hashMap);

        }


    }

}