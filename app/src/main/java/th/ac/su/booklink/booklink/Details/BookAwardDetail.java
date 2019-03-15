package th.ac.su.booklink.booklink.Details;

import org.json.JSONObject;

public class BookAwardDetail {

    private String id;
    private String title;
    private String authoor;
    private String imgPath;
    private JSONObject award;

    public BookAwardDetail(String id, String title, String authoor, String imgPath, JSONObject award) {
        this.id = id;
        this.title = title;
        this.authoor = authoor;
        this.imgPath = imgPath;
        this.award = award;
    }

    public BookAwardDetail(String id, String title, String authoor, String imgPath) {
        this.id = id;
        this.title = title;
        this.authoor = authoor;
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

    public String getAuthoor() {
        return authoor;
    }

    public void setAuthoor(String authoor) {
        this.authoor = authoor;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public JSONObject getAward() {
        return award;
    }

    public void setAward(JSONObject award) {
        this.award = award;
    }
}
