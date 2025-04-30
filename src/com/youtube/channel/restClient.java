package com.youtube.channel;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class restClient {

    static  String InstagramSocialHostURL="https://sdr.istnetworks.com:8443/SDRRestSocial/rest/SocialRestApi/";

    public String GetInstagramFeed()
    {
        try {

            String RequestURL = "NewGetNewInstagramComments";
            Client client = Client.create();
            WebResource webResource = client
                    .resource(InstagramSocialHostURL+RequestURL);
            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);
            if (response.getStatus() == 200) {
                String output = response.getEntity(String.class);

                return output;
            }else
                return null;
        } catch (Exception e) {

            return e.getMessage();
        }

    }



}
