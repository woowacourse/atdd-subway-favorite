import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import showSnackbar from "../../lib/snackbar/index.js";
import api from "../../api/index.js";

const validateEmail = (email) => {
  const re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
  return re.test(String(email).toLowerCase())
}

const validate = ({emailValue, nameValue, passwordValue, passwordCheckValue}) => {
  if (!emailValue && !nameValue && !passwordValue && !passwordCheckValue) {
    showSnackbar(ERROR_MESSAGE.JOIN_EMPTY_VALUE);
    return
  }

  if (!validateEmail(emailValue)) {
    showSnackbar(ERROR_MESSAGE.JOIN_INVALID_EMAIL);
    return
  }

  if (passwordValue !== passwordCheckValue) {
    showSnackbar(ERROR_MESSAGE.JOIN_PASSWORD_NOT_EQUALS);
    return
  }
}

function Join() {
  const $joinButton = document.querySelector('#join-button')
  const onJoin = event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const nameValue = document.querySelector('#name').value
    const passwordValue = document.querySelector('#password').value
    const passwordCheckValue = document.querySelector('#password-check').value

    const data = {
      emailValue: emailValue,
      nameValue: nameValue,
      passwordValue: passwordValue,
      passwordCheckValue: passwordCheckValue
    }

    validate(data)

    const validData = {
      email: emailValue,
      name: nameValue,
      password: passwordValue,
    }

    api.member.create(validData)
      .then(response => {
        if (!response.ok) {
          alert(ERROR_MESSAGE.JOIN_FAIL)
        } else {
          location.href = '/'
        }
      })
  }

  this.init = () => {
    $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
  }
}


const join = new Join()
join.init()
