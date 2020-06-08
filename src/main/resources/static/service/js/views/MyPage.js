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
  const $editButton = document.querySelector("#edit-button");

  const onEditHandler = async event => {
    event.preventDefault();
    location.href='/mypage-edit'
  };

  const initMyInfo = async () => {
    try {
      const member = await api.loginMember.get();
      if (member) {
        $email.value = member.email;
        $name.value = member.name;
      }
    } catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  this.init = () => {
    initMyInfo();
    $editButton.addEventListener(EVENT_TYPE.CLICK, onEditHandler);
  };
}

const myInfo = new MyInfo();
myInfo.init();
