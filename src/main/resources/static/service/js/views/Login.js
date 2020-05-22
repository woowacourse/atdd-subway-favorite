import { ERROR_MESSAGE, EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js';

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = event => {
    event.preventDefault();
    const emailValue = document.querySelector('#email').value;
    const passwordValue = document.querySelector('#password').value;
    const data = {
      email: emailValue,
      password: passwordValue,
    };
    if (!emailValue || !passwordValue) {
      Snackbar.show({
        text: ERROR_MESSAGE.LOGIN_FAIL,
        pos: 'bottom-center',
        showAction: false,
        duration: 2000
      });
      return;
    }
    api.member.login(data)
    .then(response => {
      if (!response.ok) {
        throw response;
      }
      return response.json();
    }).then(tokenResponse => {
      setCookie(tokenResponse.accessToken);
      window.location = "/my-page";

    }).catch(error => error.json())
    .then(errorResponse => {
      if (errorResponse) {
        alert(errorResponse.message);
      }
    });
  }

  const setCookie = function (value) {
    const date = new Date();
    date.setTime(date.getTime() + 5 * 60 * 1000);
    document.cookie = "token=" + value + ';expires=' + date.toUTCString();
  };



  const deleteCookie = function () {
    document.cookie = 'token=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
  }

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin);
  }
}

const login = new Login();
login.init();
