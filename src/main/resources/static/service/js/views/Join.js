import api from "../../api/index.js";
import { EVENT_TYPE } from '../../utils/constants.js';

function Join() {

  const $joinForm = document.querySelector("#join-form");
  const $emailInput = document.querySelector("#email");
  const $nameInput = document.querySelector("#name");
  const $passwordInput = document.querySelector("#password");
  const $passwordCheckInput = document.querySelector("#password-check");

  const onSubmitHandler = event => {
    event.preventDefault();
    const email = $emailInput.value;
    const name = $nameInput.value;
    const password = $passwordInput.value;
    const passwordCheck = $passwordCheckInput.value;
    if (!(email && name && password)) {
      return;
    }
    if (password !== passwordCheck) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      return;
    }
    const joinRequest = {
      email,
      name,
      password,
    }
    api.member.join(joinRequest)
    .then(response => {
      if (response.status === 201) {
        return location.href = "/login";
      }
      return response.json().then(error => {
        throw new Error(error.message);
      });
    }).catch(error => alert(error));
  }

  $joinForm.addEventListener(EVENT_TYPE.SUBMIT, onSubmitHandler);
}

const join = new Join();