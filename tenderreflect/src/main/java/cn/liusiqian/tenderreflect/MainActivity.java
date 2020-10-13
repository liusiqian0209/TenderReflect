package cn.liusiqian.tenderreflect;

import java.lang.reflect.Method;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

  private static final String TAG = "ReflectTag";

  private TextView tvHello;
  private TextView tvCallDirect, tvChangeHiddenApiPolicy, tvCallMetaRef;
  private TextView tvSetHiddenApiExemptions, tvSetHiddenApiExemptionsNative;

  // Used to load the 'native-lib' library on application startup.
  static {
    System.loadLibrary("native-lib");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initWidgets();
  }

  private void initWidgets() {
    tvHello = findViewById(R.id.txt_hello);
    tvCallDirect = findViewById(R.id.txt_call_ref_direct);
    tvChangeHiddenApiPolicy = findViewById(R.id.txt_change_hiddenapipolicy);
    tvCallMetaRef = findViewById(R.id.txt_call_meta_reflect);
    tvSetHiddenApiExemptions = findViewById(R.id.txt_set_hidden_exemptions);
    tvSetHiddenApiExemptionsNative = findViewById(R.id.txt_set_hidden_exemptions_native);

    tvHello.setText(stringFromJNI());
    tvCallDirect.setOnClickListener(ocl);
    tvChangeHiddenApiPolicy.setOnClickListener(ocl);
    tvCallMetaRef.setOnClickListener(ocl);
    tvSetHiddenApiExemptions.setOnClickListener(ocl);
    tvSetHiddenApiExemptionsNative.setOnClickListener(ocl);
  }

  private View.OnClickListener ocl = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if (view.getId() == R.id.txt_call_ref_direct) {
        callReflectDirect();
      } else if (view.getId() == R.id.txt_change_hiddenapipolicy) {
        callChangeHiddenApiPolicyNative(getApplicationInfo().targetSdkVersion);
      } else if (view.getId() == R.id.txt_call_meta_reflect) {
        callMetaReflect();
      } else if (view.getId() == R.id.txt_set_hidden_exemptions) {
        setHiddenApiExemptions();
      } else if (view.getId() == R.id.txt_set_hidden_exemptions_native) {
        callChangeHiddenApiExemptionsNative(getApplicationInfo().targetSdkVersion, Build.VERSION.SDK_INT);
      }
    }
  };

  private void setHiddenApiExemptions() {
    String[] exemptionApiArray = {"L"};
    Class cls = null;
    try {
      cls = Class.forName("dalvik.system.VMRuntime");
      Method metaMethod =
          Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

      Method methodGetRuntime = (Method) metaMethod.invoke(cls, "getRuntime", null);
      methodGetRuntime.setAccessible(true);
      Object objRuntime = methodGetRuntime.invoke(null);

      Method method =
          (Method) metaMethod.invoke(cls, "setHiddenApiExemptions", new Class[]{String[].class});
      method.setAccessible(true);
      method.invoke(objRuntime, new Object[]{exemptionApiArray});
      logI("setHiddenApiExemptions success!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void callMetaReflect() {
    try {
      Method metaMethod =
          Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
      Method hiddenApiMethod = (Method) metaMethod.invoke(WifiManager.class, "getChannel", null);
      hiddenApiMethod.setAccessible(true);
      logI("callMetaReflect success!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void callReflectDirect() {
    try {
      Method method = WifiManager.class.getDeclaredMethod("getChannel");
      method.setAccessible(true);
      logI("callReflectDirect success!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * A native method that is implemented by the 'native-lib' native library,
   * which is packaged with this application.
   */
  public native String stringFromJNI();

  public native void callChangeHiddenApiPolicyNative(int targetSdkVersion);

  public native void callChangeHiddenApiExemptionsNative(int targetSdkVersion, int osVersion);

  private void logI(String message) {
    Log.i(TAG, message);
  }

  private void logE(String message) {
    Log.e(TAG, message);
  }
}
