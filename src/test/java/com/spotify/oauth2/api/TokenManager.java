package com.spotify.oauth2.api;

import com.spotify.oauth2.api.utils.ConfigLoader;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

public class TokenManager {
  private static String access_token;
  private static Instant expiry_time;

  private static Response renewToken() {
    HashMap<String, String> formParams = new HashMap<>();
    formParams.put("grant_type", ConfigLoader.getInstance().getGrantType());
    formParams.put("refresh_token", ConfigLoader.getInstance().getRefreshToken());
    formParams.put("client_id", ConfigLoader.getInstance().getClientId());
    formParams.put("client_secret", ConfigLoader.getInstance().getClientSecret());

    return RestResource.postAccount(formParams);
  }

  public synchronized static String getToken() {
    try {
      if (access_token == null || Instant.now().isAfter(expiry_time)) {
        System.out.println("Renewing token ...");
        Response response = renewToken();
        access_token = response.path("access_token");
        int expiryDurationInSeconds = response.path("expires_in");
        expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 300);
      } else {
        System.out.println("Token is good to use");
      }
    } catch (Exception ignored) {

    }
    return access_token;
  }
}
