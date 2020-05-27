import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'
import api from "../../api/index.js";
import { validateNotLogin } from '../../login/ValidateLogin.js'

function Join() {
  const $joinButton = document.querySelector('#join-button')
  const onJoin = event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const nameValue = document.querySelector('#name').value
    const passwordValue = document.querySelector('#password').value
    const passwordCheckValue = document.querySelector('#password-check').value
    if (!(emailValue && nameValue && passwordValue && passwordCheckValue)) {
      Snackbar.show({ text: ERROR_MESSAGE.JOIN_EMPTY_FAIL, pos: 'bottom-center', showAction: false, duration: 2000 })
      return
    }
    if (!(passwordValue === passwordCheckValue)) {
      Snackbar.show({ text: ERROR_MESSAGE.JOIN_PASSWORD_FAIL, pos: 'bottom-center', showAction: false, duration: 2000 })
      return
    }

    const userInfo = {
      email: emailValue,
      name: nameValue,
      password: passwordValue
    }
    api.member
        .create(userInfo)
  }

  this.init = () => {
    $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
  }
}

validateNotLogin()
const join = new Join()
join.init()
