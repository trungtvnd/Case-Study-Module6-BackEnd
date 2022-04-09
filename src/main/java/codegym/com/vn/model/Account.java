package codegym.com.vn.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private String phone;
    private String avatar;
    private Boolean status;

    @ManyToOne
    private Role role;

}
