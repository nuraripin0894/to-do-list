package enigma.to_do_list.utils.specification;

import enigma.to_do_list.model.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    public static Specification<Task> getSpecification(String dayOfTask, String username){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(username != null && !username.isBlank()){
                predicates.add(criteriaBuilder.equal(root.get("user").get("email"), username));
            }

            if(dayOfTask != null && !dayOfTask.isBlank()){
                predicates.add(criteriaBuilder.like(root.get("dayOfTask"), "%"+dayOfTask+"%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
