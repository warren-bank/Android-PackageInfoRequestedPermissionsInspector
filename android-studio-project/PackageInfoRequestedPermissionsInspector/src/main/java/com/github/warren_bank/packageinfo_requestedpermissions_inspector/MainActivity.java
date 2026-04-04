package com.github.warren_bank.packageinfo_requestedpermissions_inspector;

import com.github.warren_bank.packageinfo_requestedpermissions_inspector.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
  private static final int REQUEST_CODE_PERMISSIONS  = 0;

  private TextView textview_requested_permissions;
  private Button button_request_permissions;
  private ArrayList<String> denied;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textview_requested_permissions = (TextView) findViewById(R.id.textview_requested_permissions);
    button_request_permissions     = (Button)   findViewById(R.id.button_request_permissions);

    button_request_permissions.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if ((denied != null) && !denied.isEmpty()) {
          requestPermissions(denied.toArray(new String[denied.size()]), REQUEST_CODE_PERMISSIONS);
        }
      }
    });

    reset();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode != REQUEST_CODE_PERMISSIONS)
      return;

    reset();
    summarizeRequestPermissionsResult(permissions, grantResults);
  }

  private void reset() {
    show_requested_permissions(
      get_requested_permissions()
    );
  }

  private void summarizeRequestPermissionsResult(String[] permissions, int[] grantResults) {
    StringBuffer buf = new StringBuffer();
    ArrayList<String> grant_results = new ArrayList<String>();

    for (int i = 0; i < grantResults.length; i++) {
      grant_results.add(
        (grantResults[i] == PackageManager.PERMISSION_GRANTED)
          ? getString(R.string.result_heading_granted)
          : getString(R.string.result_heading_denied)
      );
    }

    appendHeading(buf, getString(R.string.result_heading_on_request_permissions_result_permissions));
    appendList(buf, permissions);

    appendHeading(buf, getString(R.string.result_heading_on_request_permissions_result_grant_results));
    appendList(buf, grant_results);

    append_requested_permissions(buf);
  }

  private void show_requested_permissions(String text) {
    textview_requested_permissions.setText(text);
  }

  private void append_requested_permissions(String text) {
    append_requested_permissions(
      new StringBuffer(text)
    );
  }

  private void append_requested_permissions(StringBuffer buf) {
    buf.insert( //prepend
      0,
      "\n\n"
    );
    buf.insert( //prepend
      0,
      textview_requested_permissions.getText().toString()
    );
    show_requested_permissions(
      buf.toString()
    );
  }

  private String get_requested_permissions() {
    try {
        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);

        if (info.requestedPermissions == null)
          throw new Exception();

        StringBuffer buf = new StringBuffer();

        if (Build.VERSION.SDK_INT < 16) {
          appendHeading(buf, getString(R.string.result_heading_all));
          appendList(buf, info.requestedPermissions);
        }
        else {
          denied                    = new ArrayList<String>();
          ArrayList<String> granted = new ArrayList<String>();

          for (int i = 0; i < info.requestedPermissions.length; i++) {
            if ((info.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) == 0)
              denied.add(info.requestedPermissions[i]);
            else
              granted.add(info.requestedPermissions[i]);
          }

          appendHeading(buf, getString(R.string.result_heading_denied));
          appendList(buf, denied);

          appendHeading(buf, getString(R.string.result_heading_granted));
          appendList(buf, granted);

          button_request_permissions.setVisibility(
            (!denied.isEmpty() && (Build.VERSION.SDK_INT >= 23))
              ? View.VISIBLE
              : View.GONE
          );
        }

        appendHeading(buf, getString(R.string.result_heading_check));
        appendList(buf, filterByCheckCallingOrSelfPermission(info.requestedPermissions));

        return buf.toString();
    }
    catch (Exception e) {
        return "";
    }
  }

  private ArrayList<String> filterByCheckCallingOrSelfPermission(String[] requestedPermissions) {
    ArrayList<String> filtered = new ArrayList<String>();
    for (int i = 0; i < requestedPermissions.length; i++) {
      String permission = requestedPermissions[i];
      if (checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
        filtered.add(permission);
      }
    }
    return filtered;
  }

  private void appendHeading(StringBuffer buf, String heading) {
    String divider = new String(new char[heading.length()]).replace("\0", "=");

    if (buf.length() > 0) {
      buf.append("\n\n");
    }
    buf.append(heading);buf.append("\n");
    buf.append(divider);buf.append("\n");
  }

  private void appendList(StringBuffer buf, ArrayList<String> list) {
    if ((list == null) || list.isEmpty()) {
      buf.append(
        getString(R.string.result_list_empty)
      );
    }
    else {
      buf.append(
        TextUtils.join("\n", list)
      );
    }
  }

  private void appendList(StringBuffer buf, String[] list) {
    if ((list == null) || (list.length == 0)) {
      buf.append(
        getString(R.string.result_list_empty)
      );
    }
    else {
      buf.append(
        TextUtils.join("\n", list)
      );
    }
  }
}
