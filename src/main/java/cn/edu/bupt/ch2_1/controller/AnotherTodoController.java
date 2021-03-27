package cn.edu.bupt.ch2_1.controller;

import cn.edu.bupt.ch2_1.common.Result;
import cn.edu.bupt.ch2_1.entity.Todo;
import cn.edu.bupt.ch2_1.repository.TodoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "TodoController v3", description = "利用自定义消息格式的控制器")
@RequestMapping("/api/v3/todos")
public class AnotherTodoController {
    final
    TodoRepository todoRepository;
    @Autowired
    public AnotherTodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping(path = "/", produces = "application/json")
    public Result<?> listAll() {
        List<Todo> todos = todoRepository.findAll();
        if (todos.isEmpty()) {
            return Result.error(204, "todos为空");
        }
        return Result.ok(todos);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Result<?> getById(@PathVariable("id") Long id) {
        Optional<Todo> result = todoRepository.findById(id);
        if (result.isPresent()) {
            return Result.ok(result.get());
        } else {
            return Result.error(404, "该todo不存在");
        }
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public Result<?> add(@RequestBody Todo todo) {
        todo = todoRepository.save(todo);
        return Result.ok(todo);
    }


    @DeleteMapping(path = "/{id}")
    public Result<?> delete(@PathVariable Long id) {
        Optional<Todo> result = todoRepository.findById(id);
        if (result.isPresent()) {
            todoRepository.deleteById(id);
            return Result.ok();
        } else {
            return Result.error(204, "该todo不存在，删除失败");
        }
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public Result<?> update(@PathVariable Long id, @RequestBody Todo todo) {
        Optional<Todo> result = todoRepository.findById(id);
        if (result.isPresent()) {
            Todo currentTodo = result.get();
            currentTodo.setContent(todo.getContent());
            todoRepository.save(currentTodo);
            return Result.ok(currentTodo);
        } else {
            return Result.error(404, "该todo不存在");
        }
    }
}
