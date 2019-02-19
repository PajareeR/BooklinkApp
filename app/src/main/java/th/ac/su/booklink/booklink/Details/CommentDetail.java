package th.ac.su.booklink.booklink.Details;


import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class CommentDetail {
    public String commentKey;
    public String commentUser;
    public Date commentTime;
    public String userImg;
    public String comment;
    public String commentImg;
    public boolean statusLike;
    public int countLike;

    public CommentDetail(String commentUser, String comment, int countLike) {
        this.commentUser = commentUser;
        this.comment = comment;
        this.countLike = countLike;
    }

    public CommentDetail(String commentKey, String commentUser, Date commentTime, String userImg,
                         String comment, String commentImg, boolean statusLike, int countLike) {
        this.commentKey = commentKey;
        this.commentUser = commentUser;
        this.commentTime = commentTime;
        this.userImg = userImg;
        this.comment = comment;
        this.commentImg = commentImg;
        this.statusLike = statusLike;
        this.countLike = countLike;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentImg() {
        return commentImg;
    }

    public void setCommentImg(String commentImg) {
        this.commentImg = commentImg;
    }

    public boolean isStatusLike() {
        return statusLike;
    }

    public void setStatusLike(boolean statusLike) {
        this.statusLike = statusLike;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public String getCommentTimeDring() {
        return calTime(commentTime);
    }

    public String calTime(Date commentTime){
        Calendar now = Calendar.getInstance();

        long diff = commentTime.getTime() - now.getTimeInMillis();
        long seconds = Math.abs(diff / 1000);
        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        long days = hours / 24;
        hours = hours % 24;

        if (days > 0){
            return " "+days+" วันที่ผ่านมา";
        }else if (hours > 0){
            return " "+hours+" ชม.ที่ผ่านมา";
        }else if (minutes > 0){
            return " "+minutes+" น.ที่ผ่านมา";
        }else if (seconds > 0){
            return " "+seconds+" วินาทีที่ผ่านมา";
        }else {
            return " เมื่อสักครู่";
        }


    }
}
