import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'

function Login() {
  const $loginButton = document.querySelector('#login-button')

      function login(emailValue, passwordValue) {
        fetch("/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
            , body: JSON.stringify({
                email: emailValue,
                password: passwordValue
            })
        }).then(data => data.json())
            .then(data => {
                 sessionStorage.setItem("token", data.accessToken)
                sessionStorage.setItem("email", data.email)
            })
    }

    const onLogin = event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const passwordValue = document.querySelector('#password').value
    if (!emailValue && !passwordValue) {
      Snackbar.show({ text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000 })
      return
    }
    login(emailValue, passwordValue);

  }

  this.init  = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
