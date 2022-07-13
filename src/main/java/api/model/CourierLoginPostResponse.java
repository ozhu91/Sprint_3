package api.model;

public class CourierLoginPostResponse {
    private String login;
    private String password;

    public CourierLoginPostResponse(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierLoginPostResponse() {

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() {
        this.password = password;
    }
}
