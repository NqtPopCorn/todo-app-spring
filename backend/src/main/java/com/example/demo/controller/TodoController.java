package com.example.demo.controller;

import com.example.demo.dto.TodoRequest;
import com.example.demo.dto.TodoFilterDto;
import com.example.demo.dto.TodoResponse;
import com.example.demo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<TodoResponse>> getPage(
            @ModelAttribute TodoFilterDto dto,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal String userId) {
        var res = todoService.getTodos(dto, pageable, userId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TodoResponse>> getAll(
            @ModelAttribute TodoFilterDto dto,
            @AuthenticationPrincipal String userId) {
        var res = todoService.getAllTodos(dto, userId);
        return ResponseEntity.ok(res);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TodoResponse> createTodo(
            @RequestBody @Valid TodoRequest dto,
            @AuthenticationPrincipal String userId) {
        var res = todoService.addTodo(userId, dto);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{todoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable String todoId,
            @RequestBody @Valid TodoRequest dto) {
        var res = todoService.updateTodo(todoId, dto);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{todoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteTodo(
            @PathVariable String todoId) {
        todoService.deleteTodo(todoId);
        return ResponseEntity.ok("Success");
    }

}
