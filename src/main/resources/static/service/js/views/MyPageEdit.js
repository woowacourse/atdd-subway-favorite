import { EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js'

function MyPageEdit() {
  const $id = document.querySelector('#id');
  const $email = document.querySelector('#email');
  const $name = document.querySelector('#name');
  const $oldPassword = document.querySelector('#old-password');
  const $password = document.querySelector('#password');
  const $passwordCheck = document.querySelector('#password-check');
  const $updateButton = document.querySelector('#update-button');
  const $deleteButton = document.querySelector('#delete-button');

  const onClickUpdateButton = event => {
    event.preventDefault();

    const updateForm = {
      email: $email.value,
      name: $name.value,
      oldPassword: $oldPassword.value,
      password: $password.value,
      passwordCheck: $passwordCheck.value,
    };

    api.member.update($id.dataset.id, updateForm)
    .then(response => {
      if (!response.ok) {
        throw response;
      }
      window.location = "/my-page";
    }).catch(response => response.json())
    .then(errorResponse => {
      if (errorResponse) {
        alert(errorResponse.message);
      }
    });
  }

  const initEventListener = () => {
    $updateButton.addEventListener(EVENT_TYPE.CLICK, onClickUpdateButton)
  }

  const getCookie = function () {
    const value = document.cookie.match('(^|;) ?token=([^;]*)(;|$)');
    return value ? value[2] : null;
  };

  this.init = () => {
    if(getCookie()) {
      api.member.myPage().then(response => {
        $id.dataset.id = response.id;
        $email.value = response.email;
        $name.value = response.name;
      }).catch(alert);
      initEventListener();
    } else{
      alert("로그인을 해주세요");
      window.location = '/login';
    }
  }
}

const myPageEdit = new MyPageEdit();
myPageEdit.init();
