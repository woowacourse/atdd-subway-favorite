import {EVENT_TYPE} from "../../../admin/utils/constants.js";
import api from "../../api/index.js"
import cookieApi from "../../utils/cookieApi.js";

function MyPageEdit() {

  const $saveButton = document.querySelector("#save-button");
  const $deleteButton = document.querySelector("#delete-button");

  const isInvalid = function(emailValue, nameValue, passwordValue, passwordCheckValue) {
    return false;  // Todo: 마저 작성
  };

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

  const onDelete = event => {
    event.preventDefault();
    if (!window.confirm("진짜로 탈퇴하시겠습니까!?")) {
      return;
    }
    const token = cookieApi.getCookie("token");
    const id = cookieApi.getCookie("id");

    api.user.delete(token, id).then(response => {
      if (!response.ok) {
        throw new Error(response.message);
      }
      window.location.href = "/"
    }).catch(e => alert("삭제 실패.."));
  };

  this.init = () => {
    $saveButton.addEventListener(EVENT_TYPE.CLICK, onSave);
    $deleteButton.addEventListener(EVENT_TYPE.CLICK, onDelete);
  }
}

const myPageEdit = new MyPageEdit();
myPageEdit.init();