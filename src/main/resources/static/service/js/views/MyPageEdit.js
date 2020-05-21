import { ERROR_MESSAGE, EVENT_TYPE } from '../../utils/constants.js'
import user from '../../api/user.js'

function Join() {
  const $saveButton = document.querySelector('#save-button')
  const $deleteAccountButton = document.querySelector('#delete-account-button')
  const $email = document.querySelector('#email')
  const $name = document.querySelector('#name')
  const $password = document.querySelector('#password')
  const $passwordCheck = document.querySelector('#password-check')

  const validate = ({ name, password, passwordCheck }) => {
    if (!password && !name && !passwordCheck) {
      throw new Error(ERROR_MESSAGE.FORM_EMPTY)
    }

    if (!name || name.includes(" ")) {
      throw new Error(ERROR_MESSAGE.INVALID_NAME)
    }

    if (!password || password.includes(" ")) {
      throw new Error(ERROR_MESSAGE.INVALID_PASSWORD)
    }

    if (password !== passwordCheck) {
      throw new Error(ERROR_MESSAGE.PASSWORD_MISMATCH)
    }
  }

  const onSave = async event => {
    event.preventDefault()

    const memberInfo = {
      name: $name.value,
      password: $password.value,
    }

    try {
      validate({ ...memberInfo, passwordCheck: $passwordCheck.value })
      await user.update(memberInfo)
      location.replace("/mypage")
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

  const onDelete = async event => {
    try {
      await user.deleteAccount()
      location.replace("/service")

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

  this.init = async () => {
    !user.isLoggedIn() && location.replace('/login')

    const { email, name } = await user.getInfo();
    $email.value = email;
    $name.value = name;

    $saveButton.addEventListener(EVENT_TYPE.CLICK, onSave)
    $deleteAccountButton.addEventListener(EVENT_TYPE.CLICK, onDelete)
  }
}

const join = new Join()
join.init()
