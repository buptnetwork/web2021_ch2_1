package cn.edu.bupt.ch2_1.controller;

import cn.edu.bupt.ch2_1.entity.Todo;
import cn.edu.bupt.ch2_1.repository.TodoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
//@Tag注解用来定义一组API
@Tag(name = "TodoController v1", description = "利用HTTP状态码表示操作结果状态的控制器")
@RequestMapping("/api/v1/todos")
public class TodoController {
    final
    TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    //@Operation用来对API添加说明
    @Operation(summary = "获取todolist", description = "返回值为全部的todolist，为空时状态码为204")
    //@ApiResponses对返回值添加说明
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "正常返回时状态码设置为200"),
            @ApiResponse(responseCode = "204", description = "为空时状态码设置为204") })
    @GetMapping(path = "/", produces = "application/json")
    ResponseEntity<List<Todo>> listAll(){
        List<Todo> todos = todoRepository.findAll();
        if (todos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @Operation(summary = "根据id获取相应的todo", description = "如果不存在，返回状态码为404")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Todo> getById(
            //@Parameter对参数进行补充说明及限制
            @Parameter(description = "todo的ID", required = true, schema = @Schema(example = "100"))
            @PathVariable("id") Long id) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null) {
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "添加新的todo", description = "创建成功，返回状态码201")
    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Todo> add(
            @Parameter(description = "请输入todo", required = true,
                    schema = @Schema(example = "{\"content\":\"todo的内容\"}"))
            @RequestBody Todo todo) {
        todo = todoRepository.save(todo);
        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }

    @Operation(summary = "根据id删除todo", description = "如果不存在，则返回状态码404，如果存在且删除成功，则返回状态码204")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Todo> result = todoRepository.findById(id);
        if (result.isPresent()) {
            todoRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "更新todo", description = "如果不存在，则返回状态码404")
    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo todo) {
        if (todoRepository.existsById(id)) {
            todo.setId(id);
            todoRepository.save(todo);
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
