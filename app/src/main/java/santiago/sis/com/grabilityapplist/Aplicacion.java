package santiago.sis.com.grabilityapplist;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by santi on 13/03/2016.
 */
public class Aplicacion implements Serializable
{
    private String name;
    private String summary;
    private double price;
    private String currency;
    private String contentType;
    private String rights;
    private String title;
    private String link;
    private String id;
    private String artist;
    private String category;
    private String releaseDate;
    private ArrayList<String> pictures;

    public Aplicacion(String nombre, String resumen, double precio, String moneda, String contenido, String derechos, String titulo,
                      String enlace, String iD, String desarrollador, String categoria, String fecha, ArrayList imagenes)
    {
        setName(nombre);
        setSummary(resumen);
        setPrice(precio);
        setCurrency(moneda);
        setContentType(contenido);
        setRights(derechos);
        setTitle(titulo);
        setLink(enlace);
        setId(iD);
        setArtist(desarrollador);
        setCategory(categoria);
        setReleaseDate(fecha);
        setPictures(imagenes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public String toString()
    {
        return "Name: "+ getName()+ System.getProperty("line.separator") + " Resumen: "+
                getSummary() + System.getProperty("line.separator") + "Precio: "+ getPrice() + System.getProperty("line.separator") +
                "Moneda: "+ getCurrency() + System.getProperty("line.separator") + "Categoria: "+ getCategory() +
                System.getProperty("line.separator")+ "Contenido: " + getContentType() + System.getProperty("line.separator")+
                "Desarrollador: "+ getArtist() + System.getProperty("line.separator") + "ID: " +getId() + System.getProperty("line.separator")
                + "Link: " + getLink() + System.getProperty("line.separator") + "Fecha: "+ getReleaseDate() + System.getProperty("line.separator")
                + "Derechos: "+ getRights();
    }

}
