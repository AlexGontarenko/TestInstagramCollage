package alex_gontarenko.testinstagramcollage.Parcers;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Alex on 21.06.2014.
 */
public class InstagramTokenParcer extends InstagramDataParser<String> {
    private final static String LOG_TAG = "InstagramTokenParcer";
    @Override
    public String parceData(InputStream is) {
        String response = null,token=null;
        try {
            response = streamToString(is);
            JSONObject jsonObject = null;
                jsonObject = (JSONObject) new JSONTokener(response).nextValue();
            if(jsonObject!=null && jsonObject.has("access_token"))
                token=jsonObject.getString("access_token");
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            token=null;
        }  catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
            token=null;
        }
        return token;
    }
}
