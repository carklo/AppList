package santiago.sis.com.grabilityapplist;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


import org.w3c.dom.Text;

import java.util.ArrayList;
/**
 * Created by santi on 16/03/2016.
 */
public class AplicacionAdapter extends ArrayAdapter<Object>
{
    private Context context;
    private ArrayList<Aplicacion> aplicaciones;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private int res;
    private boolean tablet;

    public AplicacionAdapter(Context context, ArrayList<Aplicacion> aplicaciones, boolean tablet, int res)
    {
        super(context,res);
        this.context = context;
        this.aplicaciones = aplicaciones;
        this.res = res;
        this.tablet = tablet;
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new BitmapCache(10));
    }

    @Override
    public int getCount() {
        return aplicaciones.size();
    }

    private static class PlaceHolder
    {

        TextView title;
        TextView developer;
        TextView dateV;

        ImageView picture;

        public static PlaceHolder generate(View convertView, boolean tablet)
        {
            PlaceHolder placeHolder = new PlaceHolder();
            if(!tablet)
            {
                placeHolder.dateV = (TextView) convertView.findViewById(R.id.app_textView_categoria);
                placeHolder.title = (TextView) convertView.findViewById(R.id.app_textview_title);
                placeHolder.developer = (TextView) convertView.findViewById(R.id.app_textview_developer);
                placeHolder.picture = (ImageView) convertView.findViewById(R.id.app_ImageView);
            }
            else
            {
                placeHolder.title = (TextView) convertView.findViewById(R.id.textView18);
                placeHolder.developer = (TextView) convertView.findViewById(R.id.textView19);
                placeHolder.picture = (ImageView) convertView.findViewById(R.id.imageView3);
            }
            return placeHolder;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        PlaceHolder placeHolder;
        if (convertView == null)
        {
            convertView = View.inflate(context, res, null);
            placeHolder = PlaceHolder.generate(convertView, tablet);
            convertView.setTag(placeHolder);
        }
        else
        {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        placeHolder.title.setText(aplicaciones.get(position).getName());
        placeHolder.developer.setText(aplicaciones.get(position).getArtist());
        if(!tablet)
        {
            placeHolder.dateV.setText(aplicaciones.get(position).getReleaseDate());

            SharedPreferences prefs = context.getSharedPreferences("santiago.sis.com.grabilityapplist", Context.MODE_PRIVATE);
            if(prefs.getString("Dimens","")!= "")
            {
                String[] parts = prefs.getString("Dimens","").split("-");
                int h = Integer.valueOf(parts[0]);
                int w = Integer.valueOf(parts[1]);
                String urlImage = "";
                if(prefs.getString("Tablet", "")!= "")
                {
                    if(prefs.getString("Tablet", "").equals("false"))
                    {
                        if(w<320 && h< 480)
                        {
                            urlImage = aplicaciones.get(position).getPictures().get(0);
                        }
                        else if(w<640 && w> 320 && h> 480 && h< 960)
                        {
                            urlImage = aplicaciones.get(position).getPictures().get(1);
                        }
                        else
                        {
                            urlImage = aplicaciones.get(position).getPictures().get(2);
                        }
                    }
                    else
                    {

                    }
                }
                imageLoader.get(urlImage, ImageLoader.getImageListener(placeHolder.picture,R.mipmap.ic_launcher,R.mipmap.ic_launcher));
            }
        }
        else
        {
            String urlImage = aplicaciones.get(position).getPictures().get(2);
            imageLoader.get(urlImage, ImageLoader.getImageListener(placeHolder.picture,R.mipmap.ic_launcher,R.mipmap.ic_launcher));
        }
        return (convertView);
    }

}
