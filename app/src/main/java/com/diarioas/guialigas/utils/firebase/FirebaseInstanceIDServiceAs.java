package com.diarioas.guialigas.utils.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDServiceAs extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {
    String DeviceToken = FirebaseInstanceId.getInstance().getToken();
    Log.d("DeviceToken ==> ",  DeviceToken);
  }

}
