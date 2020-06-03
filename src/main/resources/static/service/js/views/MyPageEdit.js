import { ERROR_MESSAGE, EVENT_TYPE, SUCCESS_MESSAGE } from "../../utils/constants.js";
import api from "../../api/index.js";
import showSnackbar from "../../lib/snackbar/index.js";

function MyInfo() {
  const $email = document.querySelector("#email");
  const $name = document.querySelector("#name");
  const $password = document.querySelector("#password");
  const $passwordCheck = document.querySelector("#password-check");
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
    if (!isValid()) {
      return;
    }
    try {
      const updatedInfo = {
        name: $name.value,
        email: $email.value,
        password: $password.value
      };
      await api.loginMember.update(updatedInfo);
      showSnackbar(SUCCESS_MESSAGE.SAVE);
    }
    catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  const isValid = () => {
    const email = $email.value;
    const name = $name.value;
    const password = $password.value;
    const passwordCheck = $passwordCheck.value;
    if (!email || !name || !password) {
      showSnackbar(ERROR_MESSAGE.COMMON);
      return false;
    }
    if (password !== passwordCheck) {
      showSnackbar(ERROR_MESSAGE.PASSWORD_CHECK);
      return false;
    }
    return true;
  }

  const initMyInfo = async () => {
    try {
      const member = await api.loginMember.get();
      if (member) {
        $email.value = member.email;
        $name.value = member.name;
      }
    }
    catch (e) {
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
