import {
  EVENT_TYPE,
  ERROR_MESSAGE,
  SUCCESS_MESSAGE
} from "../../utils/constants.js";
import api from "../../api/index.js";
import Snackbar from "../../lib/snackbar/snackbar.js";

function MyInfo() {
  const $email = document.querySelector("#email");
  const $name = document.querySelector("#name");
  const $prePassword = document.querySelector("#present-password");
  const $newPassword = document.querySelector("#new-password");
  const $signOutButton = document.querySelector("#sign-out-button");
  const $updateButton = document.querySelector("#update-button");

  const onSignOutHandler = event => {
    event.preventDefault();
    if (confirm("정말 탈퇴하시겠습니까?")) {
      const $userId = document.querySelector(".dropdown-menu");
      const id = $userId.dataset.id;
      api.member
        .delete(id)
        .then(() => {
          localStorage.setItem("jwt", "");
          location.href = "/";
        })
        .catch(() => Snackbar.show({text: `${ERROR_MESSAGE.COMMON}`}));
    }
  };

  const onUpdateHandler = event => {
    event.preventDefault();
    const updatedInfo = {
      name: $name.value,
      password: $prePassword.value,
      newPassword: $newPassword.value
    };

    const $userId = document.querySelector(".dropdown-menu");
    const id = $userId.dataset.id;
    api.member
      .update(id, updatedInfo)
      .then((response) => {
        if(!response.ok) {
          alert(ERROR_MESSAGE.PASSWORD_CHECK);
          return;
        }
        alert(SUCCESS_MESSAGE.SAVE);
      });
  };

  const initMyInfo = () => {
    api.loginMember
      .get()
      .then(member => {
        $email.value = member.email;
        $name.value = member.name;

      })
      .catch(() => Snackbar.show({text: `${ERROR_MESSAGE.COMMON}`}));
  };

  this.init = () => {
    initMyInfo();
    $signOutButton.addEventListener(EVENT_TYPE.CLICK, onSignOutHandler);
    $updateButton.addEventListener(EVENT_TYPE.CLICK, onUpdateHandler);
  };
}

const myInfo = new MyInfo();
myInfo.init();
