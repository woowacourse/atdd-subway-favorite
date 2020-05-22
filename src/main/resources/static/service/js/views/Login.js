import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = event => {
    event.preventDefault()
    const $email = document.querySelector('#email')
    const $password = document.querySelector('#password')
    if (!$email.value) {
      alert(ERROR_MESSAGE.EMAIL_EMPTY)
      return false;
    }
    if (!$password.value) {
      alert(ERROR_MESSAGE.PASSWORD_EMPTY)
      return false;
    }
    const regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    if ($email.value.match(regExp) === null) {
      alert(ERROR_MESSAGE.EMAIL_INVALID)
      return;
    }
    const loginMember = {
      email: $email.value,
      password: $password.value
    };
    api.member
        .login(loginMember)
        .then(jwt => {
          if (!!jwt) {
            localStorage.setItem("jwt", `${jwt.tokenType} ${jwt.accessToken}`);
            location.href = "/";
          }
        })
        .catch(error => alert(ERROR_MESSAGE[error.message]));
  };

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
