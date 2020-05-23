import {EVENT_TYPE, ERROR_MESSAGE} from '../../utils/constants.js'
import api from "../../api/index.js";

function Join() {
    const $joinButton = document.querySelector('#join-button')
    const onJoin = event => {
        event.preventDefault()
        const emailValue = document.querySelector('#email').value.trim()
        const nameValue = document.querySelector('#name').value.trim()
        const passwordValue = document.querySelector('#password').value.trim()
        const passwordCheckValue = document.querySelector('#password-check').value.trim()
        if (!emailValue || !nameValue || !passwordValue || !passwordCheckValue) {
            alert(ERROR_MESSAGE.EMPTY_VALUE)
            return
        }
        if (passwordValue !== passwordCheckValue) {
            alert(ERROR_MESSAGE.PASSWORD_CHECK);
            return
        }
        const newMember = {
            email: emailValue,
            name: nameValue,
            password: passwordValue
        }
        api.memberWithoutToken.create(newMember)
            .then(() => {
                alert("회원가입 성공!")
                location.href = "/login"
            })
            .catch(error => alert(error))
    }

    this.init = () => {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
    }
}

const join = new Join()
join.init()
