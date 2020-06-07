import {ERROR_MESSAGE, EVENT_TYPE} from "../../utils/constants.js";
import api from '../../api/index.js'

function Join() {
    const $joinButton = document.querySelector("#join-button")

    function onCreateMember() {
        const $email = document.querySelector("#email")
        const $name = document.querySelector("#name")
        const $password = document.querySelector("#password")
        const $passwordCheck = document.querySelector("#password-check")

        if ($password.value !== $passwordCheck.value) {
            alert(ERROR_MESSAGE.PASSWORD_CHECK_FAIL)
            return
        }

        const memberRequest = {
            email: $email.value,
            name: $name.value,
            password: $password.value
        }

        api.member.create(memberRequest)
            .then(() => {
                window.location.href = "/login"
            })
            .catch(error => console.log(error))
    }

    function initEventListeners() {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onCreateMember)
    }

    this.init = () => {
        initEventListeners()
    }
}

const join = new Join()
join.init()