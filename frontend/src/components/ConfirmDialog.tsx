// import React from "react";
// import { Dialog, DialogContent, DialogFooter, DialogClose } from "./ui/dialog";
// import { Button } from "@/components/ui/button";

// interface ConfirmDialogProps {
//   open: boolean;
//   title?: string;
//   message?: React.ReactNode;
//   confirmLabel?: string;
//   cancelLabel?: string;
//   onConfirm: () => void;
//   onCancel: () => void;
// }

// /**
//  * Controlled generic confirm dialog.
//  * - open: controls visibility
//  * - onCancel: called when dialog is closed/cancelled
//  * - onConfirm: called when OK/Confirm clicked
//  */
// export default function ConfirmDialog({
//   open,
//   title = "Confirm",
//   message = "Are you sure?",
//   confirmLabel = "OK",
//   cancelLabel = "Cancel",
//   onConfirm,
//   onCancel,
// }: ConfirmDialogProps) {
//   return (
//     <Dialog open={open} onOpenChange={(isOpen) => !isOpen && onCancel()}>
//       <DialogContent>
//         <div className="max-w-md space-y-4">
//           <h3 className="text-lg font-semibold">{title}</h3>
//           <div className="text-sm text-muted-foreground">{message}</div>

//           <DialogFooter>
//             <div className="ml-auto flex items-center gap-2">
//               <DialogClose asChild>
//                 <button
//                   type="button"
//                   className="px-3 py-2 rounded-md bg-neutral-100 hover:bg-neutral-200 text-sm"
//                   onClick={onCancel}
//                 >
//                   {cancelLabel}
//                 </button>
//               </DialogClose>

//               <Button
//                 variant="destructive"
//                 onClick={() => {
//                   onConfirm();
//                 }}
//               >
//                 {confirmLabel}
//               </Button>
//             </div>
//           </DialogFooter>
//         </div>
//       </DialogContent>
//     </Dialog>
//   );
// }
