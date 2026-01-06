import { type FormEvent, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import useAuthContext from "./AuthContext";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";

const LoginPage = () => {
  const navigate = useNavigate();
  const { signin, accessToken } = useAuthContext();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (accessToken) {
      navigate("/home", { replace: true });
    }
  }, [accessToken, navigate]);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSubmitting(true);
    await signin(username.trim(), password);
    setIsSubmitting(false);
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 p-4">
      <Card className="w-full max-w-md border-0 shadow-custom-md">
        <div className="space-y-6 p-6">
          <div className="space-y-2 text-center">
            <h1 className="text-2xl font-semibold tracking-tight text-primary">
              Đăng nhập
            </h1>
            <p className="text-sm text-muted-foreground">
              Quản lý nhiệm vụ của bạn với TodoX
            </p>
          </div>

          <form className="space-y-4" onSubmit={handleSubmit}>
            <div className="space-y-2">
              <label
                className="text-sm font-medium text-foreground"
                htmlFor="username"
              >
                Tên đăng nhập
              </label>
              <Input
                id="username"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                placeholder="Nhập tên đăng nhập"
                required
              />
            </div>

            <div className="space-y-2">
              <label
                className="text-sm font-medium text-foreground"
                htmlFor="password"
              >
                Mật khẩu
              </label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="••••••••"
                required
              />
            </div>

            <Button
              type="submit"
              className="w-full"
              variant="gradient"
              disabled={
                isSubmitting ||
                !username.trim().length ||
                !password.trim().length
              }
            >
              {isSubmitting ? "Đang đăng nhập..." : "Đăng nhập"}
            </Button>
          </form>
        </div>
      </Card>
    </div>
  );
};

export default LoginPage;
