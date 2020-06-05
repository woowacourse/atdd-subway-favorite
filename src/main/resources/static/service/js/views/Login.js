import {ERROR_MESSAGE, EVENT_TYPE} from "../../utils/constants.js";
import showSnackbar from "../../lib/snackbar/index.js";
import api from "../../api/index.js";

function Login() {
  const $loginButton = document.querySelector("#login-button");
  const $email = document.querySelector("#email");
  const $password = document.querySelector("#password");

  const onLogin = async event => {
    event.preventDefault();
    if (isInvalid()) {
      showSnackbar(ERROR_MESSAGE.COMMON);
      return;
    }
    try {
        const loginMember = {
            email: $email.value,
            password: $password.value
        };
        await api.loginMember.login(loginMember);
        location.href = "/";
    } catch (e) {
        console.dir(e);
        showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  const isInvalid = () => {
    const email = $email.value;
    const password = $password.value;
    return !email || !password;
  };

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin);
  };
}

const login = new Login();
login.init();
