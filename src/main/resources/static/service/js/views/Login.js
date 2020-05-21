import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from "../../api/index.js"
import cookieApi from "../../utils/cookieApi.js";

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const passwordValue = document.querySelector('#password').value
    if (!emailValue || !passwordValue) {
      Snackbar.show({ text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000 })
      return
    }
    const data = {
      email: emailValue,
      password: passwordValue
    };
    api.user.login(data).then(response => {
      const token = response.accessToken;
      cookieApi.setCookie("token", token, 1);
      window.location.href = "/";
    }).catch(error => alert(error.message));
  };

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
