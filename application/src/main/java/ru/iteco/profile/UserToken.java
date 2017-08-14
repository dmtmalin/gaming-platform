package ru.iteco.profile;

public class UserToken {
    private Integer id;

    private Long expirationTimestamp;

    private String salt;

    public UserToken(Integer id, Long expirationTimestamp, String salt) {
        this.id = id;
        this.expirationTimestamp = expirationTimestamp;
        this.salt = salt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
