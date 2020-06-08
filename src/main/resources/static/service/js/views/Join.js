import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Join() {
    const $emailInput = document.querySelector('#email')
    const $nameInput = document.querySelector('#name')
    const $passwordInput = document.querySelector('#password')
    const $passwordCheckInput = document.querySelector('#password-check')
    const $joinButton = document.querySelector('#join-button')

    const onJoin = async event => {
        event.preventDefault()
        const emailValue = $emailInput.value
        const nameValue = $nameInput.value
        const passwordValue = $passwordInput.value
        const passwordCheckValue = $passwordCheckInput.value
        if (!emailValue || !nameValue || !passwordValue || !passwordCheckValue) {
            Snackbar.show({text: ERROR_MESSAGE.JOIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
            return
        }

        const joinData = {
            email: emailValue,
            name: nameValue,
            password: passwordValue,
            confirmPassword: passwordCheckValue
        }

        try {
            await api.member.join(joinData);
            Snackbar.show({text: "회원가입이 성공하였습니다 👍🏻", pos: 'bottom-center', showAction: false, duration: 2000})
            setTimeout(() => {
                location.href = '/login'
            }, 1000)
        } catch (e) {
            Snackbar.show({text: e, pos: 'bottom-center', showAction: false, duration: 2000})
        }
    }

    this.init = () => {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin)
    }
}

const join = new Join()
join.init()
