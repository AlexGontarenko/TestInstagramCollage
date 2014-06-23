package alex_gontarenko.testinstagramcollage.Parcers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Alex on 21.06.2014.
 */
public class InstagramFindUserParcer extends InstagramDataParser<String> {
    private final static String LOG_TAG = "InstagramFindUserParcer";
    private String _nickname;
    public InstagramFindUserParcer(String nickname){
        _nickname=nickname;
    }
    @Override
    public String parceData(InputStream is) {
        String response = null;
        int i,size;
        String userId=null;
        JSONObject object=null;
        JSONArray array = null;
        try {
            response = streamToString(is);
            array = (new JSONObject(response)).getJSONArray("data");
            if(array!=null) {
                size = array.length();
                if (size != 0) {
                    for (i = 0; i < size; i++) {
                        object = array.getJSONObject(i);
                        if (object.has("username") && object.getString("username").compareTo(_nickname) == 0)
                            break;
                    }
                    if (i != size && array.getJSONObject(i).has("id"))
                            userId = array.getJSONObject(i).getString("id");
                }
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            userId=null;
        }  catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
            userId=null;
        }
        return userId;
    }
}