import api from '../../api/index.js';
import { EVENT_TYPE } from '../../utils/constants.js';

function MyPageEdit() {
  const $myPageEditEmail = document.querySelector("#mypage-edit-email");
  const $myPageEditName = document.querySelector("#mypage-edit-name");
  const $myPageEditPassword = document.querySelector("#mypage-edit-password");
  const $myPageEditPasswordCheck = document.querySelector("#mypage-edit-password-check");
  const $myPageEditSubmit = document.querySelector("#mypage-edit-submit");
  const $myPageEditQuit = document.querySelector("#mypage-edit-quit");

  const initMyPageEditView = () => {
    api.me.find()
    .then(response => {
      if (response.status === 200) {
        return response.json();
      }
      throw new Error("사용자 초기값을 찾는데 실패했습니다!")
    }).then(meResponse => {
      $myPageEditEmail.value = meResponse.email;
      $myPageEditName.value = meResponse.name;
    }).catch(error => alert(error.message));
  }

  const onDeleteMeHandler = event => {
    event.preventDefault();
    if (!confirm("정말 탈퇴하시겠습니까?")) {
      return;
    }
    api.me.delete()
    .then(response => {
      if (response.status === 204) {
        alert("탈퇴되었습니다.");
        return location.href = "/";
      }
      throw new Error("탈퇴에 성공하지 못 했습니다!");
    }).catch(error => alert(error.message));
  }

  const onUpdateMeHandler = event => {
    event.preventDefault();
    const email = $myPageEditEmail.value;
    const name = $myPageEditName.value;
    const password = $myPageEditPassword.value;
    const passwordCheck = $myPageEditPasswordCheck.value;

    if (!(email && name && password)) {
      return;
    }
    if (password !== passwordCheck) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      return;
    }
    const updateMemberRequest = {
      name,
      password
    }
    api.me.update(updateMemberRequest)
    .then(response => {
      if (response.status === 200) {
        return location.href = "/";
      }
      throw new Error("update에 성공하지 못 했습니다!");
    }).catch(error => alert(error.message));

  }

  const initEventListeners = () => {
    $myPageEditSubmit.addEventListener(EVENT_TYPE.CLICK, onUpdateMeHandler);
    $myPageEditQuit.addEventListener(EVENT_TYPE.CLICK, onDeleteMeHandler);
  }

  this.init = () => {
    initEventListeners();
    initMyPageEditView();
  }
}

const myPageEdit = new MyPageEdit()
myPageEdit.init()