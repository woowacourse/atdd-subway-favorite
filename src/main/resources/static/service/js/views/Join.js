import { EVENT_TYPE, ERROR_MESSAGE } from "../../utils/constants.js";
import showSnackbar from "../../lib/snackbar/index.js";
import api from "../../api/index.js";

function Join() {
  const $joinButton = document.querySelector("#join-button");
  const $email = document.querySelector("#email");
  const $name = document.querySelector("#name");
  const $password = document.querySelector("#password");
  const $passwordCheck = document.querySelector("#password-check");

  const onJoinHandler = async event => {
    event.preventDefault();
    if (isValid()) {
      const newMember = {
        email: $email.value,
        name: $name.value,
        password: $password.value
      };
      api.member
        .create(newMember)
        .then(() => {
          location.href = "/login";
        })
        .catch(error => console.log(error));
    }
  };

  const isValid = () => {
    const email = $email.value;
    const name = $name.value;
    const password = $password.value;
    const passwordCheck = $passwordCheck.value;
    if (!email || !name || !password) {
      showSnackbar(ERROR_MESSAGE.COMMON);
      return;
    }
    if (password !== passwordCheck) {
      showSnackbar(ERROR_MESSAGE.PASSWORD_CHECK);
      return;
    }
    return true;
  };

  this.init = () => {
    $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoinHandler);
  };
}

const join = new Join();
join.init();
