import {EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Join() {
    const $joinButton = document.querySelector('#join-submit');
    const $passwordCheck = document.querySelector('#password-check');
    const $password = document.querySelector('#password');
    const $passwordCheckMessage = document.querySelector("#password-check-message");

    const onJoin = event => {
        event.preventDefault();
        const emailValue = document.querySelector('#email').value;
        const nameValue = document.querySelector('#name').value;

        const param = {
            email: emailValue,
            name: nameValue,
            password: $password.value
        }

        api.member.create(param).then(response => {
            if (!response.ok) {
                throw response;
            }
            window.location.href = '/';
        }).catch(error => {
            error.json().then(error => {
                alert(error.message)
            })
        })
    }

    const validatePassword = event => {
        if ($password.value === $passwordCheck.value) {
            $joinButton.removeAttribute("disabled");
            $passwordCheckMessage.hidden = true;
            return
        }
        $joinButton.disabled = true;
        $passwordCheckMessage.removeAttribute("hidden");
    }

    this.init = () => {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
        $passwordCheck.addEventListener(EVENT_TYPE.KEY_UP, validatePassword)
    }
}

const join = new Join()
join.init()
