import { EVENT_TYPE, ERROR_MESSAGE } from "../../utils/constants.js";
import showSnackbar from "../../lib/snackbar/index.js";
import api from "../../api/index.js";

function Login() {
  const $loginButton = document.querySelector("#login-button");
  const $email = document.querySelector("#email");
  const $password = document.querySelector("#password");

  const onLogin = event => {
    event.preventDefault();
    const email = $email.value;
    const password = $password.value;
    if (!email && !password) {
      showSnackbar(ERROR_MESSAGE.COMMON);
      return;
    }
    const loginMember = {
      email: email,
      password: password
    };
    api.member
      .login(loginMember)
      .then(jwt => {
        if (!!jwt) {
          localStorage.setItem("jwt", `${jwt.tokenType} ${jwt.accessToken}`);
          location.href = "/";
        }
      })
      .catch(error => console.log(error));
  };

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin);
  };
}

const login = new Login();
login.init();
