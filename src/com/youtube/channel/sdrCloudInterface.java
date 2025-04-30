package com.youtube.channel;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.json.JSONException;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class sdrCloudInterface {

    public  static String getCredetialByte(String channelId, String apiKey) {
        String plainCreds = channelId + ":" + apiKey;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        return base64Creds;
    }



    private String token;
    private YoutubeSSLDefault cloudInterface;

    public sdrCloudInterface(String channelId, String apiKey, String REST_URL) {
        this.token =getCredetialByte(channelId, apiKey);
        this.cloudInterface = new YoutubeSSLDefault(token, REST_URL);
    }

    public String getYotubeComment() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return cloudInterface.getYoutubeComments();
    }

    public String sendYotubeMessage(String videoId, String CommentText)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return cloudInterface.addyoutubeComment(videoId, CommentText);

    }

    public String deleteYotubeComment(String CommentId)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException , JSONException {
        return cloudInterface.deleteInstagramComment(CommentId);

    }

    public String replyOnSpecificYotubeComment(String commentId, String CommentText)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return cloudInterface.addYoutubeReply(commentId, CommentText);

    }







    String RestToken="eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.91gTMFxYjuw23uefEDGj9qUOAnY_I9Z25NdyyDZwpkaEOKcQquUdjQ.RVHUCrBVm15OOFepOHSLPQ.4TOAXPxV7GAB9bhg6yaFY-EdCZWMYtNccCoAXKSaGiM.Ha9iQB3y6u9SVFL-3b6n1w" ;

    public String postReply(String commentID , String replytxt , String token ) throws NoSuchAlgorithmException,
            KeyManagementException, UnsupportedEncodingException {
        final String API_URI = "https://sdr.istnetworks.com:8443/socialdatarouter/rest/FacebookRestApi/replyToInstagramComment?commentID=" + commentID + "&StreamID=" + 18 + "&replyText=" + replytxt;


        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
                .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                        new HTTPSProperties(
                                NoopHostnameVerifier.INSTANCE,
                                rest.SSLUtil.getInsecureSSLContext()));
        final Client client = Client.create(config);
        final WebResource resource = client.resource(API_URI);


     //   String token = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.91gTMFxYjuw23uefEDGj9qUOAnY_I9Z25NdyyDZwpkaEOKcQquUdjQ.RVHUCrBVm15OOFepOHSLPQ.4TOAXPxV7GAB9bhg6yaFY-EdCZWMYtNccCoAXKSaGiM.Ha9iQB3y6u9SVFL-3b6n1w";

        final ClientResponse response = resource.type("application/json").header("Authorization", token)
                .post(ClientResponse.class);
        if (response.getStatus() == 200) {
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        } else {
            client.destroy();
            return null;
        }
    }
    public String PostComment(String mediaID , String commentText ) throws NoSuchAlgorithmException,
            KeyManagementException, UnsupportedEncodingException {
          final String API_URI ="https://sdr.istnetworks.com:8443/socialdatarouter/rest/FacebookRestApi/addInstagramComment?StreamID=18"+"&mediaID=" + mediaID + "&commentText="+ commentText;

        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
                .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                        new HTTPSProperties(
                                NoopHostnameVerifier.INSTANCE,
                                rest.SSLUtil.getInsecureSSLContext()));
        final Client client = Client.create(config);
        final WebResource resource = client.resource(API_URI);


        String token = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.91gTMFxYjuw23uefEDGj9qUOAnY_I9Z25NdyyDZwpkaEOKcQquUdjQ.RVHUCrBVm15OOFepOHSLPQ.4TOAXPxV7GAB9bhg6yaFY-EdCZWMYtNccCoAXKSaGiM.Ha9iQB3y6u9SVFL-3b6n1w";

        final ClientResponse response = resource.type("application/json").header("Authorization", token)
                .post(ClientResponse.class);
        if (response.getStatus() == 200) {
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        } else {
            client.destroy();
            return null;
        }
    }

    public String postcommentheader(String mediaID , String commentText) throws NoSuchAlgorithmException,
            KeyManagementException, UnsupportedEncodingException {
        final String API_URI ="https://sdr.istnetworks.com:8443/socialdatarouter/rest/FacebookRestApi/addInstagramCommentHeader";
      //  final String API_URI ="https://sdr.istnetworks.com:8443/socialdatarouter/rest/FacebookRestApi/addInstagramCommentHeader?StreamID=18"+"&mediaID=" + mediaID + "&commentText="+ URLEncoder.encode(commentText,"UTF-8");

        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
                .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                        new HTTPSProperties(
                                NoopHostnameVerifier.INSTANCE,
                                rest.SSLUtil.getInsecureSSLContext()));
        final Client client = Client.create(config);
        final WebResource resource = client.resource(API_URI);
        String token = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.91gTMFxYjuw23uefEDGj9qUOAnY_I9Z25NdyyDZwpkaEOKcQquUdjQ.RVHUCrBVm15OOFepOHSLPQ.4TOAXPxV7GAB9bhg6yaFY-EdCZWMYtNccCoAXKSaGiM.Ha9iQB3y6u9SVFL-3b6n1w";


        WebResource.Builder b = resource.type("application/json");
        b.header("Authorization", token);
        b.header("mediaID", mediaID);
        b.header("StreamID", "18");
        b.header("commentText", URLEncoder.encode(commentText,"UTF-8"));

        final ClientResponse response = b.get(ClientResponse.class);
        if (response.getStatus() == 200) {
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        } else {
            client.destroy();
            return null;
        }
    }
    public String DeleteComment(String CommentID) throws NoSuchAlgorithmException,
            KeyManagementException, UnsupportedEncodingException {
        final String API_URI ="https://sdr.istnetworks.com:8443/socialdatarouter/rest/FacebookRestApi/deleteInstagramComment?StreamID=18&commentID=" + CommentID;

        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
                .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                        new HTTPSProperties(
                                NoopHostnameVerifier.INSTANCE,
                                rest.SSLUtil.getInsecureSSLContext()));
        final Client client = Client.create(config);
        final WebResource resource = client.resource(API_URI);
        String token = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.91gTMFxYjuw23uefEDGj9qUOAnY_I9Z25NdyyDZwpkaEOKcQquUdjQ.RVHUCrBVm15OOFepOHSLPQ.4TOAXPxV7GAB9bhg6yaFY-EdCZWMYtNccCoAXKSaGiM.Ha9iQB3y6u9SVFL-3b6n1w";

        final ClientResponse response = resource.type("application/json").header("Authorization", token)
                .delete(ClientResponse.class);
        if (response.getStatus() == 200) {
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        } else {
            client.destroy();
            return null;
        }
    }



    public String getInstagramNewwebclient () throws NoSuchAlgorithmException, KeyManagementException {

        final String API_URI = "https://sdr.istnetworks.com:8443/SDRRestSocial/rest/SocialRestApi/GetNewInstagramComments";
      //  final String API_URI = "https://dibcloud.sdr.ist:8443/DIBProd/rest/SocialRestApi/GetNewInstagramComments";
        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
                .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                        new HTTPSProperties(
                                NoopHostnameVerifier.INSTANCE,
                                SSLUtil.getInsecureSSLContext()));
        final Client client = Client.create(config);
        final WebResource resource = client.resource(API_URI);
        String token = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.91gTMFxYjuw23uefEDGj9qUOAnY_I9Z25NdyyDZwpkaEOKcQquUdjQ.RVHUCrBVm15OOFepOHSLPQ.4TOAXPxV7GAB9bhg6yaFY-EdCZWMYtNccCoAXKSaGiM.Ha9iQB3y6u9SVFL-3b6n1w";
     //   WebResource.Builder builder = resource.header("Authorization", token);
        final ClientResponse response = resource.type("application/json").header("Authorization", token)
                .get(ClientResponse.class);
        if (response.getStatus() == 200) {
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        } else {
            client.destroy();
            return null;
        }
    }



    public String getInstagramSSLDef () throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {

        final String API_URI = "https://sdr.istnetworks.com:8443/SDRRestSocial/rest/SocialRestApi/GetNewInstagramComments";
        //  final String API_URI = "https://dibcloud.sdr.ist:8443/DIBProd/rest/SocialRestApi/GetNewInstagramComments";
        final ClientConfig config = new DefaultClientConfig();
        String token = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.91gTMFxYjuw23uefEDGj9qUOAnY_I9Z25NdyyDZwpkaEOKcQquUdjQ.RVHUCrBVm15OOFepOHSLPQ.4TOAXPxV7GAB9bhg6yaFY-EdCZWMYtNccCoAXKSaGiM.Ha9iQB3y6u9SVFL-3b6n1w";
        HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        SSLContext ctx = SSLContext.getInstance("SSL");

        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null);
        for (TrustManager t : tmf.getTrustManagers())
        {

        }
        ctx.init(null, tmf.getTrustManagers(), null);


        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));
        Client client = Client.create(config);
        final WebResource resource = client.resource(API_URI);
        final ClientResponse response = resource.type("application/json").header("Authorization", token)
                .get(ClientResponse.class);
        if (response.getStatus() == 200) {
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        } else {
            client.destroy();
            return null;
        }
    }




    public  String getInstagramComment() throws IOException {
        final String URL = "https://sdr.istnetworks.com:8443/SDRRestSocial/rest/SocialRestApi/GetNewInstagramComments";

      return  sendRequest(URL,RestToken, "GET");

    }
    private String sendRequest(String link,String token,String requestMethod) throws IOException {
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("Authorization",token);
        conn.setDoOutput(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        String inputLine;
        StringBuilder resp = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }
        in.close();
        return resp.toString();
    }


    public  String getInstagramFeedrequest() throws NoSuchAlgorithmException,
            KeyManagementException {
        final String API_URI = "https://dibcloud.sdr.ist:8443/DIBProd/rest/SocialRestApi/GetNewInstagramComments";


        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
                .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                        new HTTPSProperties(
                                NoopHostnameVerifier.INSTANCE,
                                SSLUtil.getInsecureSSLContext()));
        final Client client = Client.create(config);
        final WebResource resource = client.resource(API_URI);

        final ClientResponse response = resource.type("application/json")
                .get(ClientResponse.class);
        if (response.getStatus() == 200) {
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        }else {
            client.destroy();
            return null;
        }

    }
    public  String getInstagramFeedrequest(String SDRServerURL) throws NoSuchAlgorithmException,
            KeyManagementException {
        final String API_URI = SDRServerURL+"GetNewInstagramComments";


        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
                .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                        new HTTPSProperties(
                                NoopHostnameVerifier.INSTANCE,
                                SSLUtil.getInsecureSSLContext()));
        final Client client = Client.create(config);
        final WebResource resource = client.resource(API_URI);

        final ClientResponse response = resource.type("application/json")
                .get(ClientResponse.class);
        if (response.getStatus() == 200) {
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        }else {
            client.destroy();
            return null;
        }

    }
    // convert InputStream to String
    private String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        final StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static class SSLUtil {
        protected static SSLContext getInsecureSSLContext()
                throws KeyManagementException, NoSuchAlgorithmException {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                final java.security.cert.X509Certificate[] arg0, final String arg1)
                                throws CertificateException {
                            // do nothing and blindly accept the certificate
                        }

                        public void checkServerTrusted(
                                final java.security.cert.X509Certificate[] arg0, final String arg1)
                                throws CertificateException {
                            // do nothing and blindly accept the server
                        }

                    }
            };

            final SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            return sslcontext;
        }
    }




}
