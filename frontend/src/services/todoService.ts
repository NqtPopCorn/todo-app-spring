import axiosIns from "@/lib/axios";
import type { TodoResponse } from "@/lib/types";

const todoService = {
  getTodos: () => {
    return axiosIns.get("/tasks/all");
  },
  createTodo: (data: any) => {
    return axiosIns.post<TodoResponse>("/tasks", data);
  },
  updateTodo: (id: string, data: Partial<TodoResponse>) => {
    return axiosIns.put<TodoResponse>(`/tasks/${id}`, data);
  },
  deleteTodo: (id: string) => {
    return axiosIns.delete(`/tasks/${id}`);
  },
};

export default todoService;
