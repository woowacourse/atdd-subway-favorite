import {ERROR_MESSAGE} from "../../utils/constants.js";
import api from "../../api/index.js";
import showSnackbar from "../../lib/snackbar/index.js";

function MyPage() {
  const $name = document.querySelector("#name");
  const $email = document.querySelector("#email");

  const init = async () => {
    try {
      const member = await api.loginMember.get();
      if (member) {
        $email.innerText = member.email;
        $name.innerText = member.name;
      }
    } catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  return {
    init
  };
}

const myPage = new MyPage();
myPage.init();