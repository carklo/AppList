package santiago.sis.com.grabilityapplist;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by santi on 15/03/2016.
 */
@SuppressWarnings("serial")
public class Categoria implements Serializable
{
    private String nombre;
    private ArrayList<Aplicacion> aplicaciones;

    public Categoria(String name)
    {
        setNombre(name);
        setAplicaciones(new ArrayList<Aplicacion>());
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Aplicacion> getAplicaciones() {
        return aplicaciones;
    }

    public void setAplicaciones(ArrayList<Aplicacion> aplicaciones) {
        this.aplicaciones = aplicaciones;
    }

    public void agregarAplicacion(Aplicacion app)
    {
        aplicaciones.add(app);
    }

    @Override
    public boolean equals(Object o)
    {
        boolean res = false;
        if(o instanceof  Categoria)
        {
            Categoria cat = (Categoria) o;
            if(this.nombre.equals(cat.getNombre()))
            {
                res = true;
            }
            else
            {
                res = false;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
