import React, { useEffect, useState } from "react";
import authService from "@/services/authService";
import { toast } from "react-toastify";
import axiosIns from "@/lib/axios";

const authContext = React.createContext<{
  accessToken: string | null;
  signin: (username: string, password: string) => Promise<void>;
  signout: () => void;
  refreshToken: () => Promise<void>;
  loading: boolean;
}>({
  accessToken: null,
  signin: async () => {},
  signout: () => {},
  refreshToken: async () => {},
  loading: false,
});

export function AuthContextProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  const signin = async (username: string, password: string) => {
    try {
      setLoading(true);
      const response = await authService.signin(username, password);
      setAccessToken(response.data.accessToken);
      axiosIns.defaults.headers.common[
        "Authorization"
      ] = `Bearer ${response.data.accessToken}`;
    } catch (error) {
      toast.error("Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.");
    } finally {
      setLoading(false);
    }
  };

  const signout = () => {
    setAccessToken(null);
  };

  const refreshToken = async () => {
    setAccessToken(null);
    try {
      setLoading(true);
      const response = await authService.refreshToken();
      setAccessToken(response.data.accessToken);
      axiosIns.defaults.headers.common[
        "Authorization"
      ] = `Bearer ${response.data.accessToken}`;
    } catch (error) {
      console.error("Lỗi làm mới token:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    refreshToken();
  }, []);

  return (
    <authContext.Provider
      value={{
        accessToken,
        signout,
        signin,
        refreshToken,
        loading,
      }}
    >
      {children}
    </authContext.Provider>
  );
}

const useAuthContext = () => React.useContext(authContext);
export default useAuthContext;
