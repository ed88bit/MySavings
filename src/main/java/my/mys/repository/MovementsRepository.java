package my.mys.repository;

import my.mys.domain.Movements;
import org.junit.runners.Parameterized;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementsRepository extends CrudRepository<Movements, String> {

    @Query(value = " SELECT sum(negative_amounts) FROM movements " +
            " WHERE competence_month = ?1 ",
    nativeQuery = true)
    String getOutflowsAtMonth(@Param("month") Integer month);

    @Query(value = " SELECT sum(positive_amounts) FROM movements " +
            " WHERE competence_month = ?1 ",
            nativeQuery = true)
    String getIncomeAtMonth(Integer month);
}
