package Game;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class ServerConnector {
    HttpPost request = new HttpPost("http://iamgc.com/app_data/get1to50Rank.php");
    
    private Vector<NameValuePair> setParams(String command, ArrayList<String[]> data){
        Vector<NameValuePair> params = new Vector<NameValuePair>();
        params.add(new BasicNameValuePair("Command", command));
        
        if (data != null) {
            for(int i = 0; i<data.size(); i++){
                params.add(new BasicNameValuePair(data.get(i)[0], data.get(i)[1]));
            }
        }
        return params;
    }
    
    private void setEntity(Vector<NameValuePair> params) throws UnsupportedEncodingException{
        HttpEntity entity = null;
        entity = new UrlEncodedFormEntity(params,"UTF-8");
        request.setEntity(entity);
    }
    
    public String send(String command, ArrayList<String[]> data) throws IOException {
        setEntity(setParams(command, data));

        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> reshandler = new BasicResponseHandler();

        return client.execute(request, reshandler);
    }
}