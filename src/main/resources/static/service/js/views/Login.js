import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Login() {
    const $loginButton = document.querySelector('#login-button')
    const $emailValue = document.querySelector('#email')
    const $passwordValue = document.querySelector('#password')

    const onLogin = async event => {
        event.preventDefault()
        const loginForm = {
            email: $emailValue.value,
            password: $passwordValue.value
        }
        if (!$emailValue.value && !$passwordValue.value) {
            Snackbar.show({
                text: ERROR_MESSAGE.LOGIN_FAIL,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            })
            return
        }
        const token = await api.member.login(loginForm)
            .then(res => res)
            .catch(err => console.log(err.message))

        localStorage["token"] = "Bearer " + token;
        window.location.href = "/map";
    }

    this.init = () => {
        $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
    }
}

const login = new Login()
login.init()
