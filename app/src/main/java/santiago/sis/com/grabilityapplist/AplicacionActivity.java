package santiago.sis.com.grabilityapplist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

public class AplicacionActivity extends AppCompatActivity
{
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_aplicacion);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        prefs = getSharedPreferences("santiago.sis.com.grabilityapplist",MODE_PRIVATE);

        if(prefs.getString("Tablet", "")!= "")
        {
            if (prefs.getString("Tablet", "").equals("false"))
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
            else
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        }
        Bundle bundle = getIntent().getExtras();
        Aplicacion aplicacion = (Aplicacion) bundle.get("parametro");
        TextView name = (TextView) findViewById(R.id.textView3);
        name.setText(aplicacion.getName());
        TextView developer = (TextView) findViewById(R.id.textView4);
        developer.setText(aplicacion.getArtist());
        TextView summary = (TextView) findViewById(R.id.textView6);
        summary.setText(aplicacion.getSummary());
        Linkify.addLinks(summary, Linkify.ALL);
        TextView price = (TextView) findViewById(R.id.textView8);
        price.setText(aplicacion.getPrice()+ " "+ aplicacion.getCurrency());
        TextView content = (TextView) findViewById(R.id.textView10);
        content.setText(aplicacion.getContentType());
        TextView rights = (TextView) findViewById(R.id.textView12);
        rights.setText(aplicacion.getRights());
        TextView link = (TextView) findViewById(R.id.textView16);
        link.setText(aplicacion.getLink());
        Linkify.addLinks(link, Linkify.WEB_URLS);
        TextView rDate = (TextView) findViewById(R.id.textView13);
        rDate.setText(aplicacion.getReleaseDate());
        TextView category = (TextView) findViewById(R.id.textView14);
        category.setText(aplicacion.getCategory());
        requestQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(requestQueue, new BitmapCache(10));
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        imageLoader.get(aplicacion.getPictures().get(2), ImageLoader.getImageListener(imageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        prefs = getSharedPreferences("santiago.sis.com.grabilityapplist",MODE_PRIVATE);
        if(prefs.contains("conectado888") == false) {
            if (isNetworkAvailable() == false || isOnline() == false) {
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("conectado888", "no");
                prefsEditor.commit();
                if (!isFinishing()) {
                    new AlertDialog.Builder(AplicacionActivity.this)
                            .setTitle("GrabilityAppList")
                            .setMessage("Conexion a internet no disponible, se continuara en modo offline.")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            }
        }
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        prefs = getSharedPreferences("santiago.sis.com.grabilityapplist",MODE_PRIVATE);
        if(prefs.contains("conectado888") == false)
        {
            if(isNetworkAvailable() == false || isOnline() == false)
            {
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("conectado888", "no");
                prefsEditor.commit();
                if (!isFinishing())
                {
                    new AlertDialog.Builder(AplicacionActivity.this)
                            .setTitle("GrabilityAppList")
                            .setMessage("Conexion a internet no disponible, se continuara en modo offline.")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            }
        }
    }
}
