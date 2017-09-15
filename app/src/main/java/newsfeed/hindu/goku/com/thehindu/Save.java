package newsfeed.hindu.goku.com.thehindu;

/**
 * Created by goku on 01-02-2015.
 */
public class Save {


    private long id;
    private String title;
    private String contnet;
    private String url;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContnet() {
        return contnet;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContnet(String contnet) {
        this.contnet = contnet;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
