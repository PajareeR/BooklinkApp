package th.ac.su.booklink.booklink.Details;

public class ProDetail {


    public static String[] arrPro;
    private String proId;
    private String nameBookPro;
    private String authorBookPro;
    private String lobPro;
    private String imageBookPro;
    private String datePro;

    public ProDetail(String proId, String nameBookPro, String authorBookPro, String lobPro, String imageBookPro, String datePro) {
        this.proId = proId;
        this.nameBookPro = nameBookPro;
        this.authorBookPro = authorBookPro;
        this.lobPro = lobPro;
        this.imageBookPro = imageBookPro;
        this.datePro = datePro;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getNameBookPro() {
        return nameBookPro;
    }

    public void setNameBookPro(String nameBookPro) {
        this.nameBookPro = nameBookPro;
    }

    public String getAuthorBookPro() {
        return authorBookPro;
    }

    public void setAuthorBookPro(String authorBookPro) {
        this.authorBookPro = authorBookPro;
    }

    public String getLobPro() {
        return lobPro;
    }

    public void setLobPro(String lobPro) {
        this.lobPro = lobPro;
    }

    public String getImageBookPro() {
        return imageBookPro;
    }

    public void setImageBookPro(String imageBookPro) {
        this.imageBookPro = imageBookPro;
    }

    public String getDatePro() {
        return datePro;
    }

    public void setDatePro(String datePro) {
        this.datePro = datePro;
    }


}
