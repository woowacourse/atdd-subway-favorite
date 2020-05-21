import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'
import api from '../../api/index.js'

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = async event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const passwordValue = document.querySelector('#password').value
    if (!emailValue || !passwordValue) {
      // Snackbar.show({ text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000 })
      alert(ERROR_MESSAGE.LOGIN_FAIL)
      return
    }

    const loginInput = {
      email: emailValue,
      password: passwordValue
    }

    await api.session
    .login(loginInput)
    .then(response => {
      console.log(response.accessToken)
      localStorage.setItem("token", response.accessToken)
    })
    .catch(error => alert(ERROR_MESSAGE.COMMON))

    window.location.replace("/search")
  }

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
