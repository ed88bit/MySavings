package my.mys.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "movements", schema="mys")
public class Movements {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private int id;

    private LocalDate accounting_date;
    private Integer competence_month;
    private Integer competence_year;
    private Double negative_amounts;
    private Double positive_amounts;
    private String detail;

}
