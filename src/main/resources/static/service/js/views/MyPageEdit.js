import api from '../../api/index.js'
import {EVENT_TYPE} from "../../utils/constants.js";

function MyPage() {
  const $email = document.querySelector('#email');
  const $name = document.querySelector('#name');
  const $password = document.querySelector('#password');
  const $updateButton = document.querySelector('#update-button');
  let id;

  this.init = () => {
    api.member.get().then(res => {
      if (!res.ok) {
        throw res;
      }
      res.json().then(member => {
        $email.value = member.email;
        $name.value = member.name;
        id = member.id;
      });
    }).catch(error => {
      error.json().then(error => alert(error.message))
      window.location.href = '/login';
    })
    initEventListener();
  }

  const onEdit = event => {
    event.preventDefault();
    const param = {
      name: $name.value,
      password: $password.value,
    }

    api.member.update(id, param).then(response => {
      if (!response.ok) {
        throw response;
      }
      window.location.href='/'
    }).catch(error => error.json().then(error => alert(error.message)));
  }

  const initEventListener = () => {
    $updateButton.addEventListener(EVENT_TYPE.CLICK, onEdit);
  }
}

const myPage = new MyPage();
myPage.init()
