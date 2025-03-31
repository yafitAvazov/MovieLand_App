package dm;

import dao.AdminDaoImpl;

import java.io.Serializable;

public class Admin  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String username;
    private String password;

    private static final String SUPER_ADMIN_USERNAME = "admin";
    private static final String SUPER_ADMIN_PASSWORD = "admin123";

    public Admin(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    public Admin() {
        this.name = "Super Admin";
        this.username = "admin";
        this.password = "admin123";
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}



}