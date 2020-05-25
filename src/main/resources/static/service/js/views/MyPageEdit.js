import {ERROR_MESSAGE, EVENT_TYPE, REGEX, SUCCESS} from '../../utils/constants.js'
import api from "../../../service/api/index.js";

function Edit() {
  const $editButton = document.querySelector('#edit-button')
  const $deleteButton = document.querySelector('#delete-button')

  const $name = document.querySelector('#name')
  const $password = document.querySelector('#password')
  const $passwordCheck = document.querySelector('#password-check')

  const onEdit = event => {
    event.preventDefault()

    if (!$password.value || !$name.value || !$passwordCheck.value) {
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
    api.member.update(localStorage.getItem("token"), {
      name: $name.value,
      password: $password.value
    }).then(res => {
        if (res.ok) {
          resetEditForm();
          alert(SUCCESS.EDIT);
          location.href = "/"
          return
        }
        Snackbar.show({text: ERROR_MESSAGE.JOIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      }
    )
  }
  const onDelete = event => {
    event.preventDefault();
    if (confirm("정말로 탈퇴하시겠습니까?")) {
      api.member.delete(localStorage.getItem("token")).then();
      alert(SUCCESS.DELETE);
      localStorage.removeItem("token");
      location.href = "/"
    }
  }

  const resetEditForm = () => {
    $name.value = "";
    $password.value = "";
    $passwordCheck.value = "";
  }

  this.init = () => {
    $editButton.addEventListener(EVENT_TYPE.CLICK, onEdit)
    $deleteButton.addEventListener(EVENT_TYPE.CLICK, onDelete)
  }
}

const join = new Edit()
join.init()