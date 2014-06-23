package alex_gontarenko.testinstagramcollage.Parcers;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import alex_gontarenko.testinstagramcollage.BaseClass.InstagramImage;
import alex_gontarenko.testinstagramcollage.BaseClass.InstagramMediaImage;

/**
 * Created by Alex on 22.06.2014.
 */
public class InstagramImageParcer extends InstagramDataParser<ArrayList<InstagramMediaImage>> {
    private final static String LOG_TAG = "InstagramImageParcer";


    @Override
    public ArrayList<InstagramMediaImage> parceData(InputStream is) {
        ArrayList<InstagramMediaImage> arrayImage = null;
        String response = null;
        JSONObject objectJSON=null,object=null,imgObject;
        JSONArray arrayJSON = null;
        int i,size;

        int likes;
        InstagramImage thumbnailImg,image;

        try {
            response = streamToString(is);
            objectJSON = new JSONObject(response);
            if(objectJSON!=null&&objectJSON.has("data")){
                arrayJSON = (new JSONObject(response)).getJSONArray("data");
                if(arrayJSON!=null&&arrayJSON.length()>0){
                    size=arrayJSON.length();
                    arrayImage = new ArrayList<InstagramMediaImage>(size);
                    for (i=0;i<size;i++){
                        objectJSON = arrayJSON.getJSONObject(i);
                        if(objectJSON==null||!(objectJSON.has("type"))||!(objectJSON.getString("type").equals("image")))
                            continue;
                        likes=-1;
                        thumbnailImg=null;
                        image=null;

                        //get likes
                        if(!objectJSON.has("likes")) continue;
                        object = objectJSON.getJSONObject("likes");

                        if(!object.has("count")) continue;
                        likes=object.getInt("count");

                        //get images
                        if(!objectJSON.has("images")) continue;
                        object = objectJSON.getJSONObject("images");

                        //get thumbnail
                        if(!object.has("thumbnail")) continue;
                        imgObject=object.getJSONObject("thumbnail");

                        if (imgObject.has("url")&&imgObject.has("width")&&imgObject.has("height")) {
                            thumbnailImg = new InstagramImage(imgObject.getString("url"),imgObject.getInt("width"),imgObject.getInt("height"));
                        } else continue;

                        //get standart image
                        if(!object.has("standard_resolution")) continue;
                        imgObject=object.getJSONObject("standard_resolution");

                        if (imgObject.has("url")&&imgObject.has("width")&&imgObject.has("height")) {
                            image = new InstagramImage(imgObject.getString("url"),imgObject.getInt("width"),imgObject.getInt("height"));
                        } else continue;

                        arrayImage.add(new InstagramMediaImage(image,thumbnailImg,likes));
                        }
                    }


            }
        }catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            arrayImage=null;
        }  catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
            arrayImage.clear();
        }
    return arrayImage;
    }
}
