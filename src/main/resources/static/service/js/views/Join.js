import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'
import api from '../../api/index.js'
// import Snackbar from '/service/lib/snackbar/snackbar.js'

function Join() {
  const $joinButton = document.querySelector('#join-button')
  console.log($joinButton)

  const onJoin = async event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const nameValue = document.querySelector("#name").value
    const passwordValue = document.querySelector('#password').value
    const passwordCheckValue = document.querySelector("#password-check").value
    if (!emailValue || !nameValue || !passwordValue || !passwordCheckValue) {
      // Snackbar.show({ text: ERROR_MESSAGE.JOIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000 })
      alert(ERROR_MESSAGE.JOIN_FAIL)
      return
    }
    if (passwordValue !== passwordCheckValue) {
      // Snackbar.show({ text: ERROR_MESSAGE.PASSWORD_NOT_EQUAL, pos: 'bottom-center', showAction: false, duration: 2000 })
      alert(ERROR_MESSAGE.PASSWORD_NOT_EQUAL)
      return
    }

    const joinInput = {
      email: emailValue,
      name: nameValue,
      password: passwordValue
    }

    await api.member
    .create(joinInput)
    .catch(error => alert(ERROR_MESSAGE.COMMON))

    window.location.replace("/login")
  }


  this.init = () => {
    $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
  }
}

const join = new Join()
join.init()
