import path from "path";
import tailwindcss from "@tailwindcss/vite";
import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  build: {
    // // 1. Xóa thư mục đích trước khi build để tránh file rác
    // emptyOutDir: true,
    // // 2. Trỏ đường dẫn output về thư mục static của Spring Boot
    // // Lưu ý: Dùng ../ để lùi ra khỏi thư mục frontend, tìm đến folder backend
    // outDir: "../backend/src/main/resources/static",
    // // (Tùy chọn) Tắt sourcemap để file nhẹ hơn và bảo mật code
    // sourcemap: false,
  },
});
