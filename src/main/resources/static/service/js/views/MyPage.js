import {
  EVENT_TYPE,
  ERROR_MESSAGE,
  SUCCESS_MESSAGE
} from "../../utils/constants.js";
import api from "../../api/index.js";
import showSnackbar from "../../lib/snackbar/index.js";

function MyInfo() {
  const $email = document.querySelector("#email");
  const $name = document.querySelector("#name");
  const $password = document.querySelector("#password");
  const $signOutButton = document.querySelector("#sign-out-button");
  const $updateButton = document.querySelector("#update-button");

  const onSignOutHandler = async event => {
    event.preventDefault();
    if (!confirm("정말 탈퇴하시겠습니까?")) {
      return;
    }
    try {
      await api.loginMember.delete();
      localStorage.setItem("jwt", "");
      location.href = "/";
    } catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  const onUpdateHandler = async event => {
    event.preventDefault();
    try {
      const updatedInfo = {
        name: $name.value,
        email: $email.value,
        password: $password.value
      };
      await api.loginMember.update(updatedInfo);
      showSnackbar(SUCCESS_MESSAGE.SAVE);
    } catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  const initMyInfo = async () => {
    try {
      const member = await api.loginMember.get();
      if (member) {
        $email.value = member.email;
        $name.value = member.name;
        $password.value = member.password;
      }
    } catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  this.init = () => {
    initMyInfo();
    $signOutButton.addEventListener(EVENT_TYPE.CLICK, onSignOutHandler);
    $updateButton.addEventListener(EVENT_TYPE.CLICK, onUpdateHandler);
  };
}

const myInfo = new MyInfo();
myInfo.init();
