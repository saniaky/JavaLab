package entity;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: saniaky
 * Date: 27.04.13
 * Time: 10:50
 */
public class Bid implements Serializable {
    private static final long serialVersionUID = -5363773994253628299L;
    double bidValue;
    String comment;
    User user;

    public Bid(double bidValue, String comment, User user) {
        this.comment = comment;
        this.bidValue = bidValue;
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getBidValue() {
        return bidValue;
    }

    public User getAuthor() {
        return user;
    }

    public void setBidValue(double bidValue) {
        this.bidValue = bidValue;
    }

    @Override
    public String toString() {
        return bidValue + (comment.equals("") ? "" : " (" + comment + ")");
    }
}
