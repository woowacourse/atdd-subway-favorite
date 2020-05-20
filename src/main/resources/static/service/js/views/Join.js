import {EVENT_TYPE} from "../../utils/constants.js";
import api from "../../api/index.js"

function Join() {
  const $joinButton = document.querySelector('#join-button');
  const onJoin = event => {

    event.preventDefault();
    console.log("haha");
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

    api.user.join(data);
  };
  this.init = () => {
    $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
  }
}

const join = new Join();
join.init();