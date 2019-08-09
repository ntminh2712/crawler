package entity;

public class ActiveCodeParamester {

    private long code;
    private String email;

    public ActiveCodeParamester() {
    }

    public ActiveCodeParamester(long code, String email) {
        this.code = code;
        this.email = email;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
