package ua.bentleytek.messenger.entity;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import ua.bentleytek.messenger.util.UserIdJsonSerializer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue
    private int id;
    @Column
    private String text;
    @Column
    private Date date;
    @ManyToOne
    @JoinColumn(name = "user_from")
    @JsonSerialize(using = UserIdJsonSerializer.class)
    private User from;
    @ManyToOne
    @JoinColumn(name = "user_to")
    @JsonSerialize(using = UserIdJsonSerializer.class)
    private User to;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }
}
