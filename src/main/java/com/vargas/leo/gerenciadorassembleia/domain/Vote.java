package com.vargas.leo.gerenciadorassembleia.domain;

import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingOption;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Getter
@Setter
public class Vote {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id_vote")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_voting_session", referencedColumnName = "id_voting_session")
    private VotingSession votingSession;

    @Column(name = "created_at")
    private final LocalDateTime votedAt = LocalDateTime.now();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "voting_option")
    private VotingOption vote;

}
/**
 * @ManyToOne()
 * @JoinColumn(name="role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
 * private UserRole userRole;
 * and same do for userRole
 *
 * @OneToMany(targetEntity=User.class, mappedBy="userRole",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
 * private List<User> user = new ArrayList<>();
 *
 */