package com.youtube.channel;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.codehaus.jettison.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class rest {

    public void examplePost() throws NoSuchAlgorithmException,
            KeyManagementException {
        final String API_URI = "https://currencytrade-spray.herokuapp.com/v1/trade";

        /*
         * There are times during development that security certificates are not
         * available or you can not install the certificates in a particular
         * environment.
         *
         * In this situations you may face the error shown below when attempting
         * to make an SSL connection:
         *
         * javax.net.ssl.SSLHandshakeException:
         * sun.security.validator.ValidatorException: PKIX path building failed:
         * sun.security.provider.certpath.SunCertPathBuilderException: unable to
         * find valid certification path to requested target
         *
         * The ClientConfig created below uses a "Trust All"
         * SSLConnectionSocketFactory which blindly trusts all certificates.
         * This is very insecure and leaves you vulnerable to MitM attacks.
         *
         * This approach can be useful during development if security
         * certificates are not available
         */
        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
                .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                        new HTTPSProperties(
                                NoopHostnameVerifier.INSTANCE,
                                SSLUtil.getInsecureSSLContext()));
        final Client client = Client.create(config);
        final WebResource resource = client.resource(API_URI);

        /*
         * The JSON representation to be sent to the API
         * {
         * "userId": "134256",
         * "currencyFrom": "EUR",
         * "currencyTo": "GBP",
         * "amountSell": 1000,
         * "amountBuy": 747.10,
         * "rate": 0.7471,
         * "timePlaced" :"24-JAN-15 10:27:44",
         * "originatingCountry" : "FR"
         * }
         */
        final JSONObject jsonToSend = new JSONObject();
      /*  jsonToSend.put("userId", "134256");
        jsonToSend.put("currencyFrom", "EUR");
        jsonToSend.put("currencyTo", "GBP");
        jsonToSend.put("amountSell", 1000);
        jsonToSend.put("amountBuy", 747.10);
        jsonToSend.put("rate", 0.7471);
        jsonToSend.put("timePlaced", "24-JAN-15 10:27:44");
        jsonToSend.put("originatingCountry", "FR");*/

        // POST request

        final ClientResponse response = resource.type("application/json")
                .post(ClientResponse.class, jsonToSend.toString());
        final String result = getStringFromInputStream(response.getEntityInputStream());
        System.out.println("INFO >>> Response from API was: " + result);
        client.destroy();

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
