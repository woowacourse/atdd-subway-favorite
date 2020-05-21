import {EVENT_TYPE} from "../../../admin/utils/constants.js";
import api from "../../api/index.js"
import cookieApi from "../../utils/cookieApi.js";

function MyPageEdit() {

  const $saveButton = document.querySelector("#save-button");

  const isInvalid = function(emailValue, nameValue, passwordValue, passwordCheckValue) {
    return false;  // Todo: 마저 작성
  }

  const onSave = event => {
    event.preventDefault();
    const token = cookieApi.getCookie("token");
    const id = cookieApi.getCookie("id");
    const emailValue = document.querySelector('#email').value;
    const nameValue = document.querySelector("#name").value;
    const passwordValue = document.querySelector('#password').value
    const passwordCheckValue = document.querySelector('#password-check').value

    //todo: invalid 채우기
    if (isInvalid(emailValue, nameValue, passwordValue, passwordCheckValue)) {
      alert("입력이 잘못됐습니다. ㅠㅠ");
      return;
    }
    const data = {
      name: nameValue,
      password: passwordValue
    };
    api.user.update(token, id, data).then(response => {
      if (!response.ok) {
        throw new Error(response.message);
      }
      window.location.href = "/mypage"
    }).catch(error => alert("입력이 잘못됐습니다. ㅠㅠ"));
  };

  this.init = () => {
    $saveButton.addEventListener(EVENT_TYPE.CLICK, onSave)
  }
}

const myPageEdit = new MyPageEdit();
myPageEdit.init();