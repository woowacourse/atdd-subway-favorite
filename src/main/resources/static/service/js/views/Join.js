import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Join() {
    const $joinButton = document.querySelector('#join-submit');

    const onJoin = event => {
        event.preventDefault();
        const emailValue = document.querySelector('#email').value;
        const nameValue = document.querySelector('#name').value;
        const passwordValue = document.querySelector('#password').value;
        const passwordCheckValue = document.querySelector('#password-check').value;

        const param = {
            email: emailValue,
            name: nameValue,
            password: passwordValue
        }

        api.member.create(param).then(response => {
            if (!response.ok) {
                throw response;
            }
            window.location.href = '/';
        }).catch(error =>
            error.text.then(error => alert(error)))
    }

    this.init = () => {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
    }
}

const join = new Join()
join.init()
