package br.org.aacc.doacao.Domain.ObjectValue;
import java.io.Serializable;
/**
 * Created by Adilson on 26/03/2017.
 */

public abstract class MasterDomain implements Serializable {

    public static String TAG="MasterDomain";

    private  int Id;
    private  String Name;
    private  String Description;
    private  String urlImage;


    public MasterDomain(){}

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


}
