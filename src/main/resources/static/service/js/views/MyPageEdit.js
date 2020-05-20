import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'
import api from "../../api/index.js";

function MyPageEdit() {
  const $editButton = document.querySelector('#edit-button')
  const $editForm = document.querySelector('#edit-form')
  const $deleteButton = document.querySelector('#delete-button')

  const $email = document.querySelector('#email')

  const initUserInfo = async () => {
    const token = sessionStorage.getItem("accessToken");
    await api.login.getUserInfo(token).then(data => {
      $editForm.dataset.memberId = data.id
      $email.value = data.email
    })
  }

  const onEdit = async event => {
    const $nameValue = document.querySelector('#name').value
    const $passwordValue = document.querySelector('#password').value
    const $passwordCheckValue = document.querySelector('#password-check').value

    event.preventDefault()
    if (!($nameValue && $passwordValue && $passwordCheckValue)) {
      Snackbar.show({ text: ERROR_MESSAGE.EDIT_EMPTY_FAIL, pos: 'bottom-center', showAction: false, duration: 2000 })
      return
    }
    if (!($passwordValue === $passwordCheckValue)) {
      Snackbar.show({ text: ERROR_MESSAGE.EDIT_PASSWORD_FAIL, pos: 'bottom-center', showAction: false, duration: 2000 })
      return
    }

    const userInfo = {
      name: $nameValue,
      password: $passwordValue
    }
    const memberId = $editForm.dataset.memberId
    await api.member
        .update(memberId, userInfo)

    window.location = "/mypage"
  }

  const onDelete = event => {
    event.preventDefault()
    const memberId = $editForm.dataset.memberId
    api.member.delete(memberId)
    window.location = "/service";
  }

  this.init = () => {
    $editButton.addEventListener(EVENT_TYPE.CLICK, onEdit);
    $deleteButton.addEventListener(EVENT_TYPE.CLICK, onDelete);
    initUserInfo();
  }
}

const myPageEdit = new MyPageEdit()
myPageEdit.init()
