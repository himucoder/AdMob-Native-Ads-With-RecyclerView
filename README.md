# RecyclerView with Native Ads — Example Template

This project demonstrates **how to insert Google Native Ads** inside a `RecyclerView` with multiple view types (images, videos, and native ads).


## ⚙️ 1️⃣ Basic RecyclerView Setup

- `RecyclerView` with `LinearLayoutManager`.
- Custom `Adapter` named `MyRecyclerView`.

`recyclerView = findViewById(R.id.recyclerView);
myRecyclerView = new MyRecyclerView();
recyclerView.setAdapter(myRecyclerView);
recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));`
## ⚙️ 2️⃣ Data Structure
`Two ArrayLists:`

`arrayList → Holds raw JSON data (images/videos).`

`finalarrayList → Holds final list with ads inserted.`

`Each item is a HashMap<String, String>.`
`Example keys:`

`itemType = "image", "video", or "NATIVE_AD_VIEW"`

`imageidname, loadimage, videocaption, loadvideo, etc.`

## ⚙️ 3️⃣ Load JSON Data
`Uses Volley to fetch JSON from server.`

`Parses images and videos.`

`Calls finalarraylistview() to insert native ad placeholders.`

`Example:`

`java
Copy
Edit
if(x > 1 && x % 5 == 0){
    hashMap = new HashMap<>();
    hashMap.put("itemType", "NATIVE_AD_VIEW");
    finalarrayList.add(hashMap);
}`
## ⚙️ 4️⃣ Adapter: Multiple View Types
`✅ getItemViewType()
Checks itemType in the HashMap.`

`"image" → IMAGE_VIEW`

`"video" → VIDEO_VIEW`

`Else → NATIVE_AD_VIEW`

`✅ onCreateViewHolder()
Inflates different layouts based on viewType:`

`image_layout.xml`

`video_layout.xml`

`native_ad_layout.xml`

`✅ onBindViewHolder()
For image items → Sets text, loads image using Glide.`

`For video items → Loads YouTube thumbnail.`

`For native ads:`

`Uses AdLoader to load Google Native Ad.`

`Binds it to TemplateView.`

`Example:`

`java
Copy
Edit
AdLoader adLoader = new AdLoader.Builder(context, getString(R.string.native_add_unit_id))
   .forNativeAd(nativeAd -> {
       NVHolder.templateView.setNativeAd(nativeAd);
   })
   .build();`

`adLoader.loadAd(new AdRequest.Builder().build());`
## ⚙️ 5️⃣ Layout
`native_ad_layout.xml must include:`

`xml
Copy
Edit
<com.google.android.ads.nativetemplates.TemplateView
    android:id="@+id/my_template"
    ... />`
## ⚙️ 6️⃣ Final Flow
`✅ App launch → fetch JSON → build arrayList → insert native ad placeholder every 5 items → build finalarrayList → pass to Adapter → Adapter shows images/videos/ads dynamically.`
