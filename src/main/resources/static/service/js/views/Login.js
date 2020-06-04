import { ERROR_MESSAGE, EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js'
import user from '../../api/user.js'

function Login() {
  const $loginButton = document.querySelector('#login-button')

  const validate = ({ email, password }) => {
    if (!email && !password) {
      throw new Error(ERROR_MESSAGE.FORM_EMPTY)
    }

    if (!email.match("^[a-z-A-z-0-9]+@[a-z-A-z-0-9]+.[a-z-A-z-0-9]+$")) {
      throw new Error(ERROR_MESSAGE.INVALID_EMAIL_FORMAT)
    }

    if (!password || password.includes(" ")) {
      throw new Error(ERROR_MESSAGE.INVALID_PASSWORD)
    }
  }

  const onLogin = async event => {
    event.preventDefault()
    const email = document.querySelector('#email').value.trim()
    const password = document.querySelector('#password').value.trim()

    const loginInfo = {
      email,
      password
    }

    try {
      validate(loginInfo)
      user.login(await api.member.login(loginInfo))
      location.replace('/service')
    }
    catch (error) {
      Snackbar.show({
        text: error.message,
        pos: 'bottom-center',
        showAction: false,
        duration: 2000
      })
    }
  }

  this.init = () => {
    user.isLoggedIn() && location.replace('/service')
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
