package alex_gontarenko.testinstagramcollage.Instagram;

public class InstagramAPI {
    //Application data
    private static final String CLIENT_ID = "f758b38e3c1f49e0bbb0e3b25958c91c";
    private static final String CLIENT_SECRET = "518dade2374b4387a7d2533efe6222f5";
    private static final String WEBSITE_URL = "http://m.vk.com/alexander_gontarenko";
    private static final String REDIRECT_URI = "http://m.vk.com/alexander_gontarenko";

    //URL API
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";

    private static InstagramAPI _instance;
    private String _token;

    synchronized public static InstagramAPI instance(){
        if(_instance==null)
            _instance=new InstagramAPI();
        return _instance;
    }

    public void setToken(String token){
        _token=token;
    }

    public String getToken(){
        return _token;
    }

    public static String getRedirectURI(){
        return REDIRECT_URI;
    }

    public static String getClientId(){
        return CLIENT_ID;
    }

    public static String getClientSecret(){
        return CLIENT_SECRET;
    }

    public static String getURLInstagramAuth(){
        StringBuffer buffer=new StringBuffer();
        buffer.append(AUTH_URL);
        buffer.append("?client_id=");
        buffer.append(CLIENT_ID);
        buffer.append("&redirect_uri=");
        buffer.append(REDIRECT_URI);
        buffer.append("&response_type=code");
        return buffer.toString();
    }

    public static String getURLInstagramToken(){
        StringBuffer buffer=new StringBuffer();
        buffer.append(TOKEN_URL);
        buffer.append("?client_id=");
        buffer.append(CLIENT_ID);
        buffer.append("&client_secret=");
        buffer.append(CLIENT_SECRET);
        buffer.append("&redirect_uri=");
        buffer.append(REDIRECT_URI);
        buffer.append("&grant_type=authorization_code");
        return buffer.toString();
    }

    public static String getPOSTCodeData(String code){
        StringBuffer buffer=new StringBuffer();
        buffer.append("client_id=");
        buffer.append(CLIENT_ID);
        buffer.append("&client_secret=");
        buffer.append(CLIENT_SECRET);
        buffer.append("&grant_type=authorization_code");
        buffer.append("&redirect_uri=");
        buffer.append(REDIRECT_URI);
        buffer.append("&code=");
        buffer.append(code);
        return buffer.toString();
    }

    public static String getURLFindUser(String token,String nickname){
        StringBuffer buffer = new StringBuffer();
        buffer.append(API_URL);
        buffer.append("/users/search?q=");
        buffer.append(nickname);
        buffer.append("&access_token=");
        buffer.append(token);
        return buffer.toString();
    }

    public static String getURLMediaDataUser(String token,String user_id){
        StringBuffer buffer = new StringBuffer();
        buffer.append(API_URL);
        buffer.append("/users/");
        buffer.append(user_id);
        buffer.append("/media/recent/?access_token=");
        buffer.append(token);
        return buffer.toString();
    }
}
