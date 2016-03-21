package kexincom.cbpm.entity;

/**
 * Created by Leon on 2015/8/3 0003.
 */
public class Image {
    private int id;
    private String neme;
    private String src;
    private int abbrId;
    private String abbrSrc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNeme() {
        return neme;
    }

    public void setNeme(String neme) {
        this.neme = neme;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getAbbrId() {
        return abbrId;
    }

    public void setAbbrId(int abbrId) {
        this.abbrId = abbrId;
    }

    public String getAbbrSrc() {
        return abbrSrc;
    }

    public void setAbbrSrc(String abbrSrc) {
        this.abbrSrc = abbrSrc;
    }
}
