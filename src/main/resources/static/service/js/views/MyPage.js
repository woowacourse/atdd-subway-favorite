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
  const $password = document.querySelector("#password");
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
      email: $email.value,
      password: $password.value
    };

    const $userId = document.querySelector(".dropdown-menu");
    const id = $userId.dataset.id;
    api.member
      .update(id, updatedInfo)
      .then(() => {
        Snackbar.show({text: `${SUCCESS_MESSAGE.SAVE}`});
      })
      .catch(() => Snackbar.show({text: `${ERROR_MESSAGE.COMMON}`}));
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
