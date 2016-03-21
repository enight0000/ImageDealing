package kexincom.cbpm.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 2015/8/3 0003.
 */
public class Album {
    private int id;
    private String name;
    private String dir;
    private Image show;
    private List<Image> list = new ArrayList<Image>();
    private boolean deafault = false;

    public boolean isDeafault() {
        return deafault;
    }

    public void setDeafault(boolean deafault) {
        this.deafault = deafault;
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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Image getShow() {
        return show;
    }

    public void setShow(Image show) {
        this.show = show;
    }

    public List<Image> getList() {
        return list;
    }

    public void setList(List<Image> list) {
        this.list = list;
    }
}
