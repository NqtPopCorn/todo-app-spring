import { Navigate, Outlet } from "react-router-dom";
import useAuthContext from "./AuthContext";

const PrivateRoute = () => {
  const { accessToken, loading } = useAuthContext();

  if (loading) {
    return <div>Loading...</div>;
  }

  return accessToken ? <Outlet /> : <Navigate to="/login" replace />;
};

export default PrivateRoute;
