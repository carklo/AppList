package santiago.sis.com.grabilityapplist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by santi on 15/03/2016.
 */
public class CategoriaAdapter extends ArrayAdapter<Object>
{
    private Context context;
    private ArrayList<Categoria> categorias;
    private boolean tablet;
    private int res;
    public CategoriaAdapter(Context context, ArrayList<Categoria> categorias, boolean tablet, int res)
    {
        super(context, res);
        this.context = context;
        this.categorias = categorias;
        this.tablet = tablet;
        this.res = res;
    }

    @Override
    public int getCount()
    {
        return categorias.size();
    }

    private static class PlaceHolder
    {
        TextView titulo;

        public static PlaceHolder generate(View convertView, boolean ta)
        {
            PlaceHolder placeHolder = new PlaceHolder();
            if(ta ==  false)
            {
                placeHolder.titulo = (TextView)convertView.findViewById(R.id.categoria_textView);
            }
            else
            {
                placeHolder.titulo = (TextView)convertView.findViewById(R.id.textView17);
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
        placeHolder.titulo.setText(categorias.get(position).getNombre());
        return (convertView);
    }
}
