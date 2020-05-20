import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Join() {
    const $joinButton = document.querySelector('#join-button')
    const onJoin = event => {
        event.preventDefault()
        const emailValue = document.querySelector('#email').value
        const nameValue = document.querySelector('#name').value
        const passwordValue = document.querySelector('#password').value
        const passwordCheckValue = document.querySelector('#password-check').value

        const memberDetail = {
            "email" : emailValue,
            "name" : nameValue,
            "password" : passwordValue,
        }

        if (!emailValue && !nameValue && !passwordValue && !passwordCheckValue) {
            // Snackbar.show({
            //     text: ERROR_MESSAGE.LOGIN_FAIL,
            //     pos: 'bottom-center',
            //     showAction: false,
            //     duration: 2000
            // })
        }
        console.log("버튼버튼")
        return api.member.create(memberDetail);
    }

    this.init = () => {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
    }
}

const join = new Join()
join.init()
