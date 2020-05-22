import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = event => {
    event.preventDefault()
    const $email = document.querySelector('#email')
    const $password = document.querySelector('#password')
    if (!$email.value && !$password.value) {
      Snackbar.show({text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      return
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
