import {ERROR_MESSAGE, EVENT_TYPE, REGEX, SUCCESS} from '../../utils/constants.js'
import api from "../../../service/api/index.js";

function Join() {
  const $joinButton = document.querySelector('#join-button')
  const $email = document.querySelector('#email')
  const $name = document.querySelector('#name')
  const $password = document.querySelector('#password')
  const $passwordCheck = document.querySelector('#password-check')

  const onJoin = event => {
    if (event.type !== EVENT_TYPE.CLICK && event.key !== "Enter") {
      return;
    }

    event.preventDefault()

    if (!REGEX.EMAIL.test($email.value)) {
      Snackbar.show({text: ERROR_MESSAGE.EMAIL_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      return
    }
    if (!$email.value || !$password.value || !$name.value || !$passwordCheck.value) {
      Snackbar.show({text: ERROR_MESSAGE.JOIN_INPUT_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      return
    }
    if (!REGEX.EMPTY_SPACE.test($name.value)) {
      Snackbar.show({text: ERROR_MESSAGE.EMPTY_SPACE_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      return
    }
    if ($password.value !== $passwordCheck.value) {
      Snackbar.show({text: ERROR_MESSAGE.PASSWORD_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      return
    }
    api.member.create({
      email: $email.value,
      name: $name.value,
      password: $password.value
    }).then(async res => {
        if (res.ok) {
          resetJoinForm();
          alert(SUCCESS.JOIN);
          location.href = "/login"
          return
        }
        Snackbar.show({
          text: await res.text() + ERROR_MESSAGE.JOIN_FAIL,
          pos: 'bottom-center',
          showAction: false,
          duration: 2000
        })
      }
    )
  }

  const resetJoinForm = () => {
    $email.value = "";
    $name.value = "";
    $password.value = "";
    $passwordCheck.value = "";
  }

  this.init = () => {
    $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
    $passwordCheck.addEventListener(EVENT_TYPE.KEY_UP, onJoin)
  }
}

const join = new Join()
join.init()
