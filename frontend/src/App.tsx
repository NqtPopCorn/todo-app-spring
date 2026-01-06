import "./App.css";

import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";

import Home from "./pages/HomePage";
import NotFound from "./pages/NotFound";
import Login from "./pages/LoginPage";
import { ToastContainer } from "react-toastify";
import { AuthContextProvider } from "./pages/AuthContext";
import PrivateRoute from "./pages/PrivateRoute";

function App() {
  return (
    <>
      <ToastContainer autoClose={2000} />
      <AuthContextProvider>
        <Router>
          <Routes>
            <Route path="/" element={<Navigate to="/home" />} />
            <Route path="/home" element={<PrivateRoute />}>
              <Route index element={<Home />} />
            </Route>

            <Route path="/login" element={<Login />} />

            <Route path="*" element={<NotFound />} />
          </Routes>
        </Router>
      </AuthContextProvider>
    </>
  );
}

export default App;
