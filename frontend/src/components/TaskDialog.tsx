import React from "react";
import { Dialog, DialogClose, DialogFooter, DialogContent } from "./ui/dialog";
import type { TodoResponse } from "@/lib/types";
import { Switch } from "./ui/switch";

interface TaskDialogProps {
  open: boolean;
  onSubmitFormEdit: (task: TodoResponse) => void;
  onClose: () => void;
  task?: TodoResponse | null;
}

const TaskDialog = ({
  open,
  onSubmitFormEdit,
  task,
  onClose,
}: TaskDialogProps) => {
  const [isEditing, setIsEditing] = React.useState(false);
  const [formData, setFormData] = React.useState({
    title: task?.title || "",
    description: task?.description || "",
  });

  React.useEffect(() => {
    setFormData({
      title: task?.title || "",
      description: task?.description || "",
    });
    setIsEditing(false);
  }, [task, open]);

  const handleSubmitForm = () => {
    const updatedTask = { ...(task || {}), ...formData } as TodoResponse;
    onSubmitFormEdit(updatedTask);
    setIsEditing(false);
    onClose();
  };

  return (
    <Dialog open={open} onOpenChange={(isOpen) => !isOpen && onClose()}>
      <DialogContent>
        <div className="max-w-2xl">
          <header className="mb-4">
            <h2 className="text-xl font-semibold">{task?.title || "Task"}</h2>
            <p className="text-sm text-muted-foreground">
              {task ? `Status: ${task.status}` : "No task selected"}
            </p>
          </header>

          <div className="space-y-4">
            <label className="block text-sm text-muted-foreground">
              <strong>Title</strong>
              <input
                className={
                  "w-full mt-1 text-md font-semibold bg-transparent focus:outline-none focus:ring-2 focus:ring-blue-500 rounded-md p-2 border" +
                  (isEditing ? " border-blue-500" : " border-transparent")
                }
                value={formData.title}
                readOnly={!isEditing}
                disabled={!isEditing}
                onChange={(e) =>
                  setFormData({ ...formData, title: e.target.value })
                }
                placeholder="Task title"
              />
            </label>

            <label className="block text-sm text-muted-foreground">
              <strong>Description</strong>
              <textarea
                className={
                  "w-full mt-1 p-3 rounded-md bg-transparent border focus:outline-none focus:ring-2 focus:ring-blue-500" +
                  (isEditing ? " border-blue-500" : " border-transparent")
                }
                value={formData.description}
                readOnly={!isEditing}
                onChange={(e) =>
                  setFormData({ ...formData, description: e.target.value })
                }
                placeholder="Add more details..."
                rows={5}
              />
            </label>

            <div className="text-sm text-muted-foreground">
              <p>
                <strong>Created At:</strong>{" "}
                {task?.createdAt
                  ? new Date(task.createdAt).toLocaleString()
                  : "—"}
              </p>
              <p>
                <strong>Completed At:</strong>{" "}
                {task?.completedAt
                  ? new Date(task.completedAt).toLocaleString()
                  : "—"}
              </p>
            </div>
          </div>

          <DialogFooter>
            <div className="flex items-center space-x-3">
              <Switch checked={isEditing} onCheckedChange={setIsEditing} />
              <span className="text-sm">Edit Mode</span>
            </div>

            <div className="ml-auto flex items-center gap-2">
              <DialogClose asChild>
                <button className="px-4 py-2 rounded-md bg-neutral-200 text-sm hover:bg-neutral-250">
                  Close
                </button>
              </DialogClose>

              <button
                className="px-4 py-2 rounded-md bg-blue-600 text-white text-sm disabled:opacity-50"
                disabled={!isEditing}
                onClick={handleSubmitForm}
              >
                Save
              </button>
            </div>
          </DialogFooter>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default TaskDialog;
