package codegym.com.vn.dto.request;

public class ChangeAvatar {
    private String avatar;

    public ChangeAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ChangeAvatar() {
    }
}
