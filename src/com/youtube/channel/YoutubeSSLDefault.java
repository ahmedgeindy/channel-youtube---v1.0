package com.youtube.channel;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
    public class YoutubeSSLDefault {

        private String token;
        private String REST_API_URI="http://localhost:8585/youtubeDBM";
        Proxy proxy;

        private void initializeProxy() {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.246.1.20", 8080));
        }

        public YoutubeSSLDefault(String token, String REST_API_URI) {
            this.token = token;
            this.REST_API_URI = REST_API_URI;
            initializeProxy();
        }
        public String getYoutubeComments() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
            final String API_URI =  this.REST_API_URI + "/youtubeApi/GetNewYoutubeComments";
            final ClientConfig config = new DefaultClientConfig();
            HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            SSLContext ctx = SSLContext.getInstance("SSL");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            ctx.init(null, tmf.getTrustManagers(), null);
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                    new HTTPSProperties(hostnameVerifier, ctx));
            Client client = Client.create(config);
            final WebResource resource = client.resource(API_URI);

            final ClientResponse response = resource.type("application/json").header("Authorization", "Basic VUNSVWFKcnc3YW9HaXd4VWtVZzdQTkVBOkFJemFTeURRNlpHalV6S09udUNBTlVRNFdGa1hFNWdMWTJnVF9Kaw==")
                    .get(ClientResponse.class);
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        }

        public String addyoutubeComment(String videoId, String commentText)
                throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
           // final String API_URI =  this.REST_API_URI+"/youtubeApi/insertComment?videoID="+videoId+"&text="+commentText;
           //final String API_URI = "http://localhost:8585/youtubeDBM/youtubeApi/insertComment?videoID="+videoId+"&text="+commentText;
            final String API_URI = "https://sdrsocialmedia.qcdib.com/youtub-dbm/youtubeApi/insertComment?videoID="+videoId+"&text="+commentText;

            final ClientConfig config = new DefaultClientConfig();
            JSONObject body = new JSONObject();
            try {

                body.put("videoId", videoId);
                body.put("commentText", commentText);

            } catch (JSONException e) {

                e.printStackTrace();
            }

            HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            SSLContext ctx = SSLContext.getInstance("SSL");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            ctx.init(null, tmf.getTrustManagers(), null);
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                    new HTTPSProperties(hostnameVerifier, ctx));
            Client client = Client.create(config);
            final WebResource resource = client.resource(API_URI);
            final ClientResponse response = resource.type("application/json").header("Authorization", "Basic VUNSVWFKcnc3YW9HaXd4VWtVZzdQTkVBOkFJemFTeURRNlpHalV6S09udUNBTlVRNFdGa1hFNWdMWTJnVF9Kaw==")
                    .post(ClientResponse.class, null);
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        }
        public String addYoutubeReply(String commentId, String commentText)
                throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
           // final String API_URI = this.REST_API_URI+"/youtubeApi/replyOnComment?commentID="+commentId+"&text="+commentText;
           final String API_URI = "https://sdrsocialmedia.qcdib.com/youtub-dbm/youtubeApi/replyOnComment?commentID="+commentId+"&text="+commentText;
            System.out.println("total url"+ API_URI);

            // getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube ID ... " + ID);

            final ClientConfig config = new DefaultClientConfig();
            JSONObject body = new JSONObject();
            try {

                body.put("commentID", commentId);
                body.put("commentText", commentText);

            } catch (JSONException e) {

                e.printStackTrace();
            }

            HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            SSLContext ctx = SSLContext.getInstance("SSL");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            ctx.init(null, tmf.getTrustManagers(), null);
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                    new HTTPSProperties(hostnameVerifier, ctx));
            Client client = Client.create(config);
            final WebResource resource = client.resource(API_URI);
            final ClientResponse response = resource.type("application/json").header("Authorization", "Basic VUNSVWFKcnc3YW9HaXd4VWtVZzdQTkVBOkFJemFTeURRNlpHalV6S09udUNBTlVRNFdGa1hFNWdMWTJnVF9Kaw==")
                    .post(ClientResponse.class, body.toString());
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        }

        public String deleteInstagramComment(String commentID)
                throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, JSONException {
          //  final String API_URI = this.REST_API_URI+"/youtubeApi/deleteComment?commentID="+commentID;
           final String API_URI = "https://sdrsocialmedia.qcdib.com/youtub-dbm/youtubeApi/deleteComment?commentID="+commentID;
            JSONObject body = new JSONObject();
            body.put("commentID", commentID);
            final ClientConfig config = new DefaultClientConfig();
            HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            SSLContext ctx = SSLContext.getInstance("SSL");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            ctx.init(null, tmf.getTrustManagers(), null);
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                    new HTTPSProperties(hostnameVerifier, ctx));
            Client client = Client.create(config);
            final WebResource resource = client.resource(API_URI);
            final ClientResponse response = resource.type("application/json").header("Authorization", "Basic VUNSVWFKcnc3YW9HaXd4VWtVZzdQTkVBOkFJemFTeURRNlpHalV6S09udUNBTlVRNFdGa1hFNWdMWTJnVF9Kaw==")
                    .post(ClientResponse.class, null);
            String output = response.getEntity(String.class);
            client.destroy();
            return output;
        }
    }


