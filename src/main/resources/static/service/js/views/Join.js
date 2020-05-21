import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import {isSamePassword, isValidEmail, isValidJoinPasswordLength, isValidName} from '../../utils/validation.js'
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
            Snackbar.show({
                text: ERROR_MESSAGE.WRONG_EMAIL_FORMAT,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            })
            return;
        }
        if (!isValidName(nameValue)) {
            Snackbar.show({
                text: ERROR_MESSAGE.EMPTY_NAME,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            })
            return;
        }
        if (!isSamePassword(passwordValue, passwordCheckValue)) {
            Snackbar.show({
                text: ERROR_MESSAGE.MISMATCH_PASSWORD,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            })
            return;
        }
        if (!isValidJoinPasswordLength(passwordValue)) {
            Snackbar.show({
                text: ERROR_MESSAGE.INVALID_PASSWORD_LENGTH,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            })
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
