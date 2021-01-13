package db;

import android.widget.ImageView;

public class EventMessage {
    Integer account;

    public EventMessage(Integer account) {
        this.account = account;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }
}
