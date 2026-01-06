package com.example.demo.service;

import com.example.demo.dto.TodoRequest;
import com.example.demo.dto.TodoFilterDto;
import com.example.demo.dto.TodoResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoService {
    TodoResponse addTodo(String userId, TodoRequest dto);

    Page<TodoResponse> getTodos(TodoFilterDto dto, Pageable pageable, String userId);

    List<TodoResponse> getAllTodos(TodoFilterDto dto, String userId);

    TodoResponse updateTodo(String todoId, TodoRequest dto);

    void deleteTodo(String todoId);
}
