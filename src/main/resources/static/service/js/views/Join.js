import {EVENT_TYPE, ERROR_MESSAGE} from '../../utils/constants.js'

function Join() {
    const $joinButton = document.querySelector('#join_button')

    const onJoin = event => {
        event.preventDefault()
        const $emailValue = document.querySelector('#email')
        const $passwordValue = document.querySelector('#password')
        const $nameValue = document.querySelector('#name')
        if (!$emailValue && !$passwordValue && !$nameValue) {
            alert(ERROR_MESSAGE.JOIN_FAIL);
        }
        createMember($emailValue, $nameValue, $passwordValue);
    }

    this.init = () => {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
    }

    function createMember(emailValue, nameValue, passwordValue) {
        fetch("/members", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
            , body: JSON.stringify({
                email: emailValue.value,
                name: nameValue.value,
                password: passwordValue.value
            })
        }).then(data => console.log(data));
    }
}

const join = new Join()
join.init()
