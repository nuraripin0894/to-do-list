package enigma.to_do_list.repository;

import enigma.to_do_list.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
    Optional<Task> findByDayOfTask(String dayOfTask);
    Boolean existsByDayOfTask(String dayOfTask);
}
