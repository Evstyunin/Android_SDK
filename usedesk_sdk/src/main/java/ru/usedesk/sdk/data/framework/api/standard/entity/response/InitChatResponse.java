package ru.usedesk.sdk.data.framework.api.standard.entity.response;

import ru.usedesk.sdk.domain.entity.chat.Setup;

public class InitChatResponse extends BaseResponse {

    public static final String TYPE = "@@chat/current/INITED";

    private String token;
    private Setup setup;

    public InitChatResponse() {
        super(TYPE);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Setup getSetup() {
        return setup;
    }

    public void setSetup(Setup setup) {
        this.setup = setup;
    }
}