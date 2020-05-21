import cookieApi from "../../utils/cookieApi.js";
import api from "../../api/index.js"

function MyInfo() {
  const token = cookieApi.getCookie("token");
  this.getMyInfo = function () {
    api.user.getInfo(token).then(response => {
        const $email = document.querySelector("#email");
        const $name = document.querySelector("#name");
        console.log($email);
        $email.innerHTML = response.email;
        $name.innerHTML = response.name;
        cookieApi.setCookie("id", response.id, 1)
      }
    );

  }
}

new MyInfo().getMyInfo();

