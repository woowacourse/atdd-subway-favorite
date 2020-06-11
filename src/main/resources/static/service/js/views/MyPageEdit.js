import api from '../../api/index.js'
import {ERROR_MESSAGE, EVENT_TYPE} from "../../utils/constants.js";

const $saveButton = document.querySelector("#save-button");

const $dropoutButton = document.querySelector("#dropout-button");

function MyPageEdit() {

    function onUpdateMember() {
        const $name = document.querySelector("#name");
        const $password = document.querySelector("#password");
        const $passwordCheck = document.querySelector("#password-check");

        if ($password.value !== $passwordCheck.value) {
            alert(ERROR_MESSAGE.PASSWORD_CHECK_FAIL)
            return
        }

        const updateRequest = {
            name: $name.value,
            password: $password.value
        }

        const token = localStorage.getItem("token")

        api.member.update(token, updateRequest)
            .then(() => {
                window.location.href = "/mypage"
            })
            .catch(error => console.log(error))
    }

    function onDeleteMember() {
        const token = localStorage.getItem("token")

        api.member.delete(token)
            .then(() => {
                localStorage.removeItem("token")
                window.location.href = "/"
            })
            .catch(error => console.log(error))
    }

    function initEventListener() {
        $saveButton.addEventListener(EVENT_TYPE.CLICK, onUpdateMember)
        $dropoutButton.addEventListener(EVENT_TYPE.CLICK, onDeleteMember)
    }

    this.init = () => {
        initEventListener();
    }
}

const myPageEdit = new MyPageEdit()
myPageEdit.init()