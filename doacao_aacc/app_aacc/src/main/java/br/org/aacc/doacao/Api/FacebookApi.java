package br.org.aacc.doacao.Api;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Domain.FacebookUser;


/**
 * Created by ubuntu on 12/11/16.
 */


public class FacebookApi
{


    private Bitmap bitmap;
    private String profilePicUrl;
    private  static FacebookUser facebookUser;
    private GraphRequest graphRequest;


    public FacebookUser GetProfilePicture()
    {
        Bundle params = new Bundle();
        params.putString("fields", "id,email,gender,cover,picture.type(large),first_name,last_name,age_range,link,locale");


        facebookUser =new FacebookUser();

            graphRequest=   new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();

                                if (data.has("picture"))
                                {
                                    facebookUser.ImageUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    facebookUser.Id =data.getString("id");
                                    facebookUser.Email =data.getString("email");
                                    facebookUser.Name =data.getString("first_name");
                                    facebookUser.LastName =data.getString("last_name");
                                    facebookUser.Age_Range =data.getJSONObject("age_range").getInt("min");
                                    facebookUser.Link =data.getString("link");
                                    facebookUser.Locale=data.getString("locale");

                                }
                            } catch (Exception e) {
                                TrackHelper.WriteError(this,"GetProfilePicture",e.getMessage());
                            }
                        }
                    }
                });


        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {

                GraphResponse gResponse = graphRequest.executeAndWait();
            }
        });


        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       return facebookUser;

    }


    public void setNullProfileFacebook()
    {
        facebookUser=null;
    }


    public static FacebookUser GetProfileFacebook(){
        try{
            return facebookUser;
        }
        catch (Exception e)
        {
            throw e;
        }
    }


}




