import axiosIns from "@/lib/axios";
import type { AuthResponse, RefreshResponse } from "@/lib/types";

const authService = {
  signin: (username: string, password: string) => {
    return axiosIns.post<AuthResponse>("/auth/login", { username, password });
  },
  refreshToken: () => {
    return axiosIns.post<RefreshResponse>("/auth/refresh");
  },
};

export default authService;
