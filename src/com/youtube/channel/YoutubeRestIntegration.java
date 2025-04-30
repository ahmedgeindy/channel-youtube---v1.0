package com.youtube.channel;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.net.URLEncoder;

public class YoutubeRestIntegration {

    static  String YoutubeSocialHostURL ="https://sdr.istnetworks.com:8443/SDR_New/rest/YouTube/";


    public  String getYoutubeComments()
    {
        try {

            String RequestURL = "getComments";
            Client client = Client.create();
            WebResource webResource = client
                    .resource(YoutubeSocialHostURL+RequestURL);
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


    public  String AddComment(String videoID,String comment)
    {
        try {

            String RequestURL = "addComment?videoID="+videoID+"&text="+ URLEncoder.encode(comment,"utf-8");
            Client client = Client.create();
            WebResource webResource = client
                    .resource(YoutubeSocialHostURL+RequestURL);
            ClientResponse response = webResource.accept("application/json")
                    .post(ClientResponse.class);
            if (response.getStatus() == 200) {
                String output = response.getEntity(String.class);

                return output;
            }else
                return null;
        } catch (Exception e) {

            return e.getMessage();
        }

    }

    public  String AddReply(String commentID,String reply)
    {
        try {
            String RequestURL = "addReply?commentID="+commentID+"&text="+ URLEncoder.encode(reply,"utf-8");
            Client client = Client.create();
            WebResource webResource = client
                    .resource(YoutubeSocialHostURL+RequestURL);
            ClientResponse response = webResource.accept("application/json")
                    .post(ClientResponse.class);
            if (response.getStatus() == 200) {
                String output = response.getEntity(String.class);

                return output;
            }else
                return null;
        } catch (Exception e) {

            return e.getMessage();
        }

    }

    public  String deletecomment(String commentID)
    {
        try {

            String RequestURL = "deleteComment?commentID="+commentID;
            Client client = Client.create();
            WebResource webResource = client
                    .resource(YoutubeSocialHostURL+RequestURL);
            ClientResponse response = webResource.accept("application/json")
                    .delete(ClientResponse.class);
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
