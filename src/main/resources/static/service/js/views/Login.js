import { ERROR_MESSAGE, EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js';

function Login() {
  const $loginForm = document.querySelector("#login-form");
  const onLogin = event => {
    event.preventDefault()
    const email = document.querySelector('#email').value
    const password = document.querySelector('#password').value
    if (!email && !password) {
      Snackbar.show({
        text: ERROR_MESSAGE.LOGIN_FAIL,
        pos: 'bottom-center',
        showAction: false,
        duration: 2000
      })
      return;
    }
    const loginRequest = {
      email,
      password,
    }
    api.member.login(loginRequest)
    .then(response => {
      if (response.status === 200) {
        return response.json();
      }
      throw new Error("로그인에 실패했습니다.");
    }).then(tokenResponse => {
      localStorage.setItem("Authorization",
        tokenResponse.tokenType + " " + tokenResponse.accessToken);
      return location.href = "/";
    }).catch(error => alert(error.message));
  }

  this.init = () => {
    $loginForm.addEventListener(EVENT_TYPE.SUBMIT, onLogin);
  }
}

const login = new Login()
login.init()
