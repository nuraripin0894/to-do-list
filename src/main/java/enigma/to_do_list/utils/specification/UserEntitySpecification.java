package enigma.to_do_list.utils.specification;

import enigma.to_do_list.model.UserEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserEntitySpecification {
    public static Specification<UserEntity> getSpecification(String username){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(username != null && !username.isBlank()){
                predicates.add(criteriaBuilder.like(root.get("username"), "%"+username+"%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
