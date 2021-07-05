package my.mys.domain;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "movements", schema="mys")
public class Movements {

    @Id
    private int id;

    private LocalDate accounting_date;
    private Integer competence_month;
    private double negative_amounts;
    private double positive_amounts;
    private String detail;

}
