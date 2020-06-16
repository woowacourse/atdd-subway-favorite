import api from '../../api/index.js';
import { getCookie } from '../../utils/loginUtils.js';

function MyPage() {

  this.init = () => {
    const $email = document.querySelector("#email");
    const $name = document.querySelector("#name");

    if (getCookie()) {
      api.member.myPage().then(response => {
        $email.innerText = response.email;
        $name.innerText = response.name;
      });
    } else {
      alert("로그인을 해주세요");
      window.location = "/login";
    }
  }
}

const myPage = new MyPage();
myPage.init();
