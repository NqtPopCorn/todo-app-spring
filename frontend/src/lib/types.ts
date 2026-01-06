export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  refreshTokenMaxAge: number;
  message: string;
}

export interface RefreshResponse {
  message: string;
  accessToken: string;
}

export interface TodoRequest {
  title: string;
  description?: string;
  status: TodoStatus;
}

export interface TodoResponse {
  id: string;
  title: string;
  description: string;
  status: TodoStatus;
  userId: string;
  createdAt: string;
  completedAt?: string;
}

export type TodoStatus = "TODO" | "WORKING" | "COMPLETED";
