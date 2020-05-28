import {ERROR_SNACK_BAR, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js';
import {isValidEmail, isValidJoinPasswordLength} from "../../utils/validation.js";

function Login() {
    const $loginButton = document.querySelector('#login-button');
    const onLogin = event => {
        event.preventDefault();
        const emailValue = document.querySelector('#email').value;
        const passwordValue = document.querySelector('#password').value;

        if (!isValidEmail(emailValue)) {
            ERROR_SNACK_BAR("WRONG_EMAIL_FORMAT");
            return;
        }
        if (!isValidJoinPasswordLength(passwordValue)) {
            ERROR_SNACK_BAR("INVALID_PASSWORD_LENGTH");
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
                    ERROR_SNACK_BAR("LOGIN_FAIL");
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
