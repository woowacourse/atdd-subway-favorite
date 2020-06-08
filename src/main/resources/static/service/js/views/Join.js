import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Join() {
    const $joinButton = document.querySelector("#join-button");
    const $email = document.querySelector("#email");
    const $name = document.querySelector("#name");
    const $password = document.querySelector("#password");
    const $passwordCheck = document.querySelector("#password-check");

    const onJoinHandler = async event => {
        event.preventDefault();
        if (isValid()) {
            const newMember = {
                email: $email.value,
                name: $name.value,
                password: $password.value
            };
            api.member.create(newMember)
                .then(() => location.href = "/login")
                .catch(error => alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR)));
        }
    }

    const isValid = () => {
        const name = $name.value;
        const email = $email.value;
        const password = $password.value;
        const passwordCheck = $passwordCheck.value;
        const regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        if (!name) {
            alert(ERROR_MESSAGE.NAME_EMPTY)
            return false;
        }
        if (!email) {
            alert(ERROR_MESSAGE.EMAIL_EMPTY)
            return false;
        }
        if (!password) {
            alert(ERROR_MESSAGE.PASSWORD_EMPTY)
            return false;
        }
        if (email.match(regExp) === null) {
            alert(ERROR_MESSAGE.EMAIL_INVALID)
            return false;
        }
        if (password !== passwordCheck) {
            alert(ERROR_MESSAGE.PASSWORD_MISMATCH)
            return false;
        }
        return true;
    }

    this.init = () => {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoinHandler);
    };
}

const join = new Join();
join.init();