package ua.bentleytek.messenger.entity;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import ua.bentleytek.messenger.util.UserIdJsonDeserializer;
import ua.bentleytek.messenger.util.UserIdJsonSerializer;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue
    private int id;
    @Column
    private String text;
    @Column
    private Timestamp date;
    @ManyToOne
    @JoinColumn(name = "user_from")
    @JsonSerialize(using = UserIdJsonSerializer.class)
    private User from;
    @ManyToOne
    @JoinColumn(name = "user_to")
    @JsonSerialize(using = UserIdJsonSerializer.class)
    @JsonDeserialize(using = UserIdJsonDeserializer.class)
    private User to;
    @Column(name = "is_read")
    private boolean read;

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
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

    public boolean isRead() {
        return read;
    }

    public void setRead() {
        this.read = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        return date.equals(message.date) && text.equals(message.text);

    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
