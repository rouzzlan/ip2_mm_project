package be.kdg.musicmaker.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Band")
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User teacher;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="students_band",
               joinColumns = {@JoinColumn(name = "band_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")})
    private List<User> students;
}
