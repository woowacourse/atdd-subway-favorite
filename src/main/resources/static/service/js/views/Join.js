import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import {isSamePassword, isValidEmail, isValidName, isValidPasswordLength} from '../../utils/validation.js'
import api from '../../api/index.js'

function Join() {
    const $joinButton = document.querySelector('#join-button')
    const onJoin = event => {
        event.preventDefault()
        const emailValue = document.querySelector('#email').value
        const nameValue = document.querySelector('#name').value
        const passwordValue = document.querySelector('#password').value
        const passwordCheckValue = document.querySelector('#password-check').value

        if (!isValidEmail(emailValue)) {
            alert(ERROR_MESSAGE.WRONG_EMAIL_FORMAT);
            return;
        }
        if (!isValidName(nameValue)) {
            alert(ERROR_MESSAGE.EMPTY_NAME);
            return;
        }
        if (!isSamePassword(passwordValue, passwordCheckValue)) {
            alert(ERROR_MESSAGE.MISMATCH_PASSWORD);
            return;
        }
        if (!isValidPasswordLength(passwordValue)) {
            alert(ERROR_MESSAGE.INVALID_PASSWORD_LENGTH);
            return;
        }

        const request = {
            email: emailValue,
            name: nameValue,
            password: passwordValue
        };
        api.member.create(request).then(response => {
            if (response.ok) {
                window.location.href = '/';
            }
        })
    }

    this.init = () => {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
    }
}

const join = new Join()
join.init()
