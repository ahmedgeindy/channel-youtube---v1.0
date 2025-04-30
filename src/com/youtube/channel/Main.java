package com.youtube.channel;

import com.youtube.dm.auth.Integration;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
public class Main {

    public static void main(String[] args) throws JSONException, KeyManagementException, NoSuchAlgorithmException, IOException, Exception {

        //sdrCloudInterface sdr = new sdrCloudInterface() ;
        Integration youtube = new Integration( "UCRUaJrw7aoGiwxUkUg7PNEA"
                ,"AIzaSyDQ6ZGjUzKOnuCANUQ4WFkXE5gLY2gT_Jk","http://localhost:8098");
String r1 = youtube.replyOnSpecificYotubeComment("Ugzad2wVoa715t8c4OJ4AaABAg","Hello Nermien");
       System.out.println(r1);
     //  YoutubeRestIntegration r1= youtube.AddComment("YGFC5t0nSFE","Hello from genesys");

//      String  r= youtube.deletecomment("Ugzad2wVoa715t8c4OJ4AaABAg");

 //       System.out.println(r);
String r = youtube.getYotubeComment();
        System.out.println(r);
        JSONObject obj = new JSONObject(r);
        JSONArray YoutubeFeed = obj.getJSONArray("updates");
        for (int i = 0; i < YoutubeFeed.length(); i++) {
            JSONObject currentobj = YoutubeFeed.getJSONObject(i);
            String commentid = currentobj.getString("id");
            System.out.println(commentid);
            String commentDate = currentobj.getString("time");
            System.out.println(commentDate);
            String commentAuthor = currentobj.getString("username");

            System.out.println(commentAuthor);
            String commentText = currentobj.getString("text");

            System.out.println(commentText);
            String commentThumbnail = currentobj.getString("userThumbnail");

            System.out.println(commentThumbnail);
            String videoID = currentobj.getString("videoID");
            System.out.println(videoID);

            String videoThumbnail = currentobj.getString("videoThumbnail");
            System.out.println(videoThumbnail);
            String videoTitle = currentobj.getString("videoTitle");

            System.out.println(videoTitle);
            String videoDescription = currentobj.getString("videoDescription");

            System.out.println(videoDescription);
            String videoOwner = currentobj.getString("videoOwner");

            System.out.println(videoOwner);
        }

      //   System.out.println(  sdr.getInstagramSSLDef());
      //  System.out.println(sdr.PostComment("17854924549411830","اهلا"));
       /* InstagramRestIntegration integration = new InstagramRestIntegration("eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.i2hLWEV2LuYaqcTQT1loUncH2f_gbOYptDfj1PAKYqlNqCS4DiB_Gg.uG_fMbH1BIUzY524UNHFdg.5W_RYe9f2wBPpKZ8CpdEX2T8ocYP0ewcyfuHrPncgmdmQsuC85tniS1N-2PP2BEC0eCioCY-MNYbDn-Lv6eAAjymN51QSPgGAWVAK7XjaKnigOhU7h52tbJzU6E5ejH3lK9Uz9WXd4ZhjI-27ZDICOs4BknFOyAaF31Qnnn3oK1K8gpjJYLwtJUjZs8sQY8KVfL1Lljdcwbu5iXFPAfKjVZvV30-NI74XyeC9MdvuyC4w9Vj055SIVeNMFtV7Rm5Pn-lKsObBVCqKdD7-2IdqBd_VfsgWFfZQy7tMPPVGA8X4HK_5Hf1BTV4mDMxr2jFK7aUzNrcYOfvJ4i2pSa8MANkTdMo1zUyRnUXK934xhQgrdrhBX1TCHyp94jjMqDqMeci1RNZQXkvjXzKeACT8kOOypvO39k7joVWwoVDbJwKePzWao2MIdtVJb9sVvCU3x6E69PYjsHsEUeWBGtDMsrPVPswrK3zP6bn4HV_a6wBx0OxxmLYEWw6DIuoWPdAbbWSojlguflKL0034mUx2HsINzA_-rNGYbCQvzN0OAY.Z12ntVkV0XU0CFuyP2k3lA","AAAAAAAAAAAAAAAA","https://sdr.istnetworks.com:8443/SDRCloudInsta");
        System.out.println(integration.getInstagramComments());

System.out.println(integration.postReply("18084922921009821", "Hello"));
*/
    //    InstagramRestIntegration insta = new InstagramRestIntegration("eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.9Bc8qjo0AQs-Jt5dZByy7ZWAy8jRbBbbKTEmDE9Cba4rdq0cAMNWg.v_lazE3tL92iQgHyglFn8Q.T6MQsjea74NeDIlzBqFuBn5atFwyepIH2Zh-G5-UdBllRaGuovZ-h4AcCjPL4s1yRZ30Wr4axrf3VzVnlgQwjL0qH4FOuZAAOG-44OXHTVc5zvzSGnn1oHCZQ0Zcr1DdzjfBSb6SKcJpfweoVPiy1sTgY3V1USm_hev-K0OzLQ1krl5swRN_LPRXtShsd5Xdx8e9mCKiOX5irnaEbTZ4m199ChQYe_uqZYhTP0mwXoah3hooyOYExa0WbP06tqp.mNwmSqvv997mcHNpN_QqMw","AAAAAAAAAAAAAAAA","https://sdr.istnetworks.com:8443/SDRCloudInsta/rest");
     //   System.out.println(insta.getInstagramComments());
      //  sdrCloudInterface sdr = new sdrCloudInterface() ;
       // System.out.println(  sdr.getInstagramComment());
     /*   InstagramRestIntegration insta = new InstagramRestIntegration();
        insta.setToken("eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.91gTMFxYjuw23uefEDGj9qUOAnY_I9Z25NdyyDZwpkaEOKcQquUdjQ.RVHUCrBVm15OOFepOHSLPQ.4TOAXPxV7GAB9bhg6yaFY-EdCZWMYtNccCoAXKSaGiM.Ha9iQB3y6u9SVFL-3b6n1w");
        System.out.println(insta.getNewInstagramComments());
      //  System.out.println(insta.addNewInstagramComment("18","17854924549411830","Hello"));
        System.out.println(insta.replyToInstagramComment("18","18050998987188063","اهلا"));
//       System.out.println(insta.deleteInstagramComment("18","18061779730110374"));
*/
        //  restClient restobj = new restClient();
   // Boolean r = rest.PostYoutubeTopComment(1,"iRZpj-N9aHI","Genesys has been sucessful");
        // if(r)
//ystem.out.println(S"success");
  //  else System.out.println("fail");
   //    String r = rest.PostInstagramReply("17862972100379183" , "Hi esp ");

        //    System.out.println(r);

    /* String r = restobj.GetInstagramFeed();
        System.out.println(r);
        JSONObject obj = new JSONObject(r);
        JSONArray LinkedinFeed = obj.getJSONArray("updates");

        for (int i =0 ; i <LinkedinFeed.length();i++) {

            JSONObject currentobj = LinkedinFeed.getJSONObject(i);
            JSONObject commentBody =  currentobj.getJSONObject("commentBody");
           String Commentid = commentBody.getString("id");
            String commentDate = commentBody.getString("timestamp");
            String commentAuthor = commentBody.getString("username");

            String commentlikes = commentBody.getString("likes");
            String commentText = commentBody.getString("text");
            JSONObject PostBody =  currentobj.getJSONObject("postBody");
            String postAuthor = PostBody.getString("username");
            String postDate = PostBody.getString("timestamp");
            String postText = PostBody.getString("caption");
            String postImage = PostBody.getString("media_url");

            byte[] postImageArr = Base64.getDecoder().decode(postImage);
            String postLikes = PostBody.getString("likes");
          String postImg = PostBody.getString("profile_pic_url");

            byte[] profilepicArr = Base64.getDecoder().decode(postImg);
            String postID = PostBody.getString("id");

            System.out.println(Commentid);
         //   SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss a dd-mm-yyyy");
         //   Date date = new Date(Long.parseLong(commentDate));
          //  System.out.println(sf.format(date));
            System.out.println(commentDate);
            System.out.println(commentlikes);
            System.out.println(commentAuthor);
            System.out.println(commentText);
            System.out.println(postAuthor);
            System.out.println(postDate);
            System.out.println(postText);
            System.out.println(postImage);
            System.out.println(postLikes);
           System.out.println(postImg);
           System.out.println(postID);
            String [] strs = commentAuthor.split(" ");

            System.out.println(strs[0]);

          // System.out.println(postkey);




    }*/
}
    public static Map<String, String> customHeaders;

    private CloseableHttpClient createHttpClient() {
        String[] supportedProtocols = new String[]{"TLSv1.2"};
        String[] supportedCipherSuites = new String[]{"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256"};
        SSLContext sslContext = SSLContexts.createDefault();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, supportedProtocols, supportedCipherSuites, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient client = HttpClients.custom().setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault())).setDefaultCredentialsProvider(new SystemDefaultCredentialsProvider()).setSSLSocketFactory(sslsf).build();
        return client;
    }
    private Executor executor = Executor.newInstance(this.createHttpClient());
    private Request addCustomHeaders(Request request) {
        String key;
        if (customHeaders != null) {
            for(Iterator var2 = customHeaders.keySet().iterator(); var2.hasNext(); request = request.addHeader(key, (String)customHeaders.get(key))) {
                key = (String)var2.next();
            }
        }

        return request;
    }
    public String get(URI uri, String userToken) throws IOException {
        Request request = Request.Get(uri).connectTimeout(100000).socketTimeout(100000);
        request.addHeader("Authorization", userToken);
        request = this.addCustomHeaders(request);
        HttpResponse response =
                this.executor.execute(request).returnResponse();
        String result = EntityUtils.toString(response.getEntity(),"UTF-8");
        return result;
    }
}
