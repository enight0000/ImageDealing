package kexincom.cbpm.entity;

/**
 * Created by Leon on 2015/8/4 0004.
 */
public class Function {

    public final static int COUNTMONEY = 0;
    public final static int IDENTYWORDS = 1;
    public final static int IDENTYIMAGE = 2;

    private int id;
    private String name;
    private String description;
    private String path;

    public Function(int id, String name, String description, String path){
        this.id = id;
        this.name = name;
        this.description = description;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
