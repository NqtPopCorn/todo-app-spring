import { useEffect, useMemo, useState } from "react";

import AddTask from "@/components/AddTask";
import DateTimeFilter from "@/components/DateTimeFilter";
import Footer from "@/components/Footer";
import { Header } from "@/components/Header";
import TaskEmptyState from "@/components/TaskEmptyState";
import TaskTable from "@/components/TaskList";
import TaskListPagination from "@/components/TaskListPagination";
import { Card } from "@/components/ui/card";
import { FilterType, options, VISIBLE_TASK_LIMIT } from "@/lib/data";
import todoService from "@/services/todoService";
import { CircleCheckBig, CircleDot, Timer } from "lucide-react";
import StatsAndFilters from "@/components/StatsAndFilters";
import type { TodoResponse } from "@/lib/types";
import TaskDialog from "@/components/TaskDialog";
import { toast } from "react-toastify";

function HomePage() {
  // const [filterText, setFilterText] = useState("");
  const [filter, setFilter] = useState<keyof typeof FilterType>("all");
  const [dateQuery, setDateQuery] = useState(options[0]);
  const [page, setPage] = useState(1);
  const [tasks, setTasks] = useState<TodoResponse[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedTask, setSelectedTask] = useState<TodoResponse | null>(null);

  const getStatusIcon = (status: TodoResponse["status"]) => {
    switch (status) {
      case "TODO":
        return <CircleDot className="size-4 text-muted-foreground" />;
      case "WORKING":
        return <Timer className="size-4 text-muted-foreground" />;
      case "COMPLETED":
        return <CircleCheckBig className="size-4 text-muted-foreground" />;
    }
  };

  const fetchTasks = async () => {
    setIsLoading(true);
    try {
      const { data } = await todoService.getTodos();
      setTasks(data);
    } catch (error) {
      console.error("Không thể tải danh sách nhiệm vụ", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleNewTaskAdded = async (newTask: TodoResponse) => {
    setPage(1);
    setTasks((prevTasks) => [newTask, ...prevTasks]);
  };

  const filteredTasks = useMemo(() => {
    // const searchLower = filterText.trim().toLowerCase();

    return tasks.filter((task) => {
      // const matchesText = task.title.toLowerCase().includes(searchLower);
      const matchesFilter =
        filter === "all"
          ? true
          : filter === "completed"
          ? task.status === "COMPLETED"
          : task.status !== "COMPLETED";
      const matchesDate =
        dateQuery.value === "all"
          ? true
          : dateQuery.value === "today"
          ? new Date(task.createdAt).toDateString() ===
            new Date().toDateString()
          : dateQuery.value === "week"
          ? (() => {
              const taskDate = new Date(task.createdAt);
              const now = new Date();
              const weekAgo = new Date();
              weekAgo.setDate(now.getDate() - 7);
              return taskDate >= weekAgo && taskDate <= now;
            })()
          : dateQuery.value === "month"
          ? (() => {
              const taskDate = new Date(task.createdAt);
              const now = new Date();
              const monthAgo = new Date();
              monthAgo.setMonth(now.getMonth() - 1);
              return taskDate >= monthAgo && taskDate <= now;
            })()
          : true;
      return matchesFilter && matchesDate; //&& matchesText;
    });
  }, [tasks, filter, dateQuery]);

  const pagedTasks = useMemo(() => {
    const start = (page - 1) * VISIBLE_TASK_LIMIT;
    return filteredTasks.slice(start, start + VISIBLE_TASK_LIMIT);
  }, [filteredTasks, page]);

  const totalPages = Math.max(
    1,
    Math.ceil(filteredTasks.length / VISIBLE_TASK_LIMIT)
  );

  const activeTasksCount = filteredTasks.filter(
    (task) => task.status !== "COMPLETED"
  ).length;
  const completedTasksCount = filteredTasks.filter(
    (task) => task.status === "COMPLETED"
  ).length;

  const handleMarkCompleted = async (task?: TodoResponse) => {
    try {
      if (!task) return;
      const { data } = await todoService.updateTodo(task!.id, {
        status: "COMPLETED",
      });
      toast.success(` 🎉 Tuyệt vời, làm tốt lắm!`, {
        position: "top-right",
        autoClose: 1500,
        hideProgressBar: false,
      });
      setTasks((prevTasks) =>
        prevTasks.map((t) => (t.id === data.id ? { ...t, ...data } : t))
      );
    } catch (error) {
      console.error("Lỗi xảy ra khi đánh dấu nhiệm vụ hoàn thành.", error);
      toast.error("Lỗi xảy ra khi đánh dấu nhiệm vụ hoàn thành.");
    }
  };
  const handleDelete = async (task?: TodoResponse) => {
    if (
      confirm(`Are you sure you want to delete the task: "${task?.title}"?`)
    ) {
      if (!task) return;
      try {
        await todoService.deleteTodo(task!.id);
        toast.success(`Nhiệm vụ đã được xóa.`);
        setTasks((prevTasks) => prevTasks.filter((t) => t.id !== task?.id));
      } catch (error) {
        console.error("Lỗi xảy ra khi xóa nhiệm vụ.", error);
        toast.error("Lỗi xảy ra khi xóa nhiệm vụ.");
      }
    }
  };

  const handleSubmitFormEdit = async (updatedTask: TodoResponse) => {
    try {
      const { data } = await todoService.updateTodo(
        updatedTask.id,
        updatedTask
      );
      toast.success(`Nhiệm vụ đã được cập nhật.`);
      setTasks((prevTasks) =>
        prevTasks.map((task) =>
          task.id === data.id ? { ...task, ...data } : task
        )
      );
      setSelectedTask(null);
    } catch (e) {
      toast.error("Lỗi xảy ra khi cập nhật nhiệm vụ.");
      console.error("Lỗi xảy ra khi cập nhật nhiệm vụ.", e);
    }
  };

  return (
    <div className="container mx-auto max-w-6xl space-y-8 p-8 bg-gradient-to-br from-slate-50 to-slate-100">
      <Header />

      <AddTask handleNewTaskAdded={handleNewTaskAdded} />

      <StatsAndFilters
        completedTasksCount={completedTasksCount}
        activeTasksCount={activeTasksCount}
        filter={filter}
        setFilter={setFilter}
      />

      {/* TodoResponse list */}
      <Card className="border-0 bg-white/80 shadow-custom-md dark:bg-slate-900/70 p-4">
        {isLoading ? (
          <div className="flex items-center justify-center p-10 text-muted-foreground">
            Đang tải danh sách nhiệm vụ...
          </div>
        ) : pagedTasks.length === 0 ? (
          <div className="p-6">
            <TaskEmptyState filter={filter} dateQuery={dateQuery} />
          </div>
        ) : (
          <TaskTable
            tasks={pagedTasks}
            getStatusIcon={getStatusIcon}
            setSelectedTask={setSelectedTask}
            handleMarkCompleted={handleMarkCompleted}
            handleDelete={handleDelete}
          />
        )}

        <div className="relative border-t border-border/70 px-6 py-4">
          <TaskListPagination
            handleNext={() => setPage((p) => Math.min(totalPages, p + 1))}
            handlePrev={() => setPage((p) => Math.max(1, p - 1))}
            handlePageChange={(p: number) => setPage(p)}
            page={page}
            totalPages={totalPages}
          />
          <div className="absolute top-4 right-6 mt-4">
            <DateTimeFilter dateQuery={dateQuery} setDateQuery={setDateQuery} />
          </div>
        </div>
      </Card>

      <Footer
        completedTasksCount={completedTasksCount}
        activeTasksCount={activeTasksCount}
      />

      <TaskDialog
        open={selectedTask !== null}
        task={selectedTask}
        onClose={() => setSelectedTask(null)}
        onSubmitFormEdit={handleSubmitFormEdit}
      />
    </div>
  );
}

export default HomePage;
