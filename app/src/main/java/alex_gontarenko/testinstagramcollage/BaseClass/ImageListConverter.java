package alex_gontarenko.testinstagramcollage.BaseClass;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex on 24.06.2014.
 */
public class ImageListConverter {

    private static final String LOG_TAG="SaverImageList";

    private static final String TAG_ARRAY = "IMAGE_SAVER_ARRAY";
    private static final String TAG_URL = "IMAGE_SAVER_URL";
    private static final String TAG_WIDTH = "IMAGE_SAVER_WIDTH";
    private static final String TAG_HEIGHT = "IMAGE_SAVER_HEIGHT";

    public static String convertArrayToString(ArrayList<ImagePick> array) {
        int i,size=0;
        JSONArray jsonArray=null;
        JSONObject jsonObject=null;

        jsonArray = new JSONArray();
        if(array!=null)
            size = array.size();
        for(i=0;i<size;i++){
            if(!(array.get(i).isPick())) continue;
            try {
                jsonObject = new JSONObject();
                jsonObject.put(TAG_URL,array.get(i).getImgURL());
                jsonObject.put(TAG_WIDTH,array.get(i).getWidthImg());
                jsonObject.put(TAG_HEIGHT,array.get(i).getHeightImg());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                Log.e(LOG_TAG,"Put object: "+ e.toString());
            }
        }
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TAG_ARRAY,jsonArray);
        } catch (JSONException e) {
            Log.e(LOG_TAG,"Put array: "+ e.toString());
        }

        return jsonObject.toString();
    }

    public static ArrayList<InstagramImage> convertStringToArray(String str){
        int i,size=0;
        ArrayList<InstagramImage> arrayImage = null;
        InstagramImage image = null;
        JSONArray jsonArray=null;
        JSONObject jsonObject=null;

        try{
        jsonObject = new JSONObject(str);
        if(jsonObject!=null&&jsonObject.has(TAG_ARRAY)){
            jsonArray = jsonObject.getJSONArray(TAG_ARRAY);
            if(jsonArray!=null&&jsonArray.length()>0){
                size=jsonArray.length();
                arrayImage = new ArrayList<InstagramImage>(size);
                for (i=0;i<size;i++){
                    jsonObject = jsonArray.getJSONObject(i);

                    if(jsonObject==null||!(jsonObject.has(TAG_URL))||!(jsonObject.has(TAG_WIDTH))||!(jsonObject.has(TAG_HEIGHT)))
                        continue;
                    arrayImage.add(new InstagramImage(jsonObject.getString(TAG_URL),jsonObject.getInt(TAG_WIDTH),jsonObject.getInt(TAG_HEIGHT)));
                }
            }
        }
    }catch (JSONException e) {
        Log.e(LOG_TAG, e.toString());
        arrayImage=null;
    }
    return arrayImage;
    }
}