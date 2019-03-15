package th.ac.su.booklink.booklink.Details;

public class QuoteDetail {
    private String bookQuoteId;
    private String messageQuote;
    private String subjectMess;
    private String imageBook;
    private String nameBookQuote;
    private String authorBookQuote;

    public QuoteDetail(String bookQuoteId, String messageQuote, String subjectMess, String imageBook, String nameBookQuote, String authorBookQuote) {
        this.bookQuoteId = bookQuoteId;
        this.messageQuote = messageQuote;
        this.subjectMess = subjectMess;
        this.imageBook = imageBook;
        this.nameBookQuote = nameBookQuote;
        this.authorBookQuote = authorBookQuote;
    }

    public String getBookQuoteId() {
        return bookQuoteId;
    }

    public void setBookQuoteId(String bookQuoteId) {
        this.bookQuoteId = bookQuoteId;
    }

    public String getMessageQuote() {
        return messageQuote;
    }

    public void setMessageQuote(String messageQuote) {
        this.messageQuote = messageQuote;
    }

    public String getSubjectMess() {
        return subjectMess;
    }

    public void setSubjectMess(String subjectMess) {
        this.subjectMess = subjectMess;
    }

    public String getImageBook() {
        return imageBook;
    }

    public void setImageBook(String imageBook) {
        this.imageBook = imageBook;
    }

    public String getNameBookQuote() {
        return nameBookQuote;
    }

    public void setNameBookQuote(String nameBookQuote) {
        this.nameBookQuote = nameBookQuote;
    }

    public String getAuthorBookQuote() {
        return authorBookQuote;
    }

    public void setAuthorBookQuote(String authorBookQuote) {
        this.authorBookQuote = authorBookQuote;
    }
}
