package entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Credential {
    @Id
    private String token;
    @Index
    private long timeRevoke;


    public Credential() {
    }

    public Credential(String token, long timeRevoke) {
        this.token = token;
        this.timeRevoke = timeRevoke;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimeRevoke() {
        return timeRevoke;
    }

    public void setTimeRevoke(long timeRevoke) {
        this.timeRevoke = timeRevoke;
    }
}
