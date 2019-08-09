package entity;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Calendar;

@Entity
public class Account {

    @Id @Index
    private String email;
    private String full_name;
    private String phone;
    private ActiveCode code_active;
    @Index
    private String token;
    @Index
    private int status;
    private long updated_at;
    private long created_at;


    public Account() {
        this.status = 1;
        this.created_at = Calendar.getInstance().getTimeInMillis();
        this.updated_at = Calendar.getInstance().getTimeInMillis();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public ActiveCode getCode_active() {
        return code_active;
    }

    public void setCode_active(ActiveCode code_active) {
        this.code_active = code_active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
