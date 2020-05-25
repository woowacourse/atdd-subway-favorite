import api from '../../api/index.js'
import {ERROR_MESSAGE} from "../../utils/constants.js";

function MyPage() {
    const $email = document.querySelector("#email-field");
    const $name = document.querySelector("#name-field");

    this.init = () => {
        api.member.myinfo().then(data => {
            $email.innerHTML = data.email;
            $name.innerHTML = data.name;
        })
            .catch(error => {
                console.log(error.message);
                alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR))
            })
            .then(() => location.href = "/login");
    }
}

const myPage = new MyPage()
myPage.init();


