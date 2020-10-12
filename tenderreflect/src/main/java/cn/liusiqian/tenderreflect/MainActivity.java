package cn.liusiqian.tenderreflect;

import java.lang.reflect.Method;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

  private static final String TAG = "ReflectTag";

  private TextView tvHello;
  private TextView tvCallDirect, tvChangeHiddenApiPolicy;

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

    tvHello.setText(stringFromJNI());
    tvCallDirect.setOnClickListener(ocl);
    tvChangeHiddenApiPolicy.setOnClickListener(ocl);
  }

  private View.OnClickListener ocl = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if (view.getId() == R.id.txt_call_ref_direct) {
        callReflectDirect();
      } else if (view.getId() == R.id.txt_change_hiddenapipolicy) {
        callChangeHiddenApiPolicyNative(getApplicationInfo().targetSdkVersion);
      }
    }
  };

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

  private void logI(String message) {
    Log.i(TAG, message);
  }

  private void logE(String message) {
    Log.e(TAG, message);
  }
}
