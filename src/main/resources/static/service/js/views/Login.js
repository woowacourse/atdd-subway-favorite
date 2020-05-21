import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js';
import {isValidEmail, isValidJoinPasswordLength} from "../../utils/validation.js";

function Login() {
    const $loginButton = document.querySelector('#login-button');
    const onLogin = event => {
        event.preventDefault();
        const emailValue = document.querySelector('#email').value;
        const passwordValue = document.querySelector('#password').value;

        if (!isValidEmail(emailValue)) {
            Snackbar.show({
                text: ERROR_MESSAGE.WRONG_EMAIL_FORMAT,
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
            password: passwordValue
        };

        api.member
            .login(request)
            .then(response => {
                if (!response.ok) {
                    Snackbar.show({
                        text: ERROR_MESSAGE.LOGIN_FAIL,
                        pos: 'bottom-center',
                        showAction: false,
                        duration: 2000
                    })
                    return;
                }
                response.json()
                    .then(token => {
                        localStorage.setItem("accessToken", token.accessToken);
                        localStorage.setItem("tokenType", token.tokenType);
                        window.location.href = '/';
                    });
            })
    };

    this.init = () => {
        $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
    }
}

const login = new Login();
login.init();
