import cookieApi from "../../utils/cookieApi.js";
import api from "../../api/index.js"

function MyInfo() {

  this.getMyInfo = function () {
    const token = cookieApi.getCookie("token");
    api.user.getInfo(token).then(response => {
        const $email = document.querySelector("#email");
        const $name = document.querySelector("#name");
        console.log($email);
        $email.innerHTML = response.email;
        $name.innerHTML = response.name;
      }
    );

  }
}

new MyInfo().getMyInfo();

