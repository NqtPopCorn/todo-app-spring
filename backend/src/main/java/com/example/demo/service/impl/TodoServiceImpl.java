package com.example.demo.service.impl;

import com.example.demo.dto.TodoResponse;
import com.example.demo.entity.Todo;
import com.example.demo.exception.ApplicationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TodoRequest;
import com.example.demo.dto.TodoFilterDto;
import com.example.demo.repository.TodoRepository;
import com.example.demo.service.TodoService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final JsonMapper jsonMapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public TodoResponse addTodo(String userId, TodoRequest dto) {
        Todo todo = jsonMapper.convertValue(dto, Todo.class);
        todo.setUserId(userId);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setStatus(Todo.Status.TODO);
        return toTodoResponse(todoRepository.save(todo));
    }

    @Override
    public Page<TodoResponse> getTodos(TodoFilterDto dto, Pageable pageable, String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        if (dto.getStatus() != null) {
            query.addCriteria(Criteria.where("status").is(Todo.Status.valueOf(dto.getStatus())));
        }

        // Filter theo CreatedAt betterween from and to
        if (dto.getFrom() != null || dto.getTo() != null) {
            Criteria dateCriteria = Criteria.where("createdAt");
            if (dto.getFrom() != null) {
                dateCriteria.gte(dto.getFrom());
            }
            if (dto.getTo() != null) {
                dateCriteria.lte(dto.getTo());
            }
            query.addCriteria(dateCriteria);
        }

        long count = mongoTemplate.count(query, Todo.class);
        query.with(pageable);
        List<Todo> todos = mongoTemplate.find(query, Todo.class);

        return new PageImpl<>(todos, pageable, count).map(this::toTodoResponse);
    }

    @Override
    public List<TodoResponse> getAllTodos(TodoFilterDto dto, String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        if (dto.getStatus() != null) {
            query.addCriteria(Criteria.where("status").is(Todo.Status.valueOf(dto.getStatus())));
        }

        // Filter theo CreatedAt betterween from and to
        if (dto.getFrom() != null || dto.getTo() != null) {
            Criteria dateCriteria = Criteria.where("createdAt");
            if (dto.getFrom() != null) {
                dateCriteria.gte(dto.getFrom());
            }
            if (dto.getTo() != null) {
                dateCriteria.lte(dto.getTo());
            }
            query.addCriteria(dateCriteria);
        }

        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Todo> todos = mongoTemplate.find(query, Todo.class);

        return todos.stream().map(this::toTodoResponse).collect(Collectors.toList());
    }

    @Override
    public TodoResponse updateTodo(String todoId, TodoRequest dto) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new ApplicationException(404, "Todo not found"));
        if (dto.getStatus() != null) {
            todo.setStatus(Todo.Status.valueOf(dto.getStatus()));
            if (Todo.Status.valueOf(dto.getStatus()) == Todo.Status.COMPLETED) {
                todo.setCompletedAt(LocalDateTime.now());
            }
        }
        if (dto.getTitle() != null) {
            todo.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            todo.setDescription(dto.getDescription());
        }

        return toTodoResponse(todoRepository.save(todo));
    }

    @Override
    public void deleteTodo(String todoId) {
        todoRepository.deleteById(todoId);
    }

    private TodoResponse toTodoResponse(Todo todo) {
        return jsonMapper.convertValue(todo, TodoResponse.class);
    }

}
