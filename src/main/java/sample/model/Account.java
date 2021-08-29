package sample.model;

public class Account {
    private String login;
    private String password;
    private String account_firstname;
    private String account_lastname;
    private String account_banknumber;

    public Account() {
    }

    public Account(String login, String password, String account_firstname, String account_lastname, String account_banknumber) {
        this.login = login;
        this.password = password;
        this.account_firstname = account_firstname;
        this.account_lastname = account_lastname;
        this.account_banknumber = account_banknumber;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount_firstname() {
        return account_firstname;
    }

    public void setAccount_firstname(String account_firstname) {
        this.account_firstname = account_firstname;
    }

    public String getAccount_lastname() {
        return account_lastname;
    }

    public void setAccount_lastname(String account_lastname) {
        this.account_lastname = account_lastname;
    }

    public String getAccount_banknumber() {
        return account_banknumber;
    }

    public void setAccount_banknumber(String account_banknumber) {
        this.account_banknumber = account_banknumber;
    }
}
