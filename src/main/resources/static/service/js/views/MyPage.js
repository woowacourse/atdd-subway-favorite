import {ERROR_MESSAGE} from '../../utils/constants.js'
import api from "../../../service/api/index.js";

function MyPage() {
  const $email = document.querySelector("#email")
  const $name = document.querySelector("#name")

  this.init = () => {
    if (localStorage.getItem("tokenType") === null || localStorage.getItem("accessToken") === null) {
      alert(ERROR_MESSAGE.LOGIN_FIRST)
      location.href = "/login"
    }
    api.memberWithToken.getMyInformation()
        .then(data => {
          $email.textContent = data.email
          $name.textContent = data.name
        })
  }
}

const myPage = new MyPage();
myPage.init();