import axios from "axios";
import React from "react";
import GoogleLogin from "react-google-login";

const config = {
  headers: {
    "Content-Type": "application/json; charset=utf-8",
  },
};

const responseGoogle = async (response) => {
  console.log(1, response);
  let jwtToken = await axios.post("http://localhost:8080/oauth/jwt/google", JSON.stringify(response), config);
  if (jwtToken.status === 200) {
    console.log(2, jwtToken.data);
    localStorage.setItem("jwtToken", jwtToken.data);
  }
};

const Login = () => {
  return (
    <GoogleLogin
      clientId='구글 클라이언트 아이디입력해야해'
      buttonText='Login'
      onSuccess={responseGoogle}
      onFailure={responseGoogle}
      cookiePolicy={"single_host_origin"}
    />
  );
};

export default Login;
