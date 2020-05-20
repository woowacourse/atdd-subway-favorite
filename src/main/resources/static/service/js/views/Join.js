import api from "../../api/index.js";
import {ERROR_MESSAGE} from "../../utils/constants.js";

function Join() {
    const $email = document.querySelector("#email");
    const $name = document.querySelector("#name");
    const $password = document.querySelector("#password");
    const $passwordCheck = document.querySelector("#password-check");
    const $button = document.querySelector("button");

    const onClick = event => {
        const joinRequest = {};
        joinRequest.email = $email.value;
        joinRequest.name = $name.value;
        joinRequest.password = $password.value;
        joinRequest.passwordCheck = $passwordCheck.value;

        if (joinRequest.password !== joinRequest.passwordCheck) {
            alert(ERROR_MESSAGE.NOT_MATCH_PASSWORD);
            return false;
        }

        api.member.join(joinRequest)
            .then(body => window.location.href = "/login")
            .catch(error => alert(ERROR_MESSAGE.DUPLICATED_EMAIL));
    };

    this.init = () => {
        $button.addEventListener("click", onClick)
    }
}

const join = new Join();
join.init();