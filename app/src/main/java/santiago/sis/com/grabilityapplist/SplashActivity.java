package santiago.sis.com.grabilityapplist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;


import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Created by santi on 10/03/2016.
 */
public class SplashActivity extends AppCompatActivity
{
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("santiago.sis.com.grabilityapplist",MODE_PRIVATE);

        if(esTablet())
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        final Intent intent = new Intent(this, ListaCategoriasActivity.class);
        new Thread()
        {
            public void run()
            {
                if (isNetworkAvailable())
                {
                    if (isOnline())
                    {
                        if (prefs.getString("AppList", "") != "")
                        {
                            prefs.edit().remove("AppList").commit();
                        }
                        BufferedReader reader = null;
                        try
                        {
                            URL url = new URL("https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json");
                            reader = new BufferedReader(new InputStreamReader(url.openStream()));
                            StringBuffer buffer = new StringBuffer();
                            int read;
                            char[] chars = new char[1024];
                            while ((read = reader.read(chars)) != -1) {
                                buffer.append(chars, 0, read);
                            }

                            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
                            JsonParser parser = new JsonParser();
                            String jS = buffer.toString().replace("im:","");
                            JsonObject jsonObject = parser.parse(jS).getAsJsonObject();
                            String j = gson.toJson(jsonObject);
                            //System.out.println("JSON "+ j);
                            SharedPreferences.Editor prefsEditor = prefs.edit();
                            prefsEditor.putString("AppList", j);
                            prefsEditor.commit();
                            SplashActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    startActivity(intent);
                                    SplashActivity.this.finish();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    else
                    {
                        if (prefs.getString("AppList", "") != "")
                        {
                            //No hay conexion a internet pero si se encontro una version guardada del json
                            SplashActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    if (!isFinishing())
                                    {
                                        new AlertDialog.Builder(SplashActivity.this)
                                                .setTitle("GrabilityAppList")
                                                .setMessage("No se ha encontrado una conexion a internet, se continuara en modo offline")
                                                .setCancelable(false)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which)
                                                    {
                                                        startActivity(intent);
                                                        SplashActivity.this.finish();
                                                    }
                                                }).create().show();
                                    }
                                }
                            });

                        }
                        else
                        {
                            //No se encontro una version guardada del archivo json y no ha conexion a internet
                            if (!isFinishing())
                            {
                                SplashActivity.this.runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        new AlertDialog.Builder(SplashActivity.this)
                                                .setTitle("GrabilityAppList")
                                                .setMessage("No se ha encontrado conexion a internet y no se tienen datos guardados previamente. Es necesaria una conexion a interent para empezar.")
                                                .setCancelable(false)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which)
                                                    {
                                                        SplashActivity.this.finish();
                                                        System.exit(0);
                                                    }
                                                }).create().show();
                                    }
                                });
                            }
                        }
                    }
                }
                else
                {
                    if (prefs.getString("AppList", "") != "")
                    {
                        //No hay conexion a internet pero si se encontro una version guardada del json
                        SplashActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isFinishing()) {
                                    new AlertDialog.Builder(SplashActivity.this)
                                            .setTitle("GrabilityAppList")
                                            .setMessage("No se ha encontrado una conexion a internet, se continuara en modo offline")
                                            .setCancelable(false)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(intent);
                                                    SplashActivity.this.finish();
                                                }
                                            }).create().show();
                                }
                            }
                        });

                    } else {
                        //No se encontro una version guardada del archivo json y no ha conexion a internet
                        if (!isFinishing()) {
                            SplashActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(SplashActivity.this)
                                            .setTitle("GrabilityAppList")
                                            .setMessage("No se ha encontrado conexion a internet y no se tienen datos guardados previamente. Es necesaria una conexion a interent para empezar.")
                                            .setCancelable(false)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SplashActivity.this.finish();
                                                    System.exit(0);
                                                }
                                            }).create().show();
                                }
                            });
                        }
                    }
                }
            }
        }.start();
    }

    public Boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public Boolean isOnline()
    {
        try
        {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean esTablet()
    {
        boolean res = false;
        int mWidthPixels = 0;
        int mHeightPixels = 0;
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        // since SDK_INT = 1;
        mWidthPixels = displayMetrics.widthPixels;
        mHeightPixels = displayMetrics.heightPixels;

        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
        {
            try
            {
                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (Build.VERSION.SDK_INT >= 17)
        {
            try
            {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                mWidthPixels = realSize.x;
                mHeightPixels = realSize.y;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("Dimens", mHeightPixels+"-"+mWidthPixels);
        prefsEditor.commit();
        double x = Math.pow(mWidthPixels/dm.xdpi,2);
        double y = Math.pow(mHeightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(x+y);
        System.out.println("dimensiones "+ mHeightPixels+ " "+ mWidthPixels);
        System.out.println("screenInches "+screenInches);
        if(screenInches >= 4.5)
        {
            res = true;
        }
        prefsEditor.putString("Tablet", String.valueOf(res));
        prefsEditor.commit();
        return res;
    }
}
