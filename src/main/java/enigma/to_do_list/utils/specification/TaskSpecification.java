package enigma.to_do_list.utils.specification;

import enigma.to_do_list.model.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    public static Specification<Task> getSpecification(String dayOfTask, Integer id){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(dayOfTask != null && !dayOfTask.isBlank()){
                predicates.add(criteriaBuilder.like(root.get("dayOfTask"), "%"+dayOfTask+"%"));
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), id));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
