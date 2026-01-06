export const FilterType: Record<string, string> = {
  all: "tất cả",
  active: "đang làm",
  completed: "hoàn thành",
};

export type Option = {
  value: string;
  label: string;
};

export const options: Option[] = [
  {
    value: "today",
    label: "Hôm nay",
  },
  {
    value: "week",
    label: "Tuần này",
  },
  {
    value: "month",
    label: "Tháng này",
  },
  {
    value: "all",
    label: "Tất cả",
  },
];

export const VISIBLE_TASK_LIMIT = 4;
