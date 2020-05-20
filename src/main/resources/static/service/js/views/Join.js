import {EVENT_TYPE} from "../../utils/constants.js";
import api from "../../api/index.js"

function Join() {
  const $joinButton = document.querySelector('#join-button');
  const onJoin = event => {

    event.preventDefault();
    const emailValue = document.querySelector('#email').value
    const nameValue = document.querySelector('#name').value;
    const passwordValue = document.querySelector('#password').value
    const passwordCheckValue = document.querySelector('#password-check');

    // Todo: 유효성검사

    const data = {
      email: emailValue,
      name: nameValue,
      password: passwordValue,
    };

    api.user.join(data).then(data => {
      if (!data.ok) {
        return
      }
      window.location.href = "/login";
    });
  };
  this.init = () => {
    $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
  }
}

const join = new Join();
join.init();