package codegym.com.vn.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String content;

    @ManyToOne
    private Post Post;

    @ManyToOne
    private Account account;


}
