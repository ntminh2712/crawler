package entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Calendar;

@Entity
public class Category {
    @Id
    private long id;
    private String description;
    private String name;
    private long created_at;
    private long update_at;
    @Index
    private int status; // 1. active | 0. deactive. -1. deleted

    public Category() {
        this.id = Calendar.getInstance().getTimeInMillis();
        this.created_at = Calendar.getInstance().getTimeInMillis();;
        this.update_at = Calendar.getInstance().getTimeInMillis();;
        this.status = 1;
    }

    public boolean isDeactive() {
        return this.status == 0 || this.status == -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(long update_at) {
        this.update_at = update_at;
    }
}
