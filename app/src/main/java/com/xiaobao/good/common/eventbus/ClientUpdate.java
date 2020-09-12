package com.xiaobao.good.common.eventbus;

import java.io.Serializable;

public class ClientUpdate implements Serializable {
    private boolean update;
    private String clientInfo;

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public ClientUpdate(boolean update, String clientInfo) {
        this.update = update;
        this.clientInfo = clientInfo;
    }
}
