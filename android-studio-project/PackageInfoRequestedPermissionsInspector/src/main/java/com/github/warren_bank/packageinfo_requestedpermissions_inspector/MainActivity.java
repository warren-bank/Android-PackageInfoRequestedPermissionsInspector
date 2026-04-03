package com.github.warren_bank.packageinfo_requestedpermissions_inspector;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView requested_permissions_textview = (TextView) findViewById(R.id.requested_permissions);
    requested_permissions_textview.setText(
      get_requested_permissions()
    );
  }

  private String get_requested_permissions() {
    try {
        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);

        if ((info.requestedPermissions == null) || (info.requestedPermissions.length == 0))
          throw new Exception();

        if (Build.VERSION.SDK_INT < 16) {
          return TextUtils.join("\n", info.requestedPermissions);
        }

        StringBuffer buf          = new StringBuffer();
        ArrayList<String> denied  = new ArrayList<String>();
        ArrayList<String> granted = new ArrayList<String>();

        for (int i = 0; i < info.requestedPermissions.length; i++) {
          if ((info.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) == 0)
            denied.add(info.requestedPermissions[i]);
          else
            granted.add(info.requestedPermissions[i]);
        }

        if (!granted.isEmpty()) {
          buf.append("granted\n");
          buf.append("=======\n");
          buf.append(
            TextUtils.join("\n", granted)
          );
        }
        if (!granted.isEmpty() && !denied.isEmpty()) {
          buf.append("\n\n");
        }
        if (!denied.isEmpty()) {
          buf.append("denied\n");
          buf.append("======\n");
          buf.append(
            TextUtils.join("\n", denied)
          );
        }

        return buf.toString();
    }
    catch (Exception e) {
        return "";
    }
  }
}
