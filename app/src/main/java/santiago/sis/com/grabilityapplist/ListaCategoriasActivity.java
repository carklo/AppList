package santiago.sis.com.grabilityapplist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaCategoriasActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;
    private ArrayList<String> categorias;
    private ArrayList<Categoria> categoriasObj;
    private ArrayList<Aplicacion> aplicaciones;
    private CategoriaAdapter adapter;
    private ListView lista;
    private GridView grilla;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        aplicaciones = new ArrayList<>();
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        prefs = getSharedPreferences("santiago.sis.com.grabilityapplist",MODE_PRIVATE);
        //Toast.makeText(this,"oncreate", Toast.LENGTH_LONG).show();
        if(prefs.getString("Tablet", "")!= "")
        {
            if (prefs.getString("Tablet", "").equals("false"))
            {
                setContentView(R.layout.activity_lista_categorias_phone);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                if(prefs.getString("AppList","")!= "")
                {
                    deserializarJSONYCrearAplicaciones(prefs.getString("AppList", ""));
                    cargarCategorias();
                    inicializarListView();
                }
            }
            else
            {
                setContentView(R.layout.activity_lista_categorias_tablet);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                if(prefs.getString("AppList","")!= "")
                {
                    deserializarJSONYCrearAplicaciones(prefs.getString("AppList", ""));
                    cargarCategorias();
                    inicializarGridView();
                }
            }
        }
    }

    public void deserializarJSONYCrearAplicaciones(String json)
    {
        categorias = new ArrayList<>();
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONObject("feed").getJSONArray("entry");

            for (int i = 0; i< jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String nombre = jsonObject1.getJSONObject("name").getString("label");
                ArrayList<String> urls = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject1.getJSONArray("image");
                int max = jsonArray1.length();
                for(int j = 0; j < max;j++)
                {
                    //System.out.println(nombre + " "+ "valor j: "+ j+ " valor max: "+ max);
                    String url = jsonArray1.getJSONObject(j).getString("label");
                    urls.add(url);
                }
                String resumen = jsonObject1.getJSONObject("summary").getString("label");
                double precio = jsonObject1.getJSONObject("price").getJSONObject("attributes").getDouble("amount");
                String moneda = jsonObject1.getJSONObject("price").getJSONObject("attributes").getString("currency");
                String contenido = jsonObject1.getJSONObject("contentType").getJSONObject("attributes").getString("label");
                String derechos = jsonObject1.getJSONObject("rights").getString("label");
                String titulo = jsonObject1.getJSONObject("title").getString("label");
                String enlance = jsonObject1.getJSONObject("link").getJSONObject("attributes").getString("href");
                String iD = jsonObject1.getJSONObject("id").getJSONObject("attributes").getString("id");
                String desarrollador = jsonObject1.getJSONObject("artist").getString("label");
                String categoria = jsonObject1.getJSONObject("category").getJSONObject("attributes").getString("label");
                if(!categorias.contains(categoria))
                {
                    categorias.add(categoria);
                }
                String fechaLanza = jsonObject1.getJSONObject("releaseDate").getJSONObject("attributes").getString("label");
                Aplicacion app = new Aplicacion(nombre, resumen, precio, moneda, contenido, derechos, titulo, enlance, iD, desarrollador, categoria, fechaLanza, urls);
                //System.out.println(app.toString());
                aplicaciones.add(app);
            }
        }
        catch(org.json.JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void cargarCategorias()
    {
        categoriasObj = new ArrayList<>();
        for(int i = 0; i< categorias.size();i++)
        {
            String cat = categorias.get(i);
            Categoria catego = new Categoria(cat);
            for(int j = 0; j < aplicaciones.size();j++)
            {
                String cate = aplicaciones.get(j).getCategory();
                if(cat.equals(cate))
                {
                    catego.agregarAplicacion(aplicaciones.get(j));
                }
            }
            categoriasObj.add(catego);
        }
    }

    private void inicializarListView() {
        lista = (ListView) findViewById(R.id.categoria_listView);
        adapter = new CategoriaAdapter(this, categoriasObj, false, R.layout.item_categoria_phone);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                Intent intent = new Intent(ListaCategoriasActivity.this, ListaAplicacionesActivity.class);
                intent.putExtra("parametro", categoriasObj.get(arg2));
                startActivity(intent);
            }
        });
    }

    private void inicializarGridView()
    {
        grilla = (GridView) findViewById(R.id.gridView_Categorias);
        adapter = new CategoriaAdapter(this, categoriasObj,true, R.layout.item_categoria_tablet);
        grilla.setAdapter(adapter);
        grilla.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ListaCategoriasActivity.this, ListaAplicacionesActivity.class);
                intent.putExtra("parametro", categoriasObj.get(position));
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
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //Toast.makeText(this,"onresume", Toast.LENGTH_LONG).show();
        prefs = getSharedPreferences("santiago.sis.com.grabilityapplist",MODE_PRIVATE);
        if(prefs.contains("conectado888")== false)
        {
            if(isNetworkAvailable() == false || isOnline() == false)
            {
                //Toast.makeText(this,"onresume encontro false en conexion", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("conectado888", "no");
                prefsEditor.commit();

                if (!isFinishing())
                {
                    new AlertDialog.Builder(ListaCategoriasActivity.this)
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
        //Toast.makeText(this,"onrestart "+ prefs.contains("conectado888"), Toast.LENGTH_LONG).show();
        if(prefs.contains("conectado888") == false)
        {
            if(isNetworkAvailable() == false||isOnline() == false)
            {
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("conectado888", "no");
                prefsEditor.commit();

                if (!isFinishing())
                {
                    new AlertDialog.Builder(ListaCategoriasActivity.this)
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
