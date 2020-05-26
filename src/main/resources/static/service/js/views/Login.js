import {EVENT_TYPE, ERROR_MESSAGE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Login() {
    const $loginButton = document.querySelector('#login-button')

    const onLogin = event => {
        event.preventDefault()
        const emailValue = document.querySelector('#email').value
        const passwordValue = document.querySelector('#password').value
        if (!emailValue && !passwordValue) {
            Snackbar.show({text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
            return
        }

        const param = {
            email: emailValue,
            password: passwordValue
        }

        api.member.login(param)
            .then(res => {
                if (!res.ok) {
                    throw res;
                }
                res.json().then(token => {
                    localStorage.setItem('jwt', token.accessToken)
                })
                window.location.href = '/';
            }).catch(error => {
            error.json().then(error => alert(error.message))
        })


    }

    this.init = () => {
        $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
    }
}

const login = new Login()
login.init()
