import {ERROR_MESSAGE} from '../../utils/constants.js'
import api from "../../../service/api/index.js";
import {EVENT_TYPE} from "../../utils/constants.js";

function MyPageEdit() {
  const $email = document.querySelector("#email")
  const $name = document.querySelector("#name")
  const $password = document.querySelector("#password")
  const $passwordCheck = document.querySelector("#password-check")
  const $editButton = document.querySelector("#edit-button")
  const $deleteMemberButton = document.querySelector("#delete-member-button")

  const initMyInfo = () => {
    if (localStorage.getItem("tokenType") === null || localStorage.getItem("accessToken") === null) {
      alert(ERROR_MESSAGE.LOGIN_FIRST)
      location.href = "/login"
      return
    }
    api.memberWithToken.getMyInformation()
        .then(data => {
          $email.value = data.email
        })
  }

  const onEdit = () => {
    if (!$email.value.trim() || !$name.value.trim() || !$password.value.trim() || !$passwordCheck.value.trim()) {
      alert(ERROR_MESSAGE.EMPTY_VALUE)
      return
    }
    if ($password.value.trim() !== $passwordCheck.value.trim()) {
      alert(ERROR_MESSAGE.PASSWORD_CHECK);
      return
    }
    const editInfo = {
      name: $name.value,
      password: $password.value
    }
    api.memberWithToken.updateMyInformation(editInfo).then((data) => {
      if (!data.ok) {
        throw new Error(data.status)
      }
      alert("정상적으로 수정되었습니다.")
      location.href = "/"
    }).catch(error => {
      alert(ERROR_MESSAGE.EDIT_FAIL)
    })
  }

  const onDelete = () => {

  }

  this.init = () => {
    initMyInfo();
    $editButton.addEventListener(EVENT_TYPE.CLICK, onEdit)
    $deleteMemberButton.addEventListener(EVENT_TYPE.CLICK, onDelete)
  }
}

const myPageEdit = new MyPageEdit();
myPageEdit.init();