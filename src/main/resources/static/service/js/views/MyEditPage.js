import {ERROR_MESSAGE, EVENT_TYPE, SUCCESS_MESSAGE} from '../../utils/constants.js';
import api from '../../api/index.js';
import {isSamePassword, isValidName, isValidUpdatePasswordLength} from "../../utils/validation.js";

function MyEditPage() {
    const $emailValue = document.querySelector('#email');
    const $nameValue = document.querySelector('#name');
    const $passwordValue = document.querySelector('#password');
    const $passwordCheckValue = document.querySelector('#password-check');
    const $form = document.querySelector('#form');
    const $editButton = document.querySelector('#edit-button');
    const accessToken = localStorage.getItem("accessToken");
    const tokenType = localStorage.getItem("tokenType");

    const $signOutButton = document.querySelector('#sign-out');

    function createHeader() {
        return tokenType + " " + accessToken;
    }

    const onLoad = () => {
        const headers = createHeader();
        api.member
            .find(headers)
            .then(data => {
                $emailValue.textContent = data.email;
                $nameValue.value = data.name;
                $form.dataset.id = data.id;
            })
    };

    function onEdit(event) {
        event.preventDefault();
        const headers = createHeader();
        const data = {
            name: $nameValue.value,
            password: $passwordValue.value
        };
        if (!isValidName($nameValue.value)) {
            Snackbar.show({
                text: ERROR_MESSAGE.EMPTY_NAME,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            });
            return;
        }
        if (!isSamePassword($passwordValue.value, $passwordCheckValue.value)) {
            Snackbar.show({
                text: ERROR_MESSAGE.MISMATCH_PASSWORD,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            });
            return;
        }
        if (!isValidUpdatePasswordLength($passwordValue.value)) {
            Snackbar.show({
                text: ERROR_MESSAGE.INVALID_PASSWORD_LENGTH,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            });
            return;
        }

        const id = $form.dataset.id;
        api.member
            .update(id, headers, data)
            .then(() => {
                Snackbar.show({
                    text: SUCCESS_MESSAGE.UPDATE_SUCCESS,
                    pos: 'bottom-center',
                    showAction: false,
                    duration: 2000
                });
                window.location.href = "/mypage";
            })
    }

    function onSignOut() {
        const id = $form.dataset.id;
        const headers = createHeader();

        api.member
            .signOut(id, headers)
            .then(response => {
                if (response.ok) {
                    Snackbar.show({
                        text: SUCCESS_MESSAGE.SIGN_OUT_SUCCESS,
                        pos: 'bottom-center',
                        showAction: false,
                        duration: 2000
                    });
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
