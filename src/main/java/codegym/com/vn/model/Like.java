package codegym.com.vn.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "like_post")
@Data
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private int status;

    @ManyToOne
    private Post Post;

    @OneToOne
    private User user;
}
