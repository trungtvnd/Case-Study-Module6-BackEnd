package codegym.com.vn.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateCreate;
    private String title;
    private String content;
    private String description;
    private String avatar;
    private boolean status;

    @ManyToMany
    private List<HashTag> hashTags;

    @ManyToOne
    private Account account;


}