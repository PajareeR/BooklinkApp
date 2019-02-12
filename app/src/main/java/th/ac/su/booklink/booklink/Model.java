package th.ac.su.booklink.booklink;

public class Model {
    private int imageCelebBook;
    private String nameBook;
    private String authorBookCeleb;

    public Model(int imageCelebBook, String nameBook, String authorBookCeleb) {
        this.imageCelebBook = imageCelebBook;
        this.nameBook = nameBook;
        this.authorBookCeleb = authorBookCeleb;
    }

    public int getImageCelebBook() {
        return imageCelebBook;
    }

    public void setImageCelebBook(int imageCelebBook) {
        this.imageCelebBook = imageCelebBook;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public String getAuthorBookCeleb() {
        return authorBookCeleb;
    }

    public void setAuthorBookCeleb(String authorBookCeleb) {
        this.authorBookCeleb = authorBookCeleb;
    }
}
