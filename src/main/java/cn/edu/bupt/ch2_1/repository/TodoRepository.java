package cn.edu.bupt.ch2_1.repository;

import cn.edu.bupt.ch2_1.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
