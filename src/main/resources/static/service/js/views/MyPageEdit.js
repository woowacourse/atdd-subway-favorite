import { EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js'
import { deleteCookie, getCookie } from '../../utils/loginUtils.js';

function MyPageEdit() {
  const $id = document.querySelector('#id');
  const $email = document.querySelector('#email');
  const $name = document.querySelector('#name');
  const $oldPassword = document.querySelector('#old-password');
  const $password = document.querySelector('#password');
  const $passwordCheck = document.querySelector('#password-check');
  const $updateButton = document.querySelector('#update-button');
  const $signOutButton = document.querySelector('#sign-out-button');

  const onClickUpdateButton = event => {
    event.preventDefault();

    const updateForm = {
      name: $name.value,
      oldPassword: $oldPassword.value,
      newPassword: $password.value,
    };

    if ($password.value !== $passwordCheck.value) {
      alert("패스워드가 일치하지 않습니다. 😡");
      return;
    }

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

  const onClickSignOutButton = event => {
    event.preventDefault();

    if (!confirm("정말로 회원 탈퇴를 하실겁니까? 😳")) {
      window.location = "/my-page"
      return;
    }
    api.member.delete($id.dataset.id, $oldPassword.value)
    .then(response => {
      if(!response.ok){
        throw response;
      }
      alert("실망이야..😢");
      deleteCookie();
      window.location = "/login"
    }).catch(response => response.json())
    .then(error => {
      if (error) {
        alert(error.message);
      }
    });
  }

  const initEventListener = () => {
    $updateButton.addEventListener(EVENT_TYPE.CLICK, onClickUpdateButton);
    $signOutButton.addEventListener(EVENT_TYPE.CLICK, onClickSignOutButton);
  }

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
