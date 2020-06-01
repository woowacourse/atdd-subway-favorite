import { ERROR_MESSAGE } from "../../utils/constants.js";
import api from "../../api/index.js";
import showSnackbar from "../../lib/snackbar/index.js";

function MyInfo() {
  const $email = document.querySelector("#email");
  const $name = document.querySelector("#name");

  const initMyInfo = async () => {
    try {
      const member = await api.loginMember.get();
      if (member) {
        $email.innerText = member.email;
        $name.innerText = member.name;
      }
    }
    catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  this.init = () => {
    initMyInfo();
  };
}

const myInfo = new MyInfo();
myInfo.init();
