import api from '../../api/index.js'
import {EVENT_TYPE} from "../../../admin/utils/constants.js";

const $saveButton = document.querySelector("#save-button");

const $dropoutButton = document.querySelector("#dropout-button");

function MyPageEdit() {

    function onUpdateMyInfo() {
        const $name = document.querySelector("#name");
        const $password = document.querySelector("#password");
        const $passwordCheck = document.querySelector("#password-check");

        if ($password.value !== $passwordCheck.value) {
            alert("입력하신 비밀번호가 일치하지 않습니다.")
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

    function onDeleteMyInfo() {
        const token = localStorage.getItem("token")

        api.member.delete(token)
            .then(() => {
                localStorage.removeItem("token")
                window.location.href = "/"
            })
            .catch(error => console.log(error))
    }

    function initEventListener() {
        $saveButton.addEventListener(EVENT_TYPE.CLICK, onUpdateMyInfo)
        $dropoutButton.addEventListener(EVENT_TYPE.CLICK, onDeleteMyInfo)
    }

    this.init = () => {
        initEventListener();
    }
}

const myPageEdit = new MyPageEdit()
myPageEdit.init()