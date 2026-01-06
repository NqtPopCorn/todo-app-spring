import React from "react";
import { CheckCircle, CircleX, Edit, MoreHorizontal } from "lucide-react";
import { Button } from "./ui/button";
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
} from "./ui/dropdown-menu";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "./ui/table";
import type { TodoResponse } from "@/lib/types";

interface TaskTableProps {
  tasks: TodoResponse[];
  getStatusIcon: (status: TodoResponse["status"]) => React.ReactNode;
  setSelectedTask: (task: TodoResponse | null) => void;
  handleMarkCompleted: (task?: TodoResponse) => void;
  handleDelete: (task?: TodoResponse) => void;
}

export default function TaskTable({
  tasks,
  getStatusIcon,
  setSelectedTask,
  handleMarkCompleted,
  handleDelete,
}: TaskTableProps) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Title</TableHead>
          <TableHead>Status</TableHead>
          <TableHead>Created At</TableHead>
          <TableHead>Actions</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody className="text-left">
        {tasks.map((task) => (
          <TableRow key={task.id}>
            <TableCell className="truncate md:max-w-[260px]" title={task.title}>
              {task.title}
            </TableCell>
            <TableCell>
              <div className="flex items-center gap-2">
                {getStatusIcon(task.status)}
                <span>{task.status}</span>
              </div>
            </TableCell>
            <TableCell>
              {new Date(task.createdAt).toLocaleTimeString([], {
                year: "numeric",
                month: "short",
                day: "2-digit",
              })}
            </TableCell>
            <TableCell>
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="ghost" size="sm">
                    <MoreHorizontal className="size-4" />
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                  <DropdownMenuItem
                    onClick={() => setSelectedTask(task)}
                    className="text-blue-600"
                  >
                    <Edit className="size-4 text-blue-600" /> View/Edit
                  </DropdownMenuItem>
                  {task.status !== "COMPLETED" && (
                    <DropdownMenuItem
                      onClick={() => handleMarkCompleted(task)}
                      className="text-green-600"
                    >
                      <CheckCircle className="size-4 text-green-600" />
                      Completed
                    </DropdownMenuItem>
                  )}
                  <DropdownMenuItem
                    className="text-destructive"
                    onClick={() => handleDelete(task)}
                  >
                    <CircleX className="size-4 text-destructive" /> Delete
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
