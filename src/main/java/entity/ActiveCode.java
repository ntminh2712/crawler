package entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ActiveCode {
    @Id
    private long code;
    @Index
    private long timeRevoke;



    public ActiveCode() {

    }


    public ActiveCode(int code, long timeRevoke) {
        this.code = code;
        this.timeRevoke = timeRevoke;
    }

    public long getTimeRevoke() {
        return timeRevoke;
    }

    public void setTimeRevoke(long timeRevoke) {
        this.timeRevoke = timeRevoke;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

}
