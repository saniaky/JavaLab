package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Ad implements Serializable, Identifiable {

    private static final long serialVersionUID = -1777984074044025486L;
    private int id = 0;                                 // Идентификатор сообщения
    private String subject = "";                        // Заголовок сообщения
    private String body = "";                           // Текст сообщения
    private int authorId;                               // Автор сообщения (id)
    transient private User author;                      // Автор сообщения (ссылка, не сериализуется)
    private ArrayList<Bid> bids;                        // Ставка
    private Long endTime;
    private Long lastModified;                          // Последнее время модификации сообщения
    transient private Date lastModifiedDate;            // Последнее время модификации сообщения как объект Date

    public Ad() {
        lastModified = Calendar.getInstance().getTimeInMillis();
        endTime = lastModified + 1 * 60 * 1000; // 1 hour in milliseconds
        bids = new ArrayList<Bid>();
        bids.add(new Bid(0.97, "Начальная ставка", null));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setBid(Double bid, String comment, User user) {
        bids.add(new Bid(bid, comment, user));
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public int getBidsCount() {
        return bids.size() - 1;
    }

    public Bid getLastBid() {
        return bids.get(bids.size() - 1);
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        // При установке последнего времени изменения в секундах
        // одновременно изменяется и время последнего изменения как дата
        this.lastModified = lastModified;
        lastModifiedDate = new Date(lastModified);
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object obj) {
        // Если obj - ссылка на другой объект, равна this, то это один и тот же объект
        if (this == obj)
            return true;
        // Если ссылка на другой объект - null, то объекты не равны
        if (obj == null)
            return false;
        // Если классы объектных ссылок не совпадают, объекты не равны
        if (getClass() != obj.getClass())
            return false;
        Ad other = (Ad) obj;
        // Результат сравнения решается равенством идентификаторов
        if (id != other.id)
            return false;
        return true;
    }
}
