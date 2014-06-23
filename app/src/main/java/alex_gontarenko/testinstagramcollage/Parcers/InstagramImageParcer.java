package alex_gontarenko.testinstagramcollage.Parcers;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import alex_gontarenko.testinstagramcollage.BaseClass.InstagramImage;

/**
 * Created by Alex on 22.06.2014.
 */
public class InstagramImageParcer extends InstagramDataParser<ArrayList<InstagramImage>> {
    private final static String LOG_TAG = "InstagramImageParcer";


    @Override
    public ArrayList<InstagramImage> parceData(InputStream is) {
        ArrayList<InstagramImage> arrayImage = null;
        String response = null;
        JSONObject objectJSON=null,object=null,imgObject;
        JSONArray arrayJSON = null;
        int i,size;

        int likes;
        String thumbnailUrl,imageURL;

        try {
            response = streamToString(is);
            objectJSON = new JSONObject(response);
            if(objectJSON!=null&&objectJSON.has("data")){
                arrayJSON = (new JSONObject(response)).getJSONArray("data");
                if(arrayJSON!=null&&arrayJSON.length()>0){
                    size=arrayJSON.length();
                    arrayImage = new ArrayList<InstagramImage>(size);
                    for (i=0;i<size;i++){
                        objectJSON = arrayJSON.getJSONObject(i);
                        if(objectJSON!=null&&objectJSON.has("type")&&objectJSON.getString("type").equals("image")){
                            likes=-1;
                            thumbnailUrl=null;
                            imageURL=null;
                            if(objectJSON.has("likes")) {
                                object = objectJSON.getJSONObject("likes");
                                if(object.has("count"))
                                    likes=object.getInt("count");
                            }
                            if(objectJSON.has("images")) {
                                object = objectJSON.getJSONObject("images");
                                if(object.has("thumbnail")) {
                                    imgObject=object.getJSONObject("thumbnail");
                                    if (imgObject.has("url"))
                                        thumbnailUrl = imgObject.getString("url");
                                }
                                if(object.has("standard_resolution")) {
                                    imgObject=object.getJSONObject("standard_resolution");
                                    if (imgObject.has("url"))
                                        imageURL = imgObject.getString("url");
                                }
                            }

                            if(likes>-1&&thumbnailUrl!=null&&!thumbnailUrl.isEmpty()&&imageURL!=null&&!imageURL.isEmpty())
                                arrayImage.add(new InstagramImage(imageURL,thumbnailUrl,likes));
                        }
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
