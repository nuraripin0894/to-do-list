package enigma.to_do_list.utils.specification;

import enigma.to_do_list.model.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    public static Specification<Task> getSpecification(String status, Integer id){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(id != null){
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), id));
            }

            if(status != null && !status.isBlank()){
                predicates.add(criteriaBuilder.like(root.get("status"), "%" + status + "%"));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
