import api from '../../api/index.js'
import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'

function MyPageEdit() {
    const $email = document.querySelector("#email");
    const $name = document.querySelector("#name");
    const $password = document.querySelector("#password");
    const $passwordCheck = document.querySelector("#password-check");
    const $updateButton = document.querySelector("#update-user-button");
    const $deleteButton = document.querySelector("#delete-user-button");

    const onUpdate = event => {
        event.preventDefault();
        if (isValid()) {
            const updatedMember = {
                name: $name.value,
                password: $password.value
            }
            api.member.update(updatedMember)
                .then(() => {
                    location.href = "/mypage";
                })
                .catch(error => alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR)))
        }
    }

    const isValid = () => {
        const name = $name.value;
        const password = $password.value;
        const passwordCheck = $passwordCheck.value;

        if (!name) {
            alert(ERROR_MESSAGE.NAME_EMPTY)
            return false;
        }

        if (!password) {
            alert(ERROR_MESSAGE.PASSWORD_EMPTY)
            return false;
        }

        if (password !== passwordCheck) {
            alert(ERROR_MESSAGE.PASSWORD_MISMATCH)
            return false;
        }
        return true;
    }

    const onDelete = event => {
        event.preventDefault();
        if (confirm("정말 탈퇴하시겠습니까?")) {
            api.member.delete()
                .then(() => {
                    localStorage.setItem("jwt", "");
                    location.href = "/";
                })
                .catch(error => alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR)));
        }
    }

    const initField = event => {
        api.member.myinfo().then(data => {
            $email.value = data.email;
            $name.value = data.name;
        })
            .catch(error => alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR)))
            .then(() => location.href = "/login");
    }

    this.init = () => {
        initField();
        $updateButton.addEventListener(EVENT_TYPE.CLICK, onUpdate)
        $deleteButton.addEventListener(EVENT_TYPE.CLICK, onDelete)
    }
}

const myPageEdit = new MyPageEdit()
myPageEdit.init();


