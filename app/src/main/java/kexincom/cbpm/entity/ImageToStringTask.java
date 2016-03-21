package kexincom.cbpm.entity;

/**
 * Created by Leon on 2015/8/3 0003.
 */
public class ImageToStringTask {


    private static String URL;
    private static String KEY;

    private int id;
    private int type;
    private Image upImage;
    private String upArg;
    private Image downImage;
    private String downArg;
    private String downURL;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Image getUpImage() {
        return upImage;
    }

    public void setUpImage(Image upImage) {
        this.upImage = upImage;
    }

    public String getUpArg() {
        return upArg;
    }

    public void setUpArg(String upArg) {
        this.upArg = upArg;
    }

    public Image getDownImage() {
        return downImage;
    }

    public void setDownImage(Image downImage) {
        this.downImage = downImage;
    }

    public String getDownArg() {
        return downArg;
    }

    public void setDownArg(String downArg) {
        this.downArg = downArg;
    }

    public static String getKEY() {
        return KEY;
    }

    public static void setKEY(String KEY) {
        ImageToStringTask.KEY = KEY;
    }

    public String getDownURL() {
        return downURL;
    }

    public void setDownURL(String downURL) {
        this.downURL = downURL;
    }
}
