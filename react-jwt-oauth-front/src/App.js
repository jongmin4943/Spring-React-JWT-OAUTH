import axios from "axios";
import { useState } from "react";
import "./App.css";
import Login from "./Login";

const config = {
  headers: {
    "Content-Type": "application/json; charset=utf-8",
    Authorization: "Bearer " + localStorage.getItem("jwtToken"),
  },
};

function App() {
  const [user, setUser] = useState(null);

  const getUser = async () => {
    let res = await axios.get("http://localhost:8080/user", config);
    setUser(res.data);
  };
  const getManager = async () => {
    let res = await axios.get("http://localhost:8080/manager/reports", config);
    setUser(res.data);
  };
  const getTest = async () => {
    let res = await axios.get("http://localhost:8080/user/good", config);
    setUser(res.data);
  };

  return (
    <div>
      <Login />
      <h1>user : {user}</h1>
      <button onClick={getUser}>유저정보 가져오기</button>
      <button onClick={getManager}>매니저권한 실행</button>
      <button onClick={getTest}>일반권한 실행</button>
    </div>
  );
}

export default App;
