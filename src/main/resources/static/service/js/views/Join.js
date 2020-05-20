import { ERROR_MESSAGE, EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js';

function Join() {
  const $joinButton = document.querySelector('#join-button')

  const validate = ({ email, name, password, passwordCheck }) => {
    if (!email && !password && !name && !passwordCheck) {
      throw new Error(ERROR_MESSAGE.JOIN_FORM_EMPTY)
    }

    if (!email.match("^[a-z-A-z-0-9]+@[a-z-A-z-0-9]+.[a-z-A-z-0-9]+$")) {
      throw new Error(ERROR_MESSAGE.JOIN_INVALID_EMAIL_FORMAT)
    }

    if (!name || name.includes(" ")) {
      throw new Error(ERROR_MESSAGE.JOIN_INVALID_NAME)
    }

    if (!password || password.includes(" ")) {
      throw new Error(ERROR_MESSAGE.JOIN_INVALID_PASSWORD)
    }

    if (password !== passwordCheck) {
      throw new Error(ERROR_MESSAGE.JOIN_PASSWORD_MISMATCH)
    }
  }

  const onJogin = async event => {
    event.preventDefault()

    const email = document.querySelector('#email').value
    const name = document.querySelector('#name').value
    const password = document.querySelector('#password').value
    const passwordCheck = document.querySelector('#password-check').value

    const memberInfo = {
      email,
      name,
      password,
    }

    try {
      validate({ ...memberInfo, passwordCheck })
      await api.member.join(memberInfo)
      location.replace("/login")
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
    $joinButton.addEventListener(EVENT_TYPE.CLICK, onJogin)
  }
}

const join = new Join()
join.init()
