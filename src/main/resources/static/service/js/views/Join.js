import { EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js'

function Join() {
  const $email = document.querySelector('#email');
  const $name = document.querySelector('#name');
  const $password = document.querySelector('#password');
  const $passwordCheck = document.querySelector('#password-check');
  const $joinButton = document.querySelector('#join-button');

  const onClickJoinButton = event => {
    event.preventDefault();

    const joinForm = {
      email: $email.value,
      name: $name.value,
      password: $password.value,
      passwordCheck: $passwordCheck.value,
    };

    api.member.join(joinForm)
    .then(response => {
      if (!response.ok) {
        throw response;
      }
      window.location = "/login";
    }).catch(response => response.json())
    .then(errorResponse => {
      if (errorResponse) {
        console.log(errorResponse);
        alert(errorResponse.message);
      }
    });
  }

  const initEventListener = () => {
    $joinButton.addEventListener(EVENT_TYPE.CLICK, onClickJoinButton)
  }

  this.init = () => {
    initEventListener();
  }
}

const join = new Join();
join.init();
