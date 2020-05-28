import api from '../../api/index.js';
import {isSamePassword, isValidName, isValidUpdatePasswordLength} from "../../utils/validation.js";
import {ERROR_SNACK_BAR, EVENT_TYPE, SUCCESS_SNACK_BAR} from "../../utils/constants.js";

function MyEditPage() {
    const $emailValue = document.querySelector('#email');
    const $nameValue = document.querySelector('#name');
    const $passwordValue = document.querySelector('#password');
    const $passwordCheckValue = document.querySelector('#password-check');
    const $editButton = document.querySelector('#edit-button');

    const $signOutButton = document.querySelector('#sign-out');

    const onLoad = () => {
        api.member
            .find()
            .then(data => {
                $emailValue.textContent = data.email;
                $nameValue.value = data.name;
            })
    };

    function onEdit(event) {
        event.preventDefault();
        const data = {
            name: $nameValue.value,
            password: $passwordValue.value
        };
        if (!isValidName($nameValue.value)) {
            ERROR_SNACK_BAR("EMPTY_NAME");
            return;
        }
        if (!isSamePassword($passwordValue.value, $passwordCheckValue.value)) {
            ERROR_SNACK_BAR("MISMATCH_PASSWORD");
            return;
        }
        if (!isValidUpdatePasswordLength($passwordValue.value)) {
            ERROR_SNACK_BAR("INVALID_PASSWORD_LENGTH");
            return;
        }

        api.member
            .update(data)
            .then(() => {
                SUCCESS_SNACK_BAR("UPDATE_SUCCESS");
                window.location.href = "/mypage";
            })
    }

    function onSignOut() {
        api.member
            .signOut()
            .then(response => {
                if (response.ok) {
                    SUCCESS_SNACK_BAR("SIGN_OUT_SUCCESS");
                }
            })
    }

    const initEventListener = () => {
        $editButton.addEventListener(EVENT_TYPE.CLICK, onEdit);
        $signOutButton.addEventListener(EVENT_TYPE.CLICK, onSignOut);
    };

    this.init = () => {
        onLoad();
        initEventListener();
    }
}

const myEditPage = new MyEditPage();
myEditPage.init();
