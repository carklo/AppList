package santiago.sis.com.grabilityapplist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaAplicacionesActivity extends AppCompatActivity
{
    private SharedPreferences prefs = null;
    private ListView lista;
    private GridView grilla;
    private AplicacionAdapter adapter;
    private ArrayList<Aplicacion> aplicaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);

        prefs = getSharedPreferences("santiago.sis.com.grabilityapplist", MODE_PRIVATE);
        if(prefs.getString("Tablet", "")!= "") {
            if (prefs.getString("Tablet", "").equals("false"))
            {
                setContentView(R.layout.activity_lista_aplicaciones_phone);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                inicializarListView();
            }
            else
            {
                setContentView(R.layout.activity_lista_aplicaciones_tablet);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                inicializarGridView();
            }
        }
    }

    private void inicializarListView()
    {
        lista = (ListView) findViewById(R.id.app_listView);
        Bundle bundle = this.getIntent().getExtras();
        Categoria cate = (Categoria) bundle.get("parametro");
        aplicaciones = cate.getAplicaciones();
        adapter = new AplicacionAdapter(this, aplicaciones, false, R.layout.item_aplicacion_phone);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(ListaAplicacionesActivity.this, AplicacionActivity.class);
                intent.putExtra("parametro", aplicaciones.get(arg2));
                startActivity(intent);
            }
        });
    }

    private void inicializarGridView()
    {
        grilla = (GridView) findViewById(R.id.gridView_aplicaciones);
        Bundle bundle = this.getIntent().getExtras();
        Categoria cate = (Categoria) bundle.get("parametro");
        aplicaciones = cate.getAplicaciones();
        adapter = new AplicacionAdapter(this, aplicaciones, true, R.layout.item_aplicacion_tablet);
        grilla.setAdapter(adapter);
        grilla.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(ListaAplicacionesActivity.this, AplicacionActivity.class);
                intent.putExtra("parametro", aplicaciones.get(arg2));
                startActivity(intent);
            }
        });
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
        overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
    }

    @Override
    public void onResume()
    {
        super.onResume();
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
                    new AlertDialog.Builder(ListaAplicacionesActivity.this)
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
                    new AlertDialog.Builder(ListaAplicacionesActivity.this)
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
