package ru.usedesk.sdk.data.framework.api.entity.response;

import ru.usedesk.sdk.domain.entity.State;

public class SetEmailResponse extends BaseResponse {

    public static final String TYPE = "@@chat/current/SET";

    private State state;
    private boolean reset;

    public SetEmailResponse() {
        super(TYPE);
    }

    public State getState() {
        return state;
    }

    public boolean isReset() {
        return reset;
    }
}