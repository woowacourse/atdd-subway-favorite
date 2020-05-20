import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const passwordValue = document.querySelector('#password').value
    if (!emailValue && !passwordValue) {
      Snackbar.show({ text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000 })
      return
    }
    const value = {
      email: emailValue,
      password: passwordValue
    };
    api.login.
    loginWithIdPsw(value)
        .then((token) => {
          localStorage.setItem("accessToken", token.accessToken);
          localStorage.setItem("tokenType", token.tokenType);
          location.href='/';
        })
        .catch(error => alert(ERROR_MESSAGE))
  }

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
