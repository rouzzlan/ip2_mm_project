package be.kdg.musicmaker.frontend;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

public class RequestTokenHelperSingleton {
    private static volatile RequestTokenHelperSingleton instance;
    private static Object mutex = new Object();
    public static String OAUTH_TOKEN;

    private RequestTokenHelperSingleton() {
        try {
            String jsonData = getHttpResponse("http://127.0.0.1:8080/oauth/token", "mmapp", "mmapp");
            OAUTH_TOKEN = getTokenFromJson(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RequestTokenHelperSingleton getInstance() {
        RequestTokenHelperSingleton result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new RequestTokenHelperSingleton();
            }
        }
        return result;

    }
    private String getHttpResponse(String address, String username, String password) throws Exception {
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000); // 30 seconds time out

        StringBuilder tokenUri=new StringBuilder("username=");
        tokenUri.append(URLEncoder.encode("user3@user.com","UTF-8"));
        tokenUri.append("&password=");
        tokenUri.append(URLEncoder.encode("user3","UTF-8"));
        tokenUri.append("&grant_type=");
        tokenUri.append(URLEncoder.encode("password","UTF-8"));
        conn.setDoOutput(true);

        if (username != null && password != null){
            String user_pass = username + ":" + password;
            String encoded = Base64.getEncoder().encodeToString(user_pass.getBytes());

            conn.setRequestProperty("Authorization", "Basic " + encoded);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(tokenUri.toString());
            writer.flush();

        }

        String line = "";
        StringBuffer sb = new StringBuffer();
        BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()) );
        while((line = input.readLine()) != null)
            sb.append(line);
        input.close();
        return sb.toString();
    }

    private String getTokenFromJson(String jsondata){
        String token = "";
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsondata);
            token = obj.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }
}
