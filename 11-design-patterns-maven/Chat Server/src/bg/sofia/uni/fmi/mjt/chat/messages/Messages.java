package bg.sofia.uni.fmi.mjt.chat.messages;

public enum Messages {

    CONNECTED_OK("=> successfully connected"),
    DISCONNECT_OK("=> successfully disconnected"), DISCONNECT_FAIL("=> could not disconnect"),
    MISSING_USERNAME("=> missing username"), USERNAME_EXISTS("=> the username is taken"),
    NO_COMMAND("=> no command entered"), UNKNOWN_COMMAND("=> unknown command"),
    USER_NOT_FOUND("=> user not found"), NO_ONLINE_USERS("=> no clients apart from you are connected to the server"),
    SERVER_CLOSED("=> the server shutdown unexpectedly"),
    STOP_ALL_CLIENTS("themostrandomstringtoKILLTHEMALL12345678900987654321"),
    RESPONSE_TEXT("");

    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }

}