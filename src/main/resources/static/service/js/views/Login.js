import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js';
import {isValidEmail, isValidPasswordLength} from "../../utils/validation.js";

function Login() {
    const $loginButton = document.querySelector('#login-button');
    const onLogin = event => {
        event.preventDefault();
        const emailValue = document.querySelector('#email').value;
        const passwordValue = document.querySelector('#password').value;

        if (!isValidEmail(emailValue)) {
            alert(ERROR_MESSAGE.WRONG_EMAIL_FORMAT);
            return;
        }
        if (!isValidPasswordLength(passwordValue)) {
            alert(ERROR_MESSAGE.INVALID_PASSWORD_LENGTH);
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
                    alert("로그인 실패");
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
