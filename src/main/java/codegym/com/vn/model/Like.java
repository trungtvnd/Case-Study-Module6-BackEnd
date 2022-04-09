package codegym.com.vn.model;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.*;

@Entity
@Data
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private int status;

    @ManyToOne
    private Post Post;

    @OneToOne
    private Account account;
}
