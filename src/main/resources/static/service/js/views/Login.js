import {EVENT_TYPE, ERROR_MESSAGE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value.trim()
    const passwordValue = document.querySelector('#password').value.trim()
    if (!emailValue || !passwordValue) {
      Snackbar.show({text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      return
    }

    const loginInformation = {
      email: emailValue,
      password: passwordValue
    }
    api.memberWithoutToken.login(loginInformation)
        .then((data) => {
          alert("로그인 성공!")
          localStorage.setItem("accessToken", data.accessToken)
          localStorage.setItem("tokenType", data.tokenType)
          location.href = "/"
        })
        .catch(error => {
          alert(error)
        })
  }

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
