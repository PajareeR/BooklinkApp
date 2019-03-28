package th.ac.su.booklink.booklink.Details;

public class RecommendDetail {

    private String id;
    private String title;
    private String imgPath;

    public RecommendDetail(String id, String title, String imgPath) {
        this.id = id;
        this.title = title;
        this.imgPath = imgPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
